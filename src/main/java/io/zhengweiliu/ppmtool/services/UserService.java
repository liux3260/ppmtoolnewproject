package io.zhengweiliu.ppmtool.services;

import io.zhengweiliu.ppmtool.domain.User;
import io.zhengweiliu.ppmtool.exceptions.UsernameAlreadyExistsException;
import io.zhengweiliu.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        }
        catch (Exception e){
            throw new UsernameAlreadyExistsException(e.getMessage());
            //throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
        }
    }

}
