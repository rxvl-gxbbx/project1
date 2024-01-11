package com.rxvlvxr.dao;

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
        return jdbcTemplate.queryForStream("SELECT * FROM person WHERE person_id=?", new BeanPropertyRowMapper<>(Person.class), bookId).findAny();
    }

    public Optional<Person> show(String name) {
        return jdbcTemplate.queryForStream("SELECT * FROM person WHERE name=?", new BeanPropertyRowMapper<>(Person.class), name).findAny();
    }

    public Optional<Person> join(int bookId) {
        return jdbcTemplate.queryForStream("SELECT person.person_id, person.name, person.birth_year FROM project1.public.person JOIN book ON person.person_id=book.person_id WHERE book.book_id=?", new BeanPropertyRowMapper<>(Person.class), bookId).findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, birth_year) VALUES(?, ?)", person.getName(), person.getBirthYear());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET name=?, birth_year=? WHERE person_id=?", updatedPerson.getName(), updatedPerson.getBirthYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE person_id=?", id);
    }
}
