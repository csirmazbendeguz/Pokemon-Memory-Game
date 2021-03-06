package net.csirmazbendeguz.memory_game.state;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.csirmazbendeguz.memory_game.event.EventDispatcher;
import net.csirmazbendeguz.memory_game.event.objects.GameStartEvent;
import net.csirmazbendeguz.memory_game.util.random.RandomCardGenerator;

@Singleton
public class GameState {

    /**
     * Start the game with 4x4 cards.
     */
    private static final int DEFAULT_BOARD_DIMENSION = 4;

    private Board board;

    private RandomCardGenerator randomCardGenerator;

    private EventDispatcher eventDispatcher;

    @Inject
    public GameState(Board board, RandomCardGenerator randomCardGenerator, EventDispatcher eventDispatcher) {
        this.board = board;
        this.randomCardGenerator = randomCardGenerator;
        this.eventDispatcher = eventDispatcher;
    }

    public void newGame() {
        newGame(DEFAULT_BOARD_DIMENSION);
    }

    public void restartGame() {
        newGame(board.getDimension());
    }

    public void increaseDimension() {
        int dimension = board.getDimension();
        dimension += 2;
        if (dimension == 8) {
            dimension = 2;
        }
        newGame(dimension);
    }

    /**
     * Dispatch a {@link net.csirmazbendeguz.memory_game.event.objects.GameStartEvent} with a randomly generated board.
     *
     * @param dimension The new board's dimension.
     */
    private void newGame(int dimension) {
        eventDispatcher.dispatch(new GameStartEvent(this, dimension, randomCardGenerator.generate(dimension)));
    }

}
