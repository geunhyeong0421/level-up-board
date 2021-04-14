package com.gh.levelupboard.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 메소드 이름으로 쿼리를 생성
    // select u from User u where u.loginType = :loginType and u.loginId = :loginId
    Optional<User> findByLoginTypeAndLoginId(LoginType loginType, String loginId);

}
