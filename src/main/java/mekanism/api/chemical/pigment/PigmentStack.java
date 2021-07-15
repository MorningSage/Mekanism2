package mekanism.api.chemical.pigment;

import mekanism.api.MekanismAPI;
import mekanism.api.NBTConstants;
import mekanism.api.util.RegistryUtils;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.providers.IPigmentProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PigmentStack extends ChemicalStack<Pigment> {

    /**
     * Empty PigmentStack instance.
     */
    public static final PigmentStack EMPTY = new PigmentStack(MekanismAPI.EMPTY_PIGMENT, 0);

    /**
     * Creates a new PigmentStack with a defined pigment type and quantity.
     *
     * @param pigmentProvider - provides the pigment type of the stack
     * @param amount          - amount of the pigment to be referenced in this PigmentStack
     */
    public PigmentStack(IPigmentProvider pigmentProvider, long amount) {
        super(pigmentProvider.getChemical(), amount);
    }

    public PigmentStack(PigmentStack stack, long amount) {
        this(stack.getType(), amount);
    }

    @Override
    protected Pigment getDelegate(Pigment pigment) {
        if (MekanismAPI.pigmentRegistry().getId(pigment) == null) {
            MekanismAPI.logger.fatal("Failed attempt to create a PigmentStack for an unregistered Pigment {} (type {})", pigment.getRegistryName(),
                pigment.getClass().getName());
            throw new IllegalArgumentException("Cannot create a PigmentStack from an unregistered Pigment");
        }
        return pigment;
    }

    @Override
    protected Pigment getEmptyChemical() {
        return MekanismAPI.EMPTY_PIGMENT;
    }

    /**
     * Returns the PigmentStack stored in the defined tag compound, or null if it doesn't exist.
     *
     * @param nbtTags - tag compound to read from
     *
     * @return PigmentStack stored in the tag compound
     */
    public static PigmentStack readFromNBT(@Nullable NbtCompound nbtTags) {
        if (nbtTags == null || nbtTags.isEmpty()) {
            return EMPTY;
        }
        Pigment type = Pigment.readFromNBT(nbtTags);
        if (type.isEmptyType()) {
            return EMPTY;
        }
        long amount = nbtTags.getLong(NBTConstants.AMOUNT);
        if (amount <= 0) {
            return EMPTY;
        }
        return new PigmentStack(type, amount);
    }

    public static PigmentStack readFromPacket(PacketByteBuf buf) {
        Pigment pigment = RegistryUtils.readRegistryIdBETA(buf);
        long amount = buf.readVarLong();
        if (pigment.isEmptyType()) {
            return EMPTY;
        }
        return new PigmentStack(pigment, amount);
    }

    /**
     * Returns a copied form of this PigmentStack.
     *
     * @return copied PigmentStack
     */
    @Override
    public PigmentStack copy() {
        if (isEmpty()) {
            return EMPTY;
        }
        return new PigmentStack(this, getAmount());
    }
}
