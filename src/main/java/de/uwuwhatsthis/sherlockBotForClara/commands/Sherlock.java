package de.uwuwhatsthis.sherlockBotForClara.commands;

import de.uwuwhatsthis.sherlockBotForClara.main.Main;
import de.uwuwhatsthis.sherlockBotForClara.objects.Args;
import de.uwuwhatsthis.sherlockBotForClara.objects.Embed;
import de.uwuwhatsthis.sherlockBotForClara.objects.Website;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Sherlock implements Runnable{
    private MessageReceivedEvent event;
    private Args args;

    private Message loadingMessage = null;

    private final String[] disallowedChars = {"&", "^", "sudo", "rm", "-fr"};

    public void execute(MessageReceivedEvent event, Args args){
        this.event = event;
        this.args = args;

        if (args.isEmpty()){
            event.getChannel().sendMessageEmbeds(new Embed("Error", "You need to provide an account name to search for!", Color.RED).build()).queue();
            return;
        }

        for (String word: disallowedChars){
            if (args.get(0).contains(word)){
                event.getChannel().sendMessageEmbeds(new Embed("Error", "The username you requested includes a bad character!", Color.RED).build()).queue();
                return;
            }
        }


        File file = new File(Main.config.getLoadingImagePath());
        MessageEmbed embed = new Embed("Searching", "Searching for username: " + args.get(0) + "\n\nPlease wait", Color.GREEN).setThumbnail("attachment://loading.gif").build();

        loadingMessage = event.getChannel().sendMessageEmbeds(embed).addFile(file, "loading.gif").complete();


        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        ArrayList<Website> websites = doSherlock(args.get(0));
        if (websites == null) {
            // error message is being sent from doSherlock();
            return;
        }

        if (websites.isEmpty()){
            loadingMessage.editMessageEmbeds(new Embed("No result", "The user was not found anywhere", Color.ORANGE).setThumbnail("attachment://loading.gif").build()).queue();
            return;
        }

        Embed embed = new Embed("Results", "Here are the results for your search", Color.GREEN).setThumbnail("attachment://loading.gif");

        for (Website site: websites){
            embed.addField(site.getWebsiteName(), "[Profile](" + site.getWebsiteUrl() + ")", true);
        }

        loadingMessage.editMessageEmbeds(embed.build()).queue();
    }

    private ArrayList<Website> doSherlock(String accountName){
        Runtime runtime = Runtime.getRuntime();
        Process process = null;

        // System.out.println(args.getArgs().toString());
        try {
            String execString = Main.config.getPythonInterpreter() + " " + Main.config.getSherlockPyFilePath() + " " + accountName + " -l --print-found";
            // System.out.println("running: " + execString);
            process = runtime.exec(execString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (process == null){
            loadingMessage.editMessageEmbeds(new Embed("Error", "Something failed while trying to run sherlock", Color.RED).build()).queue();
            return null;
        }


        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // System.out.println("Sherlock exited with code: " + process.exitValue());

        String output = null;
        String errOutput = null;
        try {
            output = IOUtils.toString(process.getInputStream(), Charset.defaultCharset());
            errOutput = IOUtils.toString(process.getErrorStream(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errOutput != null && errOutput.length() > 0){
            loadingMessage.editMessageEmbeds(new Embed("Error", "An error was encountered while trying to run sherlock (Exit code: " + process.exitValue() + "): \n\n```" + errOutput + "```", Color.RED).setThumbnail("attachment://loading.gif").build()).queue();
            return null;
        }

        if (output == null || output.length() == 0){
            return new ArrayList<>();
        }

        // System.out.println(output);

        String[] list = output.split("\n");

        ArrayList<Website> websites = new ArrayList<>();

        for (int i = 1; i <= list.length - 1; i++){
            String line = list[i];
            if (line.isEmpty() || line.length() <= 1) continue;

            websites.add(new Website(line));

        }


        return websites;
    }
}
