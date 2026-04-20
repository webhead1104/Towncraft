/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.webhead1104.towncraft.menus;

import lombok.Getter;
import me.devnatan.inventoryframework.ViewContainer;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.Viewer;
import me.webhead1104.towncraft.platform.TowncraftItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public final class TowncraftViewContainer implements ViewContainer {
    @Getter
    private final AbstractInventory inventory;
    @Getter
    private final boolean shared;
    private final ViewType type;
    private final boolean proxied;

    public TowncraftViewContainer(@NotNull AbstractInventory inventory, boolean shared, ViewType type, boolean proxied) {
        this.inventory = inventory;
        this.shared = shared;
        this.type = type;
        this.proxied = proxied;
    }

    @Override
    public boolean isProxied() {
        return proxied;
    }

    @Override
    public String getTitle() {
        if (inventory instanceof Inventory realInventory) {
            Component title = realInventory.getTitle();
            return PlainTextComponentSerializer.plainText().serialize(title);
        }
        return "";
    }

    @Override
    public String getTitle(@NotNull Viewer viewer) {
        //since we will probably never use shared contexts we can do this
        return getTitle();
    }

    @Override
    public @NotNull ViewType getType() {
        return type;
    }

    @Override
    public int getRowsCount() {
        return getSize() / getColumnsCount();
    }

    @Override
    public int getColumnsCount() {
        return type.getColumns();
    }

    @Override
    public void renderItem(int slot, Object item) {
        requireSupportedItem(item);
        inventory.setItemStack(slot, (ItemStack) item);
    }

    @Override
    public void removeItem(int slot) {
        inventory.setItemStack(slot, ItemStack.AIR);
    }

    @Override
    public boolean matchesItem(int slot, Object item, boolean exactly) {
        requireSupportedItem(item);
        final ItemStack target = inventory.getItemStack(slot);
        if (item instanceof ItemStack)
            return exactly ? target.equals(item) : target.isSimilar((ItemStack) item);

        return false;
    }

    @Override
    public boolean isSupportedItem(Object item) {
        return item == null || item instanceof TowncraftItemStack;
    }

    private void requireSupportedItem(Object item) {
        if (isSupportedItem(item)) return;

        throw new IllegalStateException(
                "Unsupported item type: " + item.getClass().getName());
    }

    @Override
    public boolean hasItem(int slot) {
        try {
            return !inventory.getItemStack(slot).isAir();
        } catch (final ArrayIndexOutOfBoundsException ignored) {
            // just supress AIOOBE here, we cannot check if slot matches container constraints
            // by `size >= 0 && size <= getLastSlot()` because some containers are not aligned.
            // Aligned inventory types = perfect grid (NxN) like chest, workbench..
            return false;
        }
    }

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public int getSlotsCount() {
        return getSize() - 1;
    }

    @Override
    public int getFirstSlot() {
        return 0;
    }

    @Override
    public int getLastSlot() {
        final int[] resultSlots = getType().getResultSlots();
        int lastSlot = getSlotsCount();
        if (resultSlots != null) {
            for (final int resultSlot : resultSlots) {
                if (resultSlot == lastSlot) lastSlot--;
            }
        }

        return lastSlot;
    }

    @Override
    public void changeTitle(@Nullable Object title, @NotNull Viewer target) {
        changeTitle(title);
    }

    public void changeTitle(@Nullable Object title) {
        if (inventory instanceof Inventory realInventory) {
            if (title instanceof Component component) {
                realInventory.setTitle(component);
                return;
            }
            if (title instanceof String string) {
                realInventory.setTitle(Component.text(string));
            }
        }
    }

    @Override
    public boolean isEntityContainer() {
        return inventory instanceof PlayerInventory;
    }

    @Override
    public void open(@NotNull final Viewer viewer) {
        viewer.open(this);
    }

    @Override
    public void close() {
        new ArrayList<>(inventory.getViewers()).forEach(Player::closeInventory);
    }

    @Override
    public void close(@NotNull Viewer viewer) {
        viewer.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TowncraftViewContainer that = (TowncraftViewContainer) o;
        return shared == that.shared
                && Objects.equals(inventory, that.inventory)
                && Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventory, shared, getType());
    }

    @Override
    public String toString() {
        return "TowncraftViewContainer{" + "inventory=" + inventory + ", shared=" + shared + ", type=" + type + '}';
    }
}
