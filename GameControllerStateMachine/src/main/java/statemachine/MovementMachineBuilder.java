package statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import ui.UI;
/**
 * Konfiguration einer Movement State Machine
 * @author Hauptverantwortlich: Patrick Behrens, Mitwirkend: Pascal Piora
 */
public class MovementMachineBuilder extends MachineBuilder{
    private int dashTime;

    /**
     * Klassenkonstruktor mit Initialisierung der dashTime
     * @param dashTime Zeitdauer für einen Dash
     */
    public MovementMachineBuilder(int dashTime) {
        this.dashTime = dashTime;
        id = "Movement";
    }

    /**
     * Aufbau einer Movement State Machine
     * Definition von Zuständen und Zustandsübergängen
     * @return Gibt erzeugte State Machine zurück
     */
    @Override
    public StateMachine<String, String> buildMachine() {
        Builder<String, String> builder = StateMachineBuilder.builder();

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
                    .state(States.walk, walkEntry(), null)
                    .state(States.run, runEntry(), runExit())
                    .state(States.dash, dashEntry(), dashExit());

            builder.configureTransitions()
                    .withExternal().source(States.idle).target(States.walk).event(Events.w_pressed)
                    .and()
                    .withExternal().source(States.idle).target(States.dash).event(Events.e_pressed)
                    .and()
                    .withInternal().source(States.dash).action(timedTransition()).timerOnce(dashTime)
                    .and()
                    .withExternal().source(States.dash).target(States.idle).event(Events.timer)
                    .and()
                    .withExternal().source(States.walk).target(States.idle).event(Events.w_released)
                    .and()
                    .withExternal().source(States.walk).target(States.dash).event(Events.e_pressed)
                    .and()
                    .withExternal().source(States.walk).target(States.run).event(Events.shift_pressed)
                    .and()
                    .withExternal().source(States.run).target(States.walk).event(Events.shift_released)
                    .and()
                    .withExternal().source(States.run).target(States.dash).event(Events.e_pressed)
                    .and()
                    .withExternal().source(States.run).target(States.idle).event(Events.w_released);

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
        ui.printlnMovement(state);
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
     * Setzt walk State für stateContext
     * @return stateContext
     */
    public Action<String, String> walkEntry() {
        return stateContext -> {
            print(stateContext, States.walk);
        };
    }


    /**
     * Setzt dash State für stateContext
     * sendet lock Events an Jumping und Combat State Machine
     * @return stateContext
     */
    public Action<String, String> dashEntry() {
        return stateContext -> {
            print(stateContext, States.dash);
            StateMachine<String, String> jumping = (StateMachine<String, String>) stateContext.getExtendedState().getVariables().get("Jumping");
            StateMachine<String, String> combat = (StateMachine<String, String>) stateContext.getExtendedState().getVariables().get("Combat");
            jumping.sendEvent(Events.lock);
            combat.sendEvent(Events.lock);
        };
    }

    /**
     * sendet unlock Events an Jumping und Combat State Machine
     * @return stateContext
     */
    public Action<String, String> dashExit() {
        return stateContext -> {
            StateMachine<String, String> jumping = (StateMachine<String, String>) stateContext.getExtendedState().getVariables().get("Jumping");
            StateMachine<String, String> combat = (StateMachine<String, String>) stateContext.getExtendedState().getVariables().get("Combat");

            new Thread(() -> {
                jumping.sendEvent(Events.unlock);
                combat.sendEvent(Events.unlock);
            }).start();
        };
    }

    /**
     * Setzt run State für stateContext
     * sendet lock Event an Combat State Machine
     * @return stateContext
     */
    public Action<String, String> runEntry() {
        return stateContext -> {
            print(stateContext, States.run);
            StateMachine<String, String> combat = (StateMachine<String, String>) stateContext.getExtendedState().getVariables().get("Combat");
            combat.sendEvent(Events.lock);
        };
    }

    /**
     * sendet unlock Events an Combat State Machine
     * @return stateContext
     */
    public Action<String, String> runExit() {
        return stateContext -> {
            StateMachine<String, String> combat = (StateMachine<String, String>) stateContext.getExtendedState().getVariables().get("Combat");
            combat.sendEvent(Events.unlock);
        };
    }
}
