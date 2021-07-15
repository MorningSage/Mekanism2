package mekanism.api.text;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class TextComponentUtil {

    private TextComponentUtil() {
    }

    /**
     * @deprecated Use {@link Text#copy()}.
     */
    @Deprecated//TODO - 1.17: Remove this
    public static MutableText getFormattableComponent(Text component) {
        return component.copy();
    }

    /**
     * Builds a formattable text component out of a list of components using a "smart" combination system to allow for automatic replacements, and coloring to take
     * place.
     *
     * @param components Argument components.
     *
     * @return Formattable Text Component.
     */
    public static MutableText build(Object... components) {
        //TODO: Verify that just appending them to the first text component works properly.
        // My suspicion is we will need to chain downwards and append it that way so that the formatting matches
        // from call to call without resetting back to
        MutableText result = null;
        Style cachedStyle = Style.EMPTY;
        for (Object component : components) {
            if (component == null) {
                //If the component doesn't exist just skip it
                continue;
            }
            MutableText current = null;
            if (component instanceof IHasTextComponent) {
                current = ((IHasTextComponent) component).getTextComponent().copy();
            } else if (component instanceof IHasTranslationKey) {
                current = translate(((IHasTranslationKey) component).getTranslationKey());
            } else if (component instanceof EnumColor) {
                cachedStyle = cachedStyle.withColor(((EnumColor) component).getColor());
            } else if (component instanceof Text) {
                //Just append if a text component is being passed
                current = ((Text) component).copy();
            } else if (component instanceof Formatting) {
                cachedStyle = cachedStyle.withFormatting((Formatting) component);
            } else if (component instanceof ClickEvent) {
                cachedStyle = cachedStyle.withClickEvent((ClickEvent) component);
            } else if (component instanceof HoverEvent) {
                cachedStyle = cachedStyle.withHoverEvent((HoverEvent) component);
            } else if (component instanceof Block) {
                current = translate(((Block) component).getTranslationKey());
            } else if (component instanceof Item) {
                current = translate(((Item) component).getTranslationKey());
            } else if (component instanceof ItemStack) {
                current = ((ItemStack) component).getName().copy();
                //} else if (component instanceof FluidStack) {
                //    current = translate(((FluidStack) component).getTranslationKey());
                //} else if (component instanceof Fluid) {
                //    current = translate(((Fluid) component).getAttributes().getTranslationKey());
            } else if (component instanceof Direction) {
                current = getTranslatedDirection((Direction) component);
            } else {
                //Fallback to a generic replacement
                // this handles strings, booleans, numbers, and any type we don't necessarily know about
                current = getString(component.toString());
            }
            if (current == null) {
                //If we don't have a component to add, don't
                continue;
            }
            if (!cachedStyle.isEmpty()) {
                //Apply the style and reset
                current.setStyle(cachedStyle);
                cachedStyle = Style.EMPTY;
            }
            if (result == null) {
                result = current;
            } else {
                result.append(current);
            }
        }
        //TODO: Make this more like smartTranslate? Including back to back formatting where we already have that type of formatting set
        // then convert that
        //Ignores any trailing formatting
        return result;
    }

    private static MutableText getTranslatedDirection(Direction direction) {
        switch (direction) {
            case DOWN:
                return APILang.DOWN.translate();
            case UP:
                return APILang.UP.translate();
            case NORTH:
                return APILang.NORTH.translate();
            case SOUTH:
                return APILang.SOUTH.translate();
            case WEST:
                return APILang.WEST.translate();
            case EAST:
                return APILang.EAST.translate();
        }
        return getString(direction.toString());
    }

    /**
     * Helper to call the constructor for string text components and also convert any non-breaking spaces to spaces so that they render properly.
     *
     * @param component String
     *
     * @return String Text Component.
     */
    public static LiteralText getString(String component) {
        return new LiteralText(cleanString(component));
    }

    private static String cleanString(String component) {
        return component.replace("\u00A0", " ");
    }

    /**
     * Helper to call the constructor for translation text components in case we end up ever needing to do any extra processing.
     *
     * @param key  Translation Key.
     * @param args Arguments.
     *
     * @return Translation Text Component.
     */
    public static TranslatableText translate(String key, Object... args) {
        return new TranslatableText(key, args);
    }

    /**
     * Smarter version of {@link #translate(String, Object...)} that uses a "smart" replacement scheme for parameters to allow for automatic replacements, and coloring to
     * take place.
     *
     * @param key        Translation Key.
     * @param components Argument components.
     *
     * @return Translation Text Component.
     */
    public static TranslatableText smartTranslate(String key, Object... components) {
        if (components.length == 0) {
            //If we don't have any args just short circuit to creating the translation key
            return translate(key);
        }
        List<Object> args = new ArrayList<>();
        Style cachedStyle = Style.EMPTY;
        for (Object component : components) {
            if (component == null) {
                //If the component doesn't exist add it anyways, because we may want to be replacing it
                // with a literal null in the formatted text
                args.add(null);
                cachedStyle = Style.EMPTY;
                continue;
            }
            MutableText current = null;
            if (component instanceof IHasTextComponent) {
                current = ((IHasTextComponent) component).getTextComponent().copy();
            } else if (component instanceof IHasTranslationKey) {
                current = translate(((IHasTranslationKey) component).getTranslationKey());
            } else if (component instanceof Block) {
                current = translate(((Block) component).getTranslationKey());
            } else if (component instanceof Item) {
                current = translate(((Item) component).getTranslationKey());
            } else if (component instanceof ItemStack) {
                current = ((ItemStack) component).getName().copy();
                //} else if (component instanceof FluidStack) {
                //    current = translate(((FluidStack) component).getTranslationKey());
                //} else if (component instanceof Fluid) {
                //    current = translate(((Fluid) component).getAttributes().getTranslationKey());
            } else if (component instanceof Direction) {
                current = getTranslatedDirection((Direction) component);
            }
            //Formatting
            else if (component instanceof EnumColor && cachedStyle.getColor() == null) {
                //No color set yet in the cached style, apply the color
                cachedStyle = cachedStyle.withColor(((EnumColor) component).getColor());
                continue;
            } else if (component instanceof Formatting && !hasStyleType(cachedStyle, (Formatting) component)) {
                //Specific formatting not in the cached style yet, apply it
                cachedStyle = cachedStyle.withFormatting((Formatting) component);
                continue;
            } else if (component instanceof ClickEvent && cachedStyle.getClickEvent() == null) {
                //No click event set yet in the cached style, add the event
                cachedStyle = cachedStyle.withClickEvent((ClickEvent) component);
                continue;
            } else if (component instanceof HoverEvent && cachedStyle.getHoverEvent() == null) {
                //No hover event set yet in the cached style, add the event
                cachedStyle = cachedStyle.withHoverEvent((HoverEvent) component);
                continue;
            } else if (!cachedStyle.isEmpty()) {
                //Only bother attempting these checks if we have a cached format, because
                // otherwise we are just going to want to use the raw text
                if (component instanceof Text) {
                    //Just append if a text component is being passed
                    current = ((Text) component).copy();
                } else if (component instanceof EnumColor) {
                    //If we already have a color in our format allow using the EnumColor's name
                    current = ((EnumColor) component).getName();
                } else {
                    //Fallback to a direct replacement just so that we can properly color it
                    // this handles strings, booleans, numbers, and any type we don't necessarily know about
                    current = getString(component.toString());
                }
            } else if (component instanceof String) {
                //If we didn't format it and it is a string make sure we clean it up
                component = cleanString((String) component);
            }
            if (!cachedStyle.isEmpty()) {
                //If we don't have a text component, then we have to just ignore the formatting and
                // add it directly as an argument. (Note: This should never happen because of the fallback)
                if (current == null) {
                    args.add(component);
                } else {
                    //Otherwise we apply the formatting and then add it
                    args.add(current.setStyle(cachedStyle));
                }
                cachedStyle = Style.EMPTY;
            } else if (current == null) {
                //Add raw
                args.add(component);
            } else {
                //Add the text component variant of it
                args.add(current);
            }
        }
        if (!cachedStyle.isEmpty()) {
            //Add trailing formatting as a color name or just directly
            //Note: We know that we have at least one element in the array, so we don't need to safety check here
            Object lastComponent = components[components.length - 1];
            if (lastComponent instanceof EnumColor) {
                args.add(((EnumColor) lastComponent).getName());
            } else {
                args.add(lastComponent);
            }
            //TODO: If we have multiple trailing formatting types such as a color and italics, we may want to eventually
            // handle how we add them to the arguments better?
        }
        return translate(key, args.toArray());
    }

    private static boolean hasStyleType(Style current, Formatting formatting) {
        return switch (formatting) {
            case OBFUSCATED    -> current.isObfuscated();
            case BOLD          -> current.isBold();
            case STRIKETHROUGH -> current.isStrikethrough();
            case UNDERLINE     -> current.isUnderlined();
            case ITALIC        -> current.isItalic();
            case RESET         -> current.isEmpty();
            default            -> current.getColor() != null;
        };
    }
}

