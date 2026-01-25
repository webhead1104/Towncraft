package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion16 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return rootNode -> replaceBuilding(rootNode, () -> {
            ConfigurationNode node = createNode();
            node.node("class").raw("StaticWorldTile");
            node.node("properties", "material").raw("minecraft:cobblestone");
            return node;
        }, () -> {
            ConfigurationNode node = createNode();
            node.node("class").raw("TownHallTile");
            return node;
        });
    }

    @Override
    public int getVersion() {
        return 16;
    }
}