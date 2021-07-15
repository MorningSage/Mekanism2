package mekanism.common.util;

import mekanism.mixin.ChunkRendererRegionAccessor;
import mekanism.mixin.ExplosionAccessor;
import mekanism.mixin.FluidBlockAccessor;
import mekanism.mixin.WorldAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.rmi.AccessException;
import java.util.function.ToIntFunction;

public final class AccessorUtils {

    public static ToIntFunction<BlockState> getLuminance(AbstractBlock.Settings settings) {
        return ((AbstractBlockSettingsAccessor) settings).getLuminance();
    }

    public static World getWorld(ChunkRendererRegion chunkRendererRegion) {
        return ((ChunkRendererRegionAccessor) chunkRendererRegion).getWorld();
    }

    public static Explosion.DestructionType getDestructionType(Explosion explosion) {
        return ((ExplosionAccessor) explosion).getDestructionType();
    }

    public static float getPower(Explosion explosion) {
        return ((ExplosionAccessor) explosion).getPower();
    }

    public static boolean isValidHorizontally(BlockPos pos) {
        try {
            return WorldAccessor.invokeIsValidHorizontally(pos);
        } catch (AccessException e) {
            return false;
        }
    }

    public static FluidBlock createFluidBlock(FlowableFluid flowableFluid, AbstractBlock.Settings settings) {
        return FluidBlockAccessor.invokeInit(flowableFluid, settings);
    }
}
