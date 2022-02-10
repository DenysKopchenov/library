package com.dkop.library.services;

import java.util.HashMap;
import java.util.Map;

import static com.dkop.library.services.RegexContainer.EMAIL_VALIDATION;
import static com.dkop.library.services.RegexContainer.PASSWORD_VALIDATION;

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
}
