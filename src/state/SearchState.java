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

		return p.searchWarrior();
	}

	@Override
	public CharacterState nextState() {
		boolean hunterAway = bf.calculateDistance(ch.getPosition(), hd.getFieldCell()) > 10 ? true:false;
		double enemyDistance = bf.calculateDistance(ch.getPosition(), wd.getFieldCell());
		

		if(wd.getInRange() && hunterAway) {
			return new AttackState(this.getCharacter());
		}//To Wait state
		else if (ch.getRange()+10>enemyDistance && ch.getRange()<enemyDistance && hunterAway) {
			return new WaitState(this.getCharacter());
		}//To PickSI state
		else if (ch.getRange()+25>enemyDistance && ch.getRange()+10<=enemyDistance && hunterAway) {
			return new WaitState(this.getCharacter());
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
}
