package de.uwuwhatsthis.sherlockBotForClara.objects;

import java.util.Arrays;

public class Website {
    private final String websiteName;
    private final String websiteUrl;

    public Website(String input){
        String[] splittedInput = input.split(" ");

        String website = splittedInput[1];
        website = website.replace(":", "");
        this.websiteName = website;

        this.websiteUrl = splittedInput[splittedInput.length - 1];
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }
}
