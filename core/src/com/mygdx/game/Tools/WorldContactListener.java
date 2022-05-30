package com.mygdx.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.sprites.InteractiveTileObject;
import com.mygdx.game.sprites.Player;

import static com.mygdx.game.Constants.BOOM_BIT;
import static com.mygdx.game.Constants.BOT_BIT;
import static com.mygdx.game.Constants.ITEM_BIT;
import static com.mygdx.game.Constants.PLAYER_BIT;


public class WorldContactListener implements ContactListener {
    PlayScreen screen;

    public WorldContactListener()
    {

    }

    public WorldContactListener(PlayScreen screen) {
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {
        int right, left, up, down;
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();


        if (fixA.getUserData() == "bang" || fixB.getUserData() == "bang") {
            Fixture head = fixA.getUserData() == "bang" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }
        if (fixA.getFilterData().categoryBits == ITEM_BIT | (fixB.getFilterData().categoryBits == ITEM_BIT)) {
            Fixture head = fixA.getFilterData().categoryBits == PLAYER_BIT ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;
            ((InteractiveTileObject) object.getUserData()).onHeadHit();
        }
        if (fixA.getFilterData().categoryBits != PLAYER_BIT && fixB.getFilterData().categoryBits != PLAYER_BIT
                && fixA.getFilterData().categoryBits != BOOM_BIT && fixB.getFilterData().categoryBits != BOOM_BIT
                && fixA.getFilterData().categoryBits == BOT_BIT | (fixB.getFilterData().categoryBits == BOT_BIT)) {
            Fixture head = fixA.getFilterData().categoryBits == BOT_BIT ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;
            ((InteractiveTileObject) object.getUserData()).setCollision(true);
            System.out.println("ok");
        }
        if (fixA.getFilterData().categoryBits == PLAYER_BIT && fixB.getFilterData().categoryBits == BOT_BIT
                || fixA.getFilterData().categoryBits == BOT_BIT && fixB.getFilterData().categoryBits == PLAYER_BIT) {
            Fixture head = fixA.getFilterData().categoryBits == BOT_BIT ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            Player player =  ((Player) object.getUserData());
            player.setDie(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


}