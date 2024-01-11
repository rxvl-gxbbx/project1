package com.rxvlvxr.controllers;

import com.rxvlvxr.dao.BookDAO;
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
    private final BookDAO bookDAO;
    private final PersonValidator personValidator;
    private final PersonYearValidator personYearValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, BookDAO bookDAO, PersonValidator personValidator, PersonYearValidator personYearValidator) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
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
        personValidator.validate(person, bindingResult);
        personYearValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);

        return "redirect:/people";
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

        model.addAttribute("books", bookDAO.index(id));

        return "people/show";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        personYearValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person);

        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);

        return "redirect:/people";
    }
}
