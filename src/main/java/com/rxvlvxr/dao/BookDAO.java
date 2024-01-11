package com.rxvlvxr.dao;

import com.rxvlvxr.models.Book;
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

    public List<Book> index(int personId) {
        return jdbcTemplate.query("SELECT * FROM book WHERE person_id=?", new BeanPropertyRowMapper<>(Book.class), personId);
    }

    public Optional<Book> show(int id) {
        return jdbcTemplate.queryForStream("SELECT * FROM book where book_id=?", new BeanPropertyRowMapper<>(Book.class), id).findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(title, author, year) values (?, ?, ?)", book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void update(int bookId, int personId, Book updatedBook) {
        jdbcTemplate.update("UPDATE book SET person_id=?, title=?, author=?, year=? WHERE book_id=?", personId, updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getYear(), bookId);
    }

    public void update(int bookId, Book updatedBook) {
        jdbcTemplate.update("UPDATE book SET person_id=?, title=?, author=?, year=? WHERE book_id=?", null, updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getYear(), bookId);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE book_id=?", id);
    }
}
