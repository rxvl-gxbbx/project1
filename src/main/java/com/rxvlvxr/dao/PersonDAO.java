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
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> show(int bookId) {
        return jdbcTemplate.queryForStream("SELECT * FROM person WHERE id=?", new BeanPropertyRowMapper<>(Person.class), bookId).findAny();
    }

    public Optional<Person> show(String name) {
        return jdbcTemplate.queryForStream("SELECT * FROM person WHERE name=?", new BeanPropertyRowMapper<>(Person.class), name).findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, birth_year) VALUES(?, ?)", person.getName(), person.getBirthYear());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET name=?, birth_year=? WHERE id=?", updatedPerson.getName(), updatedPerson.getBirthYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    // получаем список всех объектов типа Book по person_id (внешний ключ)
    // т.е. все книги у которых person_id соответствует параметру personId
    public List<Book> getBooksByPersonId(int personId) {
        return jdbcTemplate.query("SELECT * FROM book WHERE person_id=?", new BeanPropertyRowMapper<>(Book.class), personId);
    }
}
