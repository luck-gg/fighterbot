package state;

import action.CharacterAction;
import ia.battle.core.actions.Action;

public class RunAwayState implements CharacterState {

	@Override
	public Action characterAction(CharacterAction p) {
		return p.runawayFromHunter();
	}
}
