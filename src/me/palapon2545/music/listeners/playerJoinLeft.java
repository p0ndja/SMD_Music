package me.palapon2545.music.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.palapon2545.music.pluginMain;

public class playerJoinLeft implements Listener {

	pluginMain pl;

	public playerJoinLeft(pluginMain pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String noteblockSetting = pl.getConfig().getString("Players." + p.getName() + ".music");
		if (noteblockSetting == null || !p.hasPlayedBefore()) {
			pl.getConfig().set("Players." + p.getName() + ".music", "true");
			pl.saveConfig();
		} else {
			if (noteblockSetting.equalsIgnoreCase("true"))
				pluginMain.getMusicThread().getSongPlayer().addPlayer(p);
			else pluginMain.getMusicThread().getSongPlayer().removePlayer(p);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		pluginMain.getMusicThread().getSongPlayer().removePlayer(e.getPlayer());
		pl.saveConfig();
	}
}
