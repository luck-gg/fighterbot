package state;
import action.CharacterAction;
import ia.battle.core.actions.Action;
import standard.Character;

public interface CharacterState {
	double waitDistance = 2;
	double SIDistance = 20;
	double hunterMaxDistance = 7;
	
	Action characterAction(CharacterAction p);

	CharacterState nextState();
	
	Character getCharacter();

	void setCharacter(Character character);
}
