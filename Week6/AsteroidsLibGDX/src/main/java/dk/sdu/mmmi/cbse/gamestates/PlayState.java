package dk.sdu.mmmi.cbse.gamestates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.entities.Asteroid;
import static dk.sdu.mmmi.cbse.entities.Asteroid.LARGE;
import dk.sdu.mmmi.cbse.entities.Bullet;
import dk.sdu.mmmi.cbse.entities.Enemy;
import dk.sdu.mmmi.cbse.entities.Player;
import dk.sdu.mmmi.cbse.main.Game;
import dk.sdu.mmmi.cbse.managers.GameKeys;
import dk.sdu.mmmi.cbse.managers.GameStateManager;
import java.util.ArrayList;

public class PlayState extends GameState {

    private ShapeRenderer sr;

    private Player hudPlayer;

    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Bullet> enemyBullets;

    private Enemy flyingSaucer;
    private float fsTimer;
    private float fsTime;

    private int level;
    private int totalAsteroids;
    private int numAsteroidsLeft;

    private float maxDelay;
    private float minDelay;
    private float currentDelay;
    private float bgTimer;
    private boolean playLowPulse;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        sr = new ShapeRenderer();

        bullets = new ArrayList<Bullet>();

        player = new Player(bullets);

        asteroids = new ArrayList<Asteroid>();

        level = 1;
        spawnAsteroids();

        hudPlayer = new Player(null);

        fsTimer = 0;
        fsTime = 5;
        enemyBullets = new ArrayList<Bullet>();

        // set up bg music
        maxDelay = 1;
        minDelay = 0.25f;
        currentDelay = maxDelay;
        bgTimer = maxDelay;
        playLowPulse = true;

    }

    private void splitAsteroids(Asteroid a) {
        numAsteroidsLeft--;

        if (a.getType() == Asteroid.LARGE) {
            asteroids.add(
                    new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
            asteroids.add(
                    new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
        }
        if (a.getType() == Asteroid.MEDIUM) {
            asteroids.add(
                    new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
            asteroids.add(
                    new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
        }
    }

    private void spawnAsteroids() {

        asteroids.clear();

        int numToSpawn = 4 + level - 1;

        for (int i = 0; i < numToSpawn; i++) {

            float x = MathUtils.random(Game.WIDTH);
            float y = MathUtils.random(Game.HEIGHT);

            float dx = x - player.getx();
            float dy = y - player.gety();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            while (dist < 100) {
                x = MathUtils.random(Game.WIDTH);
                y = MathUtils.random(Game.HEIGHT);
                dx = x - player.getx();
                dy = y - player.gety();
                dist = (float) Math.sqrt(dx * dx + dy * dy);
            }

            asteroids.add(new Asteroid(x, y, Asteroid.LARGE));

        }

    }

    public void update(float dt) {

        // get user input
        handleInput();

        // next level
        if (asteroids.size() == 0) {
            level++;
            spawnAsteroids();
        }

        // update player
        player.update(dt);
        if (player.isDead()) {

            player.reset();
            return;
        }

        // update player bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(dt);
            if (bullets.get(i).shouldRemove()) {
                bullets.remove(i);
                i--;
            }
        }

        // update flying saucer
        if (flyingSaucer == null) {
            fsTimer += dt;
            if (fsTimer >= fsTime) {
                fsTimer = 0;
                int type = MathUtils.random() < 0.5
                        ? Enemy.SMALL : Enemy.LARGE;
                int direction = MathUtils.random() < 0.5
                        ? Enemy.RIGHT : Enemy.LEFT;
                flyingSaucer = new Enemy(
                        type,
                        direction,
                        player,
                        enemyBullets
                );
            }
        } // if there is a flying saucer already
        else {
            flyingSaucer.update(dt);
            if (flyingSaucer.shouldRemove()) {
                flyingSaucer = null;

            }
        }

        // update fs bullets
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).update(dt);
            if (enemyBullets.get(i).shouldRemove()) {
                enemyBullets.remove(i);
                i--;
            }
        }

        // update asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).update(dt);
            if (asteroids.get(i).shouldRemove()) {
                asteroids.remove(i);
                i--;
            }
        }

        // check collision
        checkCollisions();

    }

    private void checkCollisions() {

        // player-asteroid collision
        if (!player.isHit()) {
            for (int i = 0; i < asteroids.size(); i++) {
                Asteroid a = asteroids.get(i);
                if (a.intersects(player)) {
                    player.hit();
                    asteroids.remove(i);
                    i--;
                    splitAsteroids(a);
                    break;
                }
            }
        }

        // bullet-asteroid collision
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid a = asteroids.get(j);
                if (a.contains(b.getx(), b.gety())) {
                    bullets.remove(i);
                    i--;
                    asteroids.remove(j);
                    j--;
                    splitAsteroids(a);

                    break;
                }
            }
        }

    }

    public void draw() {

        sr.setProjectionMatrix(Game.cam.combined);

        // draw player
        player.draw(sr);

        // draw bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(sr);
        }

        // draw flying saucer
        if (flyingSaucer != null) {
            flyingSaucer.draw(sr);
        }

        // draw fs bullets
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).draw(sr);
        }

        // draw asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).draw(sr);
        }

    }

    public void handleInput() {

        if (!player.isHit()) {
            player.setLeft(GameKeys.isDown(GameKeys.LEFT));
            player.setRight(GameKeys.isDown(GameKeys.RIGHT));
            player.setUp(GameKeys.isDown(GameKeys.UP));
            if (GameKeys.isPressed(GameKeys.SPACE)) {
                player.shoot();
            }
        }

    }

    public void dispose() {

        sr.dispose();

    }

}
