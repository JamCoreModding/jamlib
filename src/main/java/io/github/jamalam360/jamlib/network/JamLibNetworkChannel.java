package io.github.jamalam360.jamlib.network;

import net.minecraft.util.Identifier;

/**
 * @author Jamalam
 */
public abstract class JamLibNetworkChannel<T> {
    private final Identifier identifier;
    private T handler;

    public JamLibNetworkChannel(Identifier identifier) {
        this.identifier = identifier;
    }

    protected Identifier getIdentifier() {
        return identifier;
    }

    protected T getHandler() {
        return handler;
    }

    public void registerHandler(T handler) {
        this.handler = handler;
        this.registerHandler();
    }

    protected abstract void registerHandler();
}
