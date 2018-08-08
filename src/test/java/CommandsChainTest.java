import org.junit.Test;
import org.mockito.Mockito;

public class CommandsChainTest {

    @Test
    public void shouldExecuteOneCommand() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);

        CommandsChain
                .create()
                .using(executor)
                .command(TestCommand1.class)
                .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    @Test
    public void shouldExecuteTwoCommands() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);

        CommandsChain
                .create()
                .using(executor)
                .command(TestCommand1.class)
                .command(TestCommand2.class)
                .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand2.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    @Test
    public void shouldCallGuardCallback() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);
        CommandsChainGuard guard = Mockito.mock(CommandsChainGuard.class);

        CommandsChain
                .create()
                .using(executor)
                .command(TestCommand1.class)
                .on(guard)
                .end()
                .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(guard).guard(Mockito.any(TypedProperty.class), Mockito.any(TypedProperty.class));
    }

    @Test
    public void shouldNotCallCommandAfterInvalidGuard() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);
        CommandsChainGuard guard = Mockito.mock(CommandsChainGuard.class);

        Mockito.when(guard.guard(Mockito.any(TypedProperty.class), Mockito.any(TypedProperty.class)))
                .thenReturn(false);

        CommandsChain
                .create()
                .using(executor)
                .command(TestCommand1.class)
                .on(guard)
                    .childCommand(TestCommand2.class)
                .end()
                .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor, Mockito.times(0)).executeCommand(Mockito.eq(TestCommand2.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    @Test
    public void shouldCallCommandAfterValidGuard() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);
        CommandsChainGuard guard = Mockito.mock(CommandsChainGuard.class);

        Mockito.when(guard.guard(Mockito.any(TypedProperty.class), Mockito.any(TypedProperty.class)))
                .thenReturn(true);

        CommandsChain
                .create()
                    .using(executor)
                        .command(TestCommand1.class)
                        .on(guard)
                            .childCommand(TestCommand2.class)
                        .end()
                    .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand3.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    @Test
    public void shouldCallCommandAfterInvalidGuard() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);
        CommandsChainGuard guard = Mockito.mock(CommandsChainGuard.class);

        Mockito.when(guard.guard(Mockito.any(TypedProperty.class), Mockito.any(TypedProperty.class)))
                .thenReturn(false);

        CommandsChain
                .create()
                    .using(executor)
                        .command(TestCommand1.class)
                        .on(guard)
                            .childCommand(TestCommand2.class)
                        .end()
                        .command(TestCommand3.class)
                    .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand3.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    public static class TestCommand1 extends ControllerCommand {
    }
    public static class TestCommand2 extends ControllerCommand {
    }
    public static class TestCommand3 extends ControllerCommand {
    }
}
