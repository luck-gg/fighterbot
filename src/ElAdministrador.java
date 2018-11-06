import ia.battle.core.ConfigurationManager;
import ia.battle.core.Warrior;
import ia.battle.core.WarriorManager;
import ia.exceptions.RuleException;

public class ElAdministrador extends WarriorManager {

	@Override
	public String getName() {
		return "El Administrador";
	}

	@Override
	public Warrior getNextWarrior() throws RuleException {
		
		int maxPoints = ConfigurationManager.getInstance().getMaxPointsPerWarrior();
		
		
		return new JarJarBinks("JJB", 10, 10, 10, 10, 10);
	}

}
