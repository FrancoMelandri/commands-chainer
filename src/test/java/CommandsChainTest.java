import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CommandsChainTest {

    @Test
    public void shouldExecuteOneCommand() throws Exception {
        TypedProperty result = new CommandsChain()
                .using(new CommandExecutor())
                .command(ControllerCommand.class)
                .execute();
        assertNotNull(result);
    }
}
