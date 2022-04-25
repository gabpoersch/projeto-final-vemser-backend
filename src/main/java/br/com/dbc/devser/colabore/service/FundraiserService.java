package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.category.CategoryDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUserContributionsDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.entity.CategoryEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.CategoryRepository;
import br.com.dbc.devser.colabore.repository.DonationRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class FundraiserService {

    private final ObjectMapper objectMapper;
    private final FundraiserRepository fundraiserRepository;
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public void saveFundraiser(FundraiserCreateDTO fundraiserCreate) throws UserColaboreException {
        FundraiserEntity fundraiserEntity = objectMapper.convertValue(fundraiserCreate, FundraiserEntity.class);

        fundraiserEntity.setCreationDate(LocalDateTime.now());
        fundraiserEntity.setCurrentValue(new BigDecimal("0.0"));
        fundraiserEntity.setStatusActive(true);
        fundraiserEntity.setLastUpdate(LocalDateTime.now());
        fundraiserEntity.setFundraiserCreator(userRepository.findById(userService.getLoggedUserId())
                .orElseThrow(() -> new UserColaboreException("User not found.")));
        fundraiserEntity.setCategoriesFundraiser(buildCategories(fundraiserCreate.getCategories()));
        /*Seta a foto e grava no banco*/
        FundraiserEntity fundSaved = fundraiserRepository.save(setPhotoEntity(fundraiserEntity, fundraiserCreate));

        log.info("Fundraiser with id number {} registered with success.", fundSaved.getFundraiserId());
    }

    public void updateFundraiser(Long fundraiserId, FundraiserCreateDTO fundraiserUpdate) throws FundraiserException {
        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        if (fundraiserEntity.getDonations().size() != 0) {
            throw new FundraiserException("Fundraiser already has donations.");
        }

        fundraiserEntity.setTitle(fundraiserUpdate.getTitle());
        fundraiserEntity.setGoal(fundraiserUpdate.getGoal());
        fundraiserEntity.setAutomaticClose(fundraiserUpdate.getAutomaticClose());
        fundraiserEntity.setDescription(fundraiserUpdate.getDescription());
        fundraiserEntity.setEndingDate(fundraiserUpdate.getEndingDate());
        fundraiserEntity.setCategoriesFundraiser(buildCategories(fundraiserUpdate.getCategories()));

        fundraiserEntity.setLastUpdate(LocalDateTime.now());

        fundraiserRepository.save(setPhotoEntity(fundraiserEntity, fundraiserUpdate));

        log.info("Fundraiser with id number {} updated with success.", fundraiserEntity.getFundraiserId());
    }

    private Set<CategoryEntity> buildCategories(Set<String> categories) {
        return categories.stream().map(category -> {
            //***Testando se existe***
            CategoryEntity categoryReference = categoryRepository.findByNameContainsIgnoreCase(category);
            if (categoryReference != null) {
                return categoryReference;
            }
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName(category);

            return categoryRepository.save(categoryEntity);
        }).collect(Collectors.toSet());
    }

    public FundraiserDetailsDTO fundraiserDetails(Long fundraiserId) throws FundraiserException {
        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        FundraiserDetailsDTO details = objectMapper.convertValue(fundraiserEntity, FundraiserDetailsDTO.class);

        if(fundraiserEntity.getCover()!=null){
            details.setCoverPhoto(Base64.getEncoder().encodeToString(fundraiserEntity.getCover()));
        }
        details.setCategories(convertCategories(fundraiserEntity.getCategoriesFundraiser()));
        details.setContributors(fundraiserEntity.getDonations().stream().map(donationEntity -> {
            UserEntity donatorEntity = donationEntity.getDonator();

            UserDTO userDTO = UserDTO.builder()
                    .userId(donatorEntity.getUserId())
                    .email(donatorEntity.getEmail())
                    .build();
            if (donatorEntity.getPhoto() != null) {
                userDTO.setProfilePhoto(Base64.getEncoder().encodeToString(donatorEntity.getPhoto()));
            }
            return userDTO;
        }).collect(Collectors.toSet()));

        return details;
    }

    public Page<FundraiserGenericDTO> findAllFundraisers(Integer numberPage) {
        return fundraiserRepository
                .findAllFundraisersActive(getPageableWithEndingDate(numberPage, 20))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }


    public Page<FundraiserGenericDTO> findUserFundraisers(Integer numberPage) throws UserColaboreException {
        return fundraiserRepository
                .findFundraisersOfUser(userService.getLoggedUserId(), getPageableWithEndingDate(numberPage, 30))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public Page<FundraiserUserContributionsDTO> userContributions(Integer numberPage) throws UserColaboreException {
        return donationRepository.findMyDonations(userService.getLoggedUserId(), PageRequest.of(numberPage, 20))
                .map(userContribution -> {
                    FundraiserEntity fEntity = userContribution.getFundraiserEntity();
                    FundraiserGenericDTO fundraiserGeneric = objectMapper
                            .convertValue(fEntity, FundraiserGenericDTO.class);
                    FundraiserUserContributionsDTO userContributions = objectMapper
                            .convertValue(completeFundraiser(fundraiserGeneric, fEntity)
                                    , FundraiserUserContributionsDTO.class);
                    userContributions.setStatus(fEntity.getStatusActive());
                    userContributions.setTotalContribution(userContribution.getValue());
                    return userContributions;
                });
    }

    /*Procurar uma maneira mais performática.*/
    public Page<FundraiserGenericDTO> filterByCategories(List<String> categories, Integer numberPage) {
        List<FundraiserGenericDTO> listFundGeneric = fundraiserRepository
                .findAll(getPageableWithEndingDate(numberPage, 20)).stream()
                .filter(fEntity -> {
                    Set<String> categoriesEnt = fEntity.getCategoriesFundraiser().stream()
                            .map(CategoryEntity::getName).collect(Collectors.toSet());
                    return categoriesEnt.containsAll(categories);
                })
                .map(fundraiserEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fundraiserEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fundraiserEntity);
                }).collect(Collectors.toList());
        return new PageImpl<>(listFundGeneric);
    }

    public Page<FundraiserGenericDTO> filterByFundraiserComplete(Integer numberPage) {
        return fundraiserRepository.findFundraiserCompleted(getPageableWithEndingDate(numberPage, 20))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public Page<FundraiserGenericDTO> filterByFundraiserIncomplete(Integer numberPage) {
        return fundraiserRepository.findFundraiserIncomplete(getPageableWithEndingDate(numberPage, 20))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public void deleteFundraiser(Long fundraiserId) {
        fundraiserRepository.deleteById(fundraiserId);
        log.info("Fundaraiser with id number {} deleted.", fundraiserId);
    }

    private FundraiserEntity findById(Long fundraiserId) throws FundraiserException {
        return fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new FundraiserException("Fundraiser not found."));
    }

    private Pageable getPageableWithEndingDate(Integer numberPage, Integer numberItems) {
        return PageRequest
                .of(numberPage, numberItems, Sort.by("endingDate").ascending());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void setStatusFundraiser() {
        log.info("Scheduled method running on {}", LocalDate.now());

        fundraiserRepository.finishedFundraisers(LocalDate.now())
                .forEach(fEntity -> {
                    fEntity.setStatusActive(false);
                    fundraiserRepository.save(fEntity);
                });
    }

    public void checkClosed(Long fundraiserId) throws FundraiserException {
        FundraiserEntity fundraiserEntity = fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new FundraiserException("Fundraiser not found."));

        fundraiserEntity.setStatusActive(checkClosedValue(fundraiserEntity.getCurrentValue(), fundraiserEntity.getGoal()));

        fundraiserRepository.save(fundraiserEntity);
    }

    public Boolean checkClosedValue(BigDecimal currentValue, BigDecimal goal) {
        return currentValue.compareTo(goal) < 0;
    }

    private FundraiserGenericDTO completeFundraiser(FundraiserGenericDTO generic, FundraiserEntity fEntity) {
        generic.setCategories(convertCategories(fEntity.getCategoriesFundraiser()));
        generic.setFundraiserCreator(objectMapper.convertValue(fEntity.getFundraiserCreator(), UserDTO.class));
        if (fEntity.getCover() != null) {
            generic.setCoverPhoto(Base64.getEncoder().encodeToString(fEntity.getCover()));
        }
        return generic;
    }

    private Set<CategoryDTO> convertCategories(Set<CategoryEntity> categories) {
        return categories.stream()
                .map(cEntity -> objectMapper.convertValue(cEntity, CategoryDTO.class))
                .collect(Collectors.toSet());
    }

    private FundraiserEntity setPhotoEntity(FundraiserEntity ent, FundraiserCreateDTO fundCreate) {
        try {
            MultipartFile coverPhoto = fundCreate.getCoverPhoto();
            if (coverPhoto != null) {
                ent.setCover(coverPhoto.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ent;
    }

}
