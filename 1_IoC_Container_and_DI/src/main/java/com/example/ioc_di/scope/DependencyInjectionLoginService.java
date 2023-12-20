package com.example.ioc_di.scope;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DependencyInjectionLoginService implements LoginService {
    private final LoginUser loginUser;

    public LoginUser login() {
        loginUser.setLoginTime(LocalDate.now());
        return loginUser;
    }
}
