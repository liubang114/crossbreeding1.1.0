package com.liubang.crossbreeding.util;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class AdvancementHelper {

    private AdvancementHelper() {
    }

    public static void grant(ServerPlayer player, String advancementName) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        try {
            AdvancementHolder holder = server.getAdvancements().get(
                    ResourceLocation.fromNamespaceAndPath("crossbreeding", advancementName));
            if (holder != null) {
                player.getAdvancements().award(holder, "impossible");
            }
        } catch (Exception ignored) {
        }
    }
}
