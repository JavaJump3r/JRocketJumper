package io.github.javajump3r.jrocketjumper;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Collection;

public class EventHandlers implements Listener {
    @EventHandler
    void onFireworkExplode(FireworkExplodeEvent event){
        Firework firework = event.getEntity();

        Vector fireworkPos = firework.getLocation().toVector();
        BoundingBox box = new BoundingBox(fireworkPos.getX(), fireworkPos.getY(), fireworkPos.getZ(), fireworkPos.getX(), fireworkPos.getY(), fireworkPos.getZ());
        box.expand(16);
        Util.outlineBox(box, firework.getWorld());

        Collection<Entity> affectedEntities = firework.getWorld().getNearbyEntities(fireworkPos.toLocation(firework.getWorld()),16,16,16);
        for(Entity entity : affectedEntities)
        {
            Config config = JRocketJumper.config;
            if(entity instanceof Firework)
                return;
            if(entity instanceof Player) {
                if (((Player) entity).getGameMode() == GameMode.SPECTATOR)
                    return;
            }
            Vector entityPos = entity.getLocation().toVector();
            Vector entityVelocity = entity.getVelocity();
            /*entityVelocity = new Vector(
                    entityVelocity.getX(),
                    Math.min(
                            1.0,
                            Math.abs(entityVelocity.getY())
                    ),
            entityVelocity.getZ());*/
            Vector direction = entityPos.subtract(fireworkPos).normalize();
            Vector addVelocity = direction;

            double entityTypeMultiplier=1;
            if(entity instanceof Player){
                entityTypeMultiplier = config.playerMultiplier;
            }
            else if(entity instanceof LivingEntity)
            {
                entityTypeMultiplier = config.livingEntityMultiplier;
            }
            else {
                entityTypeMultiplier = config.otherEntityMultiplier;
            }

            double distance = entityPos.distance(fireworkPos);
            double distanceMultiplier=Util.calculatePower(config,distance);

            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            double starCountMultiplier = fireworkMeta.getEffectsSize()*0.4;

            addVelocity = addVelocity
                    .multiply(entityTypeMultiplier)
                    .multiply(starCountMultiplier)
                    .multiply(distanceMultiplier);

            entityVelocity = entityVelocity.add(addVelocity);
            String debugText = "";
            debugText += " "+entity.getName();
            debugText += " "+entity.getLocation().toVector();
            debugText += " "+entity.getLocation();
            /*debugText += " "+String.format("entTypeMp: %.2f, starMp: %.2f, distanceMp: %.2f, distance %.2f, addVelocity: %s",
                    entityTypeMultiplier,
                    starCountMultiplier,
                    distanceMultiplier,
                    distance,
                    addVelocity);*/
            //debugText += " " + String.format("ent: %s, firework: %s, dir: %s, distance %.2f",entityPos,fireworkPos,direction,distance);
            Bukkit.getServer().sendMessage(Component.text(debugText));
            JRocketJumper.logger.info(debugText);
            if(addVelocity.length()>0.01)
            entity.setVelocity(entityVelocity);
        }
    }
}
