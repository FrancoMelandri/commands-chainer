import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CommandsChain implements CommandsChainContext,
                                      CommandsChainActions,
                                      CommandsChainChildActions {
    private CommandExecutor executor;
    private List<ChainItem> rootChain;
    private Stack<ChainItem> itemsStack;

    private CommandsChain() {
        this.rootChain = new ArrayList<>();
        this.itemsStack = new Stack<>();
    }

    public static CommandsChainContext create() {
        return new CommandsChain();
    }

    public CommandsChainActions using(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public CommandsChainActions command(Class<? extends ControllerCommand> commandClass) {
        this.rootChain.add(new ChainItem(commandClass));
        return this;
    }

    public CommandsChainChildActions on(CommandsChainGuard guardCallback) {
        ChainItem item = this.rootChain.get(this.rootChain.size() - 1);
        this.itemsStack.push(item);
        item.setGuardCallback(guardCallback);
        return this;
    }

    public CommandsChainChildActions childCommand(Class<? extends ControllerCommand> commandClass) {
        List<ChainItem> currentItem =  this.itemsStack.peek().getChain();
        currentItem.add(new ChainItem(commandClass));
        return this;
    }

    public CommandsChainChildActions onChild(CommandsChainGuard guardCallback) {
        ChainItem parentItem =  this.itemsStack.peek();
        ChainItem item = parentItem.getChain().get(this.rootChain.size() - 1);
        this.itemsStack.push(item);
        item.setGuardCallback(guardCallback);
        return this;
    }

    public CommandsChainActions end() {
        this.itemsStack.pop();
        return this;
    }

    public CommandsChainChildActions endChild() {
        this.itemsStack.pop();
        return this;
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

    public TypedProperty execute() throws Exception {
        TypedProperty respPropsGlobal = new TypedProperty();
        internalExecute(rootChain, respPropsGlobal);
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
