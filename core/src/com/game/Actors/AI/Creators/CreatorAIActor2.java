package com.game.Actors.AI.Creators;

import com.game.Actors.AI.Products.AIActor2;
import com.game.Actors.AI.Products.ProductAIActor;

public class CreatorAIActor2 implements CreatorAIActor {
    @Override
    public ProductAIActor factoryMethod() { return new AIActor2(); }
}
