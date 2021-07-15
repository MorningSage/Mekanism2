package mekanism.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.rmi.AccessException;

@Mixin(World.class)
public interface WorldAccessor {

    @Invoker
    static boolean invokeIsValidHorizontally(BlockPos pos) throws AccessException {
        throw new AccessException("Invoke of static method failed...");
    }

}
