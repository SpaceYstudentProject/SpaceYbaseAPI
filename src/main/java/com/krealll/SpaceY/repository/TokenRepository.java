package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken, Integer> {


    @Transactional
    Integer deleteByValue(String tokenValue);

    @Transactional
    Integer deleteByUsersId(Integer usersId);

    Optional<RefreshToken> findByValue(String value);



}
