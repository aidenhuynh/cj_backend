package com.nighthawk.spring_portfolio.mvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.nighthawk.spring_portfolio.mvc.jokes.Jokes;
import com.nighthawk.spring_portfolio.mvc.jokes.JokesJpaRepository;
import com.nighthawk.spring_portfolio.mvc.note.Note;
import com.nighthawk.spring_portfolio.mvc.note.NoteJpaRepository;
import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;
import com.nighthawk.spring_portfolio.mvc.song.Song;
import com.nighthawk.spring_portfolio.mvc.song.SongJpaRepository;
import com.nighthawk.spring_portfolio.mvc.statistics.Statistics;
import com.nighthawk.spring_portfolio.mvc.statistics.StatisticsDetailsService;



import java.util.List;

@Component
@Configuration // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  
    @Autowired JokesJpaRepository jokesRepo;
    @Autowired NoteJpaRepository noteRepo;
    @Autowired PersonDetailsService personService;
    @Autowired SongJpaRepository songRepo;
    @Autowired StatisticsDetailsService statisticsService;

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
            Statistics[] statisticsArray = Statistics.init();
            for (Statistics statistics : statisticsArray) {
                //findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase
                List<Statistics> statisticsFound = statisticsService.list(statistics.getDob(), statistics.getSongCode());  // lookup
                if (statisticsFound.size() == 0) {
                    statisticsService.save(statistics);  // save

                    // Each "test person" starts with a "test note"
                    String text = "Test " + statistics.getSongCode();
                    Note n = new Note(text, statistics);  // constructor uses new person as Many-to-One association
                    noteRepo.save(n);  // JPA Save                  
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






