package com.rxvlvxr.util;


import com.rxvlvxr.dao.PersonDAO;
import com.rxvlvxr.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    public final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        personDAO.show(person.getName()).ifPresent(value -> errors.rejectValue("name", "", "Такой человек уже существует"));
    }
}
