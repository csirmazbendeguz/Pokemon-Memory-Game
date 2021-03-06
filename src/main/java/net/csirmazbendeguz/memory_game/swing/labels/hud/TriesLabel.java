package net.csirmazbendeguz.memory_game.swing.labels.hud;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.csirmazbendeguz.memory_game.state.TriesCounter;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

/**
 * Label to show the number of tries.
 */
@Singleton
public class TriesLabel extends BaseLabel implements Observer {

    @Inject
    public TriesLabel(@Named("labelBackground") BufferedImage background, TriesCounter triesCounter) {
        super(background);
        triesCounter.addObserver(this);
    }

    /**
     * Update the label's text.
     */
    @Override
    public void update(Observable observable, Object tries) {
        setText("Tries: " + tries);
    }

}
