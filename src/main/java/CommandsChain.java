import java.util.ArrayList;
import java.util.List;

public class CommandsChain {
    CommandExecutor executor;
    List<ChainItem> chain;

    public CommandsChain() {
        this.chain = new ArrayList<>();
    }

    private CommandsChain(CommandExecutor executor, List<ChainItem> chain) {
        this.executor = executor;
        this.chain = chain;
    }

    public CommandsChain using(CommandExecutor executor) {
        return new CommandsChain(executor,
                                 new ArrayList<>(chain));
    }

    public CommandsChain command(Class<? extends ControllerCommand> commandClass) {
        this.chain.add(new ChainItem(commandClass));
        return new CommandsChain(executor,
                new ArrayList<>(chain));
    }

    public TypedProperty execute() {
        TypedProperty respPropsGlobal = new TypedProperty();
        for (ChainItem ci : chain) {
            respPropsGlobal = executor.executeCommand(ci.getCommandClass().getName());
        }
        return respPropsGlobal;

    }

    private static class ChainItem {
        private Class<? extends ControllerCommand> commandClass;

        public ChainItem(Class<? extends ControllerCommand> commandClass) {
            this.commandClass = commandClass;
        }

        public Class<? extends ControllerCommand> getCommandClass() {
            return commandClass;
        }
    }
}
