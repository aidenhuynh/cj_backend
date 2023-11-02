package com.nighthawk.spring_portfolio.mvc.statistic;

/*import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;*/
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
//import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/*import javax.persistence.ManyToMany;
import static javax.persistence.FetchType.EAGER;
import javax.validation.constraints.Email;*/
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
//import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

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
public class Statistic {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // email, password, roles are key attributes to login and authentication
    private String songCode;

    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max = 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Size(min = 2, max = 40, message = "Name (2 to 40 chars)")
    private String name;

    @NotEmpty
    private String classCode;

    @NonNull
    private String role; // student or teacher
    
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
    public Statistic(String songCode, String name, String classCode) {
        this.songCode = songCode;
        this.name = name;
        this.classCode = classCode;
    }

    // Initialize static test data 
    public static Statistic[] init() {

        // basics of class construction
        Statistic p1 = new Statistic();
        p1.setName("AJ Ruiz");
        p1.setSongCode("3K4HG9evC7dg3N0R9cYqk4");
        p1.setClassCode("33214738144urethassq");

        Statistic p2 = new Statistic();
        p2.setName("AJ Ruiz");
        p2.setSongCode("3Z9ygRxfUsSTXccm7YfHtA");
        p2.setClassCode("29104738291ureikdfsl");

        Statistic p3 = new Statistic();
        p3.setName("Hideous Kojingles");
        p3.setSongCode("2rFXXOEuktZ1xk4vj42Nts");
        p3.setClassCode("29104738291ureikdfsl");


        Statistic p4 = new Statistic();
        p4.setName("Asher Rivera");
        p4.setSongCode("0Bo5fjMtTfCD8vHGebivqc");
        p4.setClassCode("33214738144urethassq");


        Statistic p5 = new Statistic();
        p5.setName("John Smith");
        p5.setSongCode("3Qq7nbEvgBzhtsO2fMnXvf");
        p5.setClassCode("14410143130smokemoke");


        // Array definition and data initialization
        Statistic persons[] = {p1, p2, p3, p4, p5};
        return(persons);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Statistic persons[] = init();

        // iterate using "enhanced for loop"
        for( Statistic person : persons) {
            System.out.println(person);  // print object
        }
    }

}