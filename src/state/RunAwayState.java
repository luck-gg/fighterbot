package state;

import action.CharacterAction;
import ia.battle.core.BattleField;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import standard.Character;

public class RunAwayState implements CharacterState {
	private WarriorData hd = null;
	private BattleField bf = null;
	private Character ch = null;

	public RunAwayState(Character character) {
		bf = BattleField.getInstance();
		hd = bf.getHunterData();
		this.ch=character;
	}
	
	@Override
	public Action characterAction(CharacterAction p) {
		return p.runawayFromHunter();
	}

	@Override
	public CharacterState nextState() {
		boolean hunterAway = bf.calculateDistance(ch.getPosition(), hd.getFieldCell()) > 10 ? true:false;
		
		if (hunterAway) {
			return new SearchState(this.getCharacter());
		}
		return this;
	}

	@Override
	public Character getCharacter() {
		return ch;
	}

}
