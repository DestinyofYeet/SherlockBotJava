package de.uwuwhatsthis.sherlockBotForClara.main;

import de.uwuwhatsthis.sherlockBotForClara.objects.Config;
import de.uwuwhatsthis.sherlockBotForClara.utils.Constants;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {
    public static Config config;

    public static void main(String[] args) {
        config = new Config("config/config.json");
        Constants.init(config);

        JDABuilder jdaBuilder = JDABuilder.createDefault(Constants.TOKEN);
        jdaBuilder.addEventListeners(new CommandManager());
        try{
            jdaBuilder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
