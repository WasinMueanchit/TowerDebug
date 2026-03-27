package All.Tower;

import All.Enemy.FemaleGoblin;
import All.GamePanel;
import All.Particle;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Tower {
    public abstract void update();

    public abstract void draw(Graphics2D g2);

    public abstract void levelUp();

    public abstract void sell();

    public abstract Ellipse2D getAttackArea();

    public abstract Rectangle getPlacedSolidArea();

    public abstract double getDamage();

    public abstract double getAttackSpeed();

    public abstract int getRange();

    public abstract ArrayList<FemaleGoblin> getEnemyInArea();

    public abstract String getName();

    public abstract boolean getIsSold();

    public abstract boolean getIsGhostBuster();
    
    public abstract int getSellCost();
    
    public abstract int getReward();
    
    public abstract boolean getCanCreateMoney();
}
