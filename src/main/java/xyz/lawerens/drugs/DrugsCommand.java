package xyz.lawerens.drugs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static xyz.lawerens.drugs.utils.CenterMessage.sendCenteredMessage;
import static xyz.lawerens.drugs.utils.CenterMessage.sendUnderline;
import static xyz.lawerens.drugs.utils.Colorize.sendPrefixMessage;

public class DrugsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("drugs") || command.getName().equalsIgnoreCase("drogas")){
            if(!sender.hasPermission("lawerens.drugs")){
                sendPrefixMessage(sender, "#ff0000¡No tienes permisos!");
                return false;
            }

            if(args.length == 0){
                sendUnderline(sender, 'a');
                sendCenteredMessage(sender, "&a&lSUBCOMANDOS DE DROGAS");
                sendCenteredMessage(sender, "&c/drogas create [nombre] [rareza] &7- &fCrea una droga");
                sendCenteredMessage(sender, "&c/drogas remove [droga] &7- &fRemueve una droga");
                sendCenteredMessage(sender, "&c/drogas addeffect [droga] [efecto] [duración(minutos)] [nivel] &7- &fAñade un efecto a una droga");
                sendCenteredMessage(sender, "&c/drogas removeeffect [droga] [efecto] &7- &fRemueve el efecto de una droga");
                sendCenteredMessage(sender, "&c/drogas setdisplayname [droga] [nombre...] &7- &fEstablece el nombre visible de una droga");
                sendCenteredMessage(sender, "&c/drogas setmaterial [droga] [material] &7- &fEstablece el material del item de una droga");
                sendCenteredMessage(sender, "&c/drogas give [droga] [jugador] &7- &fDar a un jugador una droga");
                sendUnderline(sender, 'a');
                return false;
            }
            String subCommand = args[0];
            switch (subCommand.toLowerCase()){
                case "create": {
                    handleCreate(sender, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
                case "remove": {
                    handleRemove(sender, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
                case "addeffect": {
                    handleAddEffect(sender, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
                case "removeeffect": {
                    handleRemoveEffect(sender, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
                case "setmaterial": {
                    handleSetMaterial(sender, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
                case "setdisplayname": {
                    handleSetDisplayName(sender, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
                case "give": {
                    handleGive(sender, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
                default: {
                    sendUnderline(sender, 'a');
                    sendCenteredMessage(sender, "&a&lSUBCOMANDOS DE DROGAS");
                    sendCenteredMessage(sender, "&c/drogas create [nombre] [rareza] &7- &fCrea una droga");
                    sendCenteredMessage(sender, "&c/drogas remove [droga] &7- &fRemueve una droga");
                    sendCenteredMessage(sender, "&c/drogas addeffect [droga] [efecto] [duración(minutos)] [nivel] &7- &fAñade un efecto a una droga");
                    sendCenteredMessage(sender, "&c/drogas removeeffect [droga] [efecto] &7- &fRemueve el efecto de una droga");
                    sendCenteredMessage(sender, "&c/drogas setdisplayname [droga] [nombre...] &7- &fEstablece el nombre visible de una droga");
                    sendCenteredMessage(sender, "&c/drogas setmaterial [droga] [material] &7- &fEstablece el material del item de una droga");
                    sendCenteredMessage(sender, "&c/drogas give [droga] [jugador] &7- &fDar a un jugador una droga");
                    sendUnderline(sender, 'a');
                    return false;
                }
            }
        }
        return true;
    }

    private void handleGive(CommandSender sender, String[] args) {
        if(args.length < 2){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/drogas give [droga] [jugador]");
            return;
        }
        if(LawerensDrugs.getInstance().getDrug(args[0]) == null){
            sendPrefixMessage(sender, "#ff0000Esa droga no existe.");
            return;
        }
        Drug drug = LawerensDrugs.getInstance().getDrug(args[0]);
        Player toGive = Bukkit.getPlayer(args[1]);
        if(toGive == null || !toGive.isOnline()){
            sendPrefixMessage(sender, "#ff0000Jugador desconocido.");
            return;
        }

        toGive.getInventory().addItem(drug.getItem());
    }

    private void handleSetDisplayName(CommandSender sender, String[] args) {
        if(args.length < 2){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/drogas setdisplayname [droga] [nombre...]");
            return;
        }
        if(LawerensDrugs.getInstance().getDrug(args[0]) == null){
            sendPrefixMessage(sender, "#ff0000Esa droga no existe.");
            return;
        }

        StringBuilder dn = new StringBuilder();

        for(int i = 1 ; i < args.length ; i++){
            dn.append(args[i]).append(" ");
        }

        String displayName = dn.toString().trim();

        Drug drug = LawerensDrugs.getInstance().getDrug(args[0]);

        drug.setDisplayName(displayName);
        drug.loadItem();
        LawerensDrugs.getInstance().saveDrug(drug);
        sendPrefixMessage(sender, "&f¡Estableciste el nombre visible '&e"+displayName+"&f' a la droga &a"+drug.getName()+"&f!");

    }

    private void handleSetMaterial(CommandSender sender, String[] args) {
        if(args.length < 2){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/drogas setmaterial [droga] [material]");
            return;
        }
        if(LawerensDrugs.getInstance().getDrug(args[0]) == null){
            sendPrefixMessage(sender, "#ff0000Esa droga no existe.");
            return;
        }

        Material material;

        try{
            material = Material.matchMaterial(args[1]);
        }catch (IllegalArgumentException e){
            sendPrefixMessage(sender, "#ff0000Ese material no es válido.");
            return;
        }

        Drug drug = LawerensDrugs.getInstance().getDrug(args[0]);

        drug.setMaterial(material);
        drug.loadItem();
        LawerensDrugs.getInstance().saveDrug(drug);
        sendPrefixMessage(sender, "&f¡Estableciste el material &e"+material.name()+" &fa la droga &a"+drug.getName()+"&f!");

    }

    private void handleRemoveEffect(CommandSender sender, String[] args) {
        if(args.length < 2){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/drogas removeeffect [droga] [efecto]");
            return;
        }
        if(LawerensDrugs.getInstance().getDrug(args[0]) == null){
            sendPrefixMessage(sender, "#ff0000Esa droga no existe.");
            return;
        }

        PotionEffectType effect;

        try{
            effect = PotionEffectType.getByName(args[1]);
        }catch (IllegalArgumentException e){
            sendPrefixMessage(sender, "#ff0000Ese efecto de poción no es válido.");
            return;
        }

        Drug drug = LawerensDrugs.getInstance().getDrug(args[0]);

        for(EffectInfo potionEffects : drug.getEffects()){
            if(potionEffects.getEffectType() == effect){
                drug.getEffects().remove(potionEffects);
                drug.loadItem();
                LawerensDrugs.getInstance().saveDrug(drug);
                assert effect != null;

                sendPrefixMessage(sender, "&f¡Removiste el efecto &e"+effect.getName()+" &fa la droga &a"+drug.getName()+"&f!");
                return;
            }
        }

    }

    private void handleAddEffect(CommandSender sender, String[] args) {
        if(args.length < 4){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/drogas addeffect [droga] [efecto] [duración(minutos)] [nivel]");
            return;
        }
        if(LawerensDrugs.getInstance().getDrug(args[0]) == null){
            sendPrefixMessage(sender, "#ff0000Esa droga no existe.");
            return;
        }

        PotionEffectType effect;

        try{
            effect = PotionEffectType.getByName(args[1]);
        }catch (IllegalArgumentException e){
            sendPrefixMessage(sender, "#ff0000Ese efecto de poción no es válido.");
            return;
        }

        int duration;
        int level;

        try{
            duration = Integer.parseInt(args[2]);
            level = Integer.parseInt(args[3]);
        }catch (NumberFormatException e){
            sendPrefixMessage(sender, "#ff0000La duración y nivel deben ser números enteros.");
            return;
        }

        Drug drug = LawerensDrugs.getInstance().getDrug(args[0]);

        for(EffectInfo potionEffects : drug.getEffects()){
            if(potionEffects.getEffectType() == effect){
                drug.getEffects().remove(potionEffects);

            }
        }
        drug.getEffects().add(
                new EffectInfo(effect, level-1, duration)
        );

        drug.loadItem();
        LawerensDrugs.getInstance().saveDrug(drug);
        sendPrefixMessage(sender, "&f¡Añadiste el efecto &e"+effect.getName()+" &fdurante &b"+duration+" &fsegundos al nivel &d"+level+" &fa la droga &a"+drug.getName()+"&f!");
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if(args.length < 1){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/drogas remove [droga]");
            return;
        }
        if(LawerensDrugs.getInstance().getDrug(args[0]) == null){
            sendPrefixMessage(sender, "#ff0000Esa droga no existe.");
            return;
        }

        Drug drug = LawerensDrugs.getInstance().getDrug(args[0]);
        LawerensDrugs.getInstance().deleteDrug(drug);
        sendPrefixMessage(sender, "&f¡Removiste la droga &c"+drug.getName()+"&f!");
    }

    private void handleCreate(CommandSender sender, String[] args) {
        if(args.length < 2){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/drogas create [nombre] [rareza]");
            return;
        }
        if(LawerensDrugs.getInstance().getDrug(args[0]) != null){
            sendPrefixMessage(sender, "#ff0000Ya existe una droga llamada así.");
            return;
        }

        Rareza rareza;

        try{
            rareza = Rareza.valueOf(args[1].toUpperCase());
        }catch (IllegalArgumentException e){
            sendPrefixMessage(sender, "#ff0000Esa rareza no es válida.");
            return;
        }

        Drug drug = new Drug(args[0], args[0], rareza, new HashSet<>(), Material.PAPER);
        LawerensDrugs.getInstance().getDrugs().add(drug);
        LawerensDrugs.getInstance().saveDrug(drug);
        sendPrefixMessage(sender, "&f¡Creaste la droga &a"+drug.getName()+"&f!");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("create");
            suggestions.add("remove");
            suggestions.add("addeffect");
            suggestions.add("removeeffect");
            suggestions.add("give");
            suggestions.add("setmaterial");
            suggestions.add("setdisplayname");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                suggestions.add("[NOMBRE]");
            } else if (args[0].equalsIgnoreCase("remove") ||
                    args[0].equalsIgnoreCase("addeffect") ||
                    args[0].equalsIgnoreCase("removeeffect") ||
                    args[0].equalsIgnoreCase("give") ||
                    args[0].equalsIgnoreCase("setmaterial") ||
                    args[0].equalsIgnoreCase("setdisplayname")
            ) {
                for (Drug drug : LawerensDrugs.getInstance().getDrugs()) {
                    suggestions.add(drug.getName());
                }
            }
        } else if (args.length == 3) {
            if(args[0].equalsIgnoreCase("give")){
                for(Player online : Bukkit.getOnlinePlayers()){
                    suggestions.add(online.getName());
                }
            }
            else if(args[0].equalsIgnoreCase("setmaterial")){
                for(Material material : Material.values()){
                    suggestions.add(material.name());
                }
            }else if (args[0].equalsIgnoreCase("create")) {
                for (Rareza rareza : Rareza.values()) {
                    suggestions.add(StringUtils.capitalise(rareza.name().toLowerCase()));
                }
            } else if(args[0].equalsIgnoreCase("removeeffect")){
                Drug drug = LawerensDrugs.getInstance().getDrug(args[1]);
                if(drug != null){
                    for(EffectInfo info : drug.getEffects()){
                        suggestions.add(info.getEffectType().getName());
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("addeffect")) {
                for (PotionEffectType effectType : PotionEffectType.values()) {
                    suggestions.add(effectType.getName());
                }
            }
        } else if (args[0].equalsIgnoreCase("addeffect")) {
            if (args.length == 4) {
                suggestions.add("[DURACIÓN (segundos)]");
            } else if (args.length == 5) {
                suggestions.add("[NIVEL (>0)]");
            }
        }

        // Filtrar las sugerencias basadas en el prefijo actual
        String currentArg = args[args.length - 1];
        StringUtil.copyPartialMatches(currentArg, suggestions, completions);

        // Ordenar alfabéticamente
        Collections.sort(completions);

        return completions;
    }
}
