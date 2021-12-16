package ua.valeriihymchuk.crithittrain.mob.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import ua.valeriihymchuk.crithittrain.Plugin;
import ua.valeriihymchuk.crithittrain.mob.MobManager;

public class DamageHandler implements Listener {
    private final MobManager manager;

    public DamageHandler(MobManager manager) {
        this.manager = manager;
    }


    @EventHandler
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
        float attackStrength = damager.getCooledAttackStrength(0);
        if(attackStrength != 1.0f) {
            damager.sendMessage("Кулдаун предмета еще не прошел");
        } else {
            if(isCritical(damager)) damager.sendMessage("Ты ударил критом!");
            else damager.sendMessage("Ты ударил не критом!");
        }
    }

    public boolean isCritical(Player p) {
        if(p.isOnGround()) return false;
        return (p.getVelocity().getY() + 0.0784000015258789) <= 0;
    }

}
