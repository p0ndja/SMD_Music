package me.palapon2545.music;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.palapon2545.music.NoteblockAPI.SongPlayer;
import me.palapon2545.music.api.MusicThread;
import me.palapon2545.music.api.tools.ActionBarAPI;
import me.palapon2545.music.cmd.adminCommands;
import me.palapon2545.music.cmd.playerCommands;
import me.palapon2545.music.listeners.playerJoinLeft;

public class pluginMain extends JavaPlugin {

	public static MusicThread mt;
	public static pluginMain instance;
	public static HashMap<String, ArrayList<SongPlayer>> playingSongs = new HashMap<String, ArrayList<SongPlayer>>();
	public static HashMap<String, Byte> playerVolume = new HashMap<String, Byte>();


	public void onEnable() {
		File userfiles;
        try {
            userfiles = new File(getDataFolder() + File.separator + "/songs/");
            if(!userfiles.exists()){
                userfiles.mkdirs();
            }
        } catch(SecurityException e) {
            return;
        }
		Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "SMDMusic System: " + ChatColor.GREEN
				+ ChatColor.BOLD + "Enable");
		instance = this;
		mt = new MusicThread(getSongFolder());
		if (mt.getSongs().length == 0) {
			getLogger().warning("Alert! No songs found.");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		} else {
			Bukkit.getScheduler().runTaskTimer(this, mt, 0, 20);
			getMusicThread().randomSong();
			pluginMain.getMusicThread().getSongPlayer().setPlaying(true);
		}
		regCmds();
		regEvents();
		BukkitScheduler s = getServer().getScheduler();
		s.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {

					if (getConfig().getString("Players." + p.getName() + ".music") == "true") {
						getMusicThread().getSongPlayer().addPlayer(p);
					} else {
						getMusicThread().getSongPlayer().removePlayer(p);
					}

				}
			}
		}, 0L, 20L);
		ActionBarAPI.run();
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.performCommand("music u");
			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		}
	}

	public void onDisable() {
		getMusicThread().getSongPlayer().setPlaying(false);
		Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "SMDMusic System: " + ChatColor.RED
					+ ChatColor.BOLD + "Disable");
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 0);
			getMusicThread().getSongPlayer().removePlayer(p);
		}
	}

	public void regCmds() {
		getCommand("musicadmin").setExecutor(new adminCommands());
		getCommand("music").setExecutor(new playerCommands(this));
	}

	public void regEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new playerJoinLeft(this), this);
	}

	public static boolean isReceivingSong(Player p) {
		return ((pluginMain.playingSongs.get(p.getName()) != null)
				&& (!pluginMain.playingSongs.get(p.getName()).isEmpty()));
	}

	public static void stopPlaying(Player p) {
		if (pluginMain.playingSongs.get(p.getName()) == null) {
			return;
		}
		for (SongPlayer s : pluginMain.playingSongs.get(p.getName())) {
			s.removePlayer(p);
		}
	}

	public static void setPlayerVolume(Player p, byte volume) {
		playerVolume.put(p.getName(), volume);
	}

	public static byte getPlayerVolume(Player p) {
		Byte b = playerVolume.get(p.getName());
		if (b == null) {
			b = 100;
			playerVolume.put(p.getName(), b);
		}
		return b;
	}

	public static pluginMain getInstance() {
		return instance;
	}

	public static MusicThread getMusicThread() {
		return pluginMain.mt;
	}

	public static File getSongFolder() {
		return new File(getInstance().getDataFolder(), "songs/");
	}

}
