package com.example.vidupcoremodule;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoreApplicationConstants {

    public static final String  blankUserImage = "blank-user.jpg";
    public static final String  ipV6LoopBackAddress = "0:0:0:0:0:0:0:1";
    public static final String  ipV6LoopBackAddressFormat2 = "0.0.0.0.0.0.0.1";
    public static final String localHostAddress = "127:0:0:1";
    public static final Set<String> unsupportedCodecs = new HashSet<>(List.of("ac3","ac-3"));
    public static final String mp4AudioHandlerName = "soun";
    public static final String mp4VideoHandlerName = "vide";
    public static final String mp4UnidentifiedLanguage = "und";
    public static final String SUCCESS_MESSAGE = "SUCCESS";
    private CoreApplicationConstants() {

    }
}

