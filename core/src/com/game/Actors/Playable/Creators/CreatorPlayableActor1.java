package com.game.Actors.Playable.Creators;

import com.game.Actors.Playable.Products.PlayableActor;
import com.game.Actors.Playable.Products.PlayableActorYoungWomen;

public class CreatorPlayableActor1 implements CreatorPlayableActor{
    private static final String TAG = CreatorPlayableActor1.class.getName();

    @Override
    public PlayableActor factoryMethod() { return new PlayableActorYoungWomen(); }
}