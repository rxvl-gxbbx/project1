package com.rxvlvxr.controllers;

import com.rxvlvxr.dao.BookDAO;
import com.rxvlvxr.dao.PersonDAO;
import com.rxvlvxr.models.Book;
import com.rxvlvxr.models.Person;
import com.rxvlvxr.util.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final BookValidator bookValidator;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO, BookValidator bookValidator) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.bookValidator = bookValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());

        return "books/index";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        String view;

        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors())
            view = "books/new";
        else {
            bookDAO.save(book);
            view = "redirect:/books";
        }

        return view;
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {

        bookDAO.show(id).ifPresent(value -> model.addAttribute("book", value));
        personDAO.join(id).ifPresent(value -> model.addAttribute("person", value));

        return "books/edit";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("emptyPerson") Person person) {

        bookDAO.show(id).ifPresent(value -> model.addAttribute("book", value));
        personDAO.join(id).ifPresentOrElse(value -> model.addAttribute("person", value),
                () -> model.addAttribute("people", personDAO.index()));

        return "books/show";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @ModelAttribute("emptyPerson") Person person,
                         Model model) {
        String view;

        bookValidator.validate(book, bindingResult);

        model.addAttribute("person", person);

        if (bindingResult.hasErrors()) {
            view = "books/edit";
        } else {
            if (person.getPersonId() == 0) bookDAO.update(id, book);
            else bookDAO.update(id, person.getPersonId(), book);

            view = "redirect:/books";
        }

        return view;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);

        return "redirect:/books";
    }
}
