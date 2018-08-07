import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CommandsChainTests {

    @Test
    public void shouldBeTrue() {
        String result = new CommandsChain().hello();
        assertTrue(result == "hello");
    }
}
