package br.com.dbc.devser.colabore.dto.fundraiser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserCreateDTO {

    @NotBlank(message = "You must provide a title")
    @ApiModelProperty(value = "Título da campanha")
    private String title;

    @NotNull(message = "The goal field cannot be null")
    @ApiModelProperty(value = "Meta da campanha")
    private BigDecimal goal;

    @NotNull(message = "The field automatic close cannot be null")
    @ApiModelProperty(value = "Fechar automaticamente quando atigir a meta")
    private Boolean automaticClose;

    @ApiModelProperty(value = "Descrição")
    private String description;

    @NotNull(message = "The fundraiser must have a ending date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "Data de encerramento")
    private LocalDate endingDate;

    @JsonIgnore
    @ApiModelProperty(value = "Foto de capa")
    private MultipartFile coverPhoto;

    @JsonIgnore
    @ApiModelProperty(value = "Lista de categorias")
    private Set<String> categories;

}
