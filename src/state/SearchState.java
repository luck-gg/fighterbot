package state;
import action.CharacterAction;
import ia.battle.core.BattleField;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import standard.Character;

public class SearchState implements CharacterState {
	private WarriorData wd = null, hd = null;
	private BattleField bf = null;
	private Character ch = null;
	
	public SearchState(Character character) {
		bf = BattleField.getInstance();
		wd = bf.getEnemyData();
		hd = bf.getHunterData();
		this.ch=character;
	}

	@Override
	public Action characterAction(CharacterAction p) {

		return p.searchWarrior(bf,ch);
	}

	@Override
	public CharacterState nextState() {
		wd = bf.getEnemyData();
		hd = bf.getHunterData();
		boolean hunterAway = bf.calculateDistance(ch.getPosition(), hd.getFieldCell()) > hunterMaxDistance ? true:false;
		double enemyDistance = bf.calculateDistance(ch.getPosition(), wd.getFieldCell());
		

		if(wd.getInRange()) {
			return new AttackState(this.getCharacter());
		}//To Wait state
		else if (ch.getRange()+waitDistance>=enemyDistance && ch.getRange()<enemyDistance && hunterAway) {
			return new WaitState(this.getCharacter());
		}//To PickSI state
		else if (ch.getRange()+SIDistance<enemyDistance && hunterAway) {
			return new PickingSIState(this.getCharacter());
		}//To Runaway state
		else if(!hunterAway){
			return new RunAwayState(this.getCharacter());
		}else {
			return this;
		}
	}
	
	public Character getCharacter() {
		return ch;
	}
	public void setCharacter(Character character) {
		this.ch=character;
	}
}
