package xyz.lawerens.drugs;

import lombok.Data;
import org.bukkit.potion.PotionEffectType;

@Data
public class EffectInfo {
    private final PotionEffectType effectType;
    private final int level;
    private final int duration;

    public EffectInfo(PotionEffectType effectType, int level, int duration) {
        this.effectType = effectType;
        this.level = level;
        this.duration = duration;
    }
}
