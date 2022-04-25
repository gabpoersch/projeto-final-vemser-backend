package br.com.dbc.devser.colabore.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    @ApiModelProperty(value = "Nome do usuário")
    private String name;

    @ApiModelProperty(value = "Email do usuário")
    private String email;

    @ApiModelProperty(value = "Senha do usuário")
    private String password;

    @ApiModelProperty(value = "Foto de perfil")
    private MultipartFile profilePhoto;

}