package com.nighthawk.spring_portfolio.mvc.statistics;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

public interface  StatisticsRoleJpaRepository extends JpaRepository<StatisticsRole, Long> {
    StatisticsRole findByDob(Date dob);
}
