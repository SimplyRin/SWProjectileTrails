package me.nucha.swprojectiletrail.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.nucha.swprojectiletrail.SWProjectileTrail;
import me.nucha.swprojectiletrail.effects.GreenStarTrail;
import me.nucha.swprojectiletrail.effects.HeartsTrail;
import me.nucha.swprojectiletrail.effects.NotesTrail;
import me.nucha.swprojectiletrail.effects.ProjectileTrail;
import me.nucha.swprojectiletrail.effects.VanillaTrail;

public class ProjectileTrailManager {

	private List<ProjectileTrail> projectileTrails;
	private HashMap<String, ProjectileTrail> projectileTrailMap;
	private HashMap<String, List<String>> projectileTrailsMap;
	private SWProjectileTrail plugin;

	public ProjectileTrailManager(SWProjectileTrail plugin) {
		projectileTrails = new ArrayList<>();
		projectileTrailMap = new HashMap<>();
		projectileTrailsMap = new HashMap<>();
		registerProjectileTrail(new VanillaTrail());
		registerProjectileTrail(new HeartsTrail());
		registerProjectileTrail(new GreenStarTrail());
		registerProjectileTrail(new NotesTrail());
		this.plugin = plugin;
		loadProjectileTrails();
	}

	public void loadProjectileTrails() {
		projectileTrailMap = new HashMap<>();
		FileConfiguration dataYml = plugin.getDataYml();
		for (String uuid : dataYml.getKeys(false)) {
			String effectId = dataYml.getString(uuid + ".selected-effect");
			projectileTrailMap.put(uuid, getProjectileTrailById(effectId));
			List<String> effectIds = dataYml.getStringList(uuid + ".effects");
			projectileTrailsMap.put(uuid, effectIds);
	    }
	}

	public void saveProjectileTrails() {
		FileConfiguration dataYml = plugin.getDataYml();
		for (String uuid : projectileTrailMap.keySet()) {
			dataYml.set(uuid + ".selected-effect", projectileTrailMap.get(uuid).getId());
		}
		for (String uuid : projectileTrailsMap.keySet()) {
			dataYml.set(uuid + ".effects", projectileTrailsMap.get(uuid));
		}
		plugin.saveDataYml();
	}

	public ProjectileTrail getProjectileTrailByName(String name) {
		for (ProjectileTrail projectileTrail : projectileTrails) {
			if (projectileTrail.getName().equalsIgnoreCase(name)) {
				return projectileTrail;
			}
		}
		return null;
	}

	public void registerProjectileTrail(ProjectileTrail projectileTrail) {
		projectileTrails.add(projectileTrail);
	}

	public ProjectileTrail getProjectileTrailById(String id) {
		for (ProjectileTrail projectileTrail : projectileTrails) {
			if (projectileTrail.getId().equalsIgnoreCase(id)) {
				return projectileTrail;
			}
		}
		return null;
	}

	public ProjectileTrail getProjectileTrail(Player p) {
		String uuid = p.getUniqueId().toString();
		if (projectileTrailMap.containsKey(uuid)) {
			return projectileTrailMap.get(uuid);
		}
		return null;
	}

	public void setProjectileTrail(Player p, ProjectileTrail trail) {
		String uuid = p.getUniqueId().toString();
		if (projectileTrailMap.containsKey(uuid)) {
			projectileTrailMap.remove(uuid);
		}
		if (trail != null) {
			projectileTrailMap.put(uuid, trail);
		}
	}

	public void setProjectileTrail(Player p, String trailId) {
		String uuid = p.getUniqueId().toString();
		if (projectileTrailMap.containsKey(uuid)) {
			projectileTrailMap.remove(uuid);
		}
		ProjectileTrail trail = getProjectileTrailById(trailId);
		if (trail != null) {
			projectileTrailMap.put(uuid, trail);
		}
	}

	public boolean hasSelected(Player p, ProjectileTrail trail) {
		if (trail.equals(getProjectileTrail(p))) {
			return true;
		}
		return false;
	}

	public boolean hasSelected(Player p, String trailId) {
		if (getProjectileTrail(p).getId().contentEquals(trailId)) {
			return true;
		}
		return false;
	}

	public boolean hasProjectileTrail(Player p, ProjectileTrail effect) {
		List<String> effects = getProjectileTrails(p);
		return (effects != null) && (effects.contains(effect.getId()));
	}

	public boolean hasProjectileTrail(Player p, String effectId) {
		List<String> effects = getProjectileTrails(p);
		return (effects != null) && (effects.contains(effectId));
	}

	public List<String> getProjectileTrails(Player p) {
		String uuid = p.getUniqueId().toString();
		return this.projectileTrailMap.containsKey(uuid) ? (List<String>) this.projectileTrailsMap.get(p.getUniqueId().toString()) : null;
	}

	public void giveProjectileTrail(Player p, ProjectileTrail effect) {
		if (hasProjectileTrail(p, effect)) {
			return;
		}
		List<String> effectIds = getProjectileTrails(p);
		if (effectIds == null) {
			effectIds = new ArrayList<>();
		}
		effectIds.add(effect.getId());
		setProjectileTrails(p, effectIds);
	}

	public void takeProjectileTrail(Player p, ProjectileTrail effect) {
		if (!hasProjectileTrail(p, effect)) {
			return;
		}
		List<String> effectIds = getProjectileTrails(p);
		if (effectIds == null) {
			effectIds = new ArrayList<>();
		}
		effectIds.remove(effect.getId());
		setProjectileTrails(p, effectIds);
		if (hasSelected(p, effect)) {
			setProjectileTrail(p, "NONE");
		}
	}

	private void setProjectileTrails(Player p, List<String> effectIds) {
		this.projectileTrailsMap.put(p.getUniqueId().toString(), effectIds);
	}

}
