package io.github.javajump3r.jrocketjumper;

public class Config {
    public double boostYOffset = -1;
    public double thisPlayerMultiplier = 1;
    public double otherPlayerMultiplier = 0;
    public double livingEntityMultiplier = 0.4;
    public double otherEntityMultiplier = 0.3;
    public double perStarMultiplier = 0.1;
    public double explosionBoostDistance = 2;
    public double explodeBoostMultiplier = 0.5;
    public double dispencerMultiplier = 1;
    public boolean sendChangedBoostMessage = true;
    public String noBoostMessage = "Elytra boosting with fireworks is disabled. Please use crossbows with exploding fireworks to rocket jump";
    public String explodeBoostMessage = "Elytra boosting with empty fireworks id disabled. Please use exploding fireworks";

    public ElytraVanillaBoostMode elytraVanillaBoostMode = ElytraVanillaBoostMode.ONLY_EXPLODING;
    public ElytraExplosionBoostMode elytraExplosionBoostMode = ElytraExplosionBoostMode.IMMEDIATE_EXPLOSION;
    public double k=-1;
    public double b=5;

    public double entitySearchRadius=16;
}
