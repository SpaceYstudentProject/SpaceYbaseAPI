package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.Comment;
import com.krealll.SpaceY.model.InteractionEntity;
import com.krealll.SpaceY.model.Launch;
import com.krealll.SpaceY.repository.EntityRepository;
import com.krealll.SpaceY.repository.LaunchRepository;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@PrepareForTest(EntityServiceImpl.class)
public class EntityServiceImplTest {

    private EntityServiceImpl entityService;
    private LaunchRepository launchRepository;
    private EntityRepository entityRepository;
    private Launch launch;
    private Launch launch2;
    private InteractionEntity interactionEntity;
    private Optional<Launch> optionalLaunch;
    private Optional<Launch> optionalLaunch2;

    @BeforeMethod
    public void setUp() {
        launchRepository = mock(LaunchRepository.class);
        entityRepository = mock(EntityRepository.class);
        entityService = new EntityServiceImpl(launchRepository, entityRepository);

        launch = new Launch();
        launch.setId("123");
        launch.setEntitiesId(1);

        launch2 = new Launch();
        launch2.setId("1234");
        launch2.setEntitiesId(1);

        interactionEntity = new InteractionEntity();
        interactionEntity.setId(1);

        optionalLaunch = Optional.of(launch);
        optionalLaunch2 = Optional.of(launch2);
    }

    @Test
    public void addLaunchTrue() {
        Launch expected = launch;
        when(entityRepository.save(any(InteractionEntity.class))).thenReturn(interactionEntity);
        when(launchRepository.save(any(Launch.class))).thenReturn(launch);
        Launch actual = entityService.addLaunch("123");
        assertEquals(expected, actual);
    }

    @Test
    public void addLaunchFalse() {
        Launch expected = launch;
        when(entityRepository.save(any(InteractionEntity.class))).thenReturn(interactionEntity);
        when(launchRepository.save(any(Launch.class))).thenReturn(launch2);
        Launch actual = entityService.addLaunch("12345");
        assertNotEquals(expected, actual);
    }

    @Test
    public void findLaunchTrue() {
        Optional<Launch> expected = optionalLaunch;
        when(launchRepository.findById(anyString())).thenReturn(optionalLaunch);
        Optional<Launch> actual = entityService.findLaunch("123");
        assertEquals(expected, actual);
    }

    @Test
    public void findLaunchFalse() {
        Optional<Launch> expected = optionalLaunch;
        when(launchRepository.findById(anyString())).thenReturn(optionalLaunch2);
        Optional<Launch> actual = entityService.findLaunch("123");
        assertNotEquals(expected, actual);
    }
}
