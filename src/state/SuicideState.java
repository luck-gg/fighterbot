package state;

import action.CharacterAction;
import ia.battle.core.actions.Action;

public class SuicideState implements CharacterState {

	@Override
	public String toString() {
		return "SUICIDE";
	}

	@Override
	public Action characterAction(CharacterAction p) {
		return p.suicide();
	}

}
