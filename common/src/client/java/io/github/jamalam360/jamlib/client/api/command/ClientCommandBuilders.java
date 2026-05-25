package io.github.jamalam360.jamlib.client.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public class ClientCommandBuilders {
	public static LiteralArgumentBuilder<ClientCommandSourceStack> literal(String value) {
		return LiteralArgumentBuilder.literal(value);
	}

	public static <T> RequiredArgumentBuilder<ClientCommandSourceStack, T> argument(String value, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(value, type);
	}
}
