package ui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * User Interface
 * Erzeugt Window(GUI) und stellt Methoden zur Darstellung in der Anwendung bereit
 * @Param keys Speicherung aller aktuell Tastaturanschläge
 * @Param window GUI
 * @author Hauptverantwortlich: Pascal Piora, Mitwirkend: Patrick Behrens
 */
public class UI {
    private Set<Integer> keys;
    private Window window;

    /**
     * Klassenkonstruktor
     * Erzeugt neues Fenster fester Größe
     * Erzeugt KeyListener zum abhören der Tastaturanschläge
     */
    public UI() {
        keys = new HashSet<>();
        window = new Window();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setSize(900, 700);

        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                keys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys.remove(e.getKeyCode());
            }
        });
    }

    /**
     * Prüft ob eine Taste gedrückt wird
     * @param keyCode Code einer Tastatureingabe
     * @return true, wenn Parameter keyCode in keys enthalten
     */
    public boolean getKeyPressed(int keyCode) {
        return keys.contains(keyCode);
    }

    /**
     * Gibt Zustand der Movement StateMachine an die GUI weiter
     * @param movement Zustand der Movement StateMachine
     */
    public void printlnMovement(String movement) {

        window.printlnMovement(movement);
    }

    /**
     * Gibt Zustand der Jumping StateMachine an die GUI weiter
     * @param jumping Zustand der Jumping StateMachine
     */
    public void printlnJumping(String jumping) {

        window.printlnJumping(jumping);
    }

    /**
     * Gibt Zustand der Jumping StateMachine an die GUI weiter
     * @param combat Zustand der Combat StateMachine
     */
    public void printlnCombat(String combat) {

        window.printlnCombat(combat);
    }
}
