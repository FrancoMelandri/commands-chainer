import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CommandExecutorTest {

    @Test
    public void shouldBeTrue() throws Exception {
        TypedProperty responseProps = new CommandExecutor().executeCommand(TestCommand.class.getName(), new TypedProperty());
        assertNotNull(responseProps);
    }

    public static class TestCommand extends ControllerCommand {
        @Override
        public void performExecute() {
            this.setResponseProperties(new TypedProperty());
        }
    }
}