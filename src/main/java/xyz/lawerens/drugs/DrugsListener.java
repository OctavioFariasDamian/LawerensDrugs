package xyz.lawerens.drugs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static xyz.lawerens.drugs.utils.Colorize.sendMessage;
import static xyz.lawerens.drugs.utils.Colorize.sendPrefixMessage;

public class DrugsListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getItem() == null) return;
        for(Drug drug : LawerensDrugs.getInstance().getDrugs()) {
            if ((e.getItem().getItemMeta().hasDisplayName() &&
                    e.getItem().getItemMeta().getDisplayName().equals(drug.getItem().getItemMeta().getDisplayName())) &&
                    e.getItem().getType() == drug.getMaterial()){
                Player p = e.getPlayer();

                if(LawerensDrugs.getInstance().getPlayers().contains(p.getName())){
                    sendPrefixMessage(p, "#ff0000¡UNA DROGA A LA VEZ, POR FAVOR!");
                    return;
                }

                LawerensDrugs.getInstance().getPlayers().add(p.getName());

                ItemStack item = e.getItem();
                item.setAmount(item.getAmount() - 1);

                if (item.getAmount() <= 0) {
                    p.getInventory().remove(item);
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 260, 0));
                sendPrefixMessage(p, "#2DAF13Acabas de consumir \""+drug.getDisplayName()+"#2DAF13\" pronto sentirás los efectos...");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        LawerensDrugs.getInstance().getPlayers().remove(p.getName());
                        if(!p.isOnline()) return;
                        for(EffectInfo info : drug.getEffects()){
                            p.addPotionEffect(new PotionEffect(info.getEffectType(), info.getDuration()*20, info.getLevel()));
                            sendPrefixMessage(p, Drug.getPotionEffectColor(info.getEffectType())+"¡Recibiste "+Drug.translatePotionEffect(info.getEffectType())+" "+decimalToRoman(info.getLevel()+1)+"!");
                        }
                    }
                }.runTaskLater(LawerensDrugs.getInstance(), 260);
            }
        }
    }

    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] ROMAN_NUMERALS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public static String decimalToRoman(int number) {
        if (number <= 0 || number > 3999) {
            throw new IllegalArgumentException("El número debe estar entre 1 y 3999");
        }

        StringBuilder result = new StringBuilder();

        // Convertir el número a su equivalente en números romanos
        for (int i = 0; i < VALUES.length; i++) {
            while (number >= VALUES[i]) {
                number -= VALUES[i];
                result.append(ROMAN_NUMERALS[i]);
            }
        }

        return result.toString();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        for(ItemStack drops : e.getDrops()){
            for(Drug drug : LawerensDrugs.getInstance().getDrugs()){
                if (drops.getItemMeta().hasDisplayName() &&
                        drops.getItemMeta().getDisplayName().equals(drug.getItem().getItemMeta().getDisplayName()) && drops.getType() == drug.getMaterial()){
                    if(Math.random() > 0.6){
                        e.getDrops().remove(drops);
                        sendMessage(e.getPlayer(), "§x§3§6§f§f§3§9§lSERVER &cEl estado te ha encontrado drogas cuando fuiste abatido, serán removidas y se te cobrará una multa.");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take "+e.getPlayer().getName()+" 1500");
                        return;
                    }
                }
            }
        }
    }
}
