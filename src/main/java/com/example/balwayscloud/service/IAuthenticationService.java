package com.example.balwayscloud.service;



import com.example.balwayscloud.model.User;

import java.io.IOException;

public interface IAuthenticationService {
    User register(User customer) throws Exception;
    boolean login(String username, String password) throws IOException;
}
