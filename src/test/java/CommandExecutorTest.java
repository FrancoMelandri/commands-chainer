import org.junit.Test;

import static org.junit.Assert.*;

public class CommandExecutorTest {

    @Test
    public void shouldBeTrue() {
        TypedProperty responseProps = new CommandExecutor().executeCommand("", null);
        assertNotNull(responseProps);
    }

}