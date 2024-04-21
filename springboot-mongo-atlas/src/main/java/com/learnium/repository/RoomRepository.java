package com.learnium.repository;

import com.learnium.model.Room;
import com.learnium.model.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;

public interface RoomRepository extends MongoRepository<Room,String> {
    boolean existsByRoomIdAndReservedDate(String roomId, LocalDate date);
    boolean existsByReservedDate(LocalDate date);


}
