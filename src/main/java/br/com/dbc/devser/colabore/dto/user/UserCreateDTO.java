package br.com.dbc.devser.colabore.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserCreateDTO {

    @ApiModelProperty(value = "Nome do usuário")
    private String name;

    @ApiModelProperty(value = "Email do usuário")
    private String email;

    @ApiModelProperty(value = "Senha do usuário")
    private String password;

    @ApiModelProperty(value = "Foto de perfil")
    private String profilePhoto;
}