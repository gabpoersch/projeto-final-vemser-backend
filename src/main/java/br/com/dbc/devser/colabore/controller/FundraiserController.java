package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUserContributionsDTO;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.service.FundraiserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fundraiser")
@RequiredArgsConstructor
@Validated
public class FundraiserController {

    private final FundraiserService fundraiserService;

    @ApiOperation(value = "Salva uma campanha no banco de dados.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A campanha foi persistida com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @PostMapping(value = "/save", consumes = {"multipart/form-data"})
    public void saveFundraiser(@Valid @ModelAttribute FundraiserCreateDTO fundraiser) throws UserColaboreException {
        fundraiserService.saveFundraiser(fundraiser);
    }

    @ApiOperation(value = "Atualiza as informações de uma campanha no banco de dados.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A campanha foi atualizada com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @PostMapping(value = "/{fundraiserId}", consumes = {"multipart/form-data"})
    public void updateFundraiser(@PathVariable("fundraiserId") Long fundraiserId, @Valid @ModelAttribute FundraiserCreateDTO fundUpdate)
            throws FundraiserException, UserColaboreException {
        fundraiserService.updateFundraiser(fundraiserId, fundUpdate);
    }

    @ApiOperation(value = "Lista as campanhas do usuário. Passar número da página como parâmetro (Resultado paginado).")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/userFundraisers/{numberPage}")
    public Page<FundraiserGenericDTO> findUserFundraisers(@PathVariable("numberPage") Integer numberPage) throws UserColaboreException {
        return fundraiserService.findUserFundraisers(numberPage);
    }

    @ApiOperation(value = "Apresenta as informações da campanha de uma maneira mais detalhada. Apresentar o Id da " +
            "campanha como parâmetro na url.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A campanha foi apresentada com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/fundraiserDetails/{fundraiserId}")
    public FundraiserDetailsDTO fundraiserDetails(@PathVariable("fundraiserId") Long fundraiserId) throws FundraiserException {
        return fundraiserService.fundraiserDetails(fundraiserId);
    }

    @ApiOperation(value = "Apresenta a lista de contribuições(campanhas) que o usuário contribuiu. Passar número da página como parâmetro " +
            "(Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/userContributions/{numberPage}")
    public Page<FundraiserUserContributionsDTO> userContributions(@PathVariable("numberPage") Integer numberPage) throws UserColaboreException {
        return fundraiserService.userContributions(numberPage);
    }

    @ApiOperation(value = "Filtra as campanhas a partir das categorias listadas. Passar número da página como parâmetro (Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanha foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/byCategories/{numberPage}")
    public Page<FundraiserGenericDTO> filterByCategories(@RequestParam List<String> categories, @PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.filterByCategories(categories, numberPage);
    }

    @ApiOperation(value = "Apresenta todas as campanhas que ainda não foram alcançadas. Passar número da página como parâmetro (Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/findNotAcchieved/{numberPage}")
    public Page<FundraiserGenericDTO> findFundraisersActiveNotAcchieved(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.findFundraisersActiveNotAcchieved(numberPage);
    }

    @ApiOperation(value = "Apresenta todas as campanhas que atingiram a meta. Passar número da página como parâmetro (Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/findAcchieved/{numberPage}")
    public Page<FundraiserGenericDTO> findFundraisersActiveAcchieved(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.findFundraisersActiveAcchieved(numberPage);
    }

    @ApiOperation(value = "Apresenta todas as campanhas ativas (atingidas e não atingidas). Passar número da página como parâmetro (Resultado paginado)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "As campanhas foram listadas com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/findAllFundraisersActive/{numberPage}")
    public Page<FundraiserGenericDTO> findAllFundraisersActive(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.findAllFundraisersActive(numberPage);
    }

    @ApiOperation(value = "Deleta os registros da campanha (campanha e doações) do banco de dados.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Os registros da campanha foram deletados com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @DeleteMapping("/{fundraiserId}")
    public void deleteFundraiser(@PathVariable("fundraiserId") Long fundraiserId) throws FundraiserException, UserColaboreException {
        fundraiserService.deleteFundraiser(fundraiserId);
    }

}
