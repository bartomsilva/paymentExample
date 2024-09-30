package com.picpaysimplificado.picpaysimplificado.services;

import com.picpaysimplificado.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public void validTransaction(User user, BigDecimal amount) throws Exception {

        if(user.getUsertype() == UserType.MERCHANT){
            throw new Exception("este usuário não pode efetuar trasações.");
        }

        if(user.getBalance().compareTo(amount)<0){
            throw new Exception("saldo insulficiente");

        }
    }

    public User findUserById(Long id) throws Exception {
        return this.repository
                .findUserById(id)
                .orElseThrow(()-> new Exception("usuário não localizado."));
    }

    public User createUser(UserDTO data){
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public void saveUser(User user) {
        this.repository.save(user);
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();
    }

}
