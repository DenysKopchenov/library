package com.dkop.library.utils;

public final class RegexContainer {

    public static final String PASSWORD_VALIDATION =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"; //https://mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
    public static final String EMAIL_VALIDATION =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"; //https://mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
    public static final String NAME_VALIDATION = "[A-Z_А-ЯЇІЄҐ][a-z_а-яїієґ']{1,64}";
    public static final String BOOK_VALIDATION = "[\\w\\W_А-ЯЇІЄҐ][\\w\\W_a-z_а-яїієґ']{1,100}";
    //    String USERNAME_VALIDATION = "^(?=.{4,24}$)(?![_.-])(?!.*[_.-]{2})[a-zA-Z0-9._]+(?<![_.-])$"; //https://stackoverflow.com/questions/12018245/regular-expression-to-validate-username
    public static final String DATE_FORMAT_VALIDATION = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

    private RegexContainer() {
    }
}