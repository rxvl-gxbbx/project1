package com.rxvlvxr.dao;

import com.rxvlvxr.models.Book;
import com.rxvlvxr.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Optional<Book> show(int id) {
        return jdbcTemplate.queryForStream("SELECT * FROM book where id=?", new BeanPropertyRowMapper<>(Book.class), id).findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(title, author, year) values (?, ?, ?)", book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE book SET title=?, author=?, year=? WHERE id=?", updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?", null, id);
    }

    public Optional<Person> getPersonByBookId(int id) {
        return jdbcTemplate.queryForStream("SELECT person.id, person.name, person.birth_year FROM person JOIN public.book b on person.id = b.person_id WHERE b.id=?", new BeanPropertyRowMapper<>(Person.class), id).findAny();
    }

    public void assign(int bookId, int personId) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?", personId, bookId);
    }
}
