import java.util.ArrayList;

import ia.battle.core.BattleField;
import ia.battle.core.FieldCell;
import ia.battle.core.Warrior;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.exceptions.RuleException;

public class Character extends Warrior {

	public Character(String name, int health, int defense, int strength, int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
	}
	
	public void checkSI() {
		
	}
	
	@Override
	public Action playTurn(long tick, int actionNumber) {
		BattleField bf = BattleField.getInstance();
		WarriorData wd = bf.getEnemyData();
		WarriorData hd = bf.getHunterData();
		AStar a = new AStar(bf.getMap());
		ArrayList<FieldCell> si = bf.getSpecialItems();
		
		if (wd.getInRange()) {
			return new Attack(wd.getFieldCell());
		}else if (hd.getInRange()) {
			//return new MovimientoHuida(hd.getFieldCell());
		}else if (this.getHealth()>40 && !wd.getInRange()) {
			return new MovimientoNormal(a, this.getPosition(),getClosestSI(this.getPosition(), si));
		}
		
		

		
		
		return new MovimientoNormal(a, this.getPosition(),wd.getFieldCell());
	}

	@Override
	public void wasAttacked(int damage, FieldCell source) {
		
	}

	@Override
	public void enemyKilled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void worldChanged(FieldCell oldCell, FieldCell newCell) {
		// TODO Auto-generated method stub
		
	}
	public FieldCell getClosestSI(FieldCell myPosition,ArrayList<FieldCell> mapSI) {
		
		int x = myPosition.getX();
		int y = myPosition.getY();
		
		int distanceX = Math.abs(mapSI.get(0).getX() - x);
		int distanceY = Math.abs(mapSI.get(0).getY() - y);
		double distance = Math.hypot(distanceX, distanceY);
		int idx = 0;
		for(int c = 1; c < mapSI.size(); c++){
			distanceX = Math.abs(mapSI.get(c).getX() - x);
			distanceY = Math.abs(mapSI.get(c).getY() - y);
		    double cdistance = Math.hypot(distanceX, distanceY);
		    if(cdistance < distance){
		    	idx = c;
		        distance = cdistance;
		    }
		}
		FieldCell SIPosition = mapSI.get(idx);

		return SIPosition;
	}

}
