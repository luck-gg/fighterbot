package action;
import ia.battle.core.BattleField;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import path.AStar;
import standard.Character;

public class CharacterAction {

	private BattleField bf = null;
	private Character ch = null;
	public CharacterAction() {
		this.bf = BattleField.getInstance();
	}
	
	public Action AttackWarrior() {
		WarriorData wd1 = bf.getEnemyData();
		return new Attack(wd1.getFieldCell());
	}
	
	public Action SearchWarrior() {
		AStar a = new AStar(bf.getMap());
		WarriorData wd = bf.getEnemyData();
		return new MovimientoNormal(a, ch.getPosition(),wd.getFieldCell());
	}

	public void setWarrior(Character character) {
		this.ch = character;
	}

}
