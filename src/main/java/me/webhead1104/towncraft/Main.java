package me.webhead1104.towncraft;

import com.google.common.base.Stopwatch;
import me.webhead1104.towncraft.commands.TowncraftCommand;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.database.DatabaseManager;
import me.webhead1104.towncraft.platform.TowncraftPlayer;
import net.cytonic.cytosis.events.api.Async;
import net.cytonic.cytosis.events.api.Listener;
import net.cytonic.cytosis.plugins.CytosisPlugin;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import revxrsal.commands.Lamp;
import revxrsal.commands.minestom.MinestomLamp;
import revxrsal.commands.minestom.actor.MinestomCommandActor;

import java.util.UUID;

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

    @Async
    @Listener
    private void onJoin(PlayerSpawnEvent event) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        DatabaseManager manager = TowncraftPlatformManager.getDatabaseManager();
        TowncraftPlayer player = (TowncraftPlayer) event.getPlayer();
        UUID uuid = player.getUuid();
        if (manager.userExists(uuid)) {
            User user = User.fromJson(manager.getUser(uuid), player);
            TowncraftPlatformManager.getUserManager().setUser(uuid, user);
            Towncraft.getLogger().info("Data has been loaded for {}", player.getUsername());
            return;
        }

        Towncraft.getLogger().info("Created new user for {}", player.getUsername());
        User user = new User(uuid);
        manager.saveUser(uuid, user.toString());
        TowncraftPlatformManager.getUserManager().setUser(uuid, user);
        Towncraft.getLogger().info("Join event done in {}ms", stopwatch.elapsed().toMillis());
    }

    @Async
    @Listener
    private void onLeave(PlayerDisconnectEvent event) {
        TowncraftPlayer player = (TowncraftPlayer) event.getPlayer();

        player.getUser().save();
        TowncraftPlatformManager.getUserManager().removeUser(player.getUuid());
        Towncraft.getLogger().info("Data has been saved for {}", player.getName());
    }
}
