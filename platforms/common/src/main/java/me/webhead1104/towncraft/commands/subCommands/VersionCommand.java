package me.webhead1104.towncraft.commands.subCommands;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.platform.common.TowncraftPlayer;
import me.webhead1104.towncraft.utils.BuildInfo;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import revxrsal.commands.annotation.Subcommand;

@Keep
public class VersionCommand implements TowncraftSubCommand {
    @Subcommand("version")
    public void execute(TowncraftPlayer player) {
        Component component = Msg.format("This server is running Towncraft version %s (Data version: %d)", BuildInfo.VERSION, User.LATEST_VERSION);
        component = component.hoverEvent(HoverEvent.showText(Msg.format("<green>Click to copy!")));
        component = component.clickEvent(ClickEvent.copyToClipboard(
                "Version: %s, Data version: %d".formatted(BuildInfo.VERSION, User.LATEST_VERSION)));
        player.sendMessage(component);
    }
}
