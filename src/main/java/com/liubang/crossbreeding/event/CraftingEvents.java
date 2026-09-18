package com.liubang.crossbreeding.event;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.core.Gamete;
import com.liubang.crossbreeding.core.Gene;
import com.liubang.crossbreeding.core.GenePair;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.registry.ModItems;
import com.liubang.crossbreeding.util.AdvancementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Crossbreeding.MOD_ID)
public class CraftingEvents {

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();
        if (result.isEmpty()) return;

        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        Container input = event.getInventory();

        // --- 面包相关成就 ---
        if (result.is(ModItems.HIGH_NUTRITION_SATURATION_BREAD.get())) {
            checkAheadOfTime(serverPlayer, input);
        }

        // --- 杂交种子相关成就 ---
        if (result.is(ModItems.CROSSBREEDING_SEED.get())) {
            handleSeed(serverPlayer, input, result);
        }
    }

    private static void handleSeed(ServerPlayer player, Container input, ItemStack result) {
        // 收集所有配子
        ItemStack femaleStack = ItemStack.EMPTY;
        ItemStack maleStack = ItemStack.EMPTY;
        int otherCount = 0;

        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(ModItems.FEMALE_GAMETE.get())) femaleStack = stack;
            else if (stack.is(ModItems.MALE_GAMETE.get())) maleStack = stack;
            else otherCount++;
        }

        if (femaleStack.isEmpty() || maleStack.isEmpty() || otherCount != 0) return;

        // 9331：首次获得杂交种子
        AdvancementHelper.grant(player, "obtain_seed");

        // 补全 genome（如果没有）
        Genome genome = result.get(ModDataComponents.GENOME.get());
        if (genome == null) {
            Gamete female = femaleStack.get(ModDataComponents.GAMETE.get());
            Gamete male = maleStack.get(ModDataComponents.GAMETE.get());
            RandomSource random = player.level().random;
            if (female == null) female = randomGamete(random);
            if (male == null) male = randomGamete(random);
            genome = Genome.combine(female, male);
            result.set(ModDataComponents.GENOME.get(), genome);
        }

        // 自交 / 杂交
        BlockPos femalePos = femaleStack.get(ModDataComponents.PARENT_POS.get());
        BlockPos malePos = maleStack.get(ModDataComponents.PARENT_POS.get());
        if (femalePos != null && malePos != null) {
            if (femalePos.equals(malePos)) {
                AdvancementHelper.grant(player, "selfing");
            } else {
                AdvancementHelper.grant(player, "outcross");
            }
        }

        // 无中生有为隐性，有中生无为显性
        Genome femaleParent = femaleStack.get(ModDataComponents.PARENT_GENOME.get());
        Genome maleParent = maleStack.get(ModDataComponents.PARENT_GENOME.get());
        if (femaleParent != null && maleParent != null) {
            checkRecessiveFromDominant(player, genome, femaleParent, maleParent);
        }
    }

    private static void checkAheadOfTime(ServerPlayer player, Container input) {
        // 检查输入：恰好 1 个杂交小麦，且它少壳、高饱食、高饱和
        int count = 0;
        ItemStack wheat = ItemStack.EMPTY;
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            count++;
            wheat = stack;
        }
        if (count != 1 || wheat.isEmpty()) return;
        if (!wheat.is(ModItems.CROSSBREEDING_WHEAT.get())) return;

        Genome g = wheat.get(ModDataComponents.GENOME.get());
        if (g == null) return;

        if (g.isRecessive(Gene.E) && g.isRecessive(Gene.C) && g.isRecessive(Gene.D)) {
            AdvancementHelper.grant(player, "ahead_of_time");
        }
    }

    private static void checkRecessiveFromDominant(ServerPlayer player, Genome child,
                                                    Genome parent1, Genome parent2) {
        for (int i = 0; i < 6; i++) {
            GenePair childPair = getPair(child, i);
            // 后代必须是隐性表现
            if (!childPair.isRecessive()) continue;
            // 两个亲本该位点都必须是显性表现
            if (getPair(parent1, i).isDominant() && getPair(parent2, i).isDominant()) {
                AdvancementHelper.grant(player, "recessive_from_dominant");
                return;
            }
        }
    }

    private static GenePair getPair(Genome g, int index) {
        return switch (index) {
            case 0 -> g.pair1();
            case 1 -> g.pair2();
            case 2 -> g.pair3();
            case 3 -> g.pair4();
            case 4 -> g.pair5();
            default -> g.pair6();
        };
    }

    private static Gamete randomGamete(RandomSource random) {
        Gene[] genes = Gene.values();
        char[] alleles = new char[genes.length];
        for (int i = 0; i < genes.length; i++) {
            alleles[i] = random.nextBoolean() ? genes[i].dominant : genes[i].recessive;
        }
        return new Gamete(alleles[0], alleles[1], alleles[2],
                alleles[3], alleles[4], alleles[5]);
    }
}
