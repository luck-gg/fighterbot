package action;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ia.battle.core.BattleField;
import ia.battle.core.FieldCell;
import ia.battle.core.FieldCellType;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.battle.core.actions.Skip;
import ia.battle.core.actions.Suicide;
import path.AStar;
import standard.Character;

public class CharacterAction {

	public CharacterAction() {
	}
	
	public Action attackWarrior(BattleField bf) {
		System.out.print("Movimiento Ataque");
		try {
			return new Attack(bf.getEnemyData().getFieldCell());
		} catch (Exception e) {
			System.out.print("Error = "+ e.getMessage());
			return new Skip();
		}
	}

	public Action searchWarrior(BattleField bf2, Character ch2) {
		System.out.print("Movimiento Normal");
		AStar a = new AStar(bf2.getMap());
		FieldCell wpos = bf2.getEnemyData().getFieldCell();
		FieldCell hpos = bf2.getHunterData().getFieldCell();
		return new MovimientoNormal(a, ch2.getPosition(), wpos, hpos);
	}
	
	public Action pickingSI(BattleField bf, Character ch) {
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
	
	public Action runawayFromHunter(BattleField bf, Character ch) {
		AStar a = new AStar(bf.getMap());
		FieldCell hpos = bf.getHunterData().getFieldCell();
		FieldCell destination = runAwayVector(bf, ch.getPosition(), hpos);
		destination = randomMovement(bf, destination);
		return new MovimientoNormal(a, ch.getPosition(), destination, hpos);
	}
	/*
	public Action runawayFromEnemy() {
		AStar a = new AStar(bf.getMap());
		FieldCell wpos = bf.getEnemyData().getFieldCell();
		FieldCell hpos = bf.getHunterData().getFieldCell();
		FieldCell destination = runAwayVector(ch.getPosition(), wpos);
		return new MovimientoNormal(a, ch.getPosition(), destination, hpos);
	}
	*/
	public Action suicide(BattleField bf, Character ch) {
		float explosionRange = 5;
		FieldCell enemyFC = bf.getEnemyData().getFieldCell();
		
		if (bf.calculateDistance(ch.getPosition(),enemyFC)>=explosionRange) {
			System.out.print("kamikaze");
			return this.searchWarrior(bf,ch);
		}else{
			return new Suicide();
		}
	}
	
	public Action waitEnemy(BattleField bf, Character ch) {
		AStar a = new AStar(bf.getMap());
		FieldCell hpos = bf.getHunterData().getFieldCell();
		FieldCell dest = randomMovement(bf, ch.getPosition());
		return new MovimientoNormal(a, ch.getPosition(), dest, hpos);
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

	private FieldCell randomMovement(BattleField bf, FieldCell pos) {
		List<FieldCell> adj = bf.getAdjacentCells(pos);
		Random rand = new Random();
		for (int i = 0; i < adj.size(); i++) {
			if (adj.get(i).getFieldCellType() == FieldCellType.BLOCKED) {
				adj.remove(i);
			}
		}
        FieldCell dest = adj.get(rand.nextInt(adj.size()));
		return dest;
	}
	private FieldCell runAwayVector(BattleField bf, FieldCell myPosition, FieldCell enemyPosition)
	{
		int mapSizeX = bf.getMap().length;
		int mapSizeY = bf.getMap()[0].length;
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

		return bf.getMap()[runToX][runToY];
	}
	/*
	private FieldCell createBunker(BattleField bf, Character ch) {
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
	*/
	

	
}
