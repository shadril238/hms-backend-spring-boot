package com.shadril.doctorservice.controller;

import com.shadril.doctorservice.dto.ResponseMessageDto;
import com.shadril.doctorservice.dto.RoomDto;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.service.RoomAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomAllocationService roomService;

    @GetMapping("/available")
    public ResponseEntity<List<RoomDto>> getAvailableRooms()
            throws CustomException {
        log.info("Received request to get available rooms");
        List<RoomDto> roomList = roomService.getAvailableRooms();
        log.info("Available rooms fetched successfully");

        return new ResponseEntity<>(roomList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseMessageDto> createRoom(@RequestBody RoomDto roomDto)
            throws CustomException {
        log.info("Received request to create room with roomNo: {}", roomDto.getRoomNo());
        roomService.createRoom(roomDto);
        log.info("Room created successfully with roomNo: {}", roomDto.getRoomNo());

        return new ResponseEntity<>(new ResponseMessageDto("Room created successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseMessageDto> deleteRoom(@PathVariable Long id)
            throws CustomException {
        log.info("Received request to delete room with roomNo: {}", id);
        roomService.deleteRoom(id);
        log.info("Room deleted successfully with roomNo: {}", id);

        return new ResponseEntity<>(new ResponseMessageDto("Room deleted successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoomDto>> getAllRooms()
            throws CustomException {
        log.info("Received request to get all rooms");
        List<RoomDto> roomList = roomService.getAllRooms();
        log.info("All rooms fetched successfully");

        return new ResponseEntity<>(roomList, HttpStatus.OK);
    }

    @PostMapping("/allocate/{doctorId}/{roomNo}")
    public ResponseEntity<ResponseMessageDto> allocateRoom(@PathVariable String doctorId,@PathVariable String roomNo)
            throws CustomException {
        log.info("Received request to allocate room with roomNo: {} to doctor with id: {}", roomNo, doctorId);
        roomService.allocateRoom(doctorId, roomNo);
        log.info("Room allocated successfully with roomNo: {} to doctor with id: {}", roomNo, doctorId);

        return new ResponseEntity<>(new ResponseMessageDto("Room allocated successfully", HttpStatus.OK), HttpStatus.OK);
    }
}
