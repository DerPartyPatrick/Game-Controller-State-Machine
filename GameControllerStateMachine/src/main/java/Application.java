import org.springframework.statemachine.StateMachine;
import statemachine.CombatMachineBuilder;
import statemachine.Events;
import statemachine.JumpingMachineBuilder;
import statemachine.MovementMachineBuilder;
import ui.UI;

import java.awt.event.KeyEvent;
/**
 * Aufbau und Initialisierung der StateMachines, Abhören der Usereingaben
 * @Param ui User Interface
 * @Param movement Maschine für die Funktionalitäten Gehen, Laufen und Dashen des Charakters
 * @Param jumping Maschine für die Funktionalität Springen
 * @Param combat Maschine für die Funktionalität Angreifen
 * @Param fps Wert für die Anzahl der Frames pro Sekunde
 * @author Hauptverantwortlich: Patrick Behrens, Mitwirkend: Pascal Piora
 */
public class Application {
    private UI ui;
    private StateMachine<String, String> movement;
    private StateMachine<String, String> jumping;
    private StateMachine<String, String> combat;
    private int fps;

    /**
     * Klassenkonstruktor. Initialiserung des UIs, der StateMachines und den FPS.
     * Setzen der Variablen jumping und combat in der StateMachine movement.
     * Setzen der Variable ui in allen StateMachines
     */
    public Application(int fps) {
        this.fps = fps;
        ui = new UI();
        movement = new MovementMachineBuilder(3000).buildMachine();
        jumping = new JumpingMachineBuilder(3000).buildMachine();
        combat = new CombatMachineBuilder(3000).buildMachine();
        movement.getExtendedState().getVariables().put("Jumping", jumping);
        movement.getExtendedState().getVariables().put("Combat", combat);
        movement.getExtendedState().getVariables().put("ui", ui);
        jumping.getExtendedState().getVariables().put("ui", ui);
        combat.getExtendedState().getVariables().put("ui", ui);
    }

    /**
     * Simulation eines Spiels
     * Berechnung der Frametime
     * Ausführen der frame Methode
     * Warten bis zum nächten Frame
     */
    public void start() {
        int frameTime = 1000/fps;
        movement.start();
        jumping.start();
        combat.start();

        while(true) {
            long startTime = System.currentTimeMillis();
            frame();
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            long waitTime = frameTime - totalTime;

            if(waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Reaktion auf Nutzereingaben.
     * Senden der passenden Events an die entsprechenden StateMachines
     */
    private void frame() {
        //Movement
        if(ui.getKeyPressed(KeyEvent.VK_W)) movement.sendEvent(Events.w_pressed);
        else movement.sendEvent(Events.w_released);

        if(ui.getKeyPressed(KeyEvent.VK_SHIFT)) movement.sendEvent(Events.shift_pressed);
        else movement.sendEvent(Events.shift_released);

        if(ui.getKeyPressed(KeyEvent.VK_E)) movement.sendEvent(Events.e_pressed);

        //Jumping
        if(ui.getKeyPressed(KeyEvent.VK_SPACE)) jumping.sendEvent(Events.space_pressed);

        //Attack
        if(ui.getKeyPressed(KeyEvent.VK_K)) combat.sendEvent(Events.k_pressed);
    }
}
