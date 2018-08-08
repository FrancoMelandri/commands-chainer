public interface CommandsChainActions {
    CommandsChainActions command(Class<? extends ControllerCommand> commandClass);
    TypedProperty execute() throws Exception;
}
