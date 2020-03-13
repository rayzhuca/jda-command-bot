package com.github.raybipse.components;

import java.util.Set;

import javax.annotation.Nullable;

import com.github.raybipse.core.BotConfiguration;
import com.github.raybipse.internal.ErrorMessages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * An entity that groups {@link Command} into a group. Children of the command
 * group can only be invoked if the {@link CommandGroup}'s prefix and a space is
 * appended before the command prefix.
 * 
 * A command group can sometimes be referred as the parent of the commands
 * returned in {@link #getChildren()}. The returned
 * {@link com.github.raybipse.components.Command Command} from
 * {@link #getChildren()} can also be referred to as the children of the
 * command.
 * 
 * @author RayBipse
 */
public abstract class CommandGroup {

    private String name;
    private String prefix;
    private String description;
    private Set<Command> children;

    /**
     * The constructor for CommandGroup.
     * 
     * @param name   the name of the command group
     * @param prefix the prefix used for the command group to be invoked
     */
    public CommandGroup(String name, String prefix) {
        setName(name);
        setPrefix(prefix);

        ErrorMessages.requireNonNullReturn(getName(), "getName");
        ErrorMessages.requireNonNullReturn(getPrefix(), "getPrefix");

        BotConfiguration.addCommandGroup(this);
    }

    /**
     * @return the name of the command group. The name cannot be {@code null}.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name of the command group. The name cannot be {@code null}.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the prefix of the command group. The prefix cannot be {@code null}.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix of the command group. The prefix cannot be
     *               {@code null}.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the description of the command group. The description can be
     *         {@code null}.
     */
    public @Nullable String getDescription() {
        return description;
    }

    /**
     * @param description the description of the command group. The description can
     *                    be {@code null}.
     */
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     * @return a list of commands that the command group directly inherits. The list
     *         can be {@code null} or empty.
     */
    public @Nullable Set<Command> getChildren() {
        return children;
    }

    /**
     * @param children the children of the command group. The children can be
     *                 {@code null} or empty.
     */
    public void setChildren(@Nullable Set<Command> children) {
        this.children = children;
    }

    /**
     * @param children the children to be added to the list of childrens. The
     *                 children can be null. The elements in {@code children} can be
     *                 null.
     */
    public void addChildren(Command... children) {
        if (children == null)
            return;
        for (Command child : children) {
            if (child != null)
                this.children.add(child);
        }
    }

    /**
     * A command that gives information about for the {@link CommandGroup} and its
     * children.
     * 
     * @author RayBipse
     */
    public class Help extends Command {

        public Help() {
            super("Help", "help");
            setDescription("Gives information about the specified command.");
            setSyntax("[the command's prefix]");
            addExamples("help");
            setParent(CommandGroup.this);
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

            EmbedBuilder builder = null;

            if (arguments.length == 0 && getParent() != null) { // Shows a list of commands the command group has
                builder = new EmbedBuilder().setTitle("Command Group: \"" + getParent().getName() + "\"")
                        .setColor(BotConfiguration.getPromptColor());
                builder.setDescription(getParent().getDescription());
                builder.addField("Prefix", getParent().getPrefix(), false);

                if (getChildren().size() == 0) {
                    builder.addField("Commands", "This command group contains no commands.", false);
                } else {
                    // for (Command children : getChildren()) {
                    //     allCommandPrefixes[i] = children.getPrefix();
                    // }
                    String[] allCommandPrefixes = getChildren().stream().map(Command::getPrefix).toArray(String[]::new);
                    builder.addField("Commands", "``" + String.join("``, ``", allCommandPrefixes) + "``", false);
                }
            } else if (arguments.length == 0 && getParent() == null) { // Shows the help command's info itself
                builder = getEmbedInfo();
            } else { // Shows the command of the command group's children that the first arg
                     // specified
                for (Command children : getChildren()) {
                    if (children.getPrefix().equals(arguments[0])) {
                        builder = children.getEmbedInfo();
                        if (builder == null) {
                            builder = new EmbedBuilder()
                                    .setDescription("Information about command \"" + arguments[0] + "\" is hidden.")
                                    .setColor(BotConfiguration.getErrorColor());
                        }
                    }
                }
                if (builder == null) {
                    builder = new EmbedBuilder().setDescription("Command \"" + arguments[0] + "\" not found.")
                            .setColor(BotConfiguration.getErrorColor());
                }
            }

            event.getChannel().sendMessage(builder.build()).queue();
        }

    }
}