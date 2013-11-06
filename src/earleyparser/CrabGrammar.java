package earleyparser;

/**
 * 
 * @author xxk
 *
 */
public class CrabGrammar extends Grammar{

	public CrabGrammar(){
		super();
		initRules();
		initPOS();
	}
	
	// Create the Rules for this Simple Grammar
		private void initRules()
		{
			// S-> NP VP
			String[] s1 = {"NP", "VP"};
			RHS[] sRHS = {new RHS(s1)};
			Rules.put("S", sRHS);

			//NP-> N
			//NP-> SURNAME N
			//NP-> V N
			String[] np1 = {"Noun"};
			String[] np2 = {"Surname", "Noun"};
			String[] np3 = {"Verb","Noun"};
			RHS[] npRHS = {new RHS(np1), new RHS(np2), new RHS(np3)};
			Rules.put("NP", npRHS);

			//PP-> Prep NP
			String[] pp1 = {"Prep", "NP"};
			RHS[] ppRHS = {new RHS(pp1)};
			Rules.put("PP", ppRHS);
			
			//VP->V NP
			//VP->ADV NP
			//VP->PP VP
			String[] vp1 = {"Verb", "NP"};
			String[] vp2 = {"ADV", "VP"};
			String[] vp3 = {"PP", "VP"};
			RHS[] vpRHS = {new RHS(vp1), new RHS(vp2),new RHS(vp3)};
			Rules.put("VP", vpRHS);


			//Noun
			String[] noun1 = {"wang"};
			String[] noun2 = {"fanyi"};
			String[] noun3 = {"xiaoshuo"};
			RHS[] nounRHS = {new RHS(noun1), new RHS(noun2), new RHS(noun3)};
			Rules.put("Noun", nounRHS);

			//Verb
			String[] verb1 = {"fanyi"};
			String[] verb2 = {"zai"};
			RHS[] verbRHS = {new RHS(verb1),new RHS(verb2)};
			Rules.put("Verb", verbRHS);

			//Prep
			String[] prep = {"zai"};
			RHS[] prepRHS = {new RHS(prep)};
			Rules.put("Prep", prepRHS);

			//ADV
			String[] adv = {"zai"};
			RHS[] advRHS = {new RHS(adv)};
			Rules.put("ADV", advRHS);
			
			//Surname
			String[] surname = {"wang"};
			RHS[] surRHS = {new RHS(surname)};
			Rules.put("Surname", surRHS);
		}

		private void initPOS()
		{
			// Nouns, Verbs, and Prepositions are Parts of Speech.
			POS.add("Noun");
			POS.add("Verb");
			POS.add("Prep");
			POS.add("ADV");
			POS.add("Surname");
		}
}
