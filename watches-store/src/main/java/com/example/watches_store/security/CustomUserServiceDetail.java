package com.example.watches_store.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.watches_store.entity.Role;
import com.example.watches_store.entity.User;
import com.example.watches_store.repository.RoleRepository;
import com.example.watches_store.repository.UserRepository;

@Service
public class CustomUserServiceDetail implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isPresent()){
            Set<Role> roles = new HashSet<>();
            for(Role r : user.get().getRole()){
                roles.add(this.roleRepository.findById(r.getId()).orElse(null));
            }
            user.get().setRole(roles);
            return new CustomUserDetail(user.get());
        }
        else
            throw new UsernameNotFoundException(username);
    }
}
