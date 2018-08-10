public interface ICommandsChainFlow {
    ICommandsChainCommands flow();
    TypedProperty execute() throws Exception;
}
