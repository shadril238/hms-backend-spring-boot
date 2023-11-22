package com.shadril.doctorservice.service;

import com.shadril.doctorservice.dto.RoomDto;
import com.shadril.doctorservice.exception.CustomException;

import java.util.List;

public interface RoomAllocationService {
    void allocateRoom(String doctorId, String roomNo) throws CustomException;
    void deallocateRoom(String doctorId) throws CustomException;
    void createRoom(String roomNo) throws CustomException;
    void deleteRoom(String roomNo) throws CustomException;
    List<RoomDto> getAllRooms() throws CustomException;
    RoomDto getRoomByRoomNo(String roomNo) throws CustomException;
}
