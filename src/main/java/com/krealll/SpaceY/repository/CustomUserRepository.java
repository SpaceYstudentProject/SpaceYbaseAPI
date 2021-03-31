package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.type.UserStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomUserRepository {

    List<User> findAll(Integer limit);

    List<User> findAll(Integer limit, Integer offset);

    List<User> findAllByStatus(UserStatus userStatus,Integer limit, Integer offset );

    List<User> findAllByStatus(UserStatus userStatus,Integer limit);

}
