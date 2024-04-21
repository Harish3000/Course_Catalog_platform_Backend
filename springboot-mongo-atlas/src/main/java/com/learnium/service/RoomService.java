package com.learnium.service;

import com.learnium.model.Room;
import com.learnium.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;


    //create
    public Room addRoom(Room room) {
        List<Room> tables = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "roomId"));
        if (!tables.isEmpty()) {
            String lastRoomId = tables.get(0).getRoomId();
            String numericPart = lastRoomId.substring(5);
            int newId = Integer.parseInt(numericPart) + 1;
            String newRoomId = String.format("ROOM-%03d", newId);
            room.setRoomId(newRoomId);
        } else {
            room.setRoomId("ROOM-001");
        }
        return roomRepository.save(room);
    }

    //read all
    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    //read one
//    public Room getRoomByRoomId(String roomId) {
//        return roomRepository.findById(roomId).get();
//    }

    public Room getRoomByRoomId(String roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id " + roomId));
    }

    public Room updateRoom(Room roomRequest) {
        //get the existing document from DB
        // populate new value from request to existing object/entity/document
        Room existingRoom = roomRepository.findById(roomRequest.getRoomId()).get();
        existingRoom.setFloor(roomRequest.getFloor());
        existingRoom.setBuilding(roomRequest.getBuilding());
        existingRoom.setReservedDate(roomRequest.getReservedDate());

        return roomRepository.save(existingRoom);
    }

    public String deleteRoom(String roomId) {
        roomRepository.deleteById(roomId);
        return roomId + " room deleted from dashboard ";
    }


    public boolean doesRoomExist(String roomId) {
        return roomRepository.existsById(roomId);
    }

}
