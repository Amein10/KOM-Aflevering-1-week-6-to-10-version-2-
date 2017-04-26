/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Amein
 */
public class EnemyControlSystem implements IEntityProcessingService {

    Entity enemy;

    @Override
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
            float deltaTime = gameData.getDelta();
            float[] shapex = enemy.getShapeX();
            float[] shapey = enemy.getShapeY();

            enemy.setRadians(radians - rotationSpeed * deltaTime);
            enemy.setDx(dx + (float) Math.cos(radians));
            x += + dx * deltaTime;
            y += + dy * deltaTime;

            //Wrap
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

            enemy.setX(x);
            enemy.setY(y);
            setShape(enemy);

        }
    }

    
    private void setShape(Entity enemy) {
        float[] shapex = new float[6];
        float[] shapey = new float[6];

        shapex[0] = enemy.getX() - 24;
        shapey[0] = enemy.getY();

        shapex[1] = enemy.getX() - 8;
        shapey[1] = enemy.getY() - 12;

        shapex[2] = enemy.getX() + 12;
        shapey[2] = enemy.getY() - 12;

        shapex[3] = enemy.getX() + 24;
        shapey[3] = enemy.getY();

        shapex[4] = enemy.getX() + 8;
        shapey[4] = enemy.getY() + 12;

        shapex[5] = enemy.getX() - 8;
        shapey[5] = enemy.getY() + 12;

        enemy.setShapeX(shapex);
        enemy.setShapeY(shapey);

    }

}
