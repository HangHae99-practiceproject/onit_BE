package com.hanghae99.onit_be.user;

import com.hanghae99.onit_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByKakaoId(Long kakaoId);

    //Optional<User> findByNickname(String )
    boolean existsByNickname(String nickname);
    boolean existsByUsername(String username);


    Optional<User> findByNickname(String nickname);

    List<User> findAllById(Long planId);
}
