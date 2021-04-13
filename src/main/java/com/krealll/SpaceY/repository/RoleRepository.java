package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role, Integer> {

   Role findByName(String name);
}
