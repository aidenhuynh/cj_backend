package com.nighthawk.spring_portfolio.mvc.human;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import static javax.persistence.FetchType.EAGER;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/*
Person is a POJO, Plain Old Java Object.
First set of annotations add functionality to POJO
--- @Setter @Getter @ToString @NoArgsConstructor @RequiredArgsConstructor
The last annotation connect to database
--- @Entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TypeDef(name="json", typeClass = JsonType.class)
public class Human {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // email, password, roles are key attributes to login and authentication
    @NotEmpty
    @Size(min=5)
    @Column(unique=true)
    @Email
    private String email;

    @NotEmpty
    private String password;

    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max = 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Size(min = 2, max = 30, message = "Name (2 to 30 chars)")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    private String classCode;

    @NonNull
    private String role;
    /* HashMap is used to store JSON for daily "stats"
    "stats": {
        "2022-11-13": {
            "calories": 2200,
            "steps": 8000
        }
    }
    */
    

    // Constructor used when building object from an API
    public Human(String email, String password, String name, Date dob, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.role = role;
        
    }

    // A custom getter to return age from dob attribute
    public int getAge() {
        if (this.dob != null) {
            LocalDate birthDay = this.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(birthDay, LocalDate.now()).getYears(); }
        return -1;
    }

    // Initialize static test data 
    public static Human[] init() {

        // basics of class construction
        Human p1 = new Human();
        p1.setName("Thomas Edison");
        p1.setEmail("toby@gmail.com");
        p1.setPassword("123Toby!");
        p1.setRole("Teacher");
        // adding Note to notes collection
        try {  // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1840");
            p1.setDob(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }

        Human p2 = new Human();
        p2.setName("Alexander Graham Bell");
        p2.setEmail("lexb@gmail.com");
        p2.setPassword("123LexB!");
        p2.setRole("Teacher");
        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1845");
            p2.setDob(d);
        } catch (Exception e) {
        }

        Human p3 = new Human();
        p3.setName("Nikola Tesla");
        p3.setEmail("niko@gmail.com");
        p3.setPassword("123Niko!");
        p3.setRole("Student");
        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1850");
            p3.setDob(d);
        } catch (Exception e) {
        }

        Human p4 = new Human();
        p4.setName("Madam Currie");
        p4.setEmail("madam@gmail.com");
        p4.setPassword("123Madam!");
        p4.setRole("Student");
        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1860");
            p4.setDob(d);
        } catch (Exception e) {
        }

        Human p5 = new Human();
        p5.setName("John Mortensen");
        p5.setEmail("jm1021@gmail.com");
        p5.setPassword("123Qwerty!");
        p5.setRole("Student");
        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("10-21-1959");
            p5.setDob(d);
        } catch (Exception e) {
        }

        // Array definition and data initialization
        Human persons[] = {p1, p2, p3, p4, p5};
        return(persons);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Human persons[] = init();

        // iterate using "enhanced for loop"
        for( Human person : persons) {
            System.out.println(person);  // print object
        }
    }

}