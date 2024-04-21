package com.learnium.controller;

import com.learnium.model.Resource;
import com.learnium.service.ResourceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Resource createResource(@Valid @RequestBody Resource resource){
        try {
            Resource createdResource = service.addResource(resource);
            logger.info("Resource created successfully");
            return createdResource;
        } catch (Exception e) {
            logger.error("Error occurred while creating resource", e);
            throw e;
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public List<Resource> getResources() {
        try {
            return service.findAllResource();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all resources", e);
            throw e;
        }
    }

    @GetMapping("/{resourceId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public Resource getResource(@PathVariable String resourceId){
        try {
            return service.getResourceByResourceId(resourceId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching resource", e);
            throw e;
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Resource modifyResource(@Valid @RequestBody Resource resource){
        try {
            return service.updateResource(resource);
        } catch (Exception e) {
            logger.error("Error occurred while updating resource", e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{resourceId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteResource(@PathVariable String resourceId){
        try {
            return service.deleteResource(resourceId);
        } catch (Exception e) {
            logger.error("Error occurred while deleting resource", e);
            throw e;
        }
    }
}