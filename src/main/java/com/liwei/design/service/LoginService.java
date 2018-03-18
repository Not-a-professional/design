package com.liwei.design.service;

import com.liwei.design.model.User;
import com.liwei.design.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("loginservice")
public class LoginService implements UserDetailsService {
    @Autowired
    private UserRepository uRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = uRepo.findAllByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("用户名无效");
        }
        System.out.println(user.getUsername());
        return user;
    }

}
