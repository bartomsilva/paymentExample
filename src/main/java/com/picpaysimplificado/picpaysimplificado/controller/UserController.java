package com.picpaysimplificado.picpaysimplificado.controller;

import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.picpaysimplificado.dtos.UserResponseDTO;
import com.picpaysimplificado.picpaysimplificado.infra.ConflictException;
import com.picpaysimplificado.picpaysimplificado.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users") // Adicionando um caminho base para as rotas
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDTO data) throws Exception {
       try{
        User newUser = this.userService.createUser(data);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("usuário cadastrado com sucesso");
       }catch (Exception e){
           throw new ConflictException("usuário já cadastrado");
        }
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers(){
        List<User> listUsers = this.userService.getAllUsers();
        return listUsers.stream()
                .map(UserResponseDTO::new) // Usa o construtor que aceita User
                .collect(Collectors.toList());
    }
}
