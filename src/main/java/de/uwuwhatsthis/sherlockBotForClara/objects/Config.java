package de.uwuwhatsthis.sherlockBotForClara.objects;

import de.uwuwhatsthis.sherlockBotForClara.utils.JsonStuff;

public class Config {
    private final String configPath;
    private final String token;
    private final String prefix;
    private final String pythonInterpreter;
    private final String sherlockPyFilePath;
    private final String loadingImagePath;

    public Config(String configPath){
        this.configPath = configPath;

        this.token = JsonStuff.getStringFromJson(configPath, "token");
        this.prefix = JsonStuff.getStringFromJson(configPath, "prefix");
        this.pythonInterpreter = JsonStuff.getStringFromJson(configPath, "python_interpreter");
        this.sherlockPyFilePath = "sherlock/sherlock/sherlock.py";
        this.loadingImagePath = JsonStuff.getStringFromJson(configPath, "loading_image_path");
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getToken() {
        return token;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPythonInterpreter() {
        return pythonInterpreter;
    }

    public String getSherlockPyFilePath() {
        return sherlockPyFilePath;
    }

    public String getLoadingImagePath() {
        return loadingImagePath;
    }
}
