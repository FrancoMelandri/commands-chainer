public interface CommandsChainChildActions {
    CommandsChainChildActions childCommand(Class<? extends ControllerCommand> commandClass);
    CommandsChainActions end();
}
