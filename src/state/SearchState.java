package state;
import action.CharacterAction;

public class SearchState implements CharacterState {
	@Override
	public void characterAction(CharacterAction ca) {
		ca.SearchWarrior();
	}
}
