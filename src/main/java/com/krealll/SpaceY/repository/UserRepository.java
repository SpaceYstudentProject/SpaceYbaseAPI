package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.type.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Integer> {
    User findByLogin(String login);

    Optional<User> findById(Integer id);

    List<User> findAllByLogin(String login);


    List<User> findAllByStatus(UserStatus status);

    List<User> findAllByStatus(UserStatus status, Pageable pageable);

    Page<User> findAll (Pageable pageable);
}
