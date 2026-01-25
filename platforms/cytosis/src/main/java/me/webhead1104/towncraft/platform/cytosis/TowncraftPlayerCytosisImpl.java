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
package me.webhead1104.towncraft.platform.cytosis;

import me.webhead1104.towncraft.platform.common.TowncraftPlayer;
import me.webhead1104.towncraft.platform.common.inventory.TowncraftInventory;
import me.webhead1104.towncraft.platform.common.inventory.TowncraftPlayerInventory;
import me.webhead1104.towncraft.platform.common.item.TowncraftItemStack;
import me.webhead1104.towncraft.platform.cytosis.inventory.TowncraftPlayerInventoryCytosisImpl;
import me.webhead1104.towncraft.platform.cytosis.item.TowncraftItemStackCytosisImpl;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;

import java.util.UUID;

public record TowncraftPlayerCytosisImpl(CytosisPlayer player) implements TowncraftPlayer {

    @Override
    public UUID getUUID() {
        return player.getUuid();
    }

    @Override
    public String getName() {
        return player.getUsername();
    }

    @Override
    public TowncraftPlayerInventory getInventory() {
        return new TowncraftPlayerInventoryCytosisImpl(player.getInventory());
    }

    @Override
    public void openInventory(TowncraftInventory inventory) {
        player.openInventory((Inventory) inventory.getPlatform());
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public void sendMessage(Component message) {
        player.sendMessage(message);
    }

    @Override
    public TowncraftItemStack getItemOnCursor() {
        return new TowncraftItemStackCytosisImpl(player.getInventory().getCursorItem());
    }

    @Override
    public void setItemOnCursor(TowncraftItemStack itemStack) {
        player.getInventory().setCursorItem((ItemStack) itemStack.toPlatform());
    }
}
