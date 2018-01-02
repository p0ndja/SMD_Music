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
		pl.getConfig().set("Players." + p.getName() + ".music", "true");
		pl.saveConfig();
		pluginMain.getMusicThread().getSongPlayer().addPlayer(p);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		pluginMain.getMusicThread().getSongPlayer().removePlayer(p);
		pl.saveConfig();
	}
}
