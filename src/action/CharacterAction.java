package action;
import java.util.ArrayList;

import ia.battle.core.BattleField;
import ia.battle.core.FieldCell;
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
	
	public Action attackWarrior() {
		WarriorData wd1 = bf.getEnemyData();
		System.out.print("Movimiento Ataque");
		return new Attack(wd1.getFieldCell());
	}
	
	public Action searchWarrior() {
		System.out.print("Movimiento Normal");
		AStar a = new AStar(bf.getMap());
		WarriorData wd = bf.getEnemyData();
		return new MovimientoNormal(a, ch.getPosition(),wd.getFieldCell());
	}
	
	public Action pickingSI() {
		AStar a = new AStar(bf.getMap());
		ArrayList<FieldCell> si = bf.getSpecialItems();
		FieldCell destination = getClosestSI(ch.getPosition(), si);
		return new MovimientoNormal(a, ch.getPosition(),destination);
	}
	
	public Action runawayFromHunter() {
		AStar a = new AStar(bf.getMap());
		WarriorData hd = bf.getHunterData();
		FieldCell destination = runAwayVector(ch.getPosition(), hd.getFieldCell(), bf);
		return new MovimientoNormal(a, ch.getPosition(), destination);
	}
	
	public void setWarrior(Character character) {
		this.ch = character;
	}
	
	public void setBattlefield(BattleField bf ) {
		this.bf = bf;
	}
	
	private FieldCell getClosestSI(FieldCell myPosition, ArrayList<FieldCell> mapSI) {
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

	private FieldCell runAwayVector(FieldCell myPosition, FieldCell hunterPosition, BattleField bf)
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
	
}
