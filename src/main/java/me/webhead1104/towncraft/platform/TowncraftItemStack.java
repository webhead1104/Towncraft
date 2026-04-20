package me.webhead1104.towncraft.platform;

import com.google.common.collect.Sets;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.ItemRarity;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.ResolvableProfile;

import java.util.List;

@Setter
public final class TowncraftItemStack {
    private Material material;
    private Component name;
    private List<Component> lore;
    private boolean overrideNameColor;
    private String playerHeadTextures;
    private boolean hideTooltip;

    public TowncraftItemStack(Material material) {
        this.material = material;
    }

    public static TowncraftItemStack of(Material material) {
        return new TowncraftItemStack(material);
    }

    public static TowncraftItemStack empty() {
        return new TowncraftItemStack(Material.AIR);
    }

    public void setLore(Component... lore) {
        this.lore = List.of(lore);
    }

    public void setLore(List<Component> lore) {
        this.lore = lore;
    }

    public void overrideNameColor() {
        overrideNameColor = true;
    }

    public void hideTooltip() {
        hideTooltip = true;
    }

    public ItemStack build() {
        ItemStack.Builder builder = ItemStack.builder(material);
        builder.set(DataComponents.ITEM_NAME, name);
        builder.set(DataComponents.LORE, lore);
        if (overrideNameColor) {
            builder.set(DataComponents.RARITY, ItemRarity.COMMON);
        }
        if (playerHeadTextures != null) {
            if (material != Material.PLAYER_HEAD) {
                throw new IllegalStateException("Tried to set player head texures on a non player head item!");
            }
            ResolvableProfile profile =
                    new ResolvableProfile(new ResolvableProfile.Partial(null, null, List.of(new GameProfile.Property("textures", playerHeadTextures))));
            builder.set(DataComponents.PROFILE, profile);
        }
        if (hideTooltip) {
            builder.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(true, Sets.newHashSet()));
        }
        return builder.build();
    }
}
