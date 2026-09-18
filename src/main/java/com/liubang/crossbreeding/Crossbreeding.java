package com.liubang.crossbreeding;

import com.liubang.crossbreeding.registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Crossbreeding.MOD_ID)
public class Crossbreeding {

    public static final String MOD_ID = "crossbreeding";
    public static final String VERSION = "1.1.0";

    public Crossbreeding(IEventBus modBus, ModContainer container) {
        ModItems.register(modBus);
        ModBlocks.register(modBus);
        ModBlockEntities.register(modBus);
        ModDataComponents.register(modBus);
        ModCreativeTabs.register(modBus);
        ModRecipes.register(modBus);
        ModAttachments.register(modBus);
    }
}
