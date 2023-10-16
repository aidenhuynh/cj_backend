package com.nighthawk.spring_portfolio.mvc.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// Built using article: https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html
// or similar: https://asbnotebook.com/2020/04/11/spring-boot-thymeleaf-form-validation-example/
@Controller
@RequestMapping("/mvc/statistics")
public class StatisticsViewController {
    // Autowired enables Control to connect HTML and POJO Object to database easily for CRUD
    @Autowired
    private StatisticsDetailsService repository;

    @GetMapping("/read")
    public String statistics(Model model) {
        List<Statistics> list = repository.listAll();
        model.addAttribute("list", list);
        return "statistics/read";
    }

    /*  The HTML template Forms and PersonForm attributes are bound
        @return - template for person form
        @param - Person Class
    */
    @GetMapping("/create")
    public String statisticsAdd(Statistics statistics) {
        return "statistics/create";
    }

    /* Gathers the attributes filled out in the form, tests for and retrieves validation error
    @param - Person object with @Valid
    @param - BindingResult object
     */
    @PostMapping("/create")
    public String statisticsSave(@Valid Statistics statistics, BindingResult bindingResult) {
        // Validation of Decorated PersonForm attributes
        if (bindingResult.hasErrors()) {
            return "statistics/create";
        }
        repository.save(statistics);
        repository.addRoleToStatistics(statistics.getSongCode(), statistics.getDob());
        // Redirect to next step
        return "redirect:/mvc/statistics/read";
    }

    @GetMapping("/update/{id}")
    public String statisticsUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", repository.get(id));
        return "statistics/update";
    }

    @PostMapping("/update")
    public String statisticsUpdateSave(@Valid Statistics statistics, BindingResult bindingResult) {
        // Validation of Decorated PersonForm attributes
        if (bindingResult.hasErrors()) {
            return "statistics/update";
        }
        repository.save(statistics);
        repository.addRoleToStatistics(statistics.getSongCode(), statistics.getDob());

        // Redirect to next step
        return "redirect:/mvc/statistics/read";
    }

    @GetMapping("/delete/{id}")
    public String statisticsDelete(@PathVariable("id") long id) {
        repository.delete(id);
        return "redirect:/mvc/statistics/read";
    }

    @GetMapping("/search")
    public String statistics() {
        return "statistics/search";
    }

}