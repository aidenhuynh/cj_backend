package com.nighthawk.spring_portfolio.mvc.human;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.jwt.JwtTokenUtil;

import java.util.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/human")
public class HumanApiController {
    //     @Autowired
    // private JwtTokenUtil jwtGen;
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private HumanJpaRepository repository;
    @Autowired  // Inject PasswordEncoder
    private PasswordEncoder passwordEncoder;
    Set<String> usedClassCodes = new HashSet<>();
    @Autowired
    private JwtTokenUtil jwtUtil;

    /*
    GET List of People
     */
    @GetMapping("/")
    public ResponseEntity<List<Human>> getPeople() {
        return new ResponseEntity<>( repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    /*
    GET individual Human using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Human> getHuman(@PathVariable long id) {
        
        Optional<Human> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Human human = optional.get();  // value from findByID
            return new ResponseEntity<>(human, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Human> deleteHuman(@PathVariable long id) {
        Optional<Human> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Human human = optional.get();  // value from findByID
            repository.deleteById(id);  // value from findByID
            return new ResponseEntity<>(human, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    /*
    POST Aa record by Requesting Parameters from URI
     */
    @PostMapping( "/post")
    public ResponseEntity<Object> postHuman(@RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam("name") String name,
                                             @RequestParam("dob") String dobString,
                                             @RequestParam("role") String role) {
        Date dob;
        try {
            dob = new SimpleDateFormat("MM-dd-yyyy").parse(dobString);
        } catch (Exception e) {
            return new ResponseEntity<>(dobString +" error; try MM-dd-yyyy", HttpStatus.BAD_REQUEST);
        }
        password = passwordEncoder.encode(password);
        List<Human> humans = repository.findAll();
        for (Human hman : humans){
            usedClassCodes.add(hman.getClassCode());
        }
        String classCode = "";  
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
        Human human = new Human(email, password, name, dob, role);
        human.setClassCode(classCode);
        repository.save(human);
        return new ResponseEntity<>(email +" created successfully", HttpStatus.CREATED);
    }

    
    /*
    The personSearch API looks across database for partial match to term (k,v) passed by RequestEntity body
     */
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> humanSearch(@RequestBody final Map<String,String> map) {
        // extract term from RequestEntity
        String term = (String) map.get("term");

        // JPA query to filter on term
        List<Human> list = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

        // return resulting list and status, error checking should be added
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    
}