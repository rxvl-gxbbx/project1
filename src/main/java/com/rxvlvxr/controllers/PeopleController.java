package com.rxvlvxr.controllers;

import com.rxvlvxr.dao.PersonDAO;
import com.rxvlvxr.models.Person;
import com.rxvlvxr.util.PersonValidator;
import com.rxvlvxr.util.PersonYearValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final PersonValidator personValidator;
    private final PersonYearValidator personYearValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator, PersonYearValidator personYearValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
        this.personYearValidator = personYearValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());

        return "people/index";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        String view;

        personValidator.validate(person, bindingResult);
        personYearValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            view = "people/new";
        else {
            view = "redirect:/people";
            personDAO.save(person);
        }

        return view;
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        personDAO.show(id).ifPresent(value -> model.addAttribute("person", value));

        return "people/edit";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        personDAO.show(id).ifPresent(value -> model.addAttribute("person", value));

        model.addAttribute("books", personDAO.getBooksByPersonId(id));

        return "people/show";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        String view;

        personYearValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            view = "people/edit";
        else {
            view = "redirect:/people";
            personDAO.update(id, person);
        }

        return view;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);

        return "redirect:/people";
    }
}
