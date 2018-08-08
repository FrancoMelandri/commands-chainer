import java.util.ArrayList;
import java.util.List;

public class CommandsChain implements ComandsChainContext,
                                      CommandsChainActions {
    CommandExecutor executor;
    List<ChainItem> chain;

    private CommandsChain() {
        this.chain = new ArrayList<>();
    }

    public static ComandsChainContext create() {
        return new CommandsChain();
    }

    public CommandsChainActions using(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public CommandsChainActions command(Class<? extends ControllerCommand> commandClass) {
        this.chain.add(new ChainItem(commandClass));
        return this;
    }

    public TypedProperty execute() throws Exception {
        TypedProperty respPropsGlobal = new TypedProperty();
        for (ChainItem ci : chain) {
            respPropsGlobal = executor.executeCommand(ci.getCommandClass().getName(),
                                                      new TypedProperty());
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
