package com.driven.dm.user.application.service;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public void read() {
        throw AppException.of(UserErrorCode.USER_NOT_FOUNT);
    }
}
