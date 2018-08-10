public interface ICommandsChainChildCommandsOn {
    ICommandsChainChildCommandsOn command(Class<? extends ControllerCommand> commandClass);
    ICommandsChainChildCommandsOn on(CommandsChainGuard guardCallback);
    ICommandsChainCommandsOn end();
}
