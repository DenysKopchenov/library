package com.dkop.library.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.dkop.library.utils.Fields.EMAIL;
import static com.dkop.library.utils.LocalizationUtil.localizationBundle;
import static com.dkop.library.utils.RegexContainer.*;

public final class Validator {

    private Validator() {
    }

    public static Map<String, String> validateLoginForm(String email, String password) {
        Map<String, String> errors = new HashMap<>();
        if (!email.matches(EMAIL_VALIDATION)) {
            errors.put(EMAIL, localizationBundle.getString("validation.email"));
        }
        if (!password.matches(PASSWORD_VALIDATION)) {
            errors.put("password", localizationBundle.getString("validation.password"));
        }
        return errors;
    }

    public static Map<String, String> validateBookForm(String title, String author, String publisher, String publishingDate, String amount) {
        Map<String, String> errors = new HashMap<>();
        if (!title.matches(BOOK_VALIDATION)) {
            errors.put("title", localizationBundle.getString("not.valid"));
        }
        if (!author.matches(BOOK_VALIDATION)) {
            errors.put("author", localizationBundle.getString("not.valid"));
        }
        if (!publisher.matches(BOOK_VALIDATION)) {
            errors.put("publisher", localizationBundle.getString("not.valid"));
        }
        if (!amount.matches("\\d+")) {
            errors.put("amount", localizationBundle.getString("amount"));
        }
        if (!publishingDate.matches(DATE_FORMAT_VALIDATION)) {
            errors.put("publishingDate", localizationBundle.getString("publishing.date"));
        } else {
            LocalDate dateToValidate = LocalDate.parse(publishingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (dateToValidate.isAfter(LocalDate.now())) {
                errors.put("publishingDate", localizationBundle.getString("publishing.date.after"));
            }
        }
        return errors;
    }

    public static Map<String, String> validateRegistrationForm(String firstName, String lastName, String password, String confirmPassword, String email) {
        Map<String, String> errors = new HashMap<>();
        if (!firstName.matches(RegexContainer.NAME_VALIDATION)) {
            errors.put("firstName", localizationBundle.getString("validation.first.name"));
        }
        if (!lastName.matches(RegexContainer.NAME_VALIDATION)) {
            errors.put("lastName", localizationBundle.getString("validation.last.name"));
        }
        if (!password.matches(RegexContainer.PASSWORD_VALIDATION)) {
            errors.put("password", localizationBundle.getString("validation.password"));
        }
        if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", localizationBundle.getString("confirm.password"));
        }
        if (!email.matches(RegexContainer.EMAIL_VALIDATION)) {
            errors.put(EMAIL, localizationBundle.getString("validation.email"));
        }
        return errors;
    }
}
