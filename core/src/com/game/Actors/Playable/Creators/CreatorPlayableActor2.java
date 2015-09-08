package com.game.Actors.Playable.Creators;

import com.game.Actors.Playable.Products.PlayableActor;
import com.game.Actors.Playable.Products.PlayableActor2;

public class CreatorPlayableActor2 implements CreatorPlayableActor{
    private static final String TAG = CreatorPlayableActor2.class.getName();

    @Override
    public PlayableActor factoryMethod() { return new PlayableActor2(); }
}