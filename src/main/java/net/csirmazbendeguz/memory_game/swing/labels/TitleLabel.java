package net.csirmazbendeguz.memory_game.swing.labels;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Label to show the game's title.
 */
@Singleton
public class TitleLabel extends JLabel {

    @Inject
    public TitleLabel(@Named("title") BufferedImage title) {
        super(new ImageIcon(title));
    }

}
