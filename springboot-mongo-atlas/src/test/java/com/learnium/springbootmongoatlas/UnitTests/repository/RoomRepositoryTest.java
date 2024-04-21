package com.learnium.springbootmongoatlas.UnitTests.repository;

import com.learnium.model.Room;
import com.learnium.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomId("ROOM-001");
        room.setReservedDate(LocalDate.now());
        roomRepository.save(room);
    }

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
    }

    @Test
    void whenValidRoomIdAndReservedDate_thenRoomShouldExist() {
        assertTrue(roomRepository.existsByRoomIdAndReservedDate("ROOM-001", LocalDate.now()));
    }

    @Test
    void whenInvalidRoomIdAndReservedDate_thenRoomShouldNotExist() {
        assertFalse(roomRepository.existsByRoomIdAndReservedDate("INVALID", LocalDate.now()));
    }
}