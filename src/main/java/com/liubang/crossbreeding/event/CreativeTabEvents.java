package com.liubang.crossbreeding.event;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.registry.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = Crossbreeding.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CreativeTabEvents {

    @SubscribeEvent
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions()) {
            event.accept(new ItemStack(ModItems.GENE_DEBUG_STICK.get()));
        }
    }
}
