package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.donate.DonateCreateDTO;
import br.com.dbc.devser.colabore.service.DonationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/donation")
@Validated
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @ApiOperation(value = "Registra uma doação para uma determinada campanha.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A doação foi persistida com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @PostMapping("/{fundraiserId}")
    public void makeDonation(@PathVariable("fundraiserId") Long fundraiserId, @Valid @RequestBody DonateCreateDTO donate)
            throws Exception {
        donationService.makeDonation(fundraiserId, donate);
    }

}
