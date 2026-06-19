package io.github.jamalam360.testmod;

import io.github.jamalam360.jamlib.api.events.CommandRegistrationEvent;
import io.github.jamalam360.jamlib.api.events.InteractionEvent;
import io.github.jamalam360.jamlib.api.events.ServerConnectionEvents;
import io.github.jamalam360.jamlib.api.events.core.EventResult;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkEvents;
import io.github.jamalam360.jamlib.api.network.PacketIdentifier;
import io.github.jamalam360.jamlib.api.pack.PackReloadListenerRegistry;
import io.github.jamalam360.jamlib.api.platform.Platform;
import io.github.jamalam360.jamlib.api.config.ConfigManager;
import io.github.jamalam360.jamlib.api.registry.DeferredRegistry;
import io.github.jamalam360.jamlib.api.registry.RegistryObject;
import io.github.jamalam360.testmod.config.NestedConfigChild;
import io.github.jamalam360.testmod.config.QuickerConnectButtonTestConfig;
import io.github.jamalam360.testmod.config.TestConfig;
import io.github.jamalam360.testmod.item.PacketPotatoItem;
import io.github.jamalam360.testmod.network.PotatoPacket;
import io.github.jamalam360.testmod.pack.TestReloadListener;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
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

    public static final DeferredRegistry<Item> ITEMS = DeferredRegistry.create(MOD_ID, BuiltInRegistries.ITEM);
    public static final RegistryObject<PacketPotatoItem> POTATO_ITEM = ITEMS.register("packet_potato", (key) -> new PacketPotatoItem(new Item.Properties().setId(key)));

    public static void init() {
        LOGGER.info("Initializing JamLib Test Mod on {}", Platform.getModLoader());
        LOGGER.info("Fabric Loader: {}", Platform.getMod("fabricloader"));

        ServerConnectionEvents.CONNECT.listen(client -> TestMod.LOGGER.info("[S] Joined server!"));
        ServerConnectionEvents.DISCONNECT.listen(client -> TestMod.LOGGER.info("[S] Left server!"));

        PackReloadListenerRegistry.register(PackType.SERVER_DATA, id("test_server_data"), new TestReloadListener("server_data"));

        ITEMS.registerEntries();

        NetworkEvents.CLIENT_CAPABILITIES_HANDSHAKE_COMPLETED.listen(p -> {
            boolean recvPotato = Network.getPlayerCapability(p).canReceive(PotatoPacket.KIND);
            boolean recvFake = Network.getPlayerCapability(p).canReceive(new PacketIdentifier(id("fake_packet")));
            TestMod.LOGGER.info("Capabilities handshake with client completed! Can receive potato packet: {}, can receive fake packet: {}", recvPotato, recvFake);
        });

        CommandRegistrationEvent.EVENT.listen((dispatcher, context, selection) -> {
            dispatcher.register(Commands.literal("hello").executes(c -> {
                c.getSource().sendSuccess(() -> Component.literal("Hello, world!"), false);
                return 1;
            }));
        });

        if (Flags.DEBUG_INTERACTION_EVENTS) {
            InteractionEvent.USE_BLOCK.listen(((player, hand, pos, direction) -> {
                LOGGER.info("Used block!");

                if (player.getRandom().nextBoolean() && !player.level().isClientSide()) {
                    LOGGER.info("Cancelling interaction");
                    return EventResult.cancel(InteractionResult.PASS);
                } else {
                    return EventResult.pass();
                }
            }));
        }
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
