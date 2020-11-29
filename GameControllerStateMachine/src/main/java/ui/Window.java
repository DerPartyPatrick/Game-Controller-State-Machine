package ui;

import javax.swing.*;
import java.awt.*;
/**
 * Fügt JPanel zum JFrame hinzu
 * zusätzliche Konfiguration der Labels und der TextArea
 * farbliche Darstellung der aktuellen Zuständer der drei StateMachines
 * @author Hauptverantwortlich: Pascal Piora
 */
public class Window extends JFrame {
    private JPanel panel;
    private JLabel lb_Title;
    private JLabel lb_Controlls;
    private JTextArea tA_Controlls;
    private JLabel lb_Jumping;
    private JLabel lb_Movement;
    private JLabel lb_Idle_Movement;
    private JLabel lb_Combat;
    private JLabel lb_Idle_Jumping;
    private JLabel lb_Idle_Combat;
    private JLabel lb_Walk;
    private JLabel lb_Jump;
    private JLabel lb_Dash;
    private JLabel lb_Run;
    private JLabel lb_Attack;
    private Color defaultBackground;
    private Color activeBackground;
    private Color lockedBackground;

    /**
     * Klassenkonstruktor
     */
    public Window(){
        add(panel);
        printControlls();
        defaultBackground = new Color(225, 239, 242);
        activeBackground = new Color(154, 228, 244);
        lockedBackground = new Color(229, 107, 111);
    }

    /**
     * Markiert aktuellen Zustand mit neuer Hintergrundfarbe
     * @param movement aktueller Zustand der Movement StateMachine
     */
    protected void printlnMovement(String movement) {
        resetBackgroundMovement();
        switch (movement){
            case "IDLE":
                lb_Idle_Movement.setBackground(activeBackground);
                break;

            case "WALK":
                lb_Walk.setBackground(activeBackground);
                break;

            case "RUN":
                lb_Run.setBackground(activeBackground);
                break;

            case "DASH":
                lb_Dash.setBackground(activeBackground);
                break;
        }
    }

    /**
     * Markiert aktuellen Zustand mit neuer Hintergrundfarbe
     * @param jumping aktueller Zustand der Jumping StateMachine
     */
    protected void printlnJumping(String jumping) {
        resetBackgroundJumping();
        if (jumping.equals("IDLE")){
            lb_Idle_Jumping.setBackground(activeBackground);
        }
        else if(jumping.equals("JUMP")){
            lb_Jump.setBackground(activeBackground);
        }
        else if(jumping.equals("LOCK")){
            lb_Idle_Jumping.setBackground(lockedBackground);
            lb_Jump.setBackground(lockedBackground);
        }
    }

    /**
     * Markiert aktuellen Zustand mit neuer Hintergrundfarbe
     * @param combat aktueller Zustand der Combat StateMachine
     */
    protected void printlnCombat(String combat) {
        resetBackgroundCombat();
        if(combat.equals("IDLE")){
            lb_Idle_Combat.setBackground(activeBackground);
        }
        else if(combat.equals("ATTACK")){
            lb_Attack.setBackground(activeBackground);
        }
        else if(combat.equals("LOCK")){
            lb_Attack.setBackground(lockedBackground);
            lb_Idle_Combat.setBackground(lockedBackground);
        }
    }

    /**
     * Setzt den Hintergrund der Zustände der Movement StateMachine zurück
     */
    private void resetBackgroundMovement(){

        lb_Walk.setBackground(defaultBackground);
        lb_Dash.setBackground(defaultBackground);
        lb_Idle_Movement.setBackground(defaultBackground);
        lb_Run.setBackground(defaultBackground);
    }

    /**
     * Setzt den Hintergrund der Zustände der Combat StateMachine zurück
     */
    private void resetBackgroundCombat(){
        lb_Attack.setBackground(defaultBackground);
        lb_Idle_Combat.setBackground(defaultBackground);
    }

    /**
     * Setzt den Hintergrund der Zustände der Jumping StateMachine zurück
     */
    private void resetBackgroundJumping(){
        lb_Idle_Jumping.setBackground(defaultBackground);
        lb_Jump.setBackground(defaultBackground);
    }

    /**
     * Füllt die TextArea ta_Controlls mit der Steuerung für den Charakter
     */
    private void printControlls(){
        tA_Controlls.setText("Walk -> Press & Hold [w] \n" +
                "Run ->  Press & Hold [Lshift] \n" +
                "Jump -> Press [space] \n" +
                "Dash -> Press [e] \n" +
                "Attack -> Press [k]  ");
    }
}
