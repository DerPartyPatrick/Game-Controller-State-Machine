package statemachine;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
/**
 * Abstrakte Klasse stellt Methode buildMachine() zum Aufbau einer Movement- Jumping- oder Combat State Machine bereit
 * Bietet Action timedTransition für Zustandsübergänge mit Zeitdauer an
 * @Param id  Name zur Beschreibung einer Maschine
 * @author Hauptverantwortlich: Patrick Behrens, Mitwirkend: Pascal Piora
 */
public abstract class MachineBuilder {
    protected String id;
    public abstract StateMachine<String, String> buildMachine();
    public static Action<String, String> timedTransition() {
        return stateContext -> {
            stateContext.getStateMachine().sendEvent(Events.timer);
        };
    }
}
