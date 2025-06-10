package com.example.service;

import com.example.domain.Administrator;
import com.example.domain.LoginAdministrator;
import com.example.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginAdministratorUserDetailsService implements UserDetailsService {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException {
        Administrator admin = administratorRepository.findByMailAddress(mailAddress);
        if (admin == null) {
            // ユーザーが見つからない場合はUsernameNotFoundExceptionをスロー
            // SecurityConfigが拾って、設定したログインページにリダイレクトされる
            throw new UsernameNotFoundException("ユーザーが見つかりません");
        }

        // LoginAdministratorにラップして返す
        return new LoginAdministrator(admin, List.of(() -> "ROLE_ADMIN"));
    }
}
