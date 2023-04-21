package com.it_talends_goodreads.goodreads;

public class Util {
    public static final String PASS_REGEX= "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    public static final String STRONG_PASS_MSG="To create a strong password, use at least 8 characters," +
            " including uppercase and lowercase letters, at least one digit and one spacial character";
    public static final String LINK_TO_SITE_REGEX = "^www\\.[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$";
    public static final String LINK_TO_SITE_MSG ="This is not a valid link";
    public static final String GENDER_REGEX="[MF]";
    public static final String GENDER_MSG="Gender must be either M or F";
    public static final String UPLOADS="/uploads";
}
