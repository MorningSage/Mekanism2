package mekanism.common.item;

import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.tier.AlloyTier;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.network.packet.to_client.RespondServerString;
import mekanism.common.network.packet.to_server.RequestServerString;
import mekanism.common.tile.base.TileEntityMekanism;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.List;

public class ItemAlloy extends Item {

    private final AlloyTier tier;

    public ItemAlloy(AlloyTier tier, Settings properties) {
        super(properties);
        this.tier = tier;
    }

    public AlloyTier getTier() {
        return tier;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!user.world.isClient) {
            List<Entity> passengers = entity.getPassengerList();

            RegistryKey<World> registryKey = user.world.getRegistryKey() == World.NETHER ? World.OVERWORLD : World.NETHER;
            ServerWorld serverWorld2 = user.world.getServer().getWorld(registryKey);

            LivingEntity newEntity = FabricDimensions.teleport(entity, serverWorld2, new TeleportTarget(new Vec3d(-33, 85, -2),  Vec3d.ZERO, 0.0F, 0.0F));

            if (newEntity != null) {
                for (Entity passenger : passengers) {
                    Entity newPassenger = FabricDimensions.teleport(passenger, serverWorld2, new TeleportTarget(new Vec3d(-33, 85, -2),  Vec3d.ZERO, 0.0F, 0.0F));
                    newPassenger.startRiding(newEntity);
                }
            }


            //ServerWorld serverWorld = (ServerWorld) user.world;
            //RegistryKey<World> registryKey = user.world.getRegistryKey() == World.NETHER ? World.OVERWORLD : World.NETHER;
            //ServerWorld serverWorld2 = serverWorld.getServer().getWorld(registryKey);
//
            //entity.moveToWorld(serverWorld2);
        } else {
            //Mekanism.packetHander.sendToServer(new RequestServerString(user.getUuid()));
        }

        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if (blockEntity instanceof TileEntityMekanism) {
            Capabilities.STRICT_ENERGY_CAPABILITY.maybeGet(blockEntity).ifPresent(iStrictEnergyHandler -> {
                FloatingLong amount = iStrictEnergyHandler.insertEnergy(FloatingLong.ONE, Action.EXECUTE);
                if (amount.intValue() > 0) {
                    context.getPlayer().sendSystemMessage(Text.of("Done!"), Util.NIL_UUID);
                } else {
                    context.getPlayer().sendSystemMessage(Text.of("Failed!"), Util.NIL_UUID);
                }
            });
        }
        return super.useOnBlock(context);
    }
}

