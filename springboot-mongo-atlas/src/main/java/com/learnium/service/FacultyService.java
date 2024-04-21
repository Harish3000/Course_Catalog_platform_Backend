package com.learnium.service;

import com.learnium.model.Faculty;
import com.learnium.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.learnium.model.UserInfo;
import com.learnium.service.UserInfoService;



import java.util.List;
import java.util.UUID;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private UserInfoService userInfoService;



    public Faculty addFaculty(Faculty faculty) {
        List<Faculty> faculties = facultyRepository.findAll(Sort.by(Sort.Direction.DESC, "facultyId"));
        if (!faculties.isEmpty()) {
            String lastFacultyId = faculties.get(0).getFacultyId();
            String numericPart = lastFacultyId.substring(4);
            int newId = Integer.parseInt(numericPart) + 1;
            String newFacultyId = String.format("FAC-%03d", newId);
            faculty.setFacultyId(newFacultyId);
        } else {
            faculty.setFacultyId("FAC-001");
        }
        Faculty savedFaculty = facultyRepository.save(faculty);

        // Create a new UserInfo object and set its fields
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(savedFaculty.getFacultyName() + "-" + savedFaculty.getFacultyId());
        userInfo.setName(savedFaculty.getFacultyName());
        userInfo.setEmail(savedFaculty.getEmail());
        userInfo.setPassword(savedFaculty.getFacultyId());
        userInfo.setRoles("ROLE_FACULTY");

        // Add the new user to the user document
        userInfoService.addUser(userInfo);

        return savedFaculty;
    }

    //read all
    public List<Faculty> findAllFacultys() {
        return facultyRepository.findAll();
    }

    //read one
    public Faculty getFacultyByFacultyId(String facultyId) {
        return facultyRepository.findById(facultyId).get();
    }

    public Faculty updateFaculty(Faculty facultyRequest) {
        // Get the existing Faculty document from DB
        Faculty existingFaculty = facultyRepository.findById(facultyRequest.getFacultyId()).get();

        // Populate new value from request to existing Faculty object
        existingFaculty.setFacultyName(facultyRequest.getFacultyName());
        existingFaculty.setEmail(facultyRequest.getEmail());
        existingFaculty.setDesignation(facultyRequest.getDesignation());
        existingFaculty.setDepartment(facultyRequest.getDepartment());
        existingFaculty.setCourseCode(facultyRequest.getCourseCode());

        // Save the updated Faculty document
        Faculty updatedFaculty = facultyRepository.save(existingFaculty);

        // Get the corresponding UserInfo document
        UserInfo existingUser = userInfoService.getUserByUsername(updatedFaculty.getFacultyName() + "-" + updatedFaculty.getFacultyId());

        // Update the UserInfo document
        existingUser.setUserName(updatedFaculty.getFacultyName() + "-" + updatedFaculty.getFacultyId());
        existingUser.setName(updatedFaculty.getFacultyName());
        existingUser.setEmail(updatedFaculty.getEmail());
        existingUser.setPassword(updatedFaculty.getFacultyId());

        // Save the updated UserInfo document
        userInfoService.updateUser(existingUser);

        return updatedFaculty;
    }

    public String deleteFaculty(String facultyId) {
        // Get the existing Faculty document
        Faculty existingFaculty = facultyRepository.findById(facultyId).get();

        // Delete the Faculty document
        facultyRepository.deleteById(facultyId);

        // Get the corresponding UserInfo document
        UserInfo existingUser = userInfoService.getUserByUsername(existingFaculty.getFacultyName() + "-" + existingFaculty.getFacultyId());

        // Delete the UserInfo document
        userInfoService.deleteUser(existingUser.getId());

        return facultyId + " faculty and corresponding user deleted successfully";
    }

    public boolean doesFacultyExist(String facultyId) {
        return facultyRepository.existsById(facultyId);
    }

}
