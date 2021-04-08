package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.Launch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LaunchRepository extends JpaRepository<Launch, Integer> {

    Optional<Launch> findById(String id);

}
