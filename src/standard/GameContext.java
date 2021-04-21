package standard;
import action.CharacterAction;
import ia.battle.core.actions.Action;
import state.*;

public class GameContext {

	private CharacterState state =  null;
	private CharacterAction player = new CharacterAction();

	private static GameContext instance = null;

	public static GameContext getInstance() {
		if (instance == null) {
			instance = new GameContext(); 
		}
		return instance;
	}

	private GameContext(){
	}

	public void setState(CharacterState state) {
		this.state = state;
	}
	public Action gameAction() {

		this.setState(state.nextState());

		System.out.print(state.toString() + "\n");

		return state.characterAction(player);
	}

	public CharacterState getState() {
		return state;
	}


}