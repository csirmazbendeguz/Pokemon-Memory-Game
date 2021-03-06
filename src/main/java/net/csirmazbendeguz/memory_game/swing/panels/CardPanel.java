package net.csirmazbendeguz.memory_game.swing.panels;

import net.csirmazbendeguz.memory_game.event.EventHandlers;
import net.csirmazbendeguz.memory_game.event.listeners.CardFlipDownListener;
import net.csirmazbendeguz.memory_game.event.listeners.CardFlipUpListener;
import net.csirmazbendeguz.memory_game.event.listeners.CardHideListener;
import net.csirmazbendeguz.memory_game.event.objects.CardFlipDownEvent;
import net.csirmazbendeguz.memory_game.event.objects.CardFlipUpEvent;
import net.csirmazbendeguz.memory_game.event.objects.CardHideEvent;
import net.csirmazbendeguz.memory_game.state.Card;
import net.csirmazbendeguz.memory_game.swing.DefaultMouseListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class CardPanel extends JPanel implements DefaultMouseListener, CardFlipUpListener, CardFlipDownListener, CardHideListener {

    private static final Dimension SIZE = new Dimension(100, 100);

    private EventHandlers eventHandlers;

    private Card card;

    private BufferedImage cardFront, cardBack;

    public CardPanel(Card card, BufferedImage cardFront, BufferedImage cardBack, EventHandlers eventHandlers) {
        this.card = card;
        this.cardFront = cardFront;
        this.cardBack = cardBack;
        addMouseListener(this);
        // The card panels are not created by Guice, so they can't be discovered and registered automatically.
        eventHandlers.register(CardFlipUpListener.class, this);
        eventHandlers.register(CardFlipDownListener.class, this);
        eventHandlers.register(CardHideListener.class, this);
        this.eventHandlers = eventHandlers;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (!card.isVisible()) {
            return;
        }
        graphics.drawImage(card.isFaceUp() ? cardFront : cardBack, 0, 0, getWidth(), getHeight(), null);
    }

    /**
     * Flip up the card.
     */
    @Override
    public void mousePressed(MouseEvent me) {
        if (card.canFlipUp()) {
            card.flipUp();
        }
    }

    @Override
    public Dimension getMinimumSize() {
        return SIZE;
    }

    @Override
    public Dimension getPreferredSize() {
        return SIZE;
    }

    @Override
    public Dimension getMaximumSize() {
        return SIZE;
    }

    @Override
    public void cardFlippedUp(CardFlipUpEvent event) {
        if (event.getCard() == card) {
            update();
        }
    }

    @Override
    public void cardFlippedDown(CardFlipDownEvent event) {
        if (event.getCard() == card) {
            update();
        }
    }

    @Override
    public void cardHidden(CardHideEvent event) {
        if (event.getCard() == card) {
            update();
        }
    }

    public void update() {
        Window window = SwingUtilities.getWindowAncestor(this);
        window.repaint();
        window.revalidate();
    }

    @Override
    public void removeNotify() {
        eventHandlers.remove(CardFlipUpListener.class, this);
        eventHandlers.remove(CardFlipDownListener.class, this);
        eventHandlers.remove(CardHideListener.class, this);
        super.removeNotify();
    }

}
