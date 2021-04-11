import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ia.battle.core.BattleField;
import ia.battle.core.FieldCell;
import ia.battle.core.FieldCellType;
import ia.battle.core.Warrior;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.battle.core.actions.BuildWall;
import ia.exceptions.RuleException;

public class Character extends Warrior {
	int packsSI = 0;
	final int packsSILimit = 3;
	private boolean runningAway = false, stuckChar = false;
	private FieldCell lastPos = null, lastlastPos = null;

	public Character(String name, int health, int defense, int strength, int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
	}
	
	@Override
	public Action playTurn(long tick, int actionNumber) {
		FieldCell destination;
		BattleField bf = BattleField.getInstance();
		WarriorData wd = bf.getEnemyData();
		WarriorData hd = bf.getHunterData();
		AStar a = new AStar(bf.getMap());
		ArrayList<FieldCell> si = bf.getSpecialItems();
		
		//Check if stuck character
		checkStuck(this.getPosition());
		
		if (stuckChar) {
			FieldCell checkFC = enclosedChar(bf);
			if (checkFC != null) {
				return new BuildWall(checkFC);
			}
		}
		
		//Normal Attack
		if (wd.getInRange() && (!hd.getInRange() || !runningAway)  && !stuckChar) {
			return new Attack(wd.getFieldCell());
		}
		
		//Hunter Running Away
		else if ((hd.getInRange() || runningAway) && !stuckChar) {
			destination = runAwayVector(this.getPosition(), hd.getFieldCell(), bf);
			runningAway = runningAway ? false : true ;
			return new MovimientoNormal(a, this.getPosition(), destination);
		}
		
		//Picking SI
		else if (this.getHealth()>=40 && !wd.getInRange() && !hd.getInRange() && packsSI<packsSILimit) {
			destination = getClosestSI(this.getPosition(), si);
			packsSI++;
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
	private boolean checkStuck(FieldCell ap) {
		stuckChar = (lastlastPos==ap) ? true: false;
		lastlastPos=lastPos;
		lastPos=ap;
		System.out.print("im stuck");
		return stuckChar;
	}
	
	
	//TODO: Ver como hacer para cuando ya tiene paredes alrededor y que no quede encerrado
	public FieldCell enclosedChar(BattleField bf) {
		System.out.print("building wall");
		List<FieldCell> adj =bf.getAdjacentCells(this.getPosition());
		for (Iterator<FieldCell> iterator = adj.iterator(); iterator.hasNext();) {
			FieldCell fieldCell = iterator.next();
			if (!iterator.hasNext()) {
				stuckChar = false;
				return null;
			}
			if (fieldCell.getFieldCellType() != FieldCellType.BLOCKED) {
				iterator.remove();
				return fieldCell;
			}
		}
		return null;
	}
	
	//TODO: Reducir distancia final y considerar las esquinas
	public FieldCell runAwayVector(FieldCell myPosition, FieldCell hunterPosition, BattleField bf) {
	
		int mapSizeX = bf.getMap().length;
		int mapSizeY = bf.getMap()[0].length;
		
		int distanceX = hunterPosition.getX() - myPosition.getX(); //3 - 2 = 1   
		int distanceY = hunterPosition.getY() - myPosition.getY(); //1 - 6 = -5

		//new position
		int runToX = (myPosition.getX() - distanceX) > 0 ? (myPosition.getX() - distanceX) : 0; // 2 - 1 = 1
		int runToY = (myPosition.getY() - distanceY) > 0 ? (myPosition.getY() - distanceY) : 0; // 6 - (-5) = 11
		
		if (runToX > 0) {	
			runToX = (hunterPosition.getX() < runToX && mapSizeX > runToX) ? mapSizeX - 1 : 0;
		}
		if (runToY > 0) {
			runToY = (hunterPosition.getY() < runToY && mapSizeY > runToY) ? mapSizeX - 1 : 0;
		}
		return bf.getMap()[runToX][runToY];
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
