package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.LEFT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.RIGHT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.UP;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(PLAYER)) {
            float maxSpeed = player.getMaxSpeed();
            float acceleration = player.getAcceleration();
            float deceleration = player.getDeacceleration();
            float x = player.getX();
            float y = player.getY();
            float dx = player.getDx();
            float dy = player.getDy();
            int rotationSpeed = player.getRotationSpeed();
            float radians = player.getRadians();
            float dt = gameData.getDelta();

            // accelerating
            if (gameData.getKeys().isDown(UP)) {
                dx += Math.cos(radians) * acceleration * dt;
                dy += Math.sin(radians) * acceleration * dt;
            }

            //Left
            if (gameData.getKeys().isDown(LEFT)) {
                radians += rotationSpeed * dt;
            //Right
            } else if (gameData.getKeys().isDown(RIGHT)) {
                radians -= rotationSpeed * dt;
            }
            player.setRadians(radians);

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

            player.setDx(dx);
            player.setDy(dy);
            player.setX(x);
            player.setY(y);
            setShape(player);
        }
    }

    private void setShape(Entity player) {
        float[] shapex = player.getShapeX();
        float[] shapey = player.getShapeY();
        float x = player.getX();
        float y = player.getY();
        float radians = player.getRadians();

        shapex[0] = (float) (x + Math.cos(radians) * 8);
        shapey[0] = (float) (y + Math.sin(radians) * 8);

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 5);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 5);

        shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * 8);
        
        player.setShapeX(shapex);
        player.setShapeY(shapey);
    }

}
