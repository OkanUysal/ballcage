package uysal.okan.ballcage.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;

import uysal.okan.ballcage.config.GameConfig;

public class Star {
    public Sprite sprite;
    public float speedX = 0;
    public float speedY = 0;
    public float staticX;
    public float staticY;

    public Star(Sprite sprite) {
        this.sprite = sprite;
        calculateSpeed();
    }

    public void calculateSpeed() {
        float x = GameConfig.HUD_WIDTH - sprite.getX();
        float y = GameConfig.HUD_HEIGHT - sprite.getY();

        staticX = x / 10000f;
        staticY = y / 10000f;
    }

    public void moveStar() {
        speedX += staticX;
        speedY += staticY;

        sprite.setPosition(sprite.getX() + speedX, sprite.getY() + speedY);


    }
}
