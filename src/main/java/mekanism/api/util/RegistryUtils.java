package mekanism.api.util;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class RegistryUtils {
    private static final Map<Class<?>, Identifier> registryKeys = new HashMap<>();

    public static <T> Registry<T> registerRegistry(Class<T> clazz, Identifier identifier) {
        registryKeys.put(clazz, identifier);
        return FabricRegistryBuilder.createSimple(clazz, identifier)
            .attribute(RegistryAttribute.SYNCED)
            .attribute(RegistryAttribute.MODDED)
            .attribute(RegistryAttribute.PERSISTED)
            .buildAndRegister();
    }

    @Nullable
    private static <T> Class<T> getExtendedClass(T object) {
        try {
            return (Class<T>) ((ParameterizedType)object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception exception) {
            return null;
        }
    }
    @Nullable private static <T> Identifier getRegistryId(Class<T> clazz) {
        return registryKeys.getOrDefault(clazz, null);
    }
    @Nullable private static <T> Registry<T> getRegistry(Identifier identifier) {
        try {
            return (Registry<T>) Registry.REGISTRIES.get(identifier);
        } catch (Exception exception) {
            return null;
        }
    }

    public static <T> void writeRegistryIdBETA(PacketByteBuf packet, T object) {
        Objects.requireNonNull(object,"Cannot write a null registry entry!");
        final Class<T> clazz = Objects.requireNonNull(getExtendedClass(object),"Cannot find the object's type!");
        final Identifier name = Objects.requireNonNull(getRegistryId(clazz), "Cannot find a registry for the class: " + clazz.getName() + "!");
        final Registry<T> retrievedRegistry = Objects.requireNonNull(getRegistry(name), "Cannot find a registry for the id: " + name.toString() + "!");
        final int rawId = retrievedRegistry.getRawId(object);
        Preconditions.checkArgument(rawId != -1, "Cannot find %s object in %s", object, name);
        packet.writeIdentifier(name);
        packet.writeVarInt(rawId);
    }

    public static <T> T readRegistryIdBETA(PacketByteBuf packet) {
        Identifier location = packet.readIdentifier();
        Registry<T> registry = Objects.requireNonNull(getRegistry(location), "Cannot find a registry for the id: " + location.toString() + "!");
        return registry.get(packet.readVarInt());
    }
}

