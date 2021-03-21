package com.gh.levelupboard.config.auth.dto;

import com.gh.levelupboard.domain.user.Role;
import com.gh.levelupboard.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private Role role;
    private boolean isAdmin;

    public SessionUser(User user) {
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        picture = user.getPicture();
        role = user.getRole();
        isAdmin = role.equals(Role.ADMIN);
    }
}
