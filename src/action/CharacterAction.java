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
	}
	
	public Action attackWarrior() {
		WarriorData wd1 = bf.getEnemyData();
		System.out.print("Movimiento Ataque");
		return new Attack(wd1.getFieldCell());
	}

	public Action searchWarrior() {
		System.out.print("Movimiento Normal");
		AStar a = new AStar(bf.getMap());
		FieldCell wpos = bf.getEnemyData().getFieldCell();
		FieldCell hpos = bf.getHunterData().getFieldCell();
		return new MovimientoNormal(a, ch.getPosition(), wpos, hpos);
	}
	
	public Action pickingSI() {
		AStar a = new AStar(bf.getMap());
		FieldCell destination = getClosestSI(ch.getPosition(), bf.getSpecialItems());
		FieldCell hpos = bf.getHunterData().getFieldCell();
		if(destination==ch.getPosition()) {
			return new Skip();
		}else{
			try {
				return new MovimientoNormal(a, ch.getPosition(), destination, hpos);
			} catch (Exception e) {
				System.out.print("Error = "+ e.getMessage());
				return new Skip();
			}
			
		}
	}
	
	public Action runawayFromHunter() {
		AStar a = new AStar(bf.getMap());
		FieldCell hpos = bf.getHunterData().getFieldCell();
		FieldCell destination = runAwayVector(ch.getPosition(), hpos);
		return new MovimientoNormal(a, ch.getPosition(), destination, hpos);
	}
	
	public Action runawayFromEnemy() {
		AStar a = new AStar(bf.getMap());
		FieldCell wpos = bf.getEnemyData().getFieldCell();
		FieldCell hpos = bf.getHunterData().getFieldCell();
		FieldCell destination = runAwayVector(ch.getPosition(), wpos);
		return new MovimientoNormal(a, ch.getPosition(), destination, hpos);
	}
	
	public Action suicide() {
		float explosionRange = 5f;
		FieldCell enemyFC = bf.getEnemyData().getFieldCell();
		
		if (bf.calculateDistance(ch.getPosition(),enemyFC)>=explosionRange) {
			System.out.print("kamikaze");
			return searchWarrior();
		}else{
			return new Suicide();
		}
	}
	
	public Action waitEnemy() {
		return new Skip();
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

	private FieldCell runAwayVector(FieldCell myPosition, FieldCell enemyPosition)
	{
		int mapSizeX = getBf().getMap().length;
		int mapSizeY = getBf().getMap()[0].length;
		int runToX = 0;
		int runToY = 0;

		int distanceX = Math.abs(enemyPosition.getX() - myPosition.getX()); // 3-4=1 ; 4-3=1
		int distanceY = Math.abs(enemyPosition.getY() - myPosition.getY()); // 3-4=1 ; 4-3=1
		
		if (distanceX > 0) {
			runToX = enemyPosition.getX() - myPosition.getX() > 0 ? 1:(mapSizeX-2) ; //3-4->end ; 4-3=1
		}
		if (distanceY > 0) {
			runToY = enemyPosition.getY() - myPosition.getY() > 0 ? 1:(mapSizeY-2) ; //3-4->end ; 4-3=1
		}

		return getBf().getMap()[runToX][runToY];
	}
	
	private FieldCell createBunker(BattleField bf) {
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

	private BattleField getBf() {
		return bf;
	}

	
}
