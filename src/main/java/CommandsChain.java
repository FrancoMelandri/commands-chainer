public class CommandsChain {
    CommandExecutor executor;

    public CommandsChain() {
    }

    private CommandsChain(CommandExecutor executor) {
        this.executor = executor;
    }

    public CommandsChain using(CommandExecutor executor) {
        return new CommandsChain(executor);
    }
}
