package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {


    @Transactional
    Integer deleteByValue(String tokenValue);

    @Transactional
    Integer deleteByUsersId(Integer usersId);

}
