/******************************************************************************
 * author: Breanna Ammons
 * project: EarleyParser with parse trees
 * 
 * SimpleGrammar
 *   The SimpleGrammar is an example of an extension of the Grammar class. It
 *   implements initializing the rules of the grammar and defines what is 
 *   considered a Part of speech.
 * 
 *   S  -> NP VP
 *   NP -> NP PP
 *   NP -> Noun
 *   VP -> Verb NP
 *   VP -> VP PP
 *   PP -> Prep NP
 *   Noun -> John | Mary | Denver
 *   Verb -> called
 *   Prep -> from
 * 
 *****************************************************************************/

package earleyparser;

public class SimpleGrammar extends Grammar
{
	public SimpleGrammar()
	{
		super();
		initialize();
	}

	private void initialize()
	{
		initRules();
		initPOS();
	}

	// Create the Rules for this Simple Grammar
	private void initRules()
	{
		// A Sentence (S) is a Noun Phrase (NP) and a Verb Phrase (VP).
		String[] s1 = {"NP", "VP"};
		RHS[] sRHS = {new RHS(s1)};
		Rules.put("S", sRHS);

		// A NP is a NP and a Preposition Phrase (PP).
		// Or a NP is a Noun.
		String[] np1 = {"NP", "PP"};
		String[] np2 = {"Noun"};
		RHS[] npRHS = {new RHS(np1), new RHS(np2)};
		Rules.put("NP", npRHS);

		// A VP is a Verb and a NP.
		// A VP is a VP and a PP.
		String[] vp1 = {"Verb", "NP"};
		String[] vp2 = {"VP", "PP"};
		RHS[] vpRHS = {new RHS(vp1), new RHS(vp2)};
		Rules.put("VP", vpRHS);

		// A PP is a Preposition (Prep) and a NP.
		String[] pp1 = {"Prep", "NP"};
		RHS[] ppRHS = {new RHS(pp1)};
		Rules.put("PP", ppRHS);

		// A Noun can be "John", "Mary", or "Denver".
		String[] noun1 = {"John"};
		String[] noun2 = {"Mary"};
		String[] noun3 = {"Denver"};
		RHS[] nounRHS = {new RHS(noun1), new RHS(noun2), new RHS(noun3)};
		Rules.put("Noun", nounRHS);

		// A Verb can be "called".
		String[] verb = {"called"};
		RHS[] verbRHS = {new RHS(verb)};
		Rules.put("Verb", verbRHS);

		// A Prep can be "from".
		String[] prep = {"from"};
		RHS[] prepRHS = {new RHS(prep)};
		Rules.put("Prep", prepRHS);

	}

	private void initPOS()
	{
		// Nouns, Verbs, and Prepositions are Parts of Speech.
		POS.add("Noun");
		POS.add("Verb");
		POS.add("Prep");
	}
}
