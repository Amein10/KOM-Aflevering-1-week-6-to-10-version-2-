/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Amein
 */
public class EnemyControlSystem implements IEntityProcessingService {

    private Random rng;

    public EnemyControlSystem() {
        rng = new Random();
    }

    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(ENEMY)) {
            float maxSpeed = enemy.getMaxSpeed();
            float acceleration = enemy.getAcceleration();
            float deceleration = enemy.getDeacceleration();
            float x = enemy.getX();
            float y = enemy.getY();
            float dx = enemy.getDx();
            float dy = enemy.getDy();
            int rotationSpeed = enemy.getRotationSpeed();
            float radians = enemy.getRadians();
            float dt = gameData.getDelta();

            // accelerating
            if (rng.nextBoolean()) {
                dx += Math.cos(radians) * acceleration * dt;
                dy += Math.sin(radians) * acceleration * dt;
            }

            //Left
            if (rng.nextBoolean()) {
                radians += rotationSpeed * dt;
                //Right
            } else if (rng.nextBoolean()) {
                radians -= rotationSpeed * dt;
            }
            enemy.setRadians(radians);

            // deceleration
            float vec = (float) Math.sqrt(dx * dx + dy * dy);

            if (vec > 0) {
                dx -= (dx / vec) * deceleration * dt;
                dy -= (dy / vec) * deceleration * dt;
            }
            if (vec > maxSpeed) {
                dx = (dx / vec) * maxSpeed;
                dy = (dy / vec) * maxSpeed;
            }

            x += dx * dt;
            y += dy * dt;

            //Wrapper
            if (x < 0) {
                x = gameData.getDisplayWidth();
            }
            if (x > gameData.getDisplayWidth()) {
                x = 0;
            }
            if (y < 0) {
                y = gameData.getDisplayHeight();
            }
            if (y > gameData.getDisplayHeight()) {
                y = 0;
            }

            enemy.setDx(dx);
            enemy.setDy(dy);
            enemy.setX(x);
            enemy.setY(y);
            setShape(enemy);
        }
    }

    private void setShape(Entity enemy) {
        float[] shapex = enemy.getShapeX();
        float[] shapey = enemy.getShapeY();
        float x = enemy.getX();
        float y = enemy.getY();
        float radians = enemy.getRadians();

        shapex[0] = (float) (x + Math.cos(radians) * 8);
        shapey[0] = (float) (y + Math.sin(radians) * 8);

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 5);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 5);

        shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * 8);
        enemy.setShapeX(shapex);
        enemy.setShapeY(shapey);

    }

}
