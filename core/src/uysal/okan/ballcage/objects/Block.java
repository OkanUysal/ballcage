package uysal.okan.ballcage.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Block {
    public Sprite sprite;
    public Body body;
    public boolean vertical;
    public float x;
    public float y;

    public Block(Sprite sprite, Body body, boolean vertical) {
        this.sprite = sprite;
        this.body = body;
        this.vertical = vertical;

        setXY(body.getPosition());
    }

    private void setXY(Vector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }
}
