package mekanism.common.lib.radiation.capability;

import mekanism.api.NBTConstants;
import mekanism.api.radiation.capability.IRadiationEntity;
import net.minecraft.nbt.NbtCompound;

public class DefaultRadiationEntity implements IRadiationEntity {

    private double radiation;
    private double clientSeverity = 0;

    @Override
    public double getRadiation() {
        return radiation;
    }

    //@Override
    //public void radiate(double magnitude) {
    //    radiation += magnitude;
    //}

    //@Override
    //public void update(@Nonnull LivingEntity entity) {
    //    if (entity instanceof PlayerEntity && !MekanismUtils.isPlayingMode((PlayerEntity) entity)) {
    //        return;
    //    }
//
    //    Random rand = entity.world.getRandom();
    //    double minSeverity = MekanismConfig.general.radiationNegativeEffectsMinSeverity.get();
    //    double severityScale = RadiationScale.getScaledDoseSeverity(radiation);
    //    double chance = minSeverity + rand.nextDouble() * (1 - minSeverity);
//
    //    // Hurt randomly
    //    if (severityScale > chance && rand.nextInt() % 2 == 0) {
    //        entity.damage(MekanismDamageSource.RADIATION, 1);
    //    }
//
    //    if (entity instanceof PlayerEntity) {
    //        ServerPlayerEntity player = (ServerPlayerEntity) entity;
//
    //        if (clientSeverity != radiation) {
    //            clientSeverity = radiation;
    //            PacketRadiationData.sync(player);
    //        }
//
    //        if (severityScale > chance) {
    //            player.getHungerManager().addExhaustion(1F);
    //        }
    //    }
    //}

    @Override
    public void set(double magnitude) {
        radiation = magnitude;
    }

    //@Override
    //public void decay() {
    //    radiation = Math.max(RadiationManager.BASELINE, radiation * MekanismConfig.general.radiationTargetDecayRate.get());
    //}

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound ret = new NbtCompound();
        ret.putDouble(NBTConstants.RADIATION, radiation);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        radiation = nbt.getDouble(NBTConstants.RADIATION);
    }

    @Override
    public NbtCompound serializeNBT() {
        NbtCompound nbt = new NbtCompound();
        writeToNbt(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(NbtCompound nbt) {
        readFromNbt(nbt);
    }

    //public static class Provider implements ICapabilitySerializable<NbtCompound>, INBTSerializable<NbtCompound> {
//
    //    public static final Identifier NAME = Mekanism.rl(NBTConstants.RADIATION);
    //    private final IRadiationEntity defaultImpl = new DefaultRadiationEntity();
    //    private final CapabilityCache capabilityCache = new CapabilityCache();
//
    //    public Provider() {
    //        capabilityCache.addCapabilityResolver(BasicCapabilityResolver.constant(Capabilities.RADIATION_ENTITY_CAPABILITY, defaultImpl));
    //    }
//
    //    @Nonnull
    //    @Override
    //    public <T> Optional<T> getCapability(@Nonnull Capability<T> capability, Direction side) {
    //        return capabilityCache.getCapability(capability, side);
    //    }
//
    //    public void invalidate() {
    //        capabilityCache.invalidate(Capabilities.RADIATION_ENTITY_CAPABILITY, null);
    //    }
//
    //    @Override
    //    public NbtCompound serializeNBT() {
    //        return defaultImpl.serializeNBT();
    //    }
//
    //    @Override
    //    public void deserializeNBT(NbtCompound nbt) {
    //        defaultImpl.deserializeNBT(nbt);
    //    }
    //}
}


