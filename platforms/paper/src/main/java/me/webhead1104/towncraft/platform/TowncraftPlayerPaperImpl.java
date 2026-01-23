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
package me.webhead1104.towncraft.platform;

import me.webhead1104.towncraft.platform.inventory.TowncraftInventory;
import me.webhead1104.towncraft.platform.inventory.TowncraftInventoryPaperImpl;
import me.webhead1104.towncraft.platform.inventory.TowncraftPlayerInventory;
import me.webhead1104.towncraft.platform.inventory.TowncraftPlayerInventoryPaperImpl;
import me.webhead1104.towncraft.platform.item.TowncraftItemStack;
import me.webhead1104.towncraft.platform.item.TowncraftItemStackPaperImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record TowncraftPlayerPaperImpl(Player player) implements TowncraftPlayer {

    public TowncraftPlayerPaperImpl(HumanEntity humanEntity) {
        this((Player) humanEntity);
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public TowncraftPlayerInventory getInventory() {
        return new TowncraftPlayerInventoryPaperImpl(player.getInventory());
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
    public TowncraftInventory getOpenInventory() {
        return new TowncraftInventoryPaperImpl((Inventory) player.getOpenInventory());
    }

    @Override
    public void sendMessage(Component message) {
        player.sendMessage(message);
    }

    @Override
    public TowncraftItemStack getItemOnCursor() {
        return new TowncraftItemStackPaperImpl(player.getItemOnCursor());
    }

    @Override
    public void setItemOnCursor(TowncraftItemStack itemStack) {
        player.setItemOnCursor((ItemStack) itemStack.toPlatform());
    }
}
