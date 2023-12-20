package com.example.ioc_di.scope;

import jakarta.inject.Provider;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DependencyLookupLoginService implements LoginService {
    private final Provider<LoginUser> loginUserProvider;

    public LoginUser login() {
        LoginUser loginUser = loginUserProvider.get();
        loginUser.setLoginTime(LocalDate.now());
        return loginUser;
    }
}
