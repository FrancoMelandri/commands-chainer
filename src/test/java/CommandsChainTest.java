import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CommandsChainTest {

    @Test
    public void shouldBeTrue() {
        CommandsChain result = new CommandsChain()
                .using(new CommandExecutor());
        assertNotNull(result);
    }
}
