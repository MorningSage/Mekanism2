package mekanism.api.chemical.gas;

import mekanism.api.MekanismAPI;
import mekanism.api.NBTConstants;
import mekanism.api.util.RegistryUtils;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.providers.IGasProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * GasStack - a specified amount of a defined Gas with certain properties.
 *
 * @author aidancbrady
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GasStack extends ChemicalStack<Gas> {

    /**
     * Empty GasStack instance.
     */
    public static final GasStack EMPTY = new GasStack(MekanismAPI.EMPTY_GAS, 0);

    /**
     * Creates a new GasStack with a defined Gas type and quantity.
     *
     * @param gasProvider - provides the gas type of the stack
     * @param amount      - amount of gas to be referenced in this GasStack
     */
    public GasStack(IGasProvider gasProvider, long amount) {
        super(gasProvider.getChemical(), amount);
    }

    public GasStack(GasStack stack, long amount) {
        this(stack.getType(), amount);
    }

    @Override
    protected Gas getDelegate(Gas gas) {
        if (MekanismAPI.gasRegistry().getId(gas) == null) {
            MekanismAPI.logger.fatal("Failed attempt to create a GasStack for an unregistered Gas {} (type {})", gas.getRegistryName(), gas.getClass().getName());
            throw new IllegalArgumentException("Cannot create a GasStack from an unregistered Gas");
        }
        return gas;
    }

    @Override
    protected Gas getEmptyChemical() {
        return MekanismAPI.EMPTY_GAS;
    }

    /**
     * Returns the GasStack stored in the defined tag compound, or null if it doesn't exist.
     *
     * @param nbtTags - tag compound to read from
     *
     * @return GasStack stored in the tag compound
     */
    public static GasStack readFromNBT(@Nullable NbtCompound nbtTags) {
        if (nbtTags == null || nbtTags.isEmpty()) {
            return EMPTY;
        }
        Gas type = Gas.readFromNBT(nbtTags);
        if (type.isEmptyType()) {
            return EMPTY;
        }
        long amount = nbtTags.getLong(NBTConstants.AMOUNT);
        if (amount <= 0) {
            return EMPTY;
        }
        return new GasStack(type, amount);
    }

    public static GasStack readFromPacket(PacketByteBuf buf) {
        Gas gas = RegistryUtils.readRegistryIdBETA(buf);
        long amount = buf.readVarLong();
        if (gas.isEmptyType()) {
            return EMPTY;
        }
        return new GasStack(gas, amount);
    }

    /**
     * Returns a copied form of this GasStack.
     *
     * @return copied GasStack
     */
    @Override
    public GasStack copy() {
        if (isEmpty()) {
            return EMPTY;
        }
        return new GasStack(this, getAmount());
    }
}
