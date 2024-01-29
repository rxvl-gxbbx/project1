package com.rxvlvxr.util;

import com.rxvlvxr.models.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.ZoneId;

// класс для валидации возраста для регистрации в библиотеке
@Component
public class PersonYearValidator implements Validator {

    // указываем что эта валидация относится к классу Person
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        int currentYear = LocalDateTime.now(ZoneId.systemDefault()).getYear();
        // минимальный возраст для регистрации в библиотеке
        int minAge = 14;

        // валидируем поле
        if (person.getBirthYear() + minAge > currentYear)
            errors.rejectValue("birthYear",
                    "",
                    "Превышено максимальное допустимое значения года рождения: " + (currentYear - minAge));
    }
}
