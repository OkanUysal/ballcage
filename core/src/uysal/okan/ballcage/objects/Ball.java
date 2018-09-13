package uysal.okan.ballcage.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;

public class Ball {
    public Sprite sprite;
    public Body body;
    public int color;

    public Ball(Sprite sprite, Body body, int color) {
        this.sprite = sprite;
        this.body = body;
        this.color = color;
    }

}
