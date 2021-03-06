package net.csirmazbendeguz.memory_game.state.event_handlers;

import com.google.inject.Inject;
import net.csirmazbendeguz.memory_game.event.EventDispatcher;
import net.csirmazbendeguz.memory_game.event.listeners.CardHideListener;
import net.csirmazbendeguz.memory_game.event.listeners.GameStartListener;
import net.csirmazbendeguz.memory_game.event.objects.CardHideEvent;
import net.csirmazbendeguz.memory_game.event.objects.GameEndEvent;
import net.csirmazbendeguz.memory_game.event.objects.GameStartEvent;
import net.csirmazbendeguz.memory_game.state.Board;
import net.csirmazbendeguz.memory_game.state.Stopwatch;
import net.csirmazbendeguz.memory_game.state.TriesCounter;

public class GameEndHandler implements GameStartListener, CardHideListener {

    private Board board;

    private Stopwatch stopwatch;

    private TriesCounter triesCounter;

    private EventDispatcher eventDispatcher;

    /**
     * A flag storing whether the game already ended.
     *
     * Ensures the {@link net.csirmazbendeguz.memory_game.event.objects.GameEndEvent} is only dispatched once per game.
     */
    private boolean isGameEnded;

    @Inject
    public GameEndHandler(Board board, Stopwatch stopwatch, TriesCounter triesCounter, EventDispatcher eventDispatcher) {
        this.board = board;
        this.stopwatch = stopwatch;
        this.triesCounter = triesCounter;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void gameStarted(GameStartEvent event) {
        isGameEnded = false;
    }

    /**
     * Dispatch a {@link net.csirmazbendeguz.memory_game.event.objects.GameEndEvent} when the last card is removed from the board.
     */
    @Override
    public void cardHidden(CardHideEvent event) {
        // The events may occur in parallel, because they are dispatched by different timer threads.
        // So, checking if the board is empty is not enough.
        // A flag is needed to decide whether the game is still in progress.
        if (!isGameEnded && board.isEmpty()) {
            isGameEnded = true;
            eventDispatcher.dispatch(new GameEndEvent(
                this,
                board.getDimension(),
                stopwatch.getSeconds(),
                triesCounter.getTries()
            ));
        }
    }

}
