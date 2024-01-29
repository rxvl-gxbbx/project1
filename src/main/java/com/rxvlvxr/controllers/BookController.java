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

    // внедрение зависимостей
    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO, BookValidator bookValidator) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.bookValidator = bookValidator;
    }

    // GET запрос, который возвращает страницу index.html со всеми данными из таблицы book
    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());

        return "books/index";
    }

    // POST запрос, который при успешной валидации сохраняет объект типа Book в таблицу
    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        String view;

        // валидация
        bookValidator.validate(book, bindingResult);

        // в случае ошибок будет возвращена страница добавления книги с требованиям валидации
        if (bindingResult.hasErrors()) view = "books/new";
        else {
            bookDAO.save(book);
            view = "redirect:/books";
        }

        return view;
    }

    // возвращает страницу для добавления книги
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    // возвращает страницу редактирования книги по id
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        bookDAO.show(id).ifPresent(value -> model.addAttribute("book", value));

        return "books/edit";
    }

    // возвращает страницу книги по id
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("emptyPerson") Person person) {

        bookDAO.show(id).ifPresent(value -> model.addAttribute("book", value));
        // в случае если книга находится у человека, то вернется держатель книги
        // если нет, то будет возвращен список всех доступных людей для назначения книги
        bookDAO.getPersonByBookId(id).ifPresentOrElse(
                value -> model.addAttribute("person", value),
                () -> model.addAttribute("people", personDAO.index()));

        return "books/show";
    }

    // PATCH запрос для редактирования книги
    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        String view;

        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) view = "books/edit";
        else {
            bookDAO.update(id, book);
            view = "redirect:/books";
        }

        return view;
    }

    // DELETE запрос, который удаляет строку из таблицы по id
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);

        return "redirect:/books";
    }

    // PATCH запрос при котором книга "освобождается"
    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        // в строке таблицы значению person_id присваивается null
        bookDAO.release(id);

        return "redirect:/books";
    }

    // PATCH запрос при котором книге присваивается человек
    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int bookId,
                         @ModelAttribute("emptyPerson") Person person) {
        int personId = person.getId();

        // в строке таблицы person_id присваивается значение из объекта Person, который был передан через @ModelAttribute
        bookDAO.assign(bookId, personId);

        return "redirect:/books";
    }
}
