package me.webhead1104.towncraft;

import me.webhead1104.towncraft.commands.TowncraftCommand;
import me.webhead1104.towncraft.platform.TowncraftPlayer;
import net.cytonic.cytosis.events.api.Listener;
import net.cytonic.cytosis.plugins.CytosisPlugin;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import revxrsal.commands.Lamp;
import revxrsal.commands.minestom.MinestomLamp;
import revxrsal.commands.minestom.actor.MinestomCommandActor;

public class Main implements CytosisPlugin {

    @Override
    public void initialize() {
        MinecraftServer.getConnectionManager().setPlayerProvider(TowncraftPlayer::new);
        TowncraftPlatformManager.init();

        Lamp<MinestomCommandActor> lamp = MinestomLamp.builder()
                .parameterTypes(TowncraftPlatformManager::registerParameterTypes).build();
        lamp.register(new TowncraftCommand());
        TowncraftPlatformManager.initCommands(lamp);
    }

    @Override
    public void shutdown() {
        TowncraftPlatformManager.shutdown();
    }

    @Listener
    private void onJoin(PlayerSpawnEvent event) {
        TowncraftPlatformManager.onJoin((TowncraftPlayer) event.getPlayer());
    }

    @Listener
    private void onLeave(PlayerDisconnectEvent event) {
        TowncraftPlatformManager.onLeave((TowncraftPlayer) event.getPlayer());
    }
}
