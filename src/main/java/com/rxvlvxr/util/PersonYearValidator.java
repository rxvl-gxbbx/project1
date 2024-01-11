package com.rxvlvxr.util;

import com.rxvlvxr.models.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.Year;

@Component
public class PersonYearValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        int currentYear = Year.now().getValue();

        if (person.getBirthYear() + 14 > currentYear)
            errors.rejectValue("birthYear",
                    "",
                    "Превышено максимальное допустимое значения года рождения: " + (currentYear - 14));
    }
}
