package com.dkop.library.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.dkop.library.utils.Fields.EMAIL;
import static com.dkop.library.utils.LocalizationUtil.errorMessagesBundle;
import static com.dkop.library.utils.RegexContainer.*;

public class Validator {

    private Validator() {
    }

    public static Map<String, String> validateLoginForm(String email, String password) {
        Map<String, String> errors = new HashMap<>();
        if (!email.matches(EMAIL_VALIDATION)) {
            errors.put(EMAIL, errorMessagesBundle.getString(EMAIL));
        }
        if (!password.matches(PASSWORD_VALIDATION)) {
            errors.put("password", errorMessagesBundle.getString("password"));
        }
        return errors;
    }

    public static Map<String, String> validateBookForm(String title, String author, String publisher, String publishingDate, String amount) {
        Map<String, String> errors = new HashMap<>();
        if (!title.matches(BOOK_VALIDATION)) {
            errors.put("title", errorMessagesBundle.getString("not.valid"));
        }
        if (!author.matches(BOOK_VALIDATION)) {
            errors.put("author", errorMessagesBundle.getString("not.valid"));
        }
        if (!publisher.matches(BOOK_VALIDATION)) {
            errors.put("publisher", errorMessagesBundle.getString("not.valid"));
        }
        if (!amount.matches("\\d+")) {
            errors.put("amount", errorMessagesBundle.getString("amount"));
        }
        if (!publishingDate.matches(DATE_FORMAT_VALIDATION)) {
            errors.put("publishingDate", errorMessagesBundle.getString("publishing.date"));
        } else {
            LocalDate dateToValidate = LocalDate.parse(publishingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (dateToValidate.isAfter(LocalDate.now())) {
                errors.put("publishingDate", errorMessagesBundle.getString("publishing.date.after"));
            }
        }
        return errors;
    }

    public static Map<String, String> validateRegistrationForm(String firstName, String lastName, String password, String confirmPassword, String email) {
        Map<String, String> errors = new HashMap<>();
        if (!firstName.matches(RegexContainer.NAME_VALIDATION)) {
            errors.put("firstName", errorMessagesBundle.getString("first.name"));
        }
        if (!lastName.matches(RegexContainer.NAME_VALIDATION)) {
            errors.put("lastName", errorMessagesBundle.getString("last.name"));
        }
        if (!password.matches(RegexContainer.PASSWORD_VALIDATION)) {
            errors.put("password", errorMessagesBundle.getString("password"));
        }
        if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", errorMessagesBundle.getString("confirm.password"));
        }
        if (!email.matches(RegexContainer.EMAIL_VALIDATION)) {
            errors.put(EMAIL, errorMessagesBundle.getString(EMAIL));
        }
        return errors;
    }
}
