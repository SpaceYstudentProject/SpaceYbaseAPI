package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Integer> {
    User findByLogin(String login);

    Optional<User> findById(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password=?1 where u.id=?2")
    int changePassword(String password, Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email=?1 where u.id=?2")
    int changeEmail(String email, Integer id);

}
