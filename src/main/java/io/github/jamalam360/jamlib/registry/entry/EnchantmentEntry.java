package io.github.jamalam360.jamlib.registry.entry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;

public class EnchantmentEntry {
    private final Enchantment enchantment;
    private Identifier id = null;

    public EnchantmentEntry(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public EnchantmentEntry withIdentifier(Identifier id) {
        this.id = id;
        return this;
    }

    public Identifier getId() {
        return this.id;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }
}
