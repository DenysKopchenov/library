package com.dkop.library.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.dkop.library.services.RegexContainer.*;

public class Validator {
    private Validator() {
    }

    public static Map<String, String> validateLoginForm(String email, String password) {
        Map<String, String> errors = new HashMap<>();
        if (!email.matches(EMAIL_VALIDATION)) {
            errors.put("email", "E-mail is invalid. Must contains '@'");
        }
        if (!password.matches(PASSWORD_VALIDATION)) {
            errors.put("password", "Your password must be 8-20 characters long and must contain number, uppercase and lowercase letter, special character");
        }
        return errors;
    }

    public static Map<String, String> validateBookForm(String title, String author, String publisher, String publishingDate, String amount) {
        Map<String, String> errors = new HashMap<>();
        if (!title.matches(BOOK_VALIDATION)) {
            errors.put("title", "Not Valid");
        }
        if (!author.matches(BOOK_VALIDATION)) {
            errors.put("author", "Not Valid");
        }
        if (!publisher.matches(BOOK_VALIDATION)) {
            errors.put("publisher", "Not Valid");
        }
        if (!amount.matches("\\d+")) {
            errors.put("amount", "Can not be negative, 0, or fraction");
        }
        if (!publishingDate.matches(DATE_FORMAT_VALIDATION)) {
            errors.put("publishingDate", "Not Valid. Input in format yyyy.MM.dd");
        } else {
            LocalDate dateToValidate = LocalDate.parse(publishingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (dateToValidate.isAfter(LocalDate.now())) {
                errors.put("publishingDate", "Can not be after today");
            }
        }
        return errors;
    }
}
