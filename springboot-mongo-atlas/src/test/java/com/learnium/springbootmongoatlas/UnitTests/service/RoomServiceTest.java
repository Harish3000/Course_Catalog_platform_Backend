package com.learnium.springbootmongoatlas.UnitTests.service;

import com.learnium.model.Room;
import com.learnium.repository.RoomRepository;
import com.learnium.service.RoomService;
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

public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddRoom() {
        Room room = new Room("ROOM-001", "1", "Building1", LocalDate.now());
        when(roomRepository.findAll(Sort.by(Sort.Direction.DESC, "roomId"))).thenReturn(new ArrayList<>());
        when(roomRepository.save(room)).thenReturn(room);
        Room result = roomService.addRoom(room);
        assertEquals(room, result);
    }

    @Test
    public void testFindAllRooms() {
        List<Room> rooms = new ArrayList<>();
        when(roomRepository.findAll()).thenReturn(rooms);
        List<Room> result = roomService.findAllRooms();
        assertEquals(rooms, result);
    }

    @Test
    public void testGetRoomByRoomId() {
        String roomId = "ROOM-001";
        Room room = new Room("ROOM-001", "1", "Building1", LocalDate.now());
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        Room result = roomService.getRoomByRoomId(roomId);
        assertEquals(room, result);
    }

    @Test
    public void testGetRoomByRoomIdNotFound() {
        String roomId = "ROOM-002";
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> roomService.getRoomByRoomId(roomId));
    }

    @Test
    public void testUpdateRoom() {
        Room roomRequest = new Room("ROOM-001", "1", "Building1", LocalDate.now());
        Room existingRoom = new Room("ROOM-001", "2", "Building2", LocalDate.now());
        when(roomRepository.findById("ROOM-001")).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(roomRequest);
        Room result = roomService.updateRoom(roomRequest);
        assertEquals(roomRequest, result);
    }

    @Test
    public void testDeleteRoom() {
        String roomId = "ROOM-001";
        doNothing().when(roomRepository).deleteById(roomId);
        String result = roomService.deleteRoom(roomId);
        assertEquals(roomId + " room deleted from dashboard ", result);
    }

    @Test
    public void testDoesRoomExist() {
        String roomId = "ROOM-001";
        when(roomRepository.existsById(roomId)).thenReturn(true);
        boolean result = roomService.doesRoomExist(roomId);
        assertTrue(result);
    }

    @Test
    public void testDoesRoomExistNotFound() {
        String roomId = "ROOM-002";
        when(roomRepository.existsById(roomId)).thenReturn(false);
        boolean result = roomService.doesRoomExist(roomId);
        assertFalse(result);
    }
}