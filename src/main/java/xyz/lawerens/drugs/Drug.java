package xyz.lawerens.drugs;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import xyz.lawerens.drugs.utils.Colorize;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Data
public class Drug {

    private static final Map<PotionEffectType, String> potionColors = new HashMap<>();
    private static final Map<PotionEffectType, String> potionTranslations = new HashMap<>();

    static {
        initTranslations();
        initColors();
    }

    private static void initTranslations() {
        potionTranslations.put(PotionEffectType.SPEED, "Velocidad");
        potionTranslations.put(PotionEffectType.SLOW, "Lentitud");
        potionTranslations.put(PotionEffectType.FAST_DIGGING, "Aumento de Minería");
        potionTranslations.put(PotionEffectType.SLOW_DIGGING, "Fatiga de Minería");
        potionTranslations.put(PotionEffectType.INCREASE_DAMAGE, "Fuerza");
        potionTranslations.put(PotionEffectType.HEAL, "Curación Instantánea");
        potionTranslations.put(PotionEffectType.HARM, "Daño Instantáneo");
        potionTranslations.put(PotionEffectType.JUMP, "Aumento de Salto");
        potionTranslations.put(PotionEffectType.CONFUSION, "Náusea");
        potionTranslations.put(PotionEffectType.REGENERATION, "Regeneración");
        potionTranslations.put(PotionEffectType.DAMAGE_RESISTANCE, "Resistencia");
        potionTranslations.put(PotionEffectType.FIRE_RESISTANCE, "Resistencia al Fuego");
        potionTranslations.put(PotionEffectType.WATER_BREATHING, "Respiración Acuática");
        potionTranslations.put(PotionEffectType.INVISIBILITY, "Invisibilidad");
        potionTranslations.put(PotionEffectType.BLINDNESS, "Ceguera");
        potionTranslations.put(PotionEffectType.NIGHT_VISION, "Visión Nocturna");
        potionTranslations.put(PotionEffectType.HUNGER, "Hambre");
        potionTranslations.put(PotionEffectType.WEAKNESS, "Debilidad");
        potionTranslations.put(PotionEffectType.POISON, "Veneno");
        potionTranslations.put(PotionEffectType.WITHER, "Marchitamiento");
        potionTranslations.put(PotionEffectType.HEALTH_BOOST, "Aumento de Vida");
        potionTranslations.put(PotionEffectType.ABSORPTION, "Absorción");
        potionTranslations.put(PotionEffectType.SATURATION, "Saturación");
        potionTranslations.put(PotionEffectType.GLOWING, "Brillo");
        potionTranslations.put(PotionEffectType.LEVITATION, "Levitación");
        potionTranslations.put(PotionEffectType.LUCK, "Suerte");
        potionTranslations.put(PotionEffectType.UNLUCK, "Mala Suerte");
        potionTranslations.put(PotionEffectType.SLOW_FALLING, "Caída Lenta");
        potionTranslations.put(PotionEffectType.CONDUIT_POWER, "Poder de Conducto");
        potionTranslations.put(PotionEffectType.DOLPHINS_GRACE, "Gracia del Delfín");
        potionTranslations.put(PotionEffectType.BAD_OMEN, "Mal Augurio");
        potionTranslations.put(PotionEffectType.HERO_OF_THE_VILLAGE, "Héroe de la Aldea");
        potionTranslations.put(PotionEffectType.DARKNESS, "Oscuridad");
    }

    public static String translatePotionEffect(PotionEffectType effectType) {
        return getPotionEffectColor(effectType)+potionTranslations.getOrDefault(effectType, effectType.getName());
    }
    private final String name;
    private String displayName;
    private final Rareza rareza;
    private final Set<EffectInfo> effects;
    private Material material;
    private ItemStack item;

