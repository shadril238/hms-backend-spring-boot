package com.shadril.doctorservice.service.implementation;

import com.shadril.doctorservice.dto.ResponseMessageDto;
import com.shadril.doctorservice.dto.RoomDto;
import com.shadril.doctorservice.entitiy.DoctorEntity;
import com.shadril.doctorservice.entitiy.RoomEntity;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.repository.DoctorRepository;
import com.shadril.doctorservice.repository.RoomRepository;
import com.shadril.doctorservice.service.RoomAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoomAllocationServiceImplementation implements RoomAllocationService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void allocateRoom(String doctorId, String roomNo) throws CustomException {
        try {
            Optional<DoctorEntity> doctorEntityOptional = doctorRepository.findById(doctorId);
            if(doctorEntityOptional.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Doctor not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            Optional<RoomEntity> roomEntityOptional = roomRepository.isRoomAvailable(roomNo);
            if(roomEntityOptional.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Room not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            DoctorEntity doctorEntity = doctorEntityOptional.get();
            RoomEntity roomEntity = roomEntityOptional.get();

            doctorEntity.setRoom(roomEntity);
            doctorRepository.save(doctorEntity);
        } catch (CustomException e){
            throw e;
        } catch (Exception e){
            log.error("Error occurred while allocating room to doctor with id: {}", doctorId);
            throw new CustomException(new ResponseMessageDto("Error occurred while allocating room to doctor", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deallocateRoom(String doctorId) throws CustomException {
        try{
            Optional<DoctorEntity> doctorEntityOptional = doctorRepository.findById(doctorId);
            if(doctorEntityOptional.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Doctor not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            DoctorEntity doctorEntity = doctorEntityOptional.get();
            doctorEntity.setRoom(null);
            doctorRepository.save(doctorEntity);
        } catch (CustomException e){
            throw e;
        } catch (Exception e){
            log.error("Error occurred while deallocating room from doctor with id: {}", doctorId);
            throw new CustomException(new ResponseMessageDto("Error occurred while deallocating room from doctor", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void createRoom(RoomDto roomDto) throws CustomException {
        try{
            Optional<RoomEntity> roomEntityOptional = roomRepository.isRoomAvailable(roomDto.getRoomNo());
            if(roomEntityOptional.isPresent()){
                throw new CustomException(new ResponseMessageDto("Room already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setRoomNo(roomDto.getRoomNo());
            roomEntity.setIsAvailable(true);
            roomRepository.save(roomEntity);
        } catch (CustomException e){
            throw e;
        } catch (Exception e){
            log.error("Error occurred while creating room with room no: {}", roomDto.getRoomNo());
            throw new CustomException(new ResponseMessageDto("Error occurred while creating room", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteRoom(Long id) throws CustomException {
        try{
            Optional<RoomEntity> roomEntityOptional = roomRepository.findById(id);
            if(roomEntityOptional.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Room not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            RoomEntity roomEntity = roomEntityOptional.get();
            roomRepository.delete(roomEntity);
        } catch (CustomException e){
            throw e;
        } catch (Exception e){
            log.error("Error occurred while deleting room with room no: {}", id);
            throw new CustomException(new ResponseMessageDto("Error occurred while deleting room", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RoomDto> getAllRooms() throws CustomException {
        try{
            List<RoomEntity> roomEntityList = roomRepository.findAll();
            return roomEntityList.stream()
                    .map(roomEntity -> modelMapper.map(roomEntity, RoomDto.class))
                    .toList();
        } catch (Exception e){
            log.error("Error occurred while getting all rooms");
            throw new CustomException(new ResponseMessageDto("Error occurred while getting all rooms", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public RoomDto getRoomByRoomNo(String roomNo) throws CustomException {
        try{
            Optional<RoomEntity> roomEntityOptional = roomRepository.findById(Long.parseLong(roomNo));
            if(roomEntityOptional.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Room not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            RoomEntity roomEntity = roomEntityOptional.get();
            return modelMapper.map(roomEntity, RoomDto.class);
        } catch (CustomException e){
            throw e;
        } catch (Exception e){
            log.error("Error occurred while getting room with room no: {}", roomNo);
            throw new CustomException(new ResponseMessageDto("Error occurred while getting room", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RoomDto> getAvailableRooms() throws CustomException {
        try{
            List<RoomEntity> roomEntityList = roomRepository.findAllByIsAvailable(true);
            return roomEntityList.stream()
                    .map(roomEntity -> modelMapper.map(roomEntity, RoomDto.class))
                    .toList();
        } catch (Exception e){
            log.error("Error occurred while getting available rooms");
            throw new CustomException(new ResponseMessageDto("Error occurred while getting available rooms", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
