package com.android.sampleschoolandroid.business.domain.util

import java.util.regex.Pattern

fun isEmailValid(email: String): Boolean =
    email.isNotEmpty() && Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" +
                ")+"
    ).matcher(email).matches()