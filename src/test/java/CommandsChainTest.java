import org.junit.Test;
import org.mockito.Mockito;

public class CommandsChainTest {

    @Test
    public void shouldExecuteOneCommand() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);

        CommandsChain
                .create()
                    .using(executor)
                .flow()
                    .command(TestCommand1.class)
                .then()
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
                .flow()
                    .command(TestCommand1.class)
                    .command(TestCommand2.class)
                .then()
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
                .flow()
                    .command(TestCommand1.class)
                    .on(guard)
                    .end()
                .then()
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
                .flow()
                    .command(TestCommand1.class)
                    .on(guard)
                        .command(TestCommand2.class)
                    .end()
                .then()
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
                .flow()
                    .command(TestCommand1.class)
                    .on(guard)
                        .command(TestCommand2.class)
                    .end()
                .then()
                    .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand2.class.getName()),
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
                .flow()
                    .command(TestCommand1.class)
                    .on(guard)
                        .command(TestCommand2.class)
                    .end()
                    .command(TestCommand3.class)
                .then()
                    .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand3.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    @Test
    public void shouldCallCommandAfterSecondValidGuard() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);
        CommandsChainGuard guard = Mockito.mock(CommandsChainGuard.class);

        Mockito.when(guard.guard(Mockito.any(TypedProperty.class), Mockito.any(TypedProperty.class)))
                .thenReturn(true);

        CommandsChain
                .create()
                    .using(executor)
                .flow()
                    .command(TestCommand1.class)
                    .on(guard)
                        .command(TestCommand2.class)
                        .on(guard)
                            .command(TestCommand3.class)
                        .end()
                    .end()
                .then()
                    .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand2.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand3.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    @Test
    public void shouldCallCommandAfterAllValidGuards() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);
        CommandsChainGuard guard = Mockito.mock(CommandsChainGuard.class);

        Mockito.when(guard.guard(Mockito.any(TypedProperty.class), Mockito.any(TypedProperty.class)))
                .thenReturn(true);

        CommandsChain
                .create()
                    .using(executor)
                .flow()
                    .command(TestCommand1.class)
                    .on(guard)
                        .command(TestCommand2.class)
                        .on(guard)
                            .command(TestCommand3.class)
                        .end()
                    .end()
                    .command(TestCommand4.class)
                .then()
                    .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand2.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand3.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand4.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    @Test
    public void shouldNotCallCommandAfterSecondInvalidGuard() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);
        CommandsChainGuard guard = Mockito.mock(CommandsChainGuard.class);
        CommandsChainGuard guardInvalid = Mockito.mock(CommandsChainGuard.class);


        Mockito.when(guardInvalid.guard(Mockito.any(TypedProperty.class), Mockito.any(TypedProperty.class)))
                .thenReturn(false);

        Mockito.when(guard.guard(Mockito.any(TypedProperty.class), Mockito.any(TypedProperty.class)))
                .thenReturn(true);

        CommandsChain
                .create()
                    .using(executor)
                .flow()
                    .command(TestCommand1.class)
                    .on(guard)
                        .command(TestCommand2.class)
                        .on(guardInvalid)
                            .command(TestCommand3.class)
                        .end()
                    .end()
                .then()
                    .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand2.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor, Mockito.times(0)).executeCommand(Mockito.eq(TestCommand3.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    @Test
    public void shouldCallCommandsAfterAllValidGuards() throws Exception {
        CommandExecutor executor = Mockito.mock(CommandExecutor.class);
        CommandsChainGuard guard = Mockito.mock(CommandsChainGuard.class);

        Mockito.when(guard.guard(Mockito.any(TypedProperty.class), Mockito.any(TypedProperty.class)))
                .thenReturn(true);

        CommandsChain
                .create()
                .using(executor)
                .flow()
                    .command(TestCommand1.class)
                    .on(guard)
                        .command(TestCommand2.class)
                        .on(guard)
                            .command(TestCommand3.class)
                            .command(TestCommand4.class)
                        .end()
                    .end()
                .then()
                    .execute();

        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand1.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand2.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand3.class.getName()),
                Mockito.any(TypedProperty.class));
        Mockito.verify(executor).executeCommand(Mockito.eq(TestCommand4.class.getName()),
                Mockito.any(TypedProperty.class));
    }

    public static class TestCommand1 extends ControllerCommand {
    }

    public static class TestCommand2 extends ControllerCommand {
    }

    public static class TestCommand3 extends ControllerCommand {
    }

    public static class TestCommand4 extends ControllerCommand {
    }
}
