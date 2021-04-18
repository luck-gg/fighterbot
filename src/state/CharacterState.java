package state;
import action.CharacterAction;
import ia.battle.core.actions.Action;

public interface CharacterState {
	Action characterAction(CharacterAction p);
}
