package com.michal.crm.service;

import com.michal.crm.dao.UsersRepository;
import com.michal.crm.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("CustomUserDetailsService")
@Transactional
public class CustomUserService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Users user = usersRepository.findByUsername(s);

        if (user == null) {
            System.out.println("User not exist!!");
        }

        return user;
    }
}
