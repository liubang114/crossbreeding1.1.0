package com.liubang.crossbreeding.registry;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.core.Gamete;
import com.liubang.crossbreeding.core.Genome;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {

    public static final DeferredRegister.DataComponents REGISTRY =
            DeferredRegister.createDataComponents(Crossbreeding.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Gamete>> GAMETE =
            REGISTRY.register("gamete", () -> DataComponentType.<Gamete>builder()
                    .persistent(Gamete.CODEC)
                    .networkSynchronized(Gamete.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Genome>> GENOME =
            REGISTRY.register("genome", () -> DataComponentType.<Genome>builder()
                    .persistent(Genome.CODEC)
                    .networkSynchronized(Genome.STREAM_CODEC)
                    .build());

    /** 配子来源植株的方块坐标。 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> PARENT_POS =
            REGISTRY.register("parent_pos", () -> DataComponentType.<BlockPos>builder()
                    .persistent(BlockPos.CODEC)
                    .networkSynchronized(BlockPos.STREAM_CODEC)
                    .build());

    /** 配子来源植株的基因型。 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Genome>> PARENT_GENOME =
            REGISTRY.register("parent_genome", () -> DataComponentType.<Genome>builder()
                    .persistent(Genome.CODEC)
                    .networkSynchronized(Genome.STREAM_CODEC)
                    .build());

    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }
}
