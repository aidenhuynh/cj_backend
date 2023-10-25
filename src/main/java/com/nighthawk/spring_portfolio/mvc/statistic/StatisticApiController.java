package com.nighthawk.spring_portfolio.mvc.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/statistic")
public class StatisticApiController {
    //     @Autowired
    // private JwtTokenUtil jwtGen;
    /*
    #### RESTful API ####aa
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private StatisticJpaRepository repository;
    @Autowired  // Inject PasswordEncoder
    Set<String> usedClassCodes = new HashSet<>();
    

    /*
    GET List of People
     */
    @GetMapping("/")
    public ResponseEntity<List<Statistic>> getPeople() {
        return new ResponseEntity<>( repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    /*
    GET individual Human using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Statistic> getHuman(@PathVariable long id) {
        
        Optional<Statistic> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Statistic statistic = optional.get();  // value from findByID
            return new ResponseEntity<>(statistic, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Statistic> deleteStatistic(@PathVariable long id) {
        Optional<Statistic> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Statistic statistic = optional.get();  // value from findByID
            repository.deleteById(id);  // value from findByID
            return new ResponseEntity<>(statistic, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    /*
    POST Aa record by Requesting Parameters from URI
     */
    @PostMapping( "/post")
    public ResponseEntity<Object> postStatistic(@RequestParam("songCode") String songCode,
                                             @RequestParam("name") String name,
                                             @RequestParam("dou") String douString,
                                             @RequestParam("classCode") String classCode,
                                             @RequestParam("role") String role) {
        Date dou;
        try {
            dou = new SimpleDateFormat("MM-dd-yyyy HH:mm").parse(douString);
        } catch (Exception e) {
            return new ResponseEntity<>(douString +" error; try MM-dd-yyyy HH:mm", HttpStatus.BAD_REQUEST);
        }
        List<Statistic> statistics = repository.findAll();
        for (Statistic oneStatistic : statistics){
            usedClassCodes.add(oneStatistic.getClassCode());
        }
        if (role.equals("Teacher")){

            int CODE_LENGTH = 6; 
            SecureRandom random = new SecureRandom();
            BigInteger randomBigInt;
            do {
                randomBigInt = new BigInteger(50, random);
                classCode = randomBigInt.toString(32).toUpperCase().substring(0, CODE_LENGTH);
            } while (usedClassCodes.contains(classCode));
            usedClassCodes.add(classCode);
        }
        else{
            if (role.equals("Student")){
            classCode = null;}
            else{
                return new ResponseEntity<>("The role has to be either 'Student' or 'Teacher'", HttpStatus.BAD_REQUEST);
            }
        }
        
        // A Human object WITHOUT ID will create a new record with default roles as student
        Statistic statistic = new Statistic(songCode, name, dou, classCode, role);
        statistic.setClassCode(classCode);
        repository.save(statistic);
        return new ResponseEntity<>(name +" created successfully", HttpStatus.CREATED);
    }

    /*
    The personSearch API looks across database for partial match to term (k,v) passed by RequestEntity body
     */
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> statisticSearch(@RequestBody final Map<String,String> map) {
        // extract term from RequestEntity
        String term = (String) map.get("term");

        // JPA query to filter on term
        List<Statistic> list = repository.findByNameContainingIgnoreCaseOrSongCodeContainingIgnoreCase(term, term);

        // return resulting list and status, error checking should be added
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    
}