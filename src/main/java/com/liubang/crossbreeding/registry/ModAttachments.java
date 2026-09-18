package com.liubang.crossbreeding.registry;

import com.liubang.crossbreeding.Crossbreeding;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> REGISTRY =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Crossbreeding.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> DEBUG_GENE_INDEX =
            REGISTRY.register("debug_gene_index",
                    () -> AttachmentType.builder(() -> 0).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> DEBUG_GENE_TYPE =
            REGISTRY.register("debug_gene_type",
                    () -> AttachmentType.builder(() -> 1).build());

    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }
}
