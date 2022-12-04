package com.yaps.petstore.security.service;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yaps.petstore.security.dao.YapsUserDAO;
import com.yaps.petstore.security.domain.YapsUser;
import com.yaps.petstore.security.domain.YapsUserRole;
import com.yaps.petstore.security.exception.UsernameAlreadyExistsException;

@Service
public class YapsUserService implements UserDetailsService {

    @Autowired
    YapsUserDAO repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<YapsUser> optYapsUser = repository.findById(username);
        if (optYapsUser.isPresent()) {
            YapsUser yapsUser = optYapsUser.get();
            UserBuilder builder = User.builder()
                    .username(username)
                    .password(yapsUser.getUserPassword());
            HashSet<String> roles = new HashSet<>();

            switch (yapsUser.getRole()) {
                // case ADMIN:
                //     roles.add("ADMIN");
                //     roles.add("EMP"); // on pourrait passer au cas suivant.
                //     break;
                case EMP:
                    roles.add("EMP");
                    break;
                case CUST:
                    roles.add("CUST");
                default:
                    break;
            }
            builder.roles(roles.toArray(new String[roles.size()]));
            UserDetails details =  builder.build();
            return details;
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public YapsUser createUser(String username, String password, YapsUserRole userRole) throws UsernameAlreadyExistsException {
        // Checks that the login is not already used.
        if (repository.existsById(username)) {
            throw new UsernameAlreadyExistsException();
        }
        String encodedPassword = passwordEncoder.encode(password);
        YapsUser newUser = new YapsUser(username, YapsUserRole.CUST, encodedPassword);
        // On retourne la valeur renvoyée par la dao, et non l'objet d'origine.
        return repository.save(newUser);
    }
}
