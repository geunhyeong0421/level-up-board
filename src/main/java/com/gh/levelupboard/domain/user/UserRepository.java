package com.gh.levelupboard.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginTypeAndLoginId(LoginType loginType, String loginId);

}
