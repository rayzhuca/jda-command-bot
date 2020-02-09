package com.github.raybipse.commands.test;

import com.github.raybipse.components.Command;
import com.github.raybipse.components.CommandGroup;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A command that repeats the arguments of the input.
 * 
 * @author RayBipse
 */
public class Echo extends Command {

    @Override
    public String getName() {
        return "Echo";
    }

    @Override
    public String getDescription() {
        return "Repeats the parameters the user gives.";
    }

    @Override
    public String getPrefix() {
        return "echo";
    }

    @Override
    public String[] getExamples() {
        return new String[] { "\"Hello World!\"" };
    }

    @Override
    public String getSyntax() {
        return "[...things to be repeated]*";
    }

    @Override
    public CommandGroup getParent() {
        return Test.getInstance();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String messageContent = event.getMessage().getContentDisplay();
        if (event.getAuthor().isBot())
            return;
        if (!getInputValidity(messageContent))
            return;

        messageContent = trimInputBeginning(messageContent);
        String[] arguments = splitUserInput(messageContent);

        if (arguments.length == 0) {
            event.getChannel().sendMessage(getEmbedMissingArguments().build()).queue();
        } else if (arguments.length == 1) {
            event.getChannel().sendMessage(arguments[0]).queue();
        } else {
            StringBuilder builder = new StringBuilder();
            for (String arg : arguments) {
                builder.append(arg + " ");
            }
            event.getChannel().sendMessage(builder.substring(0, builder.length() - 1)).queue();
        }
    }
}