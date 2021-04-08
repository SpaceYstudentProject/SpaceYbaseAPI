package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.InteractionEntity;
import com.krealll.SpaceY.model.Launch;
import com.krealll.SpaceY.repository.EntityRepository;
import com.krealll.SpaceY.repository.LaunchRepository;
import com.krealll.SpaceY.service.impl.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntityServiceImpl implements EntityService {

    private final LaunchRepository launchRepository;
    private final EntityRepository entityRepository;

    @Autowired
    public EntityServiceImpl(LaunchRepository launchRepository, EntityRepository entityRepository) {
        this.launchRepository = launchRepository;
        this.entityRepository = entityRepository;
    }


    @Override
    public Launch addLaunch(String launchId) {
        InteractionEntity entity = entityRepository.save(new InteractionEntity());
        Launch launch = new Launch();
        launch.setEntitiesId(entity.getId());
        launch.setId(launchId);
        Launch createdLaunch = launchRepository.save(launch);
        return createdLaunch;
    }

    @Override
    public Optional<Launch> findLaunch(String id) {
        Optional<Launch> launchOptional = launchRepository.findById(id);
        return launchOptional;
    }

}
