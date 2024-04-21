package com.learnium.service;

import com.learnium.model.Resource;
import com.learnium.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;


    //create
    public Resource addResource(Resource resource) {
        List<Resource> tables = resourceRepository.findAll(Sort.by(Sort.Direction.DESC, "resourceId"));
        if (!tables.isEmpty()) {
            String lastResourceId = tables.get(0).getResourceId();
            String numericPart = lastResourceId.substring(4);
            int newId = Integer.parseInt(numericPart) + 1;
            String newResourceId = String.format("RES-%03d", newId);
            resource.setResourceId(newResourceId);
        } else {
            resource.setResourceId("RES-001");
        }
        return resourceRepository.save(resource);
    }

    //read all
    public List<Resource> findAllResource() {
        return resourceRepository.findAll();
    }

    //read one
//    public Resource getResourceByResourceId(String resourceId) {
//        return resourceRepository.findById(resourceId).get();
//    }

    public Resource getResourceByResourceId(String resourceId) {
        return resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found with id " + resourceId));
    }

    public Resource updateResource(Resource resourceRequest) {
        //get the existing document from DB
        // populate new value from request to existing object/entity/document
        Resource existingResource = resourceRepository.findById(resourceRequest.getResourceId()).get();
        existingResource.setName(resourceRequest.getName());
        existingResource.setType(resourceRequest.getType());
        existingResource.setReservedDate(resourceRequest.getReservedDate());


        return resourceRepository.save(existingResource);
    }

    public String deleteResource(String resourceId) {
        resourceRepository.deleteById(resourceId);
        return resourceId + " resource deleted from dashboard ";
    }


    public boolean doesResourceExist(String resourceId) {
        return resourceRepository.existsById(resourceId);
    }

}
