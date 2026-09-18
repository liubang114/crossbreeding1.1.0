package com.liubang.crossbreeding.registry;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.item.CrossbreedingSeedItem;
import com.liubang.crossbreeding.item.CrossbreedingWheatItem;
import com.liubang.crossbreeding.item.CustomBreadItem;
import com.liubang.crossbreeding.item.GameteItem;
import com.liubang.crossbreeding.item.GeneDebugStickItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items REGISTRY =
            DeferredRegister.createItems(Crossbreeding.MOD_ID);

    public static final DeferredItem<Item> FEMALE_GAMETE = REGISTRY.register(
            "female_gamete",
            () -> new GameteItem(new Item.Properties())
    );

    public static final DeferredItem<Item> MALE_GAMETE = REGISTRY.register(
            "male_gamete",
            () -> new GameteItem(new Item.Properties())
    );

    public static final DeferredItem<Item> CROSSBREEDING_SEED = REGISTRY.register(
            "crossbreeding_seed",
            () -> new CrossbreedingSeedItem(new Item.Properties())
    );

    public static final DeferredItem<Item> CROSSBREEDING_WHEAT = REGISTRY.register(
            "crossbreeding_wheat",
            () -> new CrossbreedingWheatItem(new Item.Properties())
    );

    // 原版面包：营养 5，饱和度 6
    public static final DeferredItem<Item> PLAIN_BREAD = REGISTRY.register(
            "plain_bread",
            () -> new CustomBreadItem(new Item.Properties().food(
                    new FoodProperties.Builder().nutrition(5).saturationModifier(0.6f).build()))
    );

    // 高饱食面包：营养 7（+2），饱和度仍为 6 → 6 / (7*2) ≈ 0.4286
    public static final DeferredItem<Item> HIGH_NUTRITION_BREAD = REGISTRY.register(
            "high_nutrition_bread",
            () -> new CustomBreadItem(new Item.Properties().food(
                    new FoodProperties.Builder().nutrition(7).saturationModifier(0.4286f).build()))
    );

    // 高饱和面包：营养 5，饱和度 8 → 8 / (5*2) = 0.8
    public static final DeferredItem<Item> HIGH_SATURATION_BREAD = REGISTRY.register(
            "high_saturation_bread",
            () -> new CustomBreadItem(new Item.Properties().food(
                    new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).build()))
    );

    // 高饱食高饱和面包：营养 7（+2），饱和度 9 → 9 / (7*2) ≈ 0.6428
    public static final DeferredItem<Item> HIGH_NUTRITION_SATURATION_BREAD = REGISTRY.register(
            "high_nutrition_saturation_bread",
            () -> new CustomBreadItem(new Item.Properties().food(
                    new FoodProperties.Builder().nutrition(7).saturationModifier(0.6428f).build()))
    );

    public static final DeferredItem<Item> GENE_DEBUG_STICK = REGISTRY.register(
            "gene_debug_stick",
            () -> new GeneDebugStickItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }
}
