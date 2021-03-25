package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {

}
