package com.nighthawk.spring_portfolio.mvc.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;

/*
This class has an instance of Java Persistence API (JPA)
-- @Autowired annotation. Allows Spring to resolve and inject collaborating beans into our bean.
-- Spring Data JPA will generate a proxy instance
-- Below are some CRUD methods that we can use with our database
*/
@Service
@Transactional
public class StatisticsDetailsService implements UserDetailsService {  // "implements" ties ModelRepo to Spring Security
    // Encapsulate many object into a single Bean (Person, Roles, and Scrum)
    @Autowired  // Inject StatisticsJpaRepository
    private StatisticsJpaRepository StatisticsJpaRepository;
    @Autowired  // Inject RoleJpaRepository
    private StatisticsRoleJpaRepository StatisticsRoleJpaRepository;

    /* UserDetailsService Overrides and maps Person & Roles POJO into Spring Security */
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String SongCode) throws UsernameNotFoundException {
        Statistics statistics = StatisticsJpaRepository.findBySongCode(SongCode); // setting variable user equal to the method finding the username in the database
        if(statistics==null) {
			throw new UsernameNotFoundException("User not found with username: " + SongCode);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        statistics.getRoles().forEach(role -> { //loop through roles
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(role.getDob());
            authorities.add(new SimpleGrantedAuthority(formattedDate)); //create a SimpleGrantedAuthority by passed in role, adding it all to the authorities list, list of roles gets past in for spring security
        });
        // train spring security to User and Authorities
        return new org.springframework.security.core.userdetails.User(statistics.getSongCode(), statistics.getSongCode(), authorities);
    }

    /* Person Section */

    public  List<Statistics>listAll() {
        return StatisticsJpaRepository.findAllByOrderByNameAsc();
    }

    // custom query to find match to name or email
    public  List<Statistics>list(Date dob, String SongCode) {
        return StatisticsJpaRepository.findByDobContainingIgnoreCaseOrOrSongCodeContainingIgnoreCase(dob, SongCode);
    }

    // custom query to find anything containing term in name or email ignoring case
    public  List<Statistics>listLike(String term) {
        return StatisticsJpaRepository.findByDobContainingIgnoreCaseOrSongCodeContainingIgnoreCase(term, term);
    }

    // custom query to find anything containing term in name or email ignoring case
    public  List<Statistics>listLikeNative(String term) {
        String like_term = String.format("%%%s%%",term);  // Like required % rappers
        return StatisticsJpaRepository.findByLikeTermNative(like_term);
    }

        // encode password prior to sava
    public void save(Statistics statistics) {
        statistics.setTime(statistics.getTime());
        StatisticsJpaRepository.save(statistics);
    }

    public Statistics get(long id) {
        return (StatisticsJpaRepository.findById(id).isPresent())
                ? StatisticsJpaRepository.findById(id).get()
                : null;
    }

    public Statistics getBySongCode(String SongCode) {
        return (StatisticsJpaRepository.findBySongCode(SongCode));
    }

    public void delete(long id) {
        StatisticsJpaRepository.deleteById(id);
    }

    public void defaults(Integer time, Date roleDob) {
        for (Statistics statistics: listAll()) {
            if (statistics.getTime() == null) {
                statistics.setTime(time);
            }
            if (statistics.getRoles().isEmpty()) {
                StatisticsRole role = StatisticsRoleJpaRepository.findByDob(roleDob);
                if (role != null) { // verify role
                    statistics.getRoles().add(role);
                }
            }
        }
    }


    /* Roles Section */

    public void saveRole(StatisticsRole role) {
        StatisticsRole roleObj = StatisticsRoleJpaRepository.findByDob(role.getDob());
        if (roleObj == null) {  // only add if it is not found
            StatisticsRoleJpaRepository.save(role);
        }
    }

    public  List<StatisticsRole>listAllRoles() {
        return StatisticsRoleJpaRepository.findAll();
    }

    public StatisticsRole findRole(Date roleDob) {
        return StatisticsRoleJpaRepository.findByDob(roleDob);
    }

    public void addRoleToStatistics(String SongCode,  Date roleDob) { // by passing in the two strings you are giving the user that certain role
        Statistics statistics = StatisticsJpaRepository.findBySongCode(SongCode);
        if (statistics != null) {   // verify person
            StatisticsRole role = StatisticsRoleJpaRepository.findByDob(roleDob);
            if (role != null) { // verify role
                boolean addRole = true;
                for (StatisticsRole roleObj : statistics.getRoles()) {    // only add if user is missing role
                    if (roleObj.getDob().equals(roleDob)) {
                        addRole = false;
                        break;
                    }
                }
                if (addRole) statistics.getRoles().add(role);   // everything is valid for adding role
            }
        }
    }
    
}