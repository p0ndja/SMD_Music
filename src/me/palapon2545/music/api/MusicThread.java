package me.palapon2545.music.api;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.palapon2545.music.pluginMain;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MusicThread implements Runnable {

	private SongPlayer songPlayer;
	private Song[] loadedSongs;
	private int currentSong;
	
	public List<Song> songs = new ArrayList<Song>();

	public MusicThread(File songFolder) {
		currentSong = 0;
		loadedSongs = new Song[1];
		loadSongs(songFolder);
	}

	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {

			// add action bar here to display song info forever (loop ever 2 second)
			if (!songPlayer.isPlaying()) {
				randomSong();
				songPlayer.addPlayer(player);
			}
			songPlayer.setPlaying(true);
		}
	}

	public Song getCurrentSong() {
		return getSongPlayer().getSong();
	}

	public SongPlayer getSongPlayer() {
		return songPlayer;
	}

	private void loadSongs(File songFolder) {
		File[] files = songFolder.listFiles();

		pluginMain.getInstance().getLogger().info("Loading songs from " + songFolder.getPath());
		Bukkit.broadcastMessage(
				ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Loading songs from " + songFolder.getPath());

		for (File file : files) {
			Song song = null;
			try {
				song = NBSDecoder.parse(file);
			} catch (Exception e) {
				pluginMain.getInstance().getLogger().severe("ERROR: Failed to load song " + file.getPath() + "... " + e.getMessage());
				continue;
			}

			songs.add(song);
			Bukkit.broadcastMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Add song " + song.getTitle() + ".");
		}

		if ((long) songs.size() < 1) {
			Bukkit.getServer().getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("SMDMusic"));
		}
		pluginMain.getInstance().getLogger().info("Loaded " + songs.size() + " song(s)!");
		Bukkit.broadcastMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Loaded " + ChatColor.YELLOW
				+ songs.size() + ChatColor.GRAY + " songs!");
		loadedSongs = songs.toArray(loadedSongs);
		songPlayer = new RadioSongPlayer(loadedSongs[0]);
		songPlayer.setPlaying(true);
	}

	public void nextSong(int times) {
		currentSong += times;

		if (currentSong >= loadedSongs.length) {
			currentSong = 0;
		}

		songPlayer = new RadioSongPlayer(loadedSongs[currentSong]);
	}

	public void randomSong() {
		Random rand = new Random();
		int r = rand.nextInt(songs.size());
		for (Player p : Bukkit.getOnlinePlayers()) {
			songPlayer.removePlayer(p);
		}
		songPlayer.setPlaying(false);
		nextSong(r);
		
		String title = (!(pluginMain.getMusicThread().getCurrentSong().getTitle()).isEmpty()) ? pluginMain.getMusicThread().getCurrentSong().getTitle() : ChatColor.GRAY + "Unknown Song" + ChatColor.RESET;
		String author = (!(pluginMain.getMusicThread().getCurrentSong().getAuthor()).isEmpty()) ? pluginMain.getMusicThread().getCurrentSong().getAuthor() : ChatColor.GRAY + "Unknown Author" + ChatColor.RESET;
		
		sendActionBarToAll(ChatColor.WHITE + "[" + ChatColor.YELLOW + "Song" + ChatColor.WHITE + "]"
				+ ChatColor.GRAY + ": " + ChatColor.WHITE + ChatColor.BOLD + title + ChatColor.WHITE + " - "
				+ ChatColor.GOLD + ChatColor.BOLD + author);
		
		
		pluginMain.isThisSongCheckLyric = false;
		pluginMain.isThisSongHaveLyric = false;
	}
	
	public void sendActionBarToAll(String message) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
		}
	}

	public boolean trySetSong(String songName) {
		for (Song song : loadedSongs) {
			if (song.getTitle().equalsIgnoreCase(songName)) {
				songPlayer.setPlaying(false);
				songPlayer = new RadioSongPlayer(song);
				songPlayer.setPlaying(true);

				String title = (!(pluginMain.getMusicThread().getCurrentSong().getTitle()).isEmpty()) ? pluginMain.getMusicThread().getCurrentSong().getTitle() : ChatColor.GRAY + "Unknown Song" + ChatColor.RESET;
				String author = (!(pluginMain.getMusicThread().getCurrentSong().getAuthor()).isEmpty()) ? pluginMain.getMusicThread().getCurrentSong().getAuthor() : ChatColor.GRAY + "Unknown Author" + ChatColor.RESET;

				sendActionBarToAll(ChatColor.WHITE + "[" + ChatColor.YELLOW + "Song" + ChatColor.WHITE + "]"
						+ ChatColor.GRAY + ": " + ChatColor.WHITE + ChatColor.BOLD + title + ChatColor.WHITE + " - "
						+ ChatColor.GOLD + ChatColor.BOLD + author);
				
				pluginMain.isThisSongCheckLyric = false;
				pluginMain.isThisSongHaveLyric = false;
				return true;
			}
		}

		if (!songName.endsWith(".nbs")) {
			songName = songName + ".nbs";
		}

		for (Song song : loadedSongs) {
			if (song.getPath().getName().equalsIgnoreCase(songName)) {
				songPlayer.setPlaying(false);
				songPlayer = new RadioSongPlayer(song);
				songPlayer.setPlaying(true);
				return true;
			}
		}

		return false;
	}

	public Song[] getSongs() {
		return loadedSongs;
	}

}
