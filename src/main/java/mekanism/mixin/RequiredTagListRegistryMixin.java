package mekanism.mixin;

import com.google.common.collect.ImmutableSet;
import mekanism.common.util.TagRegistryUtils;
import net.minecraft.tag.RequiredTagList;
import net.minecraft.tag.RequiredTagListRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(RequiredTagListRegistry.class)
public class RequiredTagListRegistryMixin {

    @Inject(
        at = @At("RETURN"),
        method = "getRequiredTags",
        cancellable = true
    )
    private static void getRequiredTags(CallbackInfoReturnable<Set<RequiredTagList<?>>> callbackInfo) {
        callbackInfo.setReturnValue(new ImmutableSet.Builder<RequiredTagList<?>>()
            .addAll(callbackInfo.getReturnValue())
            .addAll(TagRegistryUtils.getCustomTagLists())
            .build());
    }

}
