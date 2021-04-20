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

		boolean hunterAway = bf.calculateDistance(ch.getPosition(), hd.getFieldCell()) > 10 ? true:false;
		double enemyDistance = bf.calculateDistance(ch.getPosition(), wd.getFieldCell());
		
		
		//System.out.print("Hunter at : " + bf.calculateDistance(ch.getPosition(), hd.getFieldCell()));
		
		return p.searchWarrior();
	}
}
