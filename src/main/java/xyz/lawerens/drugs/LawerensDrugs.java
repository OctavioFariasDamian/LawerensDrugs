package xyz.lawerens.drugs;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.*;

public final class LawerensDrugs extends JavaPlugin {

    @Getter private final Set<String> players = new HashSet<>();
    @Getter private final Set<Drug> drugs = new HashSet<>();
    @Getter private static LawerensDrugs instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        loadDrugs(config);
        getCommand("drugs").setExecutor(new DrugsCommand());
        getCommand("drugs").setTabCompleter(new DrugsCommand());
        getCommand("drogas").setExecutor(new DrugsCommand());
        getCommand("drogas").setTabCompleter(new DrugsCommand());
        getServer().getPluginManager().registerEvents(new DrugsListener(), this);
    }

    public void saveDrug(Drug drug) {
        FileConfiguration config = getConfig();
        ConfigurationSection section;

        if(config.isConfigurationSection("Drugs."+drug.getName()))
            section = config.getConfigurationSection("Drugs."+drug.getName());
        else section = config.createSection("Drugs."+drug.getName());

        assert section != null;
        section.set("rareza", drug.getRareza().name());
        List<String> effects = new ArrayList<>();
        for(EffectInfo potionEffects : drug.getEffects()){
            effects.add(potionEffects.getEffectType().getName()+";"+potionEffects.getLevel()+";"+potionEffects.getDuration());
        }
        section.set("effects", effects);
        if(!drug.getDisplayName().equals(drug.getName())){
            section.set("displayName", drug.getDisplayName());
        }
        section.set("material", drug.getMaterial().name());
        saveConfig();
    }

    public void deleteDrug(Drug drug) {
        getDrugs().remove(getDrug(drug.getName()));
        getConfig().set("Drugs."+drug.getName(), null);
        saveConfig();
    }

    private void loadDrugs(FileConfiguration config) {
        for(String key : Objects.requireNonNull(config.getConfigurationSection("Drugs")).getKeys(false)){
            ConfigurationSection section = config.getConfigurationSection("Drugs."+key);
            assert section != null;
            List<String> effectsList = section.getStringList("effects");
            Set<EffectInfo> effects = new HashSet<>();
            Rareza rareza = Rareza.valueOf(Objects.requireNonNull(section.getString("rareza")).toUpperCase());
            for(String ef : effectsList){
                String[] efs = ef.split(";");
                effects.add(new EffectInfo(
                        PotionEffectType.getByName(efs[0]),
                        Integer.parseInt(efs[1]),
                        Integer.parseInt(efs[2])));
            }
            String displayName = key;
            if(section.isString("displayName")){
                displayName = section.getString("displayName");
            }
            Material mat = Material.PAPER;
            if(section.isString("material")){
                mat = Material.matchMaterial(section.getString("material").toUpperCase());
            }
            Drug drug = new Drug(key, displayName, rareza, effects, mat);
            drugs.add(drug);
        }

    }

    @Override
    public void onDisable() {

    }

    @Nullable
    public Drug getDrug(String name){
        for(Drug drug : drugs){
            if(drug.getName().equals(name)) return drug;
        }
        return null;
    }

}
