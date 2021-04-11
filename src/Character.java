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
		FieldCell destination;
		BattleField bf = BattleField.getInstance();
		WarriorData wd = bf.getEnemyData();
		WarriorData hd = bf.getHunterData();
		AStar a = new AStar(bf.getMap());
		ArrayList<FieldCell> si = bf.getSpecialItems();
		
		
		if (wd.getInRange() && !hd.getInRange()) {
			return new Attack(wd.getFieldCell());
		}
		else if (hd.getInRange() && !wd.getInRange()) {
			destination = runAwayVector(a, this.getPosition(), hd.getFieldCell());
			return new MovimientoNormal(a, this.getPosition(), destination);
		}
		else if (this.getHealth()>40 && !wd.getInRange() && !hd.getInRange()) {
			destination = getClosestSI(this.getPosition(), si);
			return new MovimientoNormal(a, this.getPosition(), destination);
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
public FieldCell runAwayVector(AStar map, FieldCell myPosition, FieldCell hunterPosition) {
	
		int mapSizeX = map.getMapSize()[0];
		int mapSizeY = map.getMapSize()[1];
		
		int distanceX = hunterPosition.getX() - myPosition.getX(); //3 - 2 = 1   
		int distanceY = hunterPosition.getY() - myPosition.getY(); //1 - 6 = -5

		//new position
		int runToX = (myPosition.getX() - distanceX) > 0 ? (myPosition.getX() - distanceX) : 0; // 2 - 1 = 1
		int runToY = (myPosition.getY() - distanceY) > 0 ? (myPosition.getY() - distanceY) : 0; // 6 - (-5) = 11
		
		if (runToX > 0) {
			while (hunterPosition.getX() < runToX && mapSizeX > runToX) {
				runToX++;
			}
			while (hunterPosition.getX() > runToX && 0 < runToX) {
				runToX--;
			}
		}
		if (runToY > 0) {
			while (hunterPosition.getY() < runToY && mapSizeY > runToY) {
				runToY++;
			}
			while (hunterPosition.getY() > runToY && 0 < runToY) {
				runToY--;
			}
		}
		return map.getMap()[runToX][runToY];
	}


	public FieldCell getClosestSI(FieldCell myPosition, ArrayList<FieldCell> mapSI) {
		
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
