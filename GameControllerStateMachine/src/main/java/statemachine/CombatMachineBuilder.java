package statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import ui.UI;
/**
 * Konfiguration einer Combat State Machine
 * @author Hauptverantwortlich: Patrick Behrens, Mitwirkend: Pascal Piora
 */
public class CombatMachineBuilder extends MachineBuilder {
    private int coolDown;

    /**
     * Klassenkonstruktor mit Initialisierung der coolDown Zeit
     * @param coolDown Zeitdauer für einen Angriff
     */
    public CombatMachineBuilder(int coolDown) {
        this.coolDown = coolDown;
        id = "Combat";
    }

    /**
     * Aufbau einer Combat State Machine
     * Definition von Zuständen und Zustandsübergängen
     * @return Gibt erzeugte State Machine zurück
     */
    @Override
    public StateMachine<String, String> buildMachine() {
        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();

        try {
            builder.configureConfiguration()
                    .withConfiguration()
                    .machineId(id)
                    .listener(new StateMachineListenerAdapter<String, String>() {
                        @Override
                        public void stateChanged(State from, State to) {
                            System.out.println("<" + id + "> " + to.getId());
                        }
                    });

            builder.configureStates().withStates()
                    .initial(States.idle)
                    .state(States.idle, idleEntry(), null)
                    .state(States.lock, lockEntry(), null)
                    .state(States.attack, attackEntry(), null);


            builder.configureTransitions()
                    .withExternal().source(States.idle).target(States.attack).event(Events.k_pressed)
                    .and()
                    .withExternal().source(States.idle).target(States.lock).event(Events.lock)
                    .and()
                    .withExternal().source(States.lock).target(States.idle).event(Events.unlock)
                    .and()
                    .withInternal().source(States.attack).action(timedTransition()).timerOnce(coolDown)
                    .and()
                    .withExternal().source(States.attack).target(States.idle).event(Events.timer);

            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Übergibt den aktuellen Zustand der State Machine an die UI
     * @param context Aktueller Kontext der State Machine
     * @param state aktueller Zustand
     */
    private void print(StateContext<String, String> context, String state) {
        UI ui = (UI) context.getExtendedState().getVariables().get("ui");
        ui.printlnCombat(state);
    }

    /**
     * Setzt idle State für stateContext
     * @return stateContext
     */
    public Action<String, String> idleEntry() {
        return stateContext -> {
            print(stateContext, States.idle);
        };
    }

    /**
     * Setzt attack State für stateContext
     * @return stateContext
     */
    public Action<String, String> attackEntry() {
        return stateContext -> {
            print(stateContext, States.attack);
        };
    }

    /**
     * Setzt lock State für stateContext
     * @return stateContext
     */
    public Action<String, String> lockEntry() {
        return stateContext -> {
            print(stateContext, States.lock);
        };
    }
}
