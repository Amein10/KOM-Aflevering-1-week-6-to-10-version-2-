/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

/**
 *
 * @author Amein
 */
public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemy(gameData);
        world.addEntity(enemy);
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity enemy : world.getEntities(ENEMY)) {
            world.removeEntity(enemy);
        }
    }

    public Entity createEnemy(GameData gameData) {
        Entity newEnemy = new Entity();
        newEnemy.setType(ENEMY);

        newEnemy.setPosition(gameData.getDisplayWidth() / 4, gameData.getDisplayHeight() / 4);

        newEnemy.setMaxSpeed(200);
        newEnemy.setAcceleration(200);
        newEnemy.setDeacceleration(10);

        newEnemy.setRadians(3.1415f / 4);
        newEnemy.setRotationSpeed(10);

        return newEnemy;
    }

}
