public interface ICommandsChainCommandsOn {
    ICommandsChainCommandsOn command(Class<? extends ControllerCommand> commandClass);
    ICommandsChainChildCommandsOn on(CommandsChainGuard guardCallback);
    ICommandsChainCommands end();
}
