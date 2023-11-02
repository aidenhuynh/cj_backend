package com.nighthawk.spring_portfolio.mvc;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.nighthawk.spring_portfolio.mvc.human.Human;
import com.nighthawk.spring_portfolio.mvc.human.HumanApiController;
import com.nighthawk.spring_portfolio.mvc.human.HumanJpaRepository;
import com.nighthawk.spring_portfolio.mvc.jokes.Jokes;
import com.nighthawk.spring_portfolio.mvc.jokes.JokesJpaRepository;
import com.nighthawk.spring_portfolio.mvc.note.Note;
import com.nighthawk.spring_portfolio.mvc.note.NoteJpaRepository;
import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;
import com.nighthawk.spring_portfolio.mvc.song.Song;
import com.nighthawk.spring_portfolio.mvc.song.SongJpaRepository;
import com.nighthawk.spring_portfolio.mvc.statistic.Statistic;
import com.nighthawk.spring_portfolio.mvc.statistic.StatisticJpaRepository;

@Component
@Configuration // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  
    @Autowired JokesJpaRepository jokesRepo;
    @Autowired NoteJpaRepository noteRepo;
    @Autowired PersonDetailsService personService;
    @Autowired SongJpaRepository songRepo;
    @Autowired StatisticJpaRepository statisticRepo;
    @Autowired HumanJpaRepository humanRepo;
    @Autowired  // Inject PasswordEncoder
    private PasswordEncoder passwordEncoder;
    Set<String> usedClassCodes = HumanApiController.usedClassCodes;

    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {

            // Joke database is populated with starting jokes
            String[] jokesArray = Jokes.init();
            for (String joke : jokesArray) {
                List<Jokes> jokeFound = jokesRepo.findByJokeIgnoreCase(joke);  // JPA lookup
                if (jokeFound.size() == 0)
                    jokesRepo.save(new Jokes(null, joke, 0, 0)); //JPA save
            }

            // Person database is populated with test data
            Person[] personArray = Person.init();
            for (Person person : personArray) {
                //findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase
                List<Person> personFound = personService.list(person.getName(), person.getEmail());  // lookup
                if (personFound.size() == 0) {
                    personService.save(person);  // save

                    // Each "test person" starts with a "test note"
                    String text = "Test " + person.getEmail();
                    Note n = new Note(text, person);  // constructor uses new person as Many-to-One association
                    noteRepo.save(n);  // JPA Save                  
                }
            }

            Human[] humanArray = Human.init();
            
            for (Human human : humanArray){
                List<Human> humanFound = humanRepo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(human.getName(), human.getEmail());
                if (humanFound.size() == 0){
                    if(human.getRole() == "Teacher"){
                        String classCode = "";  
                        if (human.getClassCode() == null || classCode.length() == 0 ){
                            int CODE_LENGTH = 6; 
                            SecureRandom random = new SecureRandom();
                            BigInteger randomBigInt;
                            do {
                                randomBigInt = new BigInteger(50, random);
                                classCode = randomBigInt.toString(32).toUpperCase().substring(0, CODE_LENGTH);
                            } while (usedClassCodes.contains(classCode));
                            usedClassCodes.add(classCode);
                        }
                        human.setClassCode(classCode);
                        Statistic stat = new Statistic();
                        stat.setClassCode(classCode);
                        stat.setName(human.getName());
                        statisticRepo.save(stat);
                    }
                    
                    human.setPassword(passwordEncoder.encode(human.getPassword()));
                    humanRepo.save(human);
                    
                }
            }

            String[] songArray = Song.init();
            for (String song : songArray) {
                List<Song> songFound = songRepo.findBySongIgnoreCase(song);
                if (songFound.size() == 0)
                    songRepo.save(new Song(null, song, 0, 0));
            }

        };
    }
}






