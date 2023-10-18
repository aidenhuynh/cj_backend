package com.nighthawk.spring_portfolio.mvc.statistics;

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
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Statistics {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // email, password, roles are key attributes to login and authentication
    @NotEmpty
    @Column(unique=false)
    private String SongCode;  // the song code, provided by spotify

    @NotEmpty
    private Integer time; // the time that the song was uploaded

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;  //the date that the song was uploaded

    // To be implemented
    @ManyToMany(fetch = EAGER)
    private Collection<StatisticsRole> roles = new ArrayList<>();

    /* HashMap is used to store JSON for daily "stats"
    "stats": {
        "2022-11-13": {
            "calories": 2200,
            "steps": 8000
        }
    }
    */
    @Type(type="json")
    @Column(columnDefinition = "jsonb")
    private Map<String,Map<String, Object>> stats = new HashMap<>(); 
    

    // Constructor used when building object from an API
    public Statistics(String SongCode, int time, Date dob) {
        this.SongCode = SongCode;
        this.time = time;
        this.dob = dob;
    }

    // A custom getter to return age from dob attribute
    public int getAge() {
        if (this.dob != null) {
            LocalDate birthDay = this.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(birthDay, LocalDate.now()).getYears(); }
        return -1;
    }

    // Initialize static test data 
    public static Statistics[] init() {

        // basics of class construction
        Statistics p1 = new Statistics();
        p1.setSongCode("g2342tefasfdsad");
        p1.setTime(12490);
        // adding Note to notes collection
        try {  // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1840");
            p1.setDob(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }

        Statistics p2 = new Statistics();
        p1.setSongCode("6gj839kbhyvfn478");
        p1.setTime(12490);
        // adding Note to notes collection
        try {  // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1840");
            p1.setDob(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }

        Statistics p3 = new Statistics();
        p1.setSongCode("7h58j439k9df2jn");
        p1.setTime(121120);
        // adding Note to notes collection
        try {  // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1840");
            p1.setDob(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }

        Statistics p4 = new Statistics();
        p1.setSongCode("67gykh832a09j2k");
        p1.setTime(12389);
        // adding Note to notes collection
        try {  // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1840");
            p1.setDob(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }

        Statistics p5 = new Statistics();
        p1.setSongCode("ubwinf8uh34f");
        p1.setTime(1213590);
        // adding Note to notes collection
        try {  // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1840");
            p1.setDob(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }

        // Array definition and data initialization
        Statistics statisticss[] = {p1, p2, p3, p4, p5};
        return(statisticss);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Statistics statisticss[] = init();

        // iterate using "enhanced for loop"
        for( Statistics statistic : statisticss) {
            System.out.println(statistic);  // print object
        }
    }

}