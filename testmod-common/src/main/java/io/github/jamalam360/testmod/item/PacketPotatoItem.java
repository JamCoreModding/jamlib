package io.github.jamalam360.testmod.item;

import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.testmod.TestMod;
import io.github.jamalam360.testmod.network.PotatoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.Random;

public class PacketPotatoItem extends Item {
	public PacketPotatoItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide()) {
			return InteractionResult.PASS;
		}

		int random = new Random().nextInt(3, 10);
		TestMod.LOGGER.info("On client: {}", random);
		Network.sendToClient((ServerPlayer) player, PotatoPacket.TYPE, new PotatoPacket.Payload(random));
		return InteractionResult.SUCCESS;
	}
}
