package io.github.jamalam360.testmod;

import io.github.jamalam360.jamlib.JamLibPlatform;
import io.github.jamalam360.jamlib.config.ConfigManager;
import io.github.jamalam360.jamlib.client.events.client.ClientPlayLifecycleEvents;
import io.github.jamalam360.testmod.config.NestedConfigChild;
import io.github.jamalam360.testmod.config.QuickerConnectButtonTestConfig;
import io.github.jamalam360.testmod.config.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMod {

    public static final String MOD_ID = "testmod";
    public static final String MOD_NAME = "JamLib Test Mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final ConfigManager<TestConfig> CONFIG_MANAGER = new ConfigManager<>(MOD_ID, "first_config", TestConfig.class);
    public static final ConfigManager<TestConfig> CONFIG_MANAGER_2 = new ConfigManager<>(MOD_ID, "second_config", TestConfig.class);
    public static final ConfigManager<QuickerConnectButtonTestConfig> QCB_CONFIG = new ConfigManager<>(MOD_ID, "quickerconnectbutton", QuickerConnectButtonTestConfig.class);
    public static final ConfigManager<NestedConfigChild> NESTED_CONFIG = new ConfigManager<>(MOD_ID, "nested", NestedConfigChild.class);

    public static void init() {
        LOGGER.info("Initializing JamLib Test Mod on {}", JamLibPlatform.getPlatform());
        System.out.println(CONFIG_MANAGER.get());
        System.out.println(QCB_CONFIG.get());

        ClientPlayLifecycleEvents.JOIN.register(client -> LOGGER.info("Joined server!"));
        ClientPlayLifecycleEvents.DISCONNECT.register(client -> LOGGER.info("Left server!"));
    }
}
