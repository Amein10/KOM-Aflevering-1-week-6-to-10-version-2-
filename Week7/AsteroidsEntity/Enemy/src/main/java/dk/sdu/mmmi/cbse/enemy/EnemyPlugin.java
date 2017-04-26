/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemy;

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
        for (Entity enemy : world.getEntities(ENEMY)) {
            world.removeEntity(enemy);
        }
    }

    public Entity createEnemy(GameData gameData) {
        Entity enemy = new Entity();
        enemy.setType(ENEMY);

        enemy.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);

        enemy.setMaxSpeed(200);
        enemy.setAcceleration(200);
        enemy.setDeacceleration(10);

        enemy.setRadians(3.1415f / 4);
        enemy.setRotationSpeed(10);

        return enemy;
    }
}
