package hotels.controller;

import hotels.entity.Hotel;
import hotels.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    HotelRepository hotelRepository;

    //CRUD
    @PostMapping
    public String addHotel(@RequestBody Hotel hotelFmFront) {
        Optional<Hotel> byName = hotelRepository.findByName(hotelFmFront.getName());
        if (byName.isPresent()) return "Hotel already exists!";
        hotelRepository.save(hotelFmFront);
        return "Hotel saved";
    }

    @GetMapping
    @RequestMapping("/getAllHotelList")
    public List<Hotel> getAllHotelList() {
        List<Hotel> hotelList = hotelRepository.findAll();
        if (hotelList.size() > 0) return hotelList;
        hotelList.add(new Hotel());
        return hotelList;
    }

    @GetMapping
    @RequestMapping("/getHotel/{hotelId}")
    public Hotel getHotel(@PathVariable Integer hotelId) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if (!optionalHotel.isPresent()) return new Hotel();
        return optionalHotel.get();
    }

    @PutMapping
    @RequestMapping("/editHotel/{hotelId}")
    public String editHotel(@PathVariable Integer hotelId, @RequestBody Hotel hotel) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if (optionalHotel.isPresent()) {
            Hotel hotelToEdt = optionalHotel.get();
            hotelToEdt.setName(hotel.getName());
            hotelRepository.save(hotelToEdt);
            return "Edited hotel id " + hotelId;
        }
        return "Hotel not found id " + hotelId;
    }

    @DeleteMapping
    @RequestMapping("/deleteHotel/{hotelId}")
    public String deleteHotel(@PathVariable Integer id) {
        hotelRepository.deleteById(id);
        return "Hotel deleted";
    }

}
