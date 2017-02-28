package me.catallena.mcaholic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.catallena.mcaholic.listeners.playerMove;

public class DelayLoadConfig implements Runnable {

	private boolean isRunning = true;

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void run() {

		while (isRunning == true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (Player p : Bukkit.getOnlinePlayers()) {

				if (playerMove.pl.getConfig().getString("Players." + p.getName() + ".music") == "true") {
					pluginMain.getMusicThread().getSongPlayer().addPlayer(p);
				} else {
					pluginMain.getMusicThread().getSongPlayer().removePlayer(p);
				}

			}

		}

	}

}
