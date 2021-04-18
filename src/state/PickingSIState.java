package state;

import action.CharacterAction;
import ia.battle.core.actions.Action;

public class PickingSIState implements CharacterState {

	@Override
	public Action characterAction(CharacterAction p) {
		return p.pickingSI();
	}

	@Override
	public String toString() {
		return "PICKINGSI";
	}

}
