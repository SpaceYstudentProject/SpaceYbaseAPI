package com.krealll.SpaceY.service.impl;

import com.krealll.SpaceY.model.Launch;

import java.util.Optional;

public interface EntityService {

    Launch addLaunch(String launchId);

    Optional<Launch> findLaunch(String id);

}
