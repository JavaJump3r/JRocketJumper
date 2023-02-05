package io.github.javajump3r.jrocketjumper;

import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Util {
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
    public static double lerp(double a, double b, double f)
    {
        return (a * (1.0 - f)) + (b * f);
    }
    public static Vector lerp(Vector a,Vector b,double f)
    {
        Vector outVector = new Vector();
        outVector.setX(lerp(a.getX(),b.getX(),f));
        outVector.setY(lerp(a.getY(),b.getY(),f));
        outVector.setZ(lerp(a.getZ(),b.getZ(),f));
        return outVector;
    }
    public static void outlineBox(BoundingBox box, World world){
        Vector a = new Vector(box.getMinX(),box.getMinY(),box.getMinZ());
        Vector b = new Vector(box.getMaxX(),box.getMaxY(),box.getMaxZ());
        List<Vector> points = new LinkedList<>();
        points.add( new Vector(a.getX(),a.getY(),a.getZ()));
        points.add( new Vector(b.getX(),a.getY(),a.getZ()));
        points.add( new Vector(b.getX(),a.getY(),b.getZ()));
        points.add( new Vector(a.getX(),a.getY(),b.getZ()));
        points.add( new Vector(a.getX(),b.getY(),a.getZ()));
        points.add( new Vector(b.getX(),b.getY(),a.getZ()));
        points.add( new Vector(b.getX(),b.getY(),b.getZ()));
        points.add( new Vector(a.getX(),b.getY(),b.getZ()));
        try{
            for(int i=0;i<=3;i++){
                line(points.get(i),points.get(i+1),world);
                line(points.get(i+4),points.get(i+5),world);
                line(points.get(i),points.get(i+4),world);
            }
        }
        catch (Exception e){}


    }
    public static void line(Vector a,Vector b,World world)
    {
        for(double i=0;i<1;i+=0.01)
        {
            Vector pos = lerp(a,b,0.1/Math.abs(a.distance(b)));
            world.spawnParticle(Particle.VILLAGER_HAPPY,pos.toLocation(world),1);
        }
    }
    public static double calculatePower(Config config,double distance){
        double power=0;
        power = config.k*distance+config.b;
        power=Math.max(power,0);
        return power;
    }
}
