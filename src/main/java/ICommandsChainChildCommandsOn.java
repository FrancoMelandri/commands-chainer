public interface ICommandsChainChildCommandsOn {
    ICommandsChainChildCommandsOn command(Class<? extends ControllerCommand> commandClass);
    ICommandsChainCommandsOn end();
}
