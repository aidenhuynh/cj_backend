package com.nighthawk.spring_portfolio.mvc.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methodsaa
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
public interface StatisticJpaRepository extends JpaRepository<Statistic, Long> {
    Statistic findByName(String name);

    List<Statistic> findAllByOrderByNameAsc();

    // JPA query, findBy does JPA magic with "Name", "Containing", "Or", "SongCode", "IgnoreCase"
    List<Statistic> findByNameContainingIgnoreCaseOrSongCodeContainingIgnoreCase(String name, String songCode);

    /* Custom JPA query articles, there are articles that show custom SQL as well
       https://springframework.guru/spring-data-jpa-query/
       https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
    */
    Statistic findByNameAndClassCode(String name, String classCode);

    // Custom JPA query
    @Query(
            value = "SELECT * FROM Statistic p WHERE p.name LIKE ?1 or p.songCode LIKE ?1",
            nativeQuery = true)
    List<Statistic> findByLikeTermNative(String term);
    /*
      https://www.baeldung.com/spring-data-jpa-query
    */
}