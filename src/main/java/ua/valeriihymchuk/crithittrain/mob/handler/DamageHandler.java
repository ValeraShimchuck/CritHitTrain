package ua.valeriihymchuk.crithittrain.mob.handler;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import ua.valeriihymchuk.crithittrain.mob.MobManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

public class DamageHandler implements Listener {
    private final MobManager manager;

    private final HashMap<Player, Long> lastDamage = new HashMap<>();

    public DamageHandler(MobManager manager) {
        this.manager = manager;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCombust(EntityCombustEvent event) {
        if(!manager.isPluginMob(event.getEntity())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!manager.isPluginMob(event.getEntity())) return;
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) event.setCancelled(true);
        event.setDamage(0);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player damager)) return;
        if(!manager.isPluginMob(event.getEntity())) return;
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        double damage = Double.parseDouble(df.format(event.getDamage()).replace(",", "."));
        double itemDamage = 1;
        itemDamage += damager.getInventory().getItemInMainHand().getType().getItemAttributes(EquipmentSlot.HAND)
                .get(Attribute.GENERIC_ATTACK_DAMAGE).stream().mapToDouble(AttributeModifier::getAmount).sum();
        if(itemDamage > damage) {
            damager.sendMessage("Кулдаун предмета еще не прошел");
        } else {
            if(damage > itemDamage) {
                double lastDamage = getLastDamageTime(damager);
                if(lastDamage < 0) damager.sendMessage("Твой первый крит!");
                else {
                    damager.sendMessage("Ты ударил критом! За " + lastDamage + " s");
                }
                logCritical(damager);
            }
            else damager.sendMessage("Ты ударил не критом!");
        }

        damager.sendMessage(String.valueOf(damage));
    }


    private double getLastDamageTime(Player p) {
        return ((double) (System.currentTimeMillis() - lastDamage.getOrDefault(p, System.currentTimeMillis() +1))) / 1000;
    }

    private void logCritical(Player p) {
        if(lastDamage.containsKey(p)) lastDamage.replace(p, System.currentTimeMillis());
        else lastDamage.put(p, System.currentTimeMillis());
    }

}
