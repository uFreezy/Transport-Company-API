package com.jws.transcomp.api.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}