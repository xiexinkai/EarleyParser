/******************************************************************************
 * author: Breanna Ammons
 * project: EarleyParser with parse trees
 * 
 * NewGrammar
 *   The NewGrammar is an example of an extension of the Grammar class. It
 *   is more complex then the SimpleGrammar. 
 * 
 *   S  -> NP VP
 * 
 *   NP -> NP PP
 *   NP -> Noun
 *   NP -> NP Conj NP
 *   NP -> Article NP
 *   NP -> Adj NP
 * 
 *   VP -> Verb NP
 *   VP -> VP PP
 *   VP -> Adv VP
 * 
 *   PP -> Prep NP
 * 		
 *   Noun -> John | Mary | Denver | men | women | Police | dogs
 *   Verb -> called | like
 *   Prep -> from
 *   Conj -> and
 *   Adj -> old
 *   Adv -> quickly
 *   Article-> the
 * 
 *****************************************************************************/

package earleyparser;

public class NewGrammar extends Grammar
{
	public NewGrammar()
	{
		super();
		initialize();
	}

	private void initialize()
	{
		initRules();
		initPOS();
	}

	// Create the Rules for this New Grammar
	private void initRules()
	{
		// S -> NP VP
		String s = "S";
		String[] s1 = { "NP", "VP" };
		RHS[] sRHS = { new RHS(s1) };
		Rules.put(s, sRHS);

		// NP -> NP PP
		// NP -> Noun
		// NP -> NP Conj NP
		// NP -> Article NP
		// NP -> Adj NP
		String np = "NP";
		String[] np1 = { "NP", "PP" };
		String[] np2 = { "Noun" };
		String[] np3 = { "NP", "Conj", "NP" };
		String[] np4 = { "Article", "NP" };
		String[] np5 = { "Adj", "NP" };
		RHS[] npRHS = { new RHS(np1), new RHS(np2), new RHS(np3),
						new RHS(np4), new RHS(np5)
		};
		Rules.put(np, npRHS);

		// VP -> Verb NP
		// VP -> VP PP
		// VP -> Adv VP
		String vp = "VP";
		String[] vp1 = { "Verb", "NP" };
		String[] vp2 = { "VP", "PP" };
		String[] vp3 = { "Adv", "VP" };
		RHS[] vpRHS = { new RHS(vp1), new RHS(vp2), new RHS(vp3) };
		Rules.put(vp, vpRHS);

		// PP -> Prep NP
		String pp = "PP";
		String[] pp1 = { "Prep", "NP" };
		RHS[] ppRHS = { new RHS(pp1) };
		Rules.put(pp, ppRHS);

		// Noun -> John | Mary | Denver | men | women | Police | dogs
		String noun = "Noun";
		String[] noun1 = { "John" };
		String[] noun2 = { "Mary" };
		String[] noun3 = { "Denver" };
		String[] noun4 = { "men" };
		String[] noun5 = { "women" };
		String[] noun6 = { "Police" };
		String[] noun7 = { "dogs" };
		RHS[] nounRHS = { new RHS(noun1), new RHS(noun2), new RHS(noun3),
						  new RHS(noun4), new RHS(noun5), new RHS(noun6),
						  new RHS(noun7)
		};
		Rules.put(noun, nounRHS);

		// Verb -> called | like
		String verb = "Verb";
		String[] verb1 = { "called" };
		String[] verb2 = { "like" };
		RHS[] verbRHS = { new RHS(verb1), new RHS(verb2) };
		Rules.put(verb, verbRHS);

		// Prep -> from
		String prep = "Prep";
		String[] prep1 = { "from" };
		RHS[] prepRHS = { new RHS(prep1) };
		Rules.put(prep, prepRHS);

		// Conj -> and
		String conj = "Conj";
		String[] conj1 = { "and" };
		RHS[] conjRHS = { new RHS(conj1) };
		Rules.put(conj, conjRHS);

		// Adj -> old
		String adj = "Adj";
		String[] adj1 = { "old" };
		RHS[] adjRHS = { new RHS(adj1) };
		Rules.put(adj, adjRHS);

		// Adv -> quickly
		String adv = "Adv";
		String[] adv1 = { "quickly" };
		RHS[] advRHS = { new RHS(adv1) };
		Rules.put(adv, advRHS);

		// Article-> the
		String article = "Article";
		String[] article1 = { "the" };
		RHS[] articleRHS = { new RHS(article1) };
		Rules.put(article, articleRHS);
	}

	private void initPOS()
	{
		POS.add("Noun");
		POS.add("Verb");
		POS.add("Prep");
		POS.add("Conj");
		POS.add("Adj");
		POS.add("Adv");
		POS.add("Article");
	}
}
