package dev.spiritstudios.aerobig.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class DyedItemList<T extends Item> implements Iterable<ItemEntry<T>> {

	private static final int COLOR_AMOUNT = DyeColor.values().length;

	private final ItemEntry<?>[] values = new ItemEntry<?>[COLOR_AMOUNT];

	public DyedItemList(Function<DyeColor, ItemEntry<? extends T>> filler) {
		for (DyeColor color : DyeColor.values())
			this.values[color.ordinal()] = filler.apply(color);
	}

	@SuppressWarnings("unchecked")
	public ItemEntry<T> get(DyeColor color) {
		return (ItemEntry<T>) this.values[color.ordinal()];
	}

	public boolean contains(Block block) {
		for (ItemEntry<?> entry : this.values) {
			if (entry.is(block))
				return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public ItemEntry<T>[] toArray() {
		return (ItemEntry<T>[]) Arrays.copyOf(this.values, this.values.length);
	}

	@Override
    @NotNull
	public Iterator<ItemEntry<T>> iterator() {
		return new Iterator<>() {

			private int index = 0;

			@Override
			public boolean hasNext() {
				return this.index < DyedItemList.this.values.length;
			}

			@SuppressWarnings("unchecked")
			@Override
			public ItemEntry<T> next() {
				if (!this.hasNext())
					throw new NoSuchElementException();

				return (ItemEntry<T>) DyedItemList.this.values[this.index++];
			}

		};
	}

}
