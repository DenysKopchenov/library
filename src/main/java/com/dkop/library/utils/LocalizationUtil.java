package com.dkop.library.utils;

import java.util.ResourceBundle;

public final class LocalizationUtil {
    public static ResourceBundle localizationBundle;

    private LocalizationUtil() {
    }

    public static void setLocalizationBundle(ResourceBundle localizationBundle) {
        LocalizationUtil.localizationBundle = localizationBundle;
    }

}
