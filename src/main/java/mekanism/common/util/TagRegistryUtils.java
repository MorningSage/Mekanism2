package mekanism.common.util;

import com.google.common.collect.Sets;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.tag.RequiredTagList;
import net.minecraft.tag.RequiredTagListRegistry;
import net.minecraft.tag.Tag;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Set;

public class TagRegistryUtils {
    private static final Set<RegistryKey<?>> CUSTOM_REGISTRY_KEYS = Sets.newHashSet();
    private static final Set<RequiredTagList<?>> CUSTOM_TAG_LISTS = Sets.newHashSet();

    public static final RequiredTagList<BlockEntityType<?>> BLOCK_ENTITY_TAG_REGISTRY = register(Registry.BLOCK_ENTITY_TYPE_KEY, "tags/block_entity_types");

    public static final Tag.Identified<BlockEntityType<?>> CARDBOARD_BOX_IGNORED = BLOCK_ENTITY_TAG_REGISTRY.add("mekanism:cardboard_box_ignored");


    private TagRegistryUtils() { }

    public static void init() { }

    public static <T> RequiredTagList<T> register(RegistryKey<? extends Registry<T>> registryKey, String dataType) {
        RequiredTagList<T> requiredTagList = RequiredTagListRegistry.register(registryKey, dataType);
        CUSTOM_REGISTRY_KEYS.add(registryKey);
        CUSTOM_TAG_LISTS.add(requiredTagList);
        return requiredTagList;
    }

    public static Set<RegistryKey<?>> getCustomRegistryKeys() {
        return CUSTOM_REGISTRY_KEYS;
    }

    public static Set<RequiredTagList<?>> getCustomTagLists() {
        return CUSTOM_TAG_LISTS;
    }

}
