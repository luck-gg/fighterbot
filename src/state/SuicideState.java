package state;

import action.CharacterAction;
import ia.battle.core.actions.Action;
import standard.Character;

public class SuicideState implements CharacterState {

	private Character ch = null;
	
	public SuicideState(Character character) {
		this.ch=character;
	}
	
	@Override
	public String toString() {
		return "SUICIDE";
	}

	@Override
	public Action characterAction(CharacterAction p) {
		return p.suicide();
	}

	@Override
	public CharacterState nextState() {
		
		//To Search state
		if(ch.getHealth()>=10) {
			return new SearchState(this.getCharacter());
		}else{
			return this;
		}
	}

	@Override
	public Character getCharacter() {
		return ch;
	}

}
