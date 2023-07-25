package com.springboot.socialmedia.utils.authUtil;

import com.springboot.socialmedia.model.UserModel;
import org.springframework.stereotype.Service;

@Service
public interface GetInforUtil {
    UserModel getUser();
}
