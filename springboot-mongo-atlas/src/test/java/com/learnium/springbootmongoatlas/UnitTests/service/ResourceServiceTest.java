package com.learnium.springbootmongoatlas.UnitTests.service;

import com.learnium.model.Resource;
import com.learnium.repository.ResourceRepository;
import com.learnium.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceService resourceService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddResource() {
        Resource resource = new Resource("RES-001", "Resource1", "Type1", LocalDate.now());
        when(resourceRepository.findAll(Sort.by(Sort.Direction.DESC, "resourceId"))).thenReturn(new ArrayList<>());
        when(resourceRepository.save(resource)).thenReturn(resource);
        Resource result = resourceService.addResource(resource);
        assertEquals(resource, result);
    }

    @Test
    public void testFindAllResource() {
        List<Resource> resources = new ArrayList<>();
        when(resourceRepository.findAll()).thenReturn(resources);
        List<Resource> result = resourceService.findAllResource();
        assertEquals(resources, result);
    }

    @Test
    public void testGetResourceByResourceId() {
        String resourceId = "RES-001";
        Resource resource = new Resource("RES-001", "Resource1", "Type1", LocalDate.now());
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        Resource result = resourceService.getResourceByResourceId(resourceId);
        assertEquals(resource, result);
    }

    @Test
    public void testGetResourceByResourceIdNotFound() {
        String resourceId = "RES-002";
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> resourceService.getResourceByResourceId(resourceId));
    }

    @Test
    public void testUpdateResource() {
        Resource resourceRequest = new Resource("RES-001", "Resource1", "Type1", LocalDate.now());
        Resource existingResource = new Resource("RES-001", "Resource2", "Type2", LocalDate.now());
        when(resourceRepository.findById("RES-001")).thenReturn(Optional.of(existingResource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(resourceRequest);
        Resource result = resourceService.updateResource(resourceRequest);
        assertEquals(resourceRequest, result);
    }

    @Test
    public void testDeleteResource() {
        String resourceId = "RES-001";
        doNothing().when(resourceRepository).deleteById(resourceId);
        String result = resourceService.deleteResource(resourceId);
        assertEquals(resourceId + " resource deleted from dashboard ", result);
    }
}