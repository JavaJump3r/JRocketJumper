package io.github.javajump3r.jrocketjumper;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import java.util.*;

import static io.github.javajump3r.jrocketjumper.JRocketJumper.config;

public class EventHandlers implements Listener {
    @EventHandler
    void onFireworkExplode(FireworkExplodeEvent event) {
        Firework firework = event.getEntity();
        Vector boostPoint = firework.getLocation().toVector();
        double power = 1;
        if(firework.getBoostedEntity()==null)
        {
            boostPoint = boostPoint.add(new Vector(0,config.boostYOffset,0));
        }
        else
        {
            var boostedPlayer = firework.getBoostedEntity();
            boostPoint = boostedPlayer.getLocation().getDirection()
                    .normalize()
                    .multiply(-1)
                    .multiply(config.explosionBoostDistance)
                    .add(boostedPlayer.getLocation().toVector());
            power *= config.explodeBoostMultiplier;
        }
        //firework.remove();
        //event.setCancelled(true);
        power *= firework.getFireworkMeta().getEffectsSize();

        createBoost(boostPoint.toLocation(firework.getWorld()),power,firework.getSpawningEntity(),firework.getSpawningEntity()==null);
    }
    @EventHandler
    void onPlayerElytraBoost(PlayerElytraBoostEvent event){
        boolean sendWarningMessage=false;
        switch (config.elytraVanillaBoostMode){
            case DISABLED:
            {
                event.setCancelled(true);
                if(config.sendChangedBoostMessage)
                event.getPlayer().sendMessage(Component.text(config.noBoostMessage));
                return;
            }
            case ONLY_EXPLODING: {
                if(event.getFirework().getFireworkMeta().getEffectsSize()==0)
                {
                    event.setCancelled(true);
                    if(config.sendChangedBoostMessage)
                    event.getPlayer().sendMessage(config.explodeBoostMessage);
                    return;
                }
            }
        }

        if(config.elytraExplosionBoostMode == ElytraExplosionBoostMode.IMMEDIATE_EXPLOSION){
            if(event.getFirework().getFireworkMeta().getEffectsSize()!=0) {
                Firework firework = event.getFirework();
                Player player = event.getPlayer();
                Vector pos = player.getLocation().getDirection().normalize().multiply(-1 * config.explosionBoostDistance);
                pos.add(player.getLocation().toVector());
                Location newFireworkPos = pos.toLocation(firework.getWorld());
                firework.teleport(newFireworkPos);
                firework.detonate();
            }
        }
    }
    @EventHandler
    void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Firework){
            event.setDamage(event.getDamage()*config.damageMultiplier);
        }
    }

    private void createBoost(Location explosionPos, double power, UUID fireworkSpawningEntity, boolean isFromDispencer){
        List<Entity> affectedEntities = new LinkedList<>(explosionPos.getNearbyEntities(config.entitySearchRadius,config.entitySearchRadius,config.entitySearchRadius));
        for (Entity entity : affectedEntities) {
            if (entity instanceof Firework)
                return;
            if (entity instanceof Player) {
                if (((Player) entity).getGameMode() == GameMode.SPECTATOR)
                    return;
            }
            Vector entityVelocity = entity.getVelocity();
            if(entity.getPose() != Pose.FALL_FLYING)
                entityVelocity = new Vector(
                        entityVelocity.getX(),
                        Math.min(
                                1.0,
                                Math.abs(entityVelocity.getY())
                        ),
                        entityVelocity.getZ());
            Vector direction = entity.getLocation().toVector().subtract(explosionPos.toVector()).normalize();
            Vector addVelocity = direction;

            double entityTypeMultiplier;

            if (entity instanceof Player player) {
                if(player.getUniqueId()==fireworkSpawningEntity)
                    entityTypeMultiplier = config.thisPlayerMultiplier;
                else
                    entityTypeMultiplier = config.otherPlayerMultiplier;
                if(isFromDispencer){
                    entityTypeMultiplier = config.thisPlayerMultiplier;
                }
            } else if (entity instanceof LivingEntity) {
                entityTypeMultiplier = config.livingEntityMultiplier;
            } else {
                entityTypeMultiplier = config.otherEntityMultiplier;
            }

            double distance = entity.getLocation().toVector().distance(explosionPos.toVector());
            double distanceMultiplier = Util.calculatePower(config, distance);
            ;
            double starCountMultiplier = power * config.perStarMultiplier;

            addVelocity = addVelocity
                    .multiply(entityTypeMultiplier)
                    .multiply(starCountMultiplier)
                    .multiply(distanceMultiplier);
            if(isFromDispencer)
                addVelocity.multiply(config.dispencerMultiplier);

            entityVelocity = entityVelocity.add(addVelocity);
            if (addVelocity.length() > 0.01) {
                entity.setVelocity(entityVelocity);
            }
        }
    }
}
