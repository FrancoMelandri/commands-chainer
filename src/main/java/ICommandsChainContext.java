public interface ICommandsChainContext {
    ICommandsChainFlow using(CommandExecutor executor);
}
