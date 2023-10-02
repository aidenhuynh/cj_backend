package com.nighthawk.spring_portfolio.mvc.song;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// JPA is an object-relational mapping (ORM) to persistent data, originally relational databases (SQL). Today JPA implementations has been extended for NoSQL.
public interface SongJpaRepository extends JpaRepository<Song, Long> {
    // JPA has many built in methods, these few have been prototyped for this application
    void save(String Song);
    List<Song> findAllByOrderBySongAsc();
    List<Song> findBySongIgnoreCase(String song);
}
