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
		return p.attackWarrior(bf);
	}

	@Override
	public CharacterState nextState() {
		wd = bf.getEnemyData();
		hd = bf.getHunterData();
		
		boolean hunterAway = bf.calculateDistance(ch.getPosition(), hd.getFieldCell()) > hunterMaxDistance ? true:false;
		double enemyDistance = bf.calculateDistance(ch.getPosition(), wd.getFieldCell());
		
		//To Search state
		if(!wd.getInRange() && hunterAway) {
			return new SearchState(this.getCharacter());
		}//To Wait state
		else if (ch.getRange()+waitDistance>=enemyDistance && ch.getRange()<enemyDistance && hunterAway) {
			return new WaitState(this.getCharacter());
		}//To Runaway state
		else if(!hunterAway){
			return new RunAwayState(this.getCharacter());
		}//To Suicide state
		else if(ch.getHealth()<10){
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
	public void setCharacter(Character character) {
		this.ch=character;
	}
}
