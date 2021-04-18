package action;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ia.battle.core.BattleField;
import ia.battle.core.FieldCell;
import ia.battle.core.FieldCellType;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.battle.core.actions.BuildWall;
import ia.battle.core.actions.Skip;
import ia.battle.core.actions.Suicide;
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
	//TODO: Incorporar logica de Hunter
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
	
	public Action runawayFromEnemy() {
		AStar a = new AStar(bf.getMap());
		WarriorData wd = bf.getEnemyData();
		FieldCell destination = runAwayVector(ch.getPosition(), wd.getFieldCell(), bf);
		return new MovimientoNormal(a, ch.getPosition(), destination);
	}
	
	public Action suicide() {
		float explosionRange = 10f;
		float runawayRange = 15f;
		FieldCell enemyFC = bf.getEnemyData().getFieldCell();
		
		if (bf.calculateDistance(ch.getPosition(),enemyFC)>=runawayRange) {
			return runawayFromHunter();
		}else{
			FieldCell destination = createBunker(bf);
			if(destination==null && bf.calculateDistance(ch.getPosition(), enemyFC)<explosionRange) {
				return new Suicide();
			}else if(destination==null)  {
				return new Skip();
			}else {
				return new BuildWall(destination);
			}
		}
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

	private FieldCell runAwayVector(FieldCell myPosition, FieldCell enemyPosition, BattleField bf)
	{
		int mapSizeX = bf.getMap().length;
		int mapSizeY = bf.getMap()[0].length;

		int distanceX = enemyPosition.getX() - myPosition.getX();
		int distanceY = enemyPosition.getY() - myPosition.getY();

		int runToX = myPosition.getX() - distanceX > 0 ? mapSizeX - 1 : 0;
		int runToY = myPosition.getY() - distanceY > 0 ? mapSizeY - 1 : 0;
		
		return bf.getMap()[runToX][runToY];
	}
	
	private FieldCell createBunker(BattleField bf2) {
		List<FieldCell> adj =bf.getAdjacentCells(ch.getPosition());
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
}
