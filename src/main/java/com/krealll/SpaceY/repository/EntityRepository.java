package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.InteractionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository extends JpaRepository<InteractionEntity, Integer> {
}
