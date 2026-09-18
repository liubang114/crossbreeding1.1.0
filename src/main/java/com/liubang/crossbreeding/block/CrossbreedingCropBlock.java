package com.liubang.crossbreeding.block;

import com.liubang.crossbreeding.core.Gene;
import com.liubang.crossbreeding.core.Gamete;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.registry.ModBlocks;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrossbreedingCropBlock extends CropBlock implements EntityBlock {

    public static final int MAX_AGE = 7;

    public CrossbreedingCropBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrossbreedingCropBlockEntity(pos, state);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.CROSSBREEDING_SEED.get();
    }

    private static @Nullable Genome getGenome(BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CrossbreedingCropBlockEntity be) {
            return be.getGenome();
        }
        return null;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;

        if (level.getBlockEntity(pos) instanceof CrossbreedingCropBlockEntity be) {
            if (be.getGenome() == null) {
                be.setGenome(Genome.random(random));
                be.setChanged();
            }
        }

        int age = getAge(state);

        if (age == 0) {
            Genome genome = getGenome(level, pos);
            if (genome != null && !genome.isDominant(Gene.B)) {
                if (random.nextInt(5) == 0) {
                    level.removeBlockEntity(pos);
                    level.setBlock(pos, ModBlocks.DISEASED_WHEAT.get().defaultBlockState(),
                            Block.UPDATE_ALL);
                    return;
                }
            }
        }

        if (age < getMaxAge() && level.getRawBrightness(pos, 0) >= 9) {
            float speed = CropBlock.getGrowthSpeed(state, level, pos);
            Genome genome = getGenome(level, pos);
            if (genome != null && genome.isDominant(Gene.F)) {
                speed *= 2.0F;
            }
            int bound = (int) (25.0F / speed) + 1;
            if (random.nextInt(bound) == 0) {
                level.setBlock(pos, getStateForAge(age + 1), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos,
                              BlockState state, @Nullable BlockEntity blockEntity,
                              ItemStack tool) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) return;

        int age = getAge(state);
        Genome genome = null;
        if (blockEntity instanceof CrossbreedingCropBlockEntity be) {
            genome = be.getGenome();
        }

        if (age == getMaxAge() && genome != null) {
            RandomSource random = serverLevel.random;
            BlockPos immutablePos = pos.immutable();

            ItemStack wheat = new ItemStack(ModItems.CROSSBREEDING_WHEAT.get());
            wheat.set(ModDataComponents.GENOME.get(), genome);
            popResource(level, pos, wheat);

            int gameteCount = 2 + random.nextInt(2);
            for (int i = 0; i < gameteCount; i++) {
                boolean female = random.nextBoolean();
                ItemStack gameteStack = new ItemStack(
                        female ? ModItems.FEMALE_GAMETE.get() : ModItems.MALE_GAMETE.get());
                Gamete gamete = genome.createGamete(random);
                gameteStack.set(ModDataComponents.GAMETE.get(), gamete);
                gameteStack.set(ModDataComponents.PARENT_POS.get(), immutablePos);
                gameteStack.set(ModDataComponents.PARENT_GENOME.get(), genome);
                popResource(level, pos, gameteStack);
            }

            if (genome.isDominant(Gene.A)) {
                int extra = 2 + random.nextInt(2);
                for (int i = 0; i < extra; i++) {
                    ItemStack extraWheat = new ItemStack(ModItems.CROSSBREEDING_WHEAT.get());
                    extraWheat.set(ModDataComponents.GENOME.get(), genome);
                    popResource(level, pos, extraWheat);
                }
            }
        } else {
            popResource(level, pos, new ItemStack(getBaseSeedId()));
        }
    }

    @Override
    public boolean isValidBonemealTarget(net.minecraft.world.level.LevelReader level,
                                         BlockPos pos, BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int nextAge = Math.min(getMaxAge(), getAge(state) + getBonemealAgeIncrease(level));
        level.setBlock(pos, getStateForAge(nextAge), Block.UPDATE_ALL);
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return 1 + level.random.nextInt(2);
    }
}
