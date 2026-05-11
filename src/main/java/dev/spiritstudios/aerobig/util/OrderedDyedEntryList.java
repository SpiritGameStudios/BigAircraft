package dev.spiritstudios.aerobig.util;

import java.util.*;
import java.util.function.Function;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

public class OrderedDyedEntryList<T, R extends RegistryEntry<T, ? extends T>> implements Iterable<R> {

    public static final DyeColor[] ORDERED_DYES = new DyeColor[] {
        DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK,
        DyeColor.BROWN, DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW,
        DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE,
        DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK
    };

    private static final int LENGTH = 16;
    private final RegistryEntry<?, ?>[] values = new RegistryEntry[LENGTH];

	public OrderedDyedEntryList(Function<DyeColor, R> filler) {
        for (int i = 0; i < LENGTH; i++)
            this.values[i] = filler.apply(ORDERED_DYES[i]);
	}

    @SuppressWarnings("unchecked")
    public R get(DyeColor color) {
        for (int i = 0; i < LENGTH; i++) {
            DyeColor dyeColor = ORDERED_DYES[i];

            if (dyeColor == color)
                return (R) this.values[i];
        }

        throw new NullPointerException();
    }

    public boolean contains(T t) {
        return Arrays.stream(this.values).anyMatch(entry -> entry.is(t));
	}

    @Override
    @NotNull
    public Iterator<R> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return this.index < LENGTH;
            }

            @SuppressWarnings("unchecked")
            @Override
            public R next() {
                if (!this.hasNext())
                    throw new NoSuchElementException();

                return (R) OrderedDyedEntryList.this.values[this.index++];
            }
        };
    }

}
