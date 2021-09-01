package io.zhengweiliu.ppmtool.services;

import io.zhengweiliu.ppmtool.domain.User;
import io.zhengweiliu.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user==null)   new UsernameNotFoundException("User not found");

        System.out.println(user.getUsername());

        return user;
    }



    @Transactional
    public User loadUserById(Long id){
        User user=userRepository.getById(id);
        if(user==null) {
            //System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println(user.getUsername()); //Need to have it for proxy to work. Not sure why...
        return user;
    }
}
