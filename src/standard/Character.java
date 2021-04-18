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

	public Character(String name, int health, int defense, int strength, int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
	}
	
	@Override
	public Action playTurn(long tick, int actionNumber) {

		context = GameContext.getInstance();
		
		return context.gameAction(this);
	
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
	
}
