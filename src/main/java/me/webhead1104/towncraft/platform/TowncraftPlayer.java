package me.webhead1104.towncraft.platform;

import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.data.objects.User;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

public class TowncraftPlayer extends CytosisPlayer {

    public TowncraftPlayer(@NotNull PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public User getUser() {
        return TowncraftPlatformManager.getUserManager().getUser(getUuid());
    }

    public ItemStack getItemOnCursor() {
        return inventory.getCursorItem();
    }

    public void setItemOnCursor(ItemStack itemStack) {
        inventory.setCursorItem(itemStack);
    }

    public void setItemOnCursor(TowncraftItemStack itemStack) {
        setItemOnCursor(itemStack.build());
    }
}
