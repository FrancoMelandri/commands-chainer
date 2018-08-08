public interface CommandsChainActions {
    CommandsChainActions command(Class<? extends ControllerCommand> commandClass);
    CommandsChainChildActions on(CommandsChainGuard guardCallback);
    TypedProperty execute() throws Exception;
}
