package pl.org.minions.stigma.client.ui.event.listeners;

import pl.org.minions.stigma.client.ui.event.UiEventListener;
import pl.org.minions.stigma.game.command.request.Move;
import pl.org.minions.stigma.game.event.actor.ActorWalk;

/**
 * Listener for event: non-player actor walk.
 */
public interface ActorWalkListener extends UiEventListener {

    /**
     * Called when event occurred.
     * @param event
     *            game event that caused this UI event
     * @param command
     *            move command which result is this event
     *            (if event was generated by different
     *            command this will be {@code null})
     * @param playerActor
     *            {@code true} when player actor was
     *            affected
     */
    void actorWalked(ActorWalk event, Move command, boolean playerActor);
}
