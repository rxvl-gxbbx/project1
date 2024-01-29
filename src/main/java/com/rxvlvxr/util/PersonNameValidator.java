package com.rxvlvxr.util;


import com.rxvlvxr.dao.PersonDAO;
import com.rxvlvxr.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// класс валидации поля name на уникальность в сущности person
@Component
public class PersonNameValidator implements Validator {

    public final PersonDAO personDAO;

    // внедряем зависимость
    @Autowired
    public PersonNameValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        // проверяем уникальность по имени (полю name)
        personDAO.show(person.getName()).ifPresent(value -> errors.rejectValue("name", "", "Такой человек уже существует"));
    }
}
