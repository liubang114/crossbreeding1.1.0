package com.liubang.crossbreeding.recipe;

import com.liubang.crossbreeding.core.Gene;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.registry.ModItems;
import com.liubang.crossbreeding.registry.ModRecipes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class CrossbreedingBreadRecipe extends CustomRecipe {

    public CrossbreedingBreadRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int count = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (!stack.is(ModItems.CROSSBREEDING_WHEAT.get())) return false;
            if (!stack.has(ModDataComponents.GENOME.get())) return false;
            count++;
        }
        return count == 1 || count == 3;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        int count = 0;
        boolean firstShellLess = false;
        boolean allHighNutrition = true;
        boolean allHighSaturation = true;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            Genome genome = stack.get(ModDataComponents.GENOME.get());
            if (genome == null) return ItemStack.EMPTY;

            count++;
            if (count == 1) {
                firstShellLess = genome.isRecessive(Gene.E);
            }
            if (!genome.isRecessive(Gene.C)) allHighNutrition = false;
            if (!genome.isRecessive(Gene.D)) allHighSaturation = false;
        }

        int required = firstShellLess ? 1 : 3;
        if (count != required) return ItemStack.EMPTY;

        if (allHighNutrition && allHighSaturation) {
            return new ItemStack(ModItems.HIGH_NUTRITION_SATURATION_BREAD.get());
        }
        if (allHighNutrition) {
            return new ItemStack(ModItems.HIGH_NUTRITION_BREAD.get());
        }
        if (allHighSaturation) {
            return new ItemStack(ModItems.HIGH_SATURATION_BREAD.get());
        }
        return new ItemStack(ModItems.PLAIN_BREAD.get());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BREAD_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<CrossbreedingBreadRecipe> {

        public static final MapCodec<CrossbreedingBreadRecipe> CODEC =
                MapCodec.unit(() -> new CrossbreedingBreadRecipe(CraftingBookCategory.MISC));

        public static final StreamCodec<RegistryFriendlyByteBuf, CrossbreedingBreadRecipe> STREAM_CODEC =
                StreamCodec.of(
                        (buf, recipe) -> {},
                        buf -> new CrossbreedingBreadRecipe(CraftingBookCategory.MISC)
                );

        @Override
        public MapCodec<CrossbreedingBreadRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrossbreedingBreadRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
