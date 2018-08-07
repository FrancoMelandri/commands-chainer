public class CommandExecutor {

    public TypedProperty executeCommand(final String commandClassKey,
                                 TypedProperty requestProperties) throws Exception {
        ControllerCommand command = (ControllerCommand)(Class.forName(commandClassKey)
                                                        .newInstance());

        command.setRequestProperties(requestProperties);
        command.performExecute();
        return command.getResponseProperties();
    }
}
