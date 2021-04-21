package state;

import action.CharacterAction;
import ia.battle.core.BattleField;
import ia.battle.core.actions.Action;
import standard.Character;

public class SuicideState implements CharacterState {
	private BattleField bf = null;
	private Character ch = null;
	
	public SuicideState(Character character) {
		bf = BattleField.getInstance();
		this.ch=character;
	}
	
	@Override
	public String toString() {
		return "SUICIDE";
	}

	@Override
	public Action characterAction(CharacterAction p) {
		return p.suicide(bf,ch);
	}

	@Override
	public CharacterState nextState() {
		bf = BattleField.getInstance();
		//To Search state
		if(ch.getHealth()>=10) {
			return new SearchState(this.getCharacter());
		}		
		else{
			return this;
		}
	}

	@Override
	public Character getCharacter() {
		return ch;
	}

	@Override
	public void setCharacter(Character character) {
		this.ch=character;
	}

}
