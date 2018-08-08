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

    public CommandsChainActions on(CommandsChainGuard guardCallback) {
        this.chain.get(this.chain.size() - 1).setGuardCallback(guardCallback);
        return this;
    }

    public TypedProperty execute() throws Exception {
        TypedProperty respPropsGlobal = new TypedProperty();
        for (ChainItem ci : chain) {
            ci.getGuardCallback().guard(new TypedProperty(), new TypedProperty());
            respPropsGlobal = executor.executeCommand(ci.getCommandClass().getName(),
                                                      new TypedProperty());
        }
        return respPropsGlobal;

    }

    private static class ChainItem {
        static CommandsChainGuard EMPTY_CALLBACK = new CommandsChainGuard() {
            @Override
            public boolean guard(TypedProperty request, TypedProperty response) {
                return false;
            }
        };

        private Class<? extends ControllerCommand> commandClass;
        private CommandsChainGuard guardCallback;

        public ChainItem(Class<? extends ControllerCommand> commandClass) {
            this.commandClass = commandClass;
            this.guardCallback = EMPTY_CALLBACK;
        }

        public Class<? extends ControllerCommand> getCommandClass() {
            return commandClass;
        }

        public CommandsChainGuard getGuardCallback() {
            return guardCallback;
        }

        public void setGuardCallback(CommandsChainGuard guardCallback) {
            this.guardCallback = guardCallback;
        }
    }
}
