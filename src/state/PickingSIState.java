package state;

import action.CharacterAction;
import ia.battle.core.BattleField;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import standard.Character;

public class PickingSIState implements CharacterState {
	private WarriorData wd = null, hd = null;
	private BattleField bf = null;
	private Character ch = null;
	
	public PickingSIState(Character character) {
		bf = BattleField.getInstance();
		wd = bf.getEnemyData();
		hd = bf.getHunterData();
		this.ch=character;
	}
	
	@Override
	public Action characterAction(CharacterAction p) {
		return p.pickingSI(bf,ch);
	}

	@Override
	public String toString() {
		return "PICKINGSI";
	}

	@Override
	public CharacterState nextState() {
		wd = bf.getEnemyData();
		hd = bf.getHunterData();
		boolean hunterAway = bf.calculateDistance(ch.getPosition(), hd.getFieldCell()) > hunterMaxDistance ? true:false;
		double enemyDistance = bf.calculateDistance(ch.getPosition(), wd.getFieldCell());
		
		if(hunterAway && ch.getRange()+SIDistance>enemyDistance) {
			return new SearchState(this.getCharacter());
		}else if(!hunterAway){
			return new RunAwayState(this.getCharacter());
		}else {
			return this;
		}
	}

	@Override
	public Character getCharacter() {
		return ch;
	}
	public void setCharacter(Character character) {
		this.ch=character;
	}
}
