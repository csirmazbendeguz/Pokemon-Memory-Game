package net.csirmazbendeguz.memory_game.swing.buttons;

import net.csirmazbendeguz.memory_game.swing.panels.GamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestartButton extends PokemonButton implements ActionListener {

    private GamePanel gp;

    public RestartButton(GamePanel gp) {
        super("Restart");
        this.gp = gp;
        this.setBounds(250, 775, super.size.width, super.size.height);
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gp.newGame(gp.get_size());
    }

}