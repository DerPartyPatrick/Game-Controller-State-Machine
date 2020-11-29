package statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import ui.UI;
/**
 * Konfiguration einer Jumping State Machine
 * @author Hauptverantwortlich: Patrick Behrens, Mitwirkend: Pascal Piora
 */
public class JumpingMachineBuilder extends MachineBuilder {
    private int jumpTime;

    /**
     * Klassenkonstruktor mit Initialisierung der jumpTime
     * @param jumpTime Zeitdauer für einen Sprung
     */
    public JumpingMachineBuilder(int jumpTime) {
        this.jumpTime = jumpTime;
        id = "Jumping";
    }

    /**
     * Aufbau einer Jumping State Machine
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
                    .state(States.jump, jumpEntry(), null)
                    .state(States.lock, lockEntry(), null);

            builder.configureTransitions()
                    .withExternal().source(States.idle).target(States.jump).event(Events.space_pressed)
                    .and()
                    .withExternal().source(States.idle).target(States.lock).event(Events.lock)
                    .and()
                    .withExternal().source(States.lock).target(States.idle).event(Events.unlock)
                    .and()
                    .withInternal().source(States.jump).action(timedTransition()).timerOnce(jumpTime)
                    .and()
                    .withExternal().source(States.jump).target(States.idle).event(Events.timer);

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
        ui.printlnJumping(state);
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
     * Setzt jump State für stateContext
     * @return stateContext
     */
    public Action<String, String> jumpEntry() {
        return stateContext -> {
            print(stateContext, States.jump);
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
