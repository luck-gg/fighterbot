package standard;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import action.MovimientoNormal;
import ia.battle.core.BattleField;
import ia.battle.core.FieldCell;
import ia.battle.core.FieldCellType;
import ia.battle.core.Warrior;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.battle.core.actions.BuildWall;
import ia.battle.core.actions.Skip;
import ia.battle.core.actions.Suicide;
import ia.exceptions.RuleException;
import path.AStar;

public class Character extends Warrior {
	GameContext context = null;
	public GameContext getContext() {
		return context;
	}


	int packsSI = 0, runningAway = 0 ;
	final int packsSILimit = 3, runningAwayTurns = 3, explosionRange = 5;

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
		
		//
		context = GameContext.getInstance();
		context.gameAction(this);
		
		//Normal Attack
		if (wd.getInRange() && (!hd.getInRange() || runningAway>0)) {
			return new Attack(wd.getFieldCell());
		}
		
		//Running Away from Hunter
		else if ((hd.getInRange() || runningAway > 0)) {
			destination = runAwayVector(this.getPosition(), hd.getFieldCell(), bf);
			runningAway = (runningAway > 0) ? runningAway-1 : runningAwayTurns;
			return new MovimientoNormal(a, this.getPosition(), destination);
		}
		
		//Picking SI
		else if (this.getHealth()>40 && !wd.getInRange() && !hd.getInRange() && packsSI<packsSILimit) {
			destination = getClosestSI(this.getPosition(), si);
			packsSI++;
			return new MovimientoNormal(a, this.getPosition(), destination);
		}
		
		//Low Health
		else if (this.getHealth()<20) {
			destination = enclosedChar(bf);
			if(destination==null && bf.calculateDistance(this.getPosition(), wd.getFieldCell())<explosionRange) {
				return new Suicide();
			}else if(destination==null)  {
				return new Skip();
			}else {
				return new BuildWall(destination);
			}
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
	

	public FieldCell enclosedChar(BattleField bf) {
		List<FieldCell> adj =bf.getAdjacentCells(this.getPosition());
		//Busco las paredes existentes y las elimino de la lista
		for (int i = 0; i < adj.size(); i++) {
			if (adj.get(i).getFieldCellType() == FieldCellType.BLOCKED) {
				adj.remove(i);
			}
		}
		//Sobre los elementos restantes, construyo pared salvo en un punto de escape
		for (Iterator<FieldCell> iterator = adj.iterator(); iterator.hasNext();) {
			FieldCell fieldCell = iterator.next();
			if (!iterator.hasNext()) {
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
	public FieldCell runAwayVector(FieldCell myPosition, FieldCell hunterPosition, BattleField bf)
	{
		int mapSizeX = bf.getMap().length;
		int mapSizeY = bf.getMap()[0].length;

		int distanceX = hunterPosition.getX() - myPosition.getX();
		int distanceY = hunterPosition.getY() - myPosition.getY();

		int runToX = myPosition.getX() - distanceX > 0 ? myPosition.getX() - distanceX : 0;
		int runToY = myPosition.getY() - distanceY > 0 ? myPosition.getY() - distanceY : 0;

		if (runToX > 0) {
			runToX = (hunterPosition.getX() < runToX) && (mapSizeX > runToX) ? mapSizeX - 1 : 0;
		}
		if (runToY > 0) {
			runToY = (hunterPosition.getY() < runToY) && (mapSizeY > runToY) ? mapSizeX - 1 : 0;
		}
		return bf.getMap()[runToX][runToY];
	}


	public FieldCell getClosestSI(FieldCell myPosition, ArrayList<FieldCell> mapSI) {
		double searchLimit = 5;
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
		    if (distance <= searchLimit) {
		    	return mapSI.get(idx);
		    }
		}
		return mapSI.get(idx);
	}

}
