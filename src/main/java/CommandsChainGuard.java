public interface CommandsChainGuard {
    boolean guard(TypedProperty request, TypedProperty response);
}
