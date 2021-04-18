package state;
import action.CharacterAction;
import ia.battle.core.actions.Action;

public class AttackState implements CharacterState {
	@Override
	public Action characterAction(CharacterAction p) {
		return p.attackWarrior();
	}
}
