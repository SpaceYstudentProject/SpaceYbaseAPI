package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Integer> {
    User findByLogin(String login);


}
