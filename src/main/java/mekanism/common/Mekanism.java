package mekanism.common;

import com.google.common.base.Preconditions;
import io.netty.buffer.Unpooled;
import mekanism.api.MekanismAPI;
import mekanism.api.events.ItemEvents;
import mekanism.common.config.MekanismConfig;
import mekanism.common.network.PacketHandler;
import mekanism.common.registries.*;
import mekanism.common.util.TagRegistryUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class Mekanism implements ModInitializer {
    public static final String MODID = MekanismAPI.MEKANISM_MODID;
    public static final String MOD_NAME = "Mekanism";
    public static final CreativeTabMekanism tabMekanism = new CreativeTabMekanism();
    public static final PacketHandler packetHander = new PacketHandler();

    @Environment(EnvType.SERVER)
    public static final String ON_SERVER = "Hello, this is from the server";

    /**
     * Mekanism logger instance
     */
    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    public static <T> T unsafeCallWhenOn(Supplier<T> client, Supplier<T> server) {
        try {
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                return client.get();
            }

            return server.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getClient() {
        return MinecraftClient.getInstance().getGameVersion();
    }
    private String getServer() {

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return getClient();
        }

        return "On Server, but still no crash";
    }


    @Override
    public void onInitialize() {
        MekanismConfig.init();
        TagRegistryUtils.init();
        MekanismItems.ITEMS.getAllItems();
        MekanismBlocks.BLOCKS.getAllBlocks();
        MekanismFluids.FLUIDS.getAllFluids();
        MekanismTileEntityTypes.TILE_ENTITY_TYPES.id("asdf");

        //packetHander.initialize();

        ItemEvents.ON_CRAFTED.register((playerEntity, stack, inventory) -> {
            playerEntity.sendSystemMessage(
                Text.of("Crafted Something - " + (playerEntity.getEntityWorld().isClient
                    ? "Client"
                    : "Server")
                ),
                Util.NIL_UUID
            );

            if (stack.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) stack.getItem()).getBlock();

                if (block instanceof BlockEntityProvider) {
                    BlockEntityProvider blockEntityProvider = (BlockEntityProvider) block;

                    BlockPos blockPos = playerEntity.getBlockPos();
                    BlockState blockState = block.getDefaultState();

                    BlockEntity blockEntity = blockEntityProvider.createBlockEntity(blockPos, blockState);

                    if (blockEntity != null) {
                        BlockEntityType<?> blockEntityType = blockEntity.getType();

                        if (TagRegistryUtils.CARDBOARD_BOX_IGNORED.contains(blockEntityType)) {
                            playerEntity.sendSystemMessage(
                                Text.of("Crafted Cardboard Box Ignored - " + (playerEntity.getEntityWorld().isClient
                                    ? "Client"
                                    : "Server")
                                ),
                                Util.NIL_UUID
                            );
                        }
                    }
                }
            }
        });

        String sideClient = unsafeCallWhenOn(
            this::getClient,
            this::getServer
        );

        logger.info(sideClient);

        Gas emptyGas = Registry.register(gasRegistry, new Identifier("mekanism:empty_gas"), Gas.EMPTY);
        Gas testGas = Registry.register(gasRegistry, new Identifier("mekanism:test_gas"), new Gas("test"));

        GasStack originalStack = new GasStack(testGas, 10);
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        originalStack.writeToPacket(packet);
        GasStack newStack = GasStack.readFromPacket(packet);

        originalStack = GasStack.EMPTY;
        packet = new PacketByteBuf(Unpooled.buffer());
        originalStack.writeToPacket(packet);
        newStack = GasStack.readFromPacket(packet);

    }

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    private static final Registry<Gas> gasRegistry = RegistryUtils.registerRegistry(Gas.class, new Identifier("mekanism:gas"));
    private static final Registry<Slurry> slurryRegistry = RegistryUtils.registerRegistry(Slurry.class, new Identifier("mekanism:slurry"));

    public static abstract class Chemical<CHEMICAL extends Chemical<CHEMICAL>> {
        public Chemical() {}
        public abstract boolean isEmptyType();
    }
    public static class Gas extends Chemical<Gas> {
        public static Gas EMPTY = new Gas("empty");

        private final String testValue;

        public Gas(String testValue) {
            this.testValue = testValue;
        }

        public String getTestValue() {
            return testValue;
        }

        @Override
        public boolean isEmptyType() {
            return this == EMPTY;
        }
    }
    public static class Slurry extends Chemical<Slurry> {
        public static Slurry EMPTY = new Slurry(0);

        private final int testValue;

        public Slurry(int testValue) {
            this.testValue = testValue;
        }

        public int getTestValue() {
            return testValue;
        }

        @Override
        public boolean isEmptyType() {
            return this == EMPTY;
        }
    }
    private static abstract class ChemicalStack<CHEMICAL extends Chemical<CHEMICAL>> {
        private final CHEMICAL base;
        private final long amount;

        public CHEMICAL getType() { return base; }
        public long getAmount() { return amount; }

        public ChemicalStack(CHEMICAL base, long amount) {
            this.base = base;
            this.amount = amount;
        }

        public void writeToPacket(PacketByteBuf packet) {
            RegistryUtils.writeChemicalRegistryId(packet, getType());
            packet.writeVarLong(getAmount());
        }
    }

    public static class GasStack extends ChemicalStack<Gas> {
        public static final GasStack EMPTY = new GasStack(Gas.EMPTY, 0);

        public GasStack(Gas gas, long amount) {
            super(gas, amount);
        }

        public static GasStack readFromPacket(PacketByteBuf packet) {
            Gas gas = RegistryUtils.readChemicalRegistryId(packet);
            long amount = packet.readVarLong();
            if (gas.isEmptyType()) {
                return EMPTY;
            }
            return new GasStack(gas, amount);
        }
    }
    public static class SlurryStack extends ChemicalStack<Slurry> {
        public static final SlurryStack EMPTY = new SlurryStack(Slurry.EMPTY, 0);

        public SlurryStack(Slurry slurry, long amount) {
            super(slurry, amount);
        }

        public static SlurryStack readFromPacket(PacketByteBuf packet) {
            Slurry slurry = RegistryUtils.readChemicalRegistryId(packet);
            long amount = packet.readVarLong();
            if (slurry.isEmptyType()) {
                return EMPTY;
            }
            return new SlurryStack(slurry, amount);
        }
    }

    public static class RegistryUtils {
        private static final Map<Class<?>, Identifier> registryKeys = new HashMap<>();

        public static <T> Registry<T> registerRegistry(Class<T> clazz, Identifier identifier) {
            registryKeys.put(clazz, identifier);
            return FabricRegistryBuilder.createSimple(clazz, identifier).buildAndRegister();
        }

        @Nullable private static <T> Class<T> getExtendedClass(T object) {
            try {
                return (Class<T>) ((ParameterizedType)object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            } catch (Exception exception) {
                return null;
            }
        }
        @Nullable private static <T> Identifier getRegistryId(Class<T> clazz) {
            return registryKeys.getOrDefault(clazz, null);
        }
        @Nullable private static <T> Registry<T> getClassRegistry(Identifier identifier) {
            try {
                return (Registry<T>) Registry.REGISTRIES.get(identifier);
            } catch (Exception exception) {
                return null;
            }
        }

        public static <T> void writeChemicalRegistryId(PacketByteBuf packet, T object) {
            Objects.requireNonNull(object,"Cannot write a null registry entry!");
            final Class<T> clazz = Objects.requireNonNull(getExtendedClass(object),"Cannot find the extended chemical type!");
            final Identifier name = Objects.requireNonNull(getRegistryId(clazz), "Cannot find a registry for the class: " + clazz.getName() + "!");
            final Registry<T> retrievedRegistry = Objects.requireNonNull(getClassRegistry(name), "Cannot find a registry for the id: " + name.toString() + "!");
            final int rawId = retrievedRegistry.getRawId(object);
            Preconditions.checkArgument(rawId != -1, "Cannot find the chemical in %s", name);
            packet.writeIdentifier(name);
            packet.writeVarInt(rawId);
        }

        public static <T> T readChemicalRegistryId(PacketByteBuf packet) {
            Identifier location = packet.readIdentifier();
            Registry<T> registry = Objects.requireNonNull(getClassRegistry(location), "Cannot find a registry for the id: " + location.toString() + "!");
            return registry.get(packet.readVarInt());
        }
    }
}

