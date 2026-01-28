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
