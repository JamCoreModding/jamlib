package io.github.jamalam360.testmod.pack;

import io.github.jamalam360.testmod.TestMod;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;

public class TestReloadListener extends SimplePreparableReloadListener<Unit> {
	private final String tag;

	public TestReloadListener(String tag) {
		this.tag = tag;
	}

	@Override
	protected Unit prepare(ResourceManager manager, ProfilerFiller profiler) {
		TestMod.LOGGER.info("[{}] {} resources found.", this.tag, manager.listResources("", (ignored) -> true).size());
		return Unit.INSTANCE;
	}

	@Override
	protected void apply(Unit preparations, ResourceManager manager, ProfilerFiller profiler) {
	}
}