    public Drug(String name, String displayName, Rareza rareza, Set<EffectInfo> effects, Material material) {
        this.name = name;
        this.displayName = displayName;
        this.rareza = rareza;
        this.effects = new HashSet<>(effects);
        this.material = material;
        loadItem();
    }
    public static String formatSeconds(long seconds) {
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.HOURS.toMinutes(hours);
        long secs = seconds - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%02d:%02d", minutes, secs);
        }
    }
    private static void initColors() {
        potionColors.put(PotionEffectType.SPEED, "#7CAFC6");
        potionColors.put(PotionEffectType.SLOW, "#5A6C81");
        potionColors.put(PotionEffectType.FAST_DIGGING, "#D9C043");
        potionColors.put(PotionEffectType.SLOW_DIGGING, "#4A4217");
        potionColors.put(PotionEffectType.INCREASE_DAMAGE, "#932423");
        potionColors.put(PotionEffectType.HEAL, "#F82423");
        potionColors.put(PotionEffectType.HARM, "#430A09");
        potionColors.put(PotionEffectType.JUMP, "#22FF4A");
        potionColors.put(PotionEffectType.CONFUSION, "#551D4A");
        potionColors.put(PotionEffectType.REGENERATION, "#CD5CAB");
        potionColors.put(PotionEffectType.DAMAGE_RESISTANCE, "#99453A");
        potionColors.put(PotionEffectType.FIRE_RESISTANCE, "#E49A24");
        potionColors.put(PotionEffectType.WATER_BREATHING, "#2E5299");
        potionColors.put(PotionEffectType.INVISIBILITY, "#7F8392");
        potionColors.put(PotionEffectType.BLINDNESS, "#1F1F23");
        potionColors.put(PotionEffectType.NIGHT_VISION, "#1F1FA1");
        potionColors.put(PotionEffectType.HUNGER, "#587653");
        potionColors.put(PotionEffectType.WEAKNESS, "#484D48");
        potionColors.put(PotionEffectType.POISON, "#4E9331");
        potionColors.put(PotionEffectType.WITHER, "#352A27");
        potionColors.put(PotionEffectType.HEALTH_BOOST, "#F87D23");
        potionColors.put(PotionEffectType.ABSORPTION, "#2552A5");
        potionColors.put(PotionEffectType.SATURATION, "#F82423");
        potionColors.put(PotionEffectType.GLOWING, "#94A61A");
        potionColors.put(PotionEffectType.LEVITATION, "#CEFFFF");
        potionColors.put(PotionEffectType.LUCK, "#339900");
        potionColors.put(PotionEffectType.UNLUCK, "#6F6A61");
        potionColors.put(PotionEffectType.SLOW_FALLING, "#F2EBDA");
        potionColors.put(PotionEffectType.CONDUIT_POWER, "#A2A2FF");
        potionColors.put(PotionEffectType.DOLPHINS_GRACE, "#0AA9B6");
        potionColors.put(PotionEffectType.BAD_OMEN, "#484D48");
        potionColors.put(PotionEffectType.HERO_OF_THE_VILLAGE, "#74D15C");
        potionColors.put(PotionEffectType.DARKNESS, "#1F1F23");
    }

    public static String getPotionEffectColor(PotionEffectType effectType) {
        return potionColors.getOrDefault(effectType, "#FFFFFF");
    }

    public void loadItem() {
        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Colorize.color(displayName));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(Colorize.color("&7 Tipo de Item: &x&E&3&3&9&1&4Ilegal"));

        lore.add(Colorize.color("&7 Rareza: "+rareza.toString()));
        lore.add(" ");
        lore.add(Colorize.color("&x&C&7&C&7&C&7 Consume esta droga y podrás"));
        lore.add(Colorize.color("&x&C&7&C&7&C&7 obtener efectos curiosos"));
        lore.add(" ");
        lore.add(Colorize.color("#ffc4bd Como este item es ilegal, si"));
        lore.add(Colorize.color("#ffc4bd este item es encontrado cuando mueras"));
        lore.add(Colorize.color("#ffc4bd posiblemente recibas una multa por poseción ilegal."));
        lore.add(" ");
        lore.add(Colorize.color("&7 Efectos:"));
        for(EffectInfo ei : effects){
            String llore = "&7 &x&C&7&C&7&C&7 - "+translatePotionEffect(ei.getEffectType())+" "+DrugsListener.decimalToRoman(ei.getLevel()+1)+"&x&C&7&C&7&C&7 - &a"+formatSeconds(ei.getDuration());
            lore.add(Colorize.color(llore));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
