package com.dkop.library.services;

import com.dkop.library.model.User;

import java.util.Map;

public class RegistrationService {

    public boolean validate(String firstName, String lastName, String password, String confirmPassword, String email, User user, Map<String, String> errors) {
        boolean isValid = true;
        if (firstName.matches(RegexContainer.NAME_VALIDATION)) {
            user.setFirstName(firstName);
        } else {
            errors.put("firstName", "First name may contains only letters, must starts with uppercase letter");
            isValid = false;
        }
        if (lastName.matches(RegexContainer.NAME_VALIDATION)) {
            user.setLastName(lastName);
        } else {
            errors.put("lastName", "Last name may contains only letters, must starts with uppercase letter");
            isValid = false;
        }
        if (password.matches(RegexContainer.PASSWORD_VALIDATION)) {
            user.setPassword(password);
        } else {
            errors.put("password", "Your password must be 8-20 characters long and must contain number, uppercase and lowercase letter, special character");
            isValid = false;
        }
        if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Does not match to password");
            isValid = false;
        }
        if (email.matches(RegexContainer.EMAIL_VALIDATION)) {
            user.setEmail(email);
        } else {
            errors.put("email", "E-mail is invalid. Must contains '@'");
            isValid = false;
        }
        return isValid;
    }
}
