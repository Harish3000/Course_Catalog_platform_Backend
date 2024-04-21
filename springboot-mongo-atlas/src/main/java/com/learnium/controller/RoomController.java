package com.learnium.controller;

import com.learnium.model.Room;
import com.learnium.service.RoomService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Room createRoom(@Valid @RequestBody Room room){
        try {
            Room createdRoom = service.addRoom(room);
            logger.info("Room created successfully");
            return createdRoom;
        } catch (Exception e) {
            logger.error("Error occurred while creating room", e);
            throw e;
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public List<Room> getRooms() {
        try {
            return service.findAllRooms();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all rooms", e);
            throw e;
        }
    }

    @GetMapping("/{roomId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public Room getRoom(@PathVariable String roomId){
        try {
            return service.getRoomByRoomId(roomId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching room", e);
            throw e;
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Room modifyRoom(@Valid @RequestBody Room room){
        try {
            return service.updateRoom(room);
        } catch (Exception e) {
            logger.error("Error occurred while updating room", e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteRoom(@PathVariable String roomId){
        try {
            return service.deleteRoom(roomId);
        } catch (Exception e) {
            logger.error("Error occurred while deleting room", e);
            throw e;
        }
    }
}