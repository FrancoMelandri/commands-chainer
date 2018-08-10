import java.util.ArrayList;
import java.util.List;

public class CommandsChain implements ICommandsChainContext,
                                      ICommandsChainFlow {
    private CommandExecutor executor;
    private List<ChainItem> rootChain;

    private CommandsChain() {
        this.rootChain = new ArrayList<>();
    }

    public static ICommandsChainContext create() {
        return new CommandsChain();
    }

    @Override
    public ICommandsChainFlow using(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public ICommandsChainCommands flow() {
        return new CommandsChainCommands(this,
                                         this.rootChain);
    }

    @Override
    public TypedProperty execute() throws Exception {
        TypedProperty respPropsGlobal = new TypedProperty();
        internalExecute(rootChain, respPropsGlobal);
        return respPropsGlobal;
    }

    private void internalExecute(List<ChainItem> commands,
                                 TypedProperty respPropsGlobal)  throws Exception{

        for (ChainItem ci : commands) {
            respPropsGlobal = executor.executeCommand(ci.getCommandClass().getName(),
                    new TypedProperty());
            if (ci.getGuardCallback().guard(new TypedProperty(), new TypedProperty())) {
                internalExecute(ci.getChain(), respPropsGlobal);
            }
        }
    }

    private static class CommandsChainCommands implements ICommandsChainCommands {

        private CommandsChain parent;
        private List<ChainItem> rootChain;

        public CommandsChainCommands (CommandsChain parent,
                                      List<ChainItem> rootChain) {
            this.parent = parent;
            this.rootChain = rootChain;
        }

        @Override
        public ICommandsChainFlow then() {
            return parent;
        }

        @Override
        public ICommandsChainCommands command(Class<? extends ControllerCommand> commandClass) {
            this.rootChain.add(new ChainItem(commandClass));
            return this;
        }

        @Override
        public ICommandsChainCommandsOn on(CommandsChainGuard guardCallback) {
            ChainItem item = this.rootChain.get(this.rootChain.size() - 1);
            item.setGuardCallback(guardCallback);
            return new CommandsChainCommandsOn(this,
                                               item.getChain());
        }
    }

    private static class CommandsChainCommandsOn implements ICommandsChainCommandsOn {
        private CommandsChainCommands parent;
        private List<ChainItem> chain;

        public CommandsChainCommandsOn(CommandsChainCommands parent,
                                       List<ChainItem> chain) {
            this.parent = parent;
            this.chain = chain;
        }

        @Override
        public ICommandsChainCommandsOn command(Class<? extends ControllerCommand> commandClass) {
            this.chain.add(new ChainItem(commandClass));
            return this;
        }

        @Override
        public ICommandsChainChildCommandsOn on(CommandsChainGuard guardCallback) {
            ChainItem item = this.chain.get(this.chain.size() - 1);
            item.setGuardCallback(guardCallback);
            return new CommandsChainChildCommandsOn(this,
                    item.getChain());
        }

        @Override
        public ICommandsChainCommands end() {
            return this.parent;
        }
    }

    private static class CommandsChainChildCommandsOn implements ICommandsChainChildCommandsOn {
        private CommandsChainCommandsOn parent;
        private List<ChainItem> chain;

        private CommandsChainChildCommandsOn(CommandsChainCommandsOn parent,
                                            List<ChainItem> chain) {
            this.parent = parent;
            this.chain = chain;
        }

        @Override
        public ICommandsChainChildCommandsOn command(Class<? extends ControllerCommand> commandClass) {
            this.chain.add(new ChainItem(commandClass));
            return this;
        }

        @Override
        public ICommandsChainChildCommandsOn on(CommandsChainGuard guardCallback) {
//            ChainItem item = this.chain.get(this.chain.size() - 1);
//            item.setGuardCallback(guardCallback);
//            return new CommandsChainChildCommandsOn(this,
//                                                    item.getChain());
            return null;
        }

        @Override
        public ICommandsChainCommandsOn end() {
            return this.parent;
        }
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
        private List<ChainItem> chain;

        public ChainItem(Class<? extends ControllerCommand> commandClass) {
            this.commandClass = commandClass;
            this.guardCallback = EMPTY_CALLBACK;
            this.chain = new ArrayList<>();
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

        public List<ChainItem> getChain() {
            return chain;
        }
    }
}
