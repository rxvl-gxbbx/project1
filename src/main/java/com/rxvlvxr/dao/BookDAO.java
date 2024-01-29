package com.rxvlvxr.dao;

import com.rxvlvxr.models.Book;
import com.rxvlvxr.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

// даем понять Spring-у что это Bean (объект Spring)
@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    // внедрение зависимости
    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // возвращаем список объекта Book (все книги из таблицы book) с помощью JdbcTemplate прописав SQL запрос
    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    // узнаем есть ли объект Book по переданному id
    public Optional<Book> show(int id) {
        return jdbcTemplate.queryForStream("SELECT * FROM book where id=?", new BeanPropertyRowMapper<>(Book.class), id).findAny();
    }

    // сохраняем объект типа Book в таблицу
    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(title, author, year) values (?, ?, ?)", book.getTitle(), book.getAuthor(), book.getYear());
    }

    // редактируем данные в таблице book по id
    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE book SET title=?, author=?, year=? WHERE id=?", updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    // удаляем строку из таблицы по id
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    // назначаем строке таблицы в поле person_id значение null (освобождаем книгу)
    public void release(int id) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?", null, id);
    }

    // получаем объект типа Person по id сущности book, так как person_id в этой сущности - внешний ключ
    public Optional<Person> getPersonByBookId(int id) {
        return jdbcTemplate.queryForStream("SELECT person.id, person.name, person.birth_year FROM person JOIN public.book b on person.id = b.person_id WHERE b.id=?", new BeanPropertyRowMapper<>(Person.class), id).findAny();
    }

    // назначаем книгу человеку по id этих сущностей
    public void assign(int bookId, int personId) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?", personId, bookId);
    }
}
