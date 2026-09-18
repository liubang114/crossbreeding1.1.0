package com.liubang.crossbreeding.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GeneDebugStickItem extends Item {

    public GeneDebugStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
