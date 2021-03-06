package br.com.dbc.devser.colabore.dto.fundraiser;

import br.com.dbc.devser.colabore.dto.category.CategoryDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundraiserDetailsDTO {

    @ApiModelProperty(value = "Id da campanha")
    private Long fundraiserId;

    @ApiModelProperty(value = "Título da campanha")
    private String title;

    @ApiModelProperty(value = "Meta da campanha")
    private BigDecimal goal;

    @ApiModelProperty(value = "Descrição")
    private String description;

    @ApiModelProperty(value = "Foto de capa")
    private String coverPhoto;

    @ApiModelProperty(value = "Valor total arrecadado")
    private BigDecimal currentValue;

    @ApiModelProperty(value = "Lista de categorias")
    private Set<CategoryDTO> categories;

    @ApiModelProperty(value = "Lista dos contribuidores da campanha")
    private Set<UserDTO> contributors;

    //>> Plus

    @ApiModelProperty(value = "Criador/autor da campanha")
    private UserDTO fundraiserCreator;

    @ApiModelProperty(value = "Fechar automaticamente quando atigir a meta")
    private Boolean automaticClose;

    @ApiModelProperty(value = "Data de encerramento")
    private LocalDate endingDate;

}
