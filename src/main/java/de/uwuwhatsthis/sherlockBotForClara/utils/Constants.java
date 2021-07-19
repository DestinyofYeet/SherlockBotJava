package de.uwuwhatsthis.sherlockBotForClara.utils;

import de.uwuwhatsthis.sherlockBotForClara.objects.Config;

public class Constants {

    public static String TOKEN;
    public static String PREFIX;

    public static void init(Config config){
        TOKEN = config.getToken();
        PREFIX = config.getPrefix();
    }
}
