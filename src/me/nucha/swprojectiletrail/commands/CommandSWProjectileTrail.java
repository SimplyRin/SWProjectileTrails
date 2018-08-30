package me.nucha.swprojectiletrail.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nucha.swprojectiletrail.SWProjectileTrail;
import me.nucha.swprojectiletrail.effects.ProjectileTrail;
import me.nucha.swprojectiletrail.utils.ConfigUtil;

public class CommandSWProjectileTrail implements CommandExecutor {

	private SWProjectileTrail plugin;
	public String prefix = "§8[§cSWProjectileTrail§8] §r";

	public CommandSWProjectileTrail(SWProjectileTrail plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("gui")) {
				if (!(sender instanceof Player)) {
					return true;
				}
				Player p = (Player) sender;
				plugin.getGuiKillEffectSelector().open(p);
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				plugin.getProjectileTrailManager().saveProjectileTrails();
				plugin.reloadConfig();
				ConfigUtil.init(plugin);
				sender.sendMessage(prefix + "§aconfig.yml reloaded!");
				return true;
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("gui")) {
				Player t = Bukkit.getPlayer(args[1]);
				if (t != null) {
					plugin.getGuiKillEffectSelector().open(t);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("give")) {
				sender.sendMessage(this.prefix + "§cUsage: /swpt give <player> <effect>");
				sender.sendMessage(this.prefix + "§bEffects: VANILLA, HEARTS, GREEN_STAR, NOTES");
				return true;
			}
			if (args[0].equalsIgnoreCase("take")) {
				sender.sendMessage(this.prefix + "§cUsage: /swpt take <player> <effect>");
				sender.sendMessage(this.prefix + "§bEffects: VANILLA, HEARTS, GREEN_STAR, NOTES");
				return true;
			}
		}
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("give")) {
				Player t = Bukkit.getPlayer(args[1]);
				if (t == null) {
					sender.sendMessage(this.prefix + "§cThat player is not online!");
					return true;
				}
				ProjectileTrail projectileTrail = this.plugin.getProjectileTrailManager().getProjectileTrailById(args[2].toUpperCase());
				if(projectileTrail == null) {
					sender.sendMessage(this.prefix + "§cProjectile Trail not found!");
					return true;
		        }
				if (!this.plugin.getProjectileTrailManager().hasProjectileTrail(t, projectileTrail)) {
					this.plugin.getProjectileTrailManager().giveProjectileTrail(t, projectileTrail);
					sender.sendMessage(this.prefix + "§aYou gave §e" + t.getName() + " §athe §b" + projectileTrail.getName() + " Projectile Trail");
				} else {
					sender.sendMessage(this.prefix + "§e" + t.getName() + " §calready has the §b" + projectileTrail.getName() + " Projectile Trail");
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("take")) {
				Player t = Bukkit.getPlayer(args[1]);
				if (t == null) {
					sender.sendMessage(this.prefix + "§cThat player is not online!");
					return true;
				}
				ProjectileTrail projectileTrail = this.plugin.getProjectileTrailManager().getProjectileTrailById(args[2].toUpperCase());
				if (projectileTrail == null) {
					sender.sendMessage(this.prefix + "§cProjectile Trail not found!");
					return true;
				}
				if (this.plugin.getProjectileTrailManager().hasProjectileTrail(t, projectileTrail)) {
					this.plugin.getProjectileTrailManager().takeProjectileTrail(t, projectileTrail);
					sender.sendMessage(this.prefix + "§aYou took §athe §b" + projectileTrail.getName() + " Projectile Trail §afrom §e" + t.getName());
				} else {
					sender.sendMessage(this.prefix + "§e" + t.getName() + " §cdoesn't have the §b" + projectileTrail.getName() + " Projectile Trail");
				}
				return true;
			}
		}
		sender.sendMessage(prefix + "§a-- §cSWProjectileTrail §aby §eNucha §a--");
		sender.sendMessage(prefix + "§a/swpt gui [player] §2--- §bOpen Projectile Trail selector");
		sender.sendMessage(prefix + "§a/swpt give <player> <effect> §2--- §bGive a player a Projectile Projectile");
		sender.sendMessage(prefix + "§a/swpt take <player> <effect> §2--- §bTake a Projectile Projectile from a player");
		sender.sendMessage(prefix + "§a/swpt reload §2--- §bReloads config.yml");
		return true;
	}

}
