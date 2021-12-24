package hotels.controller;

import hotels.entity.Hotel;
import hotels.entity.Room;
import hotels.payload.RoomDto;
import hotels.repository.HotelRepository;
import hotels.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    HotelRepository hotelRepository;

    //CRUD
    @PostMapping
    public String addRoom(@RequestBody RoomDto roomFmFront) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(roomFmFront.getHotel_id());
        if (!optionalHotel.isPresent()) return "Hotel not found id: " + roomFmFront.getHotel_id();
        Room room = new Room(null, roomFmFront.getNumber(), roomFmFront.getFloor(), roomFmFront.getSize(), optionalHotel.get());
        roomRepository.save(room);
        return "Room saved";
    }

    @GetMapping
    @RequestMapping("/getRoomById/{roomId}")
    public Room getRoomById(@PathVariable Integer roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (!optionalRoom.isPresent()) {
            Room room = new Room();
            return room;
        }
        return optionalRoom.get();
    }

    @PutMapping
    @RequestMapping("/editRoom/{roomId}")
    public String editRoom(@PathVariable Integer roomId, @RequestBody RoomDto roomFmFront) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (!optionalRoom.isPresent()) return "room not found";
        Room room = optionalRoom.get();
        room.setNumber(roomFmFront.getNumber());
        room.setFloor(roomFmFront.getFloor());
        room.setSize(roomFmFront.getSize());
        Optional<Hotel> optionalHotel = hotelRepository.findById(roomFmFront.getHotel_id());
        if (!optionalHotel.isPresent()) return "Hotel with id " + roomFmFront.getHotel_id() + " not found";
        room.setHotel(optionalHotel.get());
        roomRepository.save(room);
        return "Room edited";

    }

    @DeleteMapping
    @RequestMapping("/deleteRoom/{roomId}")
    public String deleteRoom(@PathVariable Integer roomId) {
        roomRepository.deleteById(roomId);
        return "Room delete";
    }

    //Hotel id orqali  tegishli room lar ro'yxatini pageable qilib olib keluvchi method yozing.

    @GetMapping
    @RequestMapping("/getRoomByHotelId/{hotel_id}")
    public Page<Room> getRoomByHotelId(@PathVariable Integer hotel_id, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 2);
        Page<Room> roomsByHotelId = roomRepository.findAllByHotel_Id(hotel_id, pageable);
        return roomsByHotelId;
    }


}
