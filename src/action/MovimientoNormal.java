package action;
import java.util.ArrayList;

import ia.battle.core.FieldCell;
import ia.battle.core.actions.Move;
import path.AStar;
import path.Node;

public class MovimientoNormal extends Move {

	private FieldCell from, dest, hunter;
	private AStar map;

	public MovimientoNormal() {
		
	}
	
	public MovimientoNormal(AStar a, FieldCell from, FieldCell dest, FieldCell hunter) {
		this.from = from;
		this.dest = dest;
		this.hunter = hunter;
		this.map = a;
	}
	
	@Override
	public ArrayList<FieldCell> move() {
		ArrayList<Node> bestPath = map.findPath(from,dest,hunter);
		ArrayList<FieldCell> fcpath = new ArrayList<FieldCell>();
		for (Node node : bestPath) {
			fcpath.add(node.getFieldCell());
		}
		return fcpath;
	}

}
