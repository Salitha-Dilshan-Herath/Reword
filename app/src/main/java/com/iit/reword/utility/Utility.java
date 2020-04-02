package com.iit.reword.utility;

import android.util.Patterns;

public final class Utility {

    public static boolean isValidEmail(CharSequence target) {
        return ( Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
