import java.util.ArrayList;

import ia.battle.core.BattleField;
import ia.battle.core.ConfigurationManager;
import ia.battle.core.FieldCell;
import ia.battle.core.actions.Move;

public class MovimientoNormal extends Move {

	private FieldCell from, dest;
	private AStar map;

	public MovimientoNormal() {
		
	}
	
	public MovimientoNormal(AStar a, FieldCell from, FieldCell dest) {
		this.from = from;
		this.dest = dest;
		this.map = a;
	}
	
	@Override
	public ArrayList<FieldCell> move() {
		ArrayList<Node> bestPath = map.findPath(from,dest);
		ArrayList<FieldCell> fcpath = new ArrayList<FieldCell>();
		for (Node node : bestPath) {
			fcpath.add(node.getFieldCell());
		}
		return fcpath;
	}

}
