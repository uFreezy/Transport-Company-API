package com.jws.transcomp.api.service.base;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}