package com.rxvlvxr.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Person {
    private int personId;
    @NotBlank(message = "Поле имени должно быть заполнено")
    @Size(min = 2, max = 100, message = "Имя должно быть в диапазоне от 2 до 100 символов")
    @Pattern(regexp = "[А-ЯЁ][а-яё]+\\s[А-ЯЁ][а-яё]+\\s[А-ЯЁ][а-яё]+", message = "Пример ввода: Габараев Рауль Зурабович")
    private String name;
    @Min(value = 1886, message = "Год рождения должен быть выше 1885")
    private int birthYear;

    public Person() {
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}