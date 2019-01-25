package com.example.xtalker.manprotest;

import java.util.ArrayList;

/**
 * Created by macbook on 5/2/17.
 */

public class CGlobal {
    public static String urlServer = "http://opensource.petra.ac.id/~m26415147/museum/ConnectMobile/";
    public static ArrayList<CWisata> dataWisata = new ArrayList<CWisata>();
    public static UserSessionManager user;
    CGlobal(){}

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
