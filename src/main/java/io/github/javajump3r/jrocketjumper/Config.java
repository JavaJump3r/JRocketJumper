package io.github.javajump3r.jrocketjumper;

public class Config {
    public double boostYOffset = -1;
    public double thisPlayerMultiplier = 1;
    public double otherPlayerMultiplier = 0;
    public double livingEntityMultiplier = 0.4;
    public double otherEntityMultiplier = 0.3;
    public double perStarMultiplier = 0.1;
    public boolean disableVanillaBoost = true;
    public boolean sendDisableVanillaBoostMessage = true;
    public String disableVanillaBoostMessage = "Elytra boosting with fireworks is disabled. Please use crossbows with exploding fireworks to rocket jump";


    //constants for boost calculation
    public double k=-1;
    public double b=5;

}
