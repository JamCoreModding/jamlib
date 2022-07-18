package io.github.jamalam360.jamlib.test;

import io.github.jamalam360.jamlib.network.JamLibC2SNetworkChannel;
import io.github.jamalam360.jamlib.network.JamLibS2CNetworkChannel;
import net.minecraft.util.Identifier;

/**
 * @author Jamalam
 */
public class JamLibTestNetwork {
    public static final JamLibC2SNetworkChannel NETWORK_KEYBIND_PRESS = new JamLibC2SNetworkChannel(new Identifier("jamlib-test", "network_keybind_press"));
    public static final JamLibS2CNetworkChannel NETWORK_KEYBIND_PRESS_RESPONSE = new JamLibS2CNetworkChannel(new Identifier("jamlib-test", "network_keybind_press_response"));
}
