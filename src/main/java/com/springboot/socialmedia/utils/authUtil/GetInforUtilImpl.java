package com.springboot.socialmedia.utils.authUtil;

import com.springboot.socialmedia.model.UserModel;
import com.springboot.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GetInforUtilImpl implements GetInforUtil {
    @Autowired
    private UserRepository userRepository;

    public  UserModel getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String email =  authentication.getName();
            return userRepository.findByEmail(email).orElseThrow(()
                    -> new RuntimeException("User does not found!")
            );
        }
        return null;
    }
}
