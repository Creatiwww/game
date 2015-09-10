package com.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class MyActor extends Actor {

    protected Texture texture;
    protected String texturePath;
    protected ActorPosition actorPosition;

    public ActorPosition getPosition(){
        return this.actorPosition;
    }

    protected void init(){
        texture=new Texture(Gdx.files.internal(texturePath));
        actorPosition= new ActorPosition();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public class ActorPosition{
        public int cellIndexX, cellIndexY;
    }

}
