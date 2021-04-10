import java.util.ArrayList;
import java.util.Random;

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
		
		final int totalStats = 5;
		ArrayList<Character> characterRoster = new ArrayList<>();
		int maxPoints = ConfigurationManager.getInstance().getMaxPointsPerWarrior();
		
		
		int puntoEstandar = Math.floorDiv(maxPoints, totalStats);
		int puntoMitad = puntoEstandar/2;
		int puntoExtra = maxPoints % totalStats;//Resto si es que existe

		//Defino los personajes
		characterRoster.add(new Character("EQ", puntoEstandar+puntoExtra, puntoEstandar, puntoEstandar, puntoEstandar, puntoEstandar));
		
		characterRoster.add(new Character("TANK", puntoEstandar+puntoMitad+puntoExtra, puntoEstandar+puntoMitad, puntoEstandar, puntoMitad, puntoMitad));
		
		characterRoster.add(new Character("SNIPER", puntoMitad, puntoMitad, puntoEstandar+puntoMitad, puntoEstandar, puntoEstandar+puntoMitad+puntoExtra));
		
		//Traigo un personaje random por cada llamada al método
		return getRandomCharacter(characterRoster);

	}
	
	public Character getRandomCharacter(ArrayList<Character> roster){
        Random rand = new Random();
        return roster.get(rand.nextInt(roster.size()));
    }

}
