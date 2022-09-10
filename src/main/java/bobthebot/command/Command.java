package bobthebot.command;

/**
 * Command class representing commands executed by the user.
 */
public abstract class Command {
    public String command;

    /**
     * Constructs command.
     */
    protected Command(String command) {
        this.command = command;
    }

    /**
     * Executes the command.
     *
     * @return String representing the output of the command.
     */
    public abstract String execute();
}