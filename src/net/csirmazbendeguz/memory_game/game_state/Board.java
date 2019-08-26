package net.csirmazbendeguz.memory_game.game_state;

import net.csirmazbendeguz.memory_game.game_state.event.objects.NewGameEvent;
import net.csirmazbendeguz.memory_game.swing.GameFrame;
import net.csirmazbendeguz.memory_game.util.RandomCardGenerator;

import java.util.ArrayDeque;
import java.util.Queue;

public class Board {

    private static Board instance;

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }

        return instance;
    }

    private Board() {}

    private int dimension;

    private Card[][] board;

    private Queue<Card> faceUpCards;

    public void restartGame() {
        newGame(dimension);
    }

    public void newGame(int dimension) {
        this.dimension = dimension;
        board = new RandomCardGenerator().generateBoard(dimension);
        faceUpCards = new ArrayDeque<>();

        Stopwatch stopwatch = Stopwatch.getInstance();
        stopwatch.stopTimer();
        stopwatch.resetSeconds();
        TriesCounter.getInstance().reset();

        GameFrame.eventDispatcher.dispatch(new NewGameEvent(this, this));
    }

    public int getDimension() {
        return dimension;
    }

    public Card[][] getBoard() {
        return board;
    }

    public void flipUp(Card card) {
        card.flipUp();
        faceUpCards.offer(card);
    }

    public boolean isGameWon() {
        for (Card[] cards : board) {
            for (Card card : cards) {
                if (card.isVisible()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void checkPairs() {
        TriesCounter triesCounter = TriesCounter.getInstance();

        while (faceUpCards.size() >= 2) {
            triesCounter.increase();

            Card card1 = faceUpCards.poll();
            Card card2 = faceUpCards.poll();

            if (card1.isPairOf(card2)) {
                card1.hide();
                card2.hide();
            } else {
                card1.flipDown();
                card2.flipDown();
            }
        }
    }

}
