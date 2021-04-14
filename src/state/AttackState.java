package state;
import action.CharacterAction;

public class AttackState implements CharacterState {
	@Override
	public void characterAction(CharacterAction p) {
		p.AttackWarrior();
	}
}
