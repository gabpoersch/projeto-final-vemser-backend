package br.com.dbc.devser.colabore.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @ApiModelProperty(value = "Id do usuário")
    private Long userId;

    @ApiModelProperty(value = "Nome do usuário")
    private String name;

    @ApiModelProperty(value = "Email do usuário")
    private String email;

    @ApiModelProperty(value = "Foto de perfil")
    private String profilePhoto;

}