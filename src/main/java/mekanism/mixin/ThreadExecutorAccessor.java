package mekanism.mixin;

import net.minecraft.util.thread.ThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.concurrent.CompletableFuture;

@Mixin(ThreadExecutor.class)
public interface ThreadExecutorAccessor {

    @Invoker("submitAsync")
    CompletableFuture<Void> submitAsync(Runnable runnable);

}
