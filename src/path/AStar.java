
import java.util.ArrayList;
import java.util.Collections;
import ia.battle.core.FieldCell;
import ia.battle.core.FieldCellType;

public class AStar {

	private FieldCell[][] map;

	public FieldCell[][] getMap() {
		return map;
	}


	private FieldCell fieldCellOrigin, fieldCellDestination;
	private ArrayList<Node> nodes;
	private ArrayList<Node> closedNodes, openNodes;
	private Node origin, destination;

	public AStar(FieldCell[][] fieldCells) {
		this.map = fieldCells;
	}
	
	public ArrayList<Node> findPath(FieldCell fcOrigin, FieldCell fcDestination) {
		this.fieldCellOrigin=fcOrigin;
		this.fieldCellDestination=fcDestination;
		nodes = new ArrayList<Node>();
		closedNodes = new ArrayList<Node>();
		openNodes = new ArrayList<Node>();

		// A node is added for each passable cell in the map
		for (FieldCell[] x : map) {
			for (FieldCell xy : x) {
				if(xy.getFieldCellType()==FieldCellType.NORMAL) {
					nodes.add(new Node(xy));
				}
			}
		}

		origin = nodes.get(nodes.indexOf(new Node(fieldCellOrigin)));
		destination = nodes.get(nodes.indexOf(new Node(fieldCellDestination)));

		Node currentNode = origin;
		while (!currentNode.equals(destination)) {
			processNode(currentNode);
			currentNode = getMinFValueNode();
		}

		return retrievePath();
	}

	private ArrayList<Node> retrievePath() {
		ArrayList<Node> path = new ArrayList<Node>();
		Node node = destination;

		while (!node.equals(origin)) {
			path.add(node);
			node = node.getParent();
		}

		Collections.reverse(path);

		return path;
	}

	private void processNode(Node node) {

		ArrayList<Node> adj = getAdjacentNodes(node);

		openNodes.remove(node);
		closedNodes.add(node);

		for (Node n : adj) {

			if (closedNodes.contains(n))
				continue;
			//Analizo FieldCell
			float cost = n.getFieldCell().getCost();
			
			//Compute the Manhattan distance from node 'n' to destination
			int h = Math.abs(destination.getX() - n.getX());
			h += Math.abs(destination.getY() - n.getY());

			//Compute the distance from origin to node 'n' 
			int g = node.getG();
			if (node.getX() == n.getX() || node.getY() == n.getY())
				g += 10 * cost;
			else
				g += Math.sqrt(2)*10 * cost;

			if (!openNodes.contains(n)) {

				n.setParent(node);
				n.setH(h);
				n.setG(g);

				openNodes.add(n);
			} else {

				if (h + g < n.getF()) {

					n.setParent(node);
					n.setH(h);
					n.setG(g);
				}
			}
		}
	}

	private Node getMinFValueNode() {
		Node node = openNodes.get(0);

		for (Node n : openNodes)
			if (node.getF() > n.getF())
				node = n;

		return node;
	}

	private ArrayList<Node> getAdjacentNodes(Node node) {
		ArrayList<Node> adjCells = new ArrayList<Node>();

		int x = node.getX();
		int y = node.getY();

		if (nodes.indexOf(new Node(x + 1, y)) >= 0)
			adjCells.add(nodes.get(nodes.indexOf(new Node(x + 1, y))));

		if (nodes.indexOf(new Node(x, y + 1)) >= 0)
			adjCells.add(nodes.get(nodes.indexOf(new Node(x, y + 1))));

		if (nodes.indexOf(new Node(x - 1, y)) >= 0)
			adjCells.add(nodes.get(nodes.indexOf(new Node(x - 1, y))));

		if (nodes.indexOf(new Node(x, y - 1)) >= 0)
			adjCells.add(nodes.get(nodes.indexOf(new Node(x, y - 1))));

		if (nodes.indexOf(new Node(x - 1, y - 1)) >= 0)
			adjCells.add(nodes.get(nodes.indexOf(new Node(x - 1, y - 1))));

		if (nodes.indexOf(new Node(x + 1, y + 1)) >= 0)
			adjCells.add(nodes.get(nodes.indexOf(new Node(x + 1, y + 1))));

		if (nodes.indexOf(new Node(x - 1, y + 1)) >= 0)
			adjCells.add(nodes.get(nodes.indexOf(new Node(x - 1, y + 1))));

		if (nodes.indexOf(new Node(x + 1, y - 1)) >= 0)
			adjCells.add(nodes.get(nodes.indexOf(new Node(x + 1, y - 1))));

		return adjCells;
	}


	public static void main(String[] args) {
		
		//MazeGenerator mg = new MazeGenerator((40 - 1) / 4, (40 - 1) / 2);
		
		//AStar a = new AStar(mg.getMaze());
		
		System.out.println("The maze to resolve:");
		//a.printMap();

		//ArrayList<Node> bestPath = a.findPath(1, 1, 35, 37);
		
		//a.mergePath(bestPath);
		
		System.out.println();
		System.out.println("The best path:");
		//a.printMap();
	}
}
