package com.nighthawk.spring_portfolio.mvc.human;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HumanDetailsService  implements UserDetailsService{
    @Autowired  // Inject PersonJpaRepository
    private HumanJpaRepository humanJpaRepository;

    @Autowired  // Inject PasswordEncoder
    private PasswordEncoder passwordEncoder;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Human human = humanJpaRepository.findByEmail(email); // setting variable user equal to the method finding the username in the database
        if(human==null) {
			throw new UsernameNotFoundException("User not found with username: " + email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String role = human.getRole();
        authorities.add(new SimpleGrantedAuthority(role));
        // train spring security to User and Authorities
        return new org.springframework.security.core.userdetails.User(human.getEmail(), human.getPassword(), authorities);
    }

     public  List<Human>listAll() {
        return humanJpaRepository.findAllByOrderByNameAsc();
    }

    // custom query to find match to name or email
    public  List<Human>list(String name, String email) {
        return humanJpaRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(name, email);
    }

    // custom query to find anything containing term in name or email ignoring case
    public  List<Human>listLike(String term) {
        return humanJpaRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);
    }

    // custom query to find anything containing term in name or email ignoring case
    public  List<Human>listLikeNative(String term) {
        String like_term = String.format("%%%s%%",term);  // Like required % rappers
        return humanJpaRepository.findByLikeTermNative(like_term);
    }

    // encode password prior to save
    public void save(Human human) {
        human.setPassword(passwordEncoder.encode(human.getPassword()));
        humanJpaRepository.save(human);
    }

    public Human get(long id) {
        return (humanJpaRepository.findById(id).isPresent())
                ? humanJpaRepository.findById(id).get()
                : null;
    }

    public Human getByEmail(String email) {
        return (humanJpaRepository.findByEmail(email));
    }

    public void delete(long id) {
        humanJpaRepository.deleteById(id);
    }

}
