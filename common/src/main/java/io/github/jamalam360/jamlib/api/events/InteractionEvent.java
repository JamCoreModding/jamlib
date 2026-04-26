package io.github.jamalam360.jamlib.api.events;

import io.github.jamalam360.jamlib.api.events.core.CancellableEvent;
import io.github.jamalam360.jamlib.api.events.core.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

/**
 * Events related to player interactions.
 */
public class InteractionEvent {
	/**
	 * Called when a player interacts with a block.
	 */
	public static final CancellableEvent<UseBlock, InteractionResult> USE_BLOCK = new CancellableEvent<>();

	@FunctionalInterface
	public interface UseBlock {
		EventResult<InteractionResult> onUseBlock(Player player, InteractionHand hand, BlockPos pos, Direction direction);
	}
}
