package com.game.Actors.Playable.Creators;

import com.game.Actors.Playable.Products.PlayableActor;
import com.game.Actors.Playable.Products.PlayableActorOldMan;

public class CreatorPlayableActor4 implements CreatorPlayableActor{
    private static final String TAG = CreatorPlayableActor4.class.getName();

    @Override
    public PlayableActor factoryMethod() { return new PlayableActorOldMan(); }
}