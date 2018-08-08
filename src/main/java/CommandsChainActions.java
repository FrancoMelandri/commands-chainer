public interface CommandsChainActions {
    CommandsChainActions command(Class<? extends ControllerCommand> commandClass);
    CommandsChainActions on(CommandsChainGuard guardCallback);
    TypedProperty execute() throws Exception;
}
