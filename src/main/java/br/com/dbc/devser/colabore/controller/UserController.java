package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Retorna a lista completa de usuários do sistema.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Os usuários foram listados com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/findAll")
    public List<UserDTO> list() {
        return userService.list();
    }

    @ApiOperation(value = "Retorna o usuário logado no sistema.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "O usuário foi listado com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @GetMapping("/profile")
    public UserDTO listLoggedUser() throws UserColaboreException {
        return userService.listLoggedUser();
    }

    @ApiOperation(value = "Cadastra um usuário no banco de dados.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "O usuário foi cadastrado com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public UserDTO create(@Valid @ModelAttribute UserCreateDTO userCreateDTO) throws UserColaboreException {
        return userService.create(userCreateDTO);
    }

    @ApiOperation(value = "Atualiza um usuário no banco de dados.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "O usuário foi atualizado com sucesso."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção no sistema."),})
    @PostMapping(value = "/update", consumes = {"multipart/form-data"})
    public UserDTO update(@Valid @ModelAttribute UserCreateDTO userUpdateDTO) throws UserColaboreException {
        return userService.update(userUpdateDTO);
    }
}
