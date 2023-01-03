package io.github.jamalam360.jamlib.test.registry;

import io.github.jamalam360.jamlib.registry.annotation.ContentRegistry;
import io.github.jamalam360.jamlib.test.JamLibTest;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;

@ContentRegistry(JamLibTest.MOD_ID)
public class TestEntities {

    public static final EntityType<TestEntity> TEST_ENTITY = FabricEntityTypeBuilder.create().entityFactory(TestEntity::new).build();
}
