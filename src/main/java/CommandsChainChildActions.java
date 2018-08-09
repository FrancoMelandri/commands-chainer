public interface CommandsChainChildActions {
    CommandsChainChildActions childCommand(Class<? extends ControllerCommand> commandClass);
    CommandsChainChildActions onChild(CommandsChainGuard guardCallback);
    CommandsChainChildActions endChild();
    CommandsChainActions end();
}
