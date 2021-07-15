package mekanism.common.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class MekanismFluid extends FlowableFluid {

    private final Properties properties;

    public MekanismFluid(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
        super.appendProperties(builder);
    }

    @Override public Fluid getFlowing() { return properties.getFlowingFluid().get(); }
    @Override public Fluid getStill() { return properties.getStillFluid().get(); }
    @Override public Item getBucketItem() { return properties.getBucketItem().get(); }
    @Override protected boolean isInfinite() { return properties.isInfinite(); }
    @Override protected float getBlastResistance() { return properties.getBlastResistance(); }
    @Override protected int getFlowSpeed(WorldView world) { return properties.getFlowSpeed(); }
    @Override protected int getLevelDecreasePerBlock(WorldView world) { return properties.getLevelDecreasePerBlock(); }
    @Override public int getTickRate(WorldView world) { return properties.getTickRate(); }
    @Override public Optional<SoundEvent> getBucketFillSound() { return Optional.ofNullable(properties.getFillSound()); }

    public Optional<SoundEvent> getBucketEmptySound() { return Optional.ofNullable(properties.getEmptySound()); }

    public FluidBlock getFluidBlock() { return properties.getFluidBlock().get(); }
    public Identifier getStillTexture() { return properties.getStillTexture(); }
    public Identifier getFlowingTexture() { return properties.getFlowingTexture(); }
    public Identifier getOverlayTexture() { return properties.getOverlayTexture(); }
    public int getColor() { return properties.getColor(); }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == getStill() || fluid == getFlowing();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !matchesType(fluid);
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return getFluidBlock().getDefaultState().with(net.minecraft.state.property.Properties.LEVEL_15, getBlockStateLevel(state));
    }

    public static class Still extends MekanismFluid {
        public Still(Properties properties) { super(properties); }

        @Override public boolean isStill(FluidState state) { return true; }
        @Override public int getLevel(FluidState state) { return 8; }
    }

    public static class Flowing extends MekanismFluid {
        public Flowing(Properties properties) {
            super(properties);
            setDefaultState(getStateManager().getDefaultState().with(LEVEL, 7));
        }

        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override public boolean isStill(FluidState state) { return false; }
        @Override public int getLevel(FluidState state) { return state.get(LEVEL); }
    }

    public static class Properties {
        private final Supplier<Fluid> stillFluid;
        private final Supplier<Fluid> flowingFluid;
        private final Supplier<BucketItem> bucketItem;
        private final Supplier<FluidBlock> fluidBlock;
        private final boolean infinite;
        private final float blastResistance;
        private final int flowSpeed;
        private final int levelDecreasePerBlock;
        private final int tickRate;

        private final Identifier overlayTexture;
        private final Identifier stillTexture;
        private final Identifier flowingTexture;

        private final SoundEvent fillSound;
        private final SoundEvent emptySound;
        private final boolean gaseous;
        private final Rarity rarity;
        private final int color;
        private final int temperature;
        private final int density;
        private final int viscosity;
        private final int luminosity;

        Properties(Supplier<Fluid> stillFluid, Supplier<Fluid> flowingFluid, Supplier<BucketItem> bucketItem, Supplier<FluidBlock> fluidBlock, boolean infinite, float blastResistance, int flowSpeed, int levelDecreasePerBlock, int tickRate, Identifier overlayTexture, Identifier stillTexture, Identifier flowingTexture, SoundEvent fillSound, SoundEvent emptySound, boolean gaseous, Rarity rarity, int color, int temperature, int density, int viscosity, int luminosity) {
            this.stillFluid = stillFluid;
            this.flowingFluid = flowingFluid;
            this.bucketItem = bucketItem;
            this.fluidBlock = fluidBlock;
            this.infinite = infinite;
            this.blastResistance = blastResistance;
            this.flowSpeed = flowSpeed;
            this.levelDecreasePerBlock = levelDecreasePerBlock;
            this.tickRate = tickRate;
            this.overlayTexture = overlayTexture;
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
            this.fillSound = fillSound;
            this.emptySound = emptySound;
            this.gaseous = gaseous;
            this.rarity = rarity;
            this.color = color;
            this.temperature = temperature;
            this.density = density;
            this.viscosity = viscosity;
            this.luminosity = luminosity;
        }

        public static Builder builder(Identifier stillTexture, Identifier flowingTexture) {
            return new Builder(stillTexture, flowingTexture);
        }

        public Supplier<Fluid> getStillFluid() { return stillFluid; }
        public Supplier<Fluid> getFlowingFluid() { return flowingFluid; }
        public Supplier<BucketItem> getBucketItem() { return bucketItem; }
        public Supplier<FluidBlock> getFluidBlock() { return fluidBlock; }

        public boolean isInfinite() { return infinite; }
        public float getBlastResistance() { return blastResistance; }
        public int getFlowSpeed() { return flowSpeed; }
        public int getLevelDecreasePerBlock() { return levelDecreasePerBlock; }
        public int getTickRate() { return tickRate; }

        public Identifier getOverlayTexture() { return overlayTexture; }
        public Identifier getStillTexture() { return stillTexture; }
        public Identifier getFlowingTexture() { return flowingTexture; }

        public SoundEvent getFillSound() { return fillSound; }
        public SoundEvent getEmptySound() { return emptySound; }

        public boolean isGaseous() { return gaseous; }

        public Rarity getRarity() { return rarity; }

        public int getColor() { return color; }
        public int getTemperature() { return temperature; }
        public int getDensity() { return density; }
        public int getViscosity() { return viscosity; }
        public int getLuminosity() { return luminosity; }

        public static class Builder {
            private Supplier<Fluid> stillFluid;
            private Supplier<Fluid> flowingFluid;
            private Supplier<BucketItem> bucketItem;
            private Supplier<FluidBlock> fluidBlock;
            private boolean infinite = false;
            private float blastResistance = 1.0F;
            private int flowSpeed = 4;
            private int levelDecreasePerBlock = 1;
            private int tickRate = 5;

            private Identifier overlayTexture;
            private Identifier stillTexture;
            private Identifier flowingTexture;
            private SoundEvent fillSound;
            private SoundEvent emptySound;
            private boolean gaseous = false;
            private Rarity rarity = Rarity.COMMON;
            private int color = 0xFFFFFFFF;
            private int temperature = 300;
            private int density = 1000;
            private int viscosity = 1000;
            private int luminosity = 0;

            public Builder(Identifier stillTexture, Identifier flowingTexture) {
                this.stillTexture = stillTexture;
                this.flowingTexture = flowingTexture;
            }

            public Builder stillFluid(Supplier<Fluid> stillFluid) {
                this.stillFluid = stillFluid;
                return this;
            }
            public Builder flowingFluid(Supplier<Fluid> flowingFluid) {
                this.flowingFluid = flowingFluid;
                return this;
            }
            public Builder bucketItem(Supplier<BucketItem> bucketItem) {
                this.bucketItem = bucketItem;
                return this;
            }
            public Builder fluidBlock(Supplier<FluidBlock> fluidBlock) {
                this.fluidBlock = fluidBlock;
                return this;
            }
            public Builder infinite(boolean infinite) {
                this.infinite = infinite;
                return this;
            }
            public Builder blastResistance(float blastResistance) {
                this.blastResistance = blastResistance;
                return this;
            }
            public Builder flowSpeed(int flowSpeed) {
                this.flowSpeed = flowSpeed;
                return this;
            }
            public Builder levelDecreasePerBlock(int levelDecreasePerBlock) {
                this.levelDecreasePerBlock = levelDecreasePerBlock;
                return this;
            }
            public Builder tickRate(int tickRate) {
                this.tickRate = tickRate;
                return this;
            }

            public Builder overlayTexture(Identifier overlayTexture) {
                this.overlayTexture = overlayTexture;
                return this;
            }
            public Builder stillTexture(Identifier stillTexture) {
                this.stillTexture = stillTexture;
                return this;
            }
            public Builder flowingTexture(Identifier flowingTexture) {
                this.flowingTexture = flowingTexture;
                return this;
            }
            public Builder fillSound(SoundEvent fillSound) {
                this.fillSound = fillSound;
                return this;
            }
            public Builder emptySound(SoundEvent emptySound) {
                this.emptySound = emptySound;
                return this;
            }
            public Builder gaseous() {
                this.gaseous = true;
                return this;
            }
            public Builder rarity(Rarity rarity) {
                this.rarity = rarity;
                return this;
            }
            public Builder color(int color) {
                this.color = color;
                return this;
            }
            public Builder temperature(int temperature) {
                this.temperature = temperature;
                return this;
            }
            public Builder density(int density) {
                this.density = density;
                return this;
            }
            public Builder viscosity(int viscosity) {
                this.viscosity = viscosity;
                return this;
            }
            public Builder luminosity(int luminosity) {
                this.luminosity = luminosity;
                return this;
            }

            public Properties build() {
                return new Properties(stillFluid, flowingFluid, bucketItem, fluidBlock, infinite, blastResistance, flowSpeed, levelDecreasePerBlock, tickRate, overlayTexture, stillTexture, flowingTexture, fillSound, emptySound, gaseous, rarity, color, temperature, density, viscosity, luminosity);
            }
        }

        //this.translationKey = builder.translationKey != null ? builder.translationKey :  Util.makeDescriptionId("fluid", fluid.getRegistryName());

    }

}

