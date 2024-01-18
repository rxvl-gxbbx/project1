package com.rxvlvxr.util;

import com.rxvlvxr.models.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class PersonYearValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        int currentYear = LocalDateTime.now(ZoneId.systemDefault()).getYear();
        int minAge = 14;

        if (person.getBirthYear() + minAge > currentYear)
            errors.rejectValue("birthYear",
                    "",
                    "Превышено максимальное допустимое значения года рождения: " + (currentYear - minAge));
    }
}
