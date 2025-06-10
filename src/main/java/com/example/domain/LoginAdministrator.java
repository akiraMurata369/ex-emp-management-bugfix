package com.example.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginAdministrator extends User {

    private final Administrator administrator;

    public LoginAdministrator(Administrator administrator, Collection<? extends GrantedAuthority> authorities) {
        // Userのコンストラクタにはユーザー名・パスワード・権限を渡す
        super(administrator.getMailAddress(), administrator.getPassword(), authorities);
        this.administrator = administrator;
    }

    // Administratorオブジェクト自体を外部から取得可能にする
    public Administrator getAdministrator() {
        return administrator;
    }
}
