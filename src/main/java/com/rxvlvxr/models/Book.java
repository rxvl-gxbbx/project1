package com.rxvlvxr.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Book {

    private int id;

    @NotBlank(message = "Поле названия книги должно быть заполнено")
    @Size(min = 2, max = 255, message = "Название должно быть в диапазоне от 2 до 100 символов")
    @Pattern(regexp = "[A-ZА-ЯЁ][a-zа-яё]+[\\sA-zА-яё]*", message = "Пример ввода: Солярис")
    private String title;

    @NotBlank(message = "Поле автора должно быть заполнено")
    @Size(min = 2, max = 100, message = "Имя должно быть в диапазоне от 2 до 100 символов")
    @Pattern(regexp = "[А-ЯЁ][а-яё]+\\s[А-ЯЁ][а-яё]+", message = "Пример ввода: Станислав Лем")
    private String author;
    @Min(value = 1801, message = "Год должен быть больше 1800")
    private int year;

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
