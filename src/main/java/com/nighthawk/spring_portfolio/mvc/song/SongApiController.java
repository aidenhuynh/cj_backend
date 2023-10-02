package com.nighthawk.spring_portfolio.mvc.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // annotation to simplify the creation of RESTful web services
@RequestMapping("/api/songs")  // all requests in file begin with this URI
public class SongApiController {

    // Autowired enables Control to connect URI request and POJO Object to easily for Database CRUD operations
    @Autowired
    private SongJpaRepository repository;

    /* GET List of Song
     * @GetMapping annotation is used for mapping HTTP GET requests onto specific handler methods.
     */
    @GetMapping("/")
    public ResponseEntity<List<Song>> getSong() {
        // ResponseEntity returns List of Song provide by JPA findAll()
        return new ResponseEntity<>( repository.findAll(), HttpStatus.OK);
    }

    /* Update Like
     * @PutMapping annotation is used for mapping HTTP PUT requests onto specific handler methods.
     * @PathVariable annotation extracts the templated part {id}, from the URI
     */
    @PostMapping("/like/{id}")
    public ResponseEntity<Song> setLike(@PathVariable long id) {
        /* 
        * Optional (below) is a container object which helps determine if a result is present. 
        * If a value is present, isPresent() will return true
        * get() will return the value.
        */
        Optional<Song> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Song song = optional.get();  // value from findByID
            song.setLike(song.getLike()+1); // increment value
            repository.save(song);  // save entity
            return new ResponseEntity<>(song, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Failed HTTP response: status code, headers, and body
    }

    /* Update Jeer
     */
    @PostMapping("/jeer/{id}")
    public ResponseEntity<Song> setJeer(@PathVariable long id) {
        Optional<Song> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Song song = optional.get();
            song.setDislike(song.getDislike()+1);
            repository.save(song);
            return new ResponseEntity<>(song, HttpStatus.OK);
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}