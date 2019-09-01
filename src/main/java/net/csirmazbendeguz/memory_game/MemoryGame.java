package net.csirmazbendeguz.memory_game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.csirmazbendeguz.memory_game.game_state.GameState;
import net.csirmazbendeguz.memory_game.event.EventDispatcher;
import net.csirmazbendeguz.memory_game.game_state.Stopwatch;
import net.csirmazbendeguz.memory_game.game_state.TriesCounter;
import net.csirmazbendeguz.memory_game.swing.GameFrame;
import net.csirmazbendeguz.memory_game.util.ResourceLoader;

public class MemoryGame {

    /**
     * Start the game with 4x4 cards.
     */
    private static final int DEFAULT_BOARD_DIMENSION = 4;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BasicModule());
        ResourceLoader resourceLoader = injector.getInstance(ResourceLoader.class);
        EventDispatcher eventDispatcher = injector.getInstance(EventDispatcher.class);
        GameState gameState = injector.getInstance(GameState.class);
        Stopwatch stopwatch = injector.getInstance(Stopwatch.class);
        TriesCounter triesCounter = injector.getInstance(TriesCounter.class);

        GameFrame gameFrame = new GameFrame(resourceLoader, eventDispatcher, gameState, stopwatch, triesCounter);
        gameState.newGame(DEFAULT_BOARD_DIMENSION);
    }

}
