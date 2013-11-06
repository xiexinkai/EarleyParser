/******************************************************************************
 * author: Breanna Ammons
 * project: EarleyParser with parse trees
 * 
 * Grammar
 *   This is a base class that should be extended for any grammar that 
 *   will be used within this program to parse sentences.
 *
 *****************************************************************************/

package earleyparser;

import java.util.HashMap;
import java.util.Vector;

public class Grammar
{
	// A mapping between a LHS (the String) and an array of RHS's.
	HashMap<String, RHS[]> Rules;

	// An array of LHS's that are Parts of Speech. 
	Vector<String> POS;

	public Grammar()
	{
		Rules = new HashMap<String, RHS[]>();
		POS = new Vector<String>();
	}

	public RHS[] getRHS(String lhs)
	{
		RHS[] rhs = null;
		if ( Rules.containsKey(lhs) )
			rhs = Rules.get(lhs);
		
		return rhs;
	}

	public boolean isPartOfSpeech(String s)
	{
		return POS.contains(s);
	}
}