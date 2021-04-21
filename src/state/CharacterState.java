package state;
import action.CharacterAction;
import ia.battle.core.actions.Action;
import standard.Character;

public interface CharacterState {
	Action characterAction(CharacterAction p);

	CharacterState nextState();
	
	Character getCharacter();
}
