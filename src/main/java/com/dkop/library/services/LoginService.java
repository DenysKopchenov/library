package com.dkop.library.services;

import java.util.Map;

import static com.dkop.library.services.RegexContainer.EMAIL_VALIDATION;
import static com.dkop.library.services.RegexContainer.PASSWORD_VALIDATION;

public class LoginService {

    public boolean validate(String email, String password, Map<String, String> errors){
        boolean isValid = true;
        if (!email.matches(EMAIL_VALIDATION)) {
            errors.put("email", "E-mail is invalid. Must contains '@'");
            isValid = false;
        }
        if (!password.matches(PASSWORD_VALIDATION)){
            errors.put("password", "Your password must be 8-20 characters long and must contain number, uppercase and lowercase letter, special character");
            isValid = false;
        }
        return isValid;
    }

}
