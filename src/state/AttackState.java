package state;
import action.CharacterAction;
import ia.battle.core.BattleField;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import standard.Character;

public class AttackState implements CharacterState {
	private WarriorData wd = null, hd = null;
	private BattleField bf = null;
	private Character ch = null;
	
	public AttackState(Character character) {
		bf = BattleField.getInstance();
		wd = bf.getEnemyData();
		hd = bf.getHunterData();
		this.ch=character;
	}
	@Override
	public Action characterAction(CharacterAction p) {
		return p.attackWarrior();
	}

	@Override
	public CharacterState nextState() {
		boolean hunterAway = bf.calculateDistance(ch.getPosition(), hd.getFieldCell()) > 10 ? true:false;
		double enemyDistance = bf.calculateDistance(ch.getPosition(), wd.getFieldCell());
		
		//To Search state
		if(!wd.getInRange() && hunterAway) {
			return new SearchState(this.getCharacter());
		}//To Wait state
		else if (ch.getRange()+10>enemyDistance && ch.getRange()<enemyDistance && hunterAway) {
			return new WaitState(this.getCharacter());
		}//To Runaway state
		else if(!hunterAway){
			return new RunAwayState(this.getCharacter());
		}//To Suicide state
		else if(enemyDistance<5){
			return new SuicideState(this.getCharacter());
		}
		//Keep state
		else {
			return this;
		}
	}

	@Override
	public Character getCharacter() {
		return this.ch;
	}
}
