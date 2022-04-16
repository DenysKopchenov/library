package com.dkop.library.utils;

import java.util.ResourceBundle;

public class LocalizationUtil {
    public static ResourceBundle errorMessagesBundle;
    public static ResourceBundle localizationBundle;

    private LocalizationUtil() {
    }

    public static void setErrorMessagesBundle(ResourceBundle errorMessagesBundle) {
        LocalizationUtil.errorMessagesBundle = errorMessagesBundle;
    }
    public static void setLocalizationBundle(ResourceBundle localizationBundle) {
        LocalizationUtil.localizationBundle = localizationBundle;
    }

}
