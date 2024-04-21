package com.learnium.springbootmongoatlas.UnitTests.controller;

import com.learnium.model.Room;
import com.learnium.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @BeforeEach
    public void setup() {
        Room room = new Room();
        room.setRoomId("ROOM-001");
        when(roomService.addRoom(any(Room.class))).thenReturn(room);
        when(roomService.findAllRooms()).thenReturn(Collections.singletonList(room));
        when(roomService.getRoomByRoomId(any(String.class))).thenReturn(room);
        when(roomService.updateRoom(any(Room.class))).thenReturn(room);
        when(roomService.deleteRoom(any(String.class))).thenReturn("ROOM-001 room deleted");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateRoom() throws Exception {
        mockMvc.perform(post("/api/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":\"ROOM-001\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetRooms() throws Exception {
        mockMvc.perform(get("/api/room/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetRoom() throws Exception {
        mockMvc.perform(get("/api/room/{roomId}", "ROOM-001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testModifyRoom() throws Exception {
        mockMvc.perform(put("/api/room/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":\"ROOM-001\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteRoom() throws Exception {
        mockMvc.perform(delete("/api/room/delete/{roomId}", "ROOM-001"))
                .andExpect(status().isOk());
    }

    // Negative test cases
    @Test
    public void testCreateRoomNegative() throws Exception {
        when(roomService.addRoom(any(Room.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":\"ROOM-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetRoomNegative() throws Exception {
        when(roomService.getRoomByRoomId(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/api/room/{roomId}", "ROOM-001"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testModifyRoomNegative() throws Exception {
        when(roomService.updateRoom(any(Room.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(put("/api/room/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":\"ROOM-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteRoomNegative() throws Exception {
        when(roomService.deleteRoom(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(delete("/api/room/delete/{roomId}", "ROOM-001"))
                .andExpect(status().is4xxClientError());
    }
}