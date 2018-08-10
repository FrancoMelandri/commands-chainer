public interface ICommandsChainCommands {
    ICommandsChainFlow then();
    ICommandsChainCommands command(Class<? extends ControllerCommand> commandClass);
    ICommandsChainCommandsOn on(CommandsChainGuard guardCallback);
}
