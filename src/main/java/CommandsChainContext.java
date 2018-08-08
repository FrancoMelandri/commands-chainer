public interface CommandsChainContext {
    CommandsChainActions using(CommandExecutor executor);
}
