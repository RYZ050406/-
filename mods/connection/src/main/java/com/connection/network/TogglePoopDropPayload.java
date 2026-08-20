package com.connection.network;

import com.connection.ConnectionMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record TogglePoopDropPayload() implements CustomPacketPayload {
	public static final Type<TogglePoopDropPayload> TYPE = new Type<>(
			Identifier.fromNamespaceAndPath(ConnectionMod.MOD_ID, "toggle_poop_drop")
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, TogglePoopDropPayload> CODEC = StreamCodec.unit(new TogglePoopDropPayload());

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
