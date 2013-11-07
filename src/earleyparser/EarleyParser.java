/******************************************************************************
 * author: Breanna Ammons
 * project: EarleyParser with parse trees
 * 
 * EarleyParser
 *   This does the hard labor for parsing the sentence into charts that we can
 *   use to determine all of the parse trees that describe the sentence.
 * 
 *****************************************************************************/
package earleyparser;

import java.util.Vector;

public class EarleyParser
{
	private Grammar grammar;
	private String[] sentence;
	private Chart[] charts;

	public EarleyParser(Grammar g)
	{
		grammar = g;
	}

	public Grammar getGrammar()
	{
		return grammar;
	}

	public Chart[] getCharts()
	{
		return charts;
	}

	/**************************************************************************
	 * parseSentence()
	 *   This is the main loop for parsing the sentence into the chart. It will
	 *   return true if there is at least one successful parse of the sentence.
	 *   
     *   Bug:if last state in charts is not $->S @
     *       this function will return false even if there has successful parse.
	 *   Bug fixed by @author xxk
	 *************************************************************************/
	public boolean parseSentence(String[] s)
	{
		sentence = s;
		charts = new Chart[sentence.length + 1];
		for ( int i = 0; i < charts.length; i++ )
			charts[i] = new Chart();

		// Add the initial state " $ -> @ S "
		String[] start1 = { "@", "S" };
		RHS startRHS = new RHS(start1);
		State start = new State("$", startRHS, 0, 0, null);
		charts[0].addState(start);

		for ( int i = 0; i < charts.length; i++ )
		{
			for ( int j = 0; j < charts[i].size(); j++ )
			{
				State st = charts[i].getState(j);
				String next_term = st.getAfterDot();

				if ( st.isDotLast() )
					completer(st);	// State's RHS = ... @
				else if ( grammar.isPartOfSpeech(next_term) )
					scanner(st);	// State's RHS = ... @ A ..., where A is a part of speech
				else
					predictor(st);	// State's RHS = ... @ A ..., where A is NOT a part of speech
			}
		}

		// Determine if there was a successful parse.
		// bug fixed here
		Chart lastC = charts[charts.length - 1];
		int k=lastC.size();
		State parse,finish;
		do{
			k--;
			parse = lastC.getState(k);
			String[] fin = { "S", "@" };
			RHS finRHS = new RHS(fin);
			finish = new State("$", finRHS, 0, charts.length - 1, null);
		}while(k>0 && !parse.equals(finish));
		//System.out.println(k);
		boolean hasAnswer=k>0 ? true : false;
		return hasAnswer;
	}

	/**************************************************************************
	 * predictor()
	 *   After this function completes all possible states that could 
	 *   potentially continue from the state s is added to the charts.
	 *************************************************************************/
	private void predictor(State s)
	{
		String lhs = s.getAfterDot();
		RHS[] rhs = grammar.getRHS(lhs);
		int j = s.getJ();

		for ( int i = 0; i < rhs.length; i++ )
		{
			State ns = new State(lhs, rhs[i].addDot(), j, j, s);
			charts[j].addState(ns);
		}
	}

	/**************************************************************************
	 * scanner()
	 *   After this function completes any rules for the LHS that are 1 term 
	 *   only and match the word in the sentence will be added to the chart.
	 *************************************************************************/
	private void scanner(State s)
	{
		String lhs = s.getAfterDot();
		RHS[] rhs = grammar.getRHS(lhs);

		int j = s.getJ();

		for ( int a = 0; a < rhs.length; a++ )
		{
			String[] terms = rhs[a].getTerms();
			if ( terms.length == 1 &&
					j < sentence.length &&
					terms[0].compareToIgnoreCase(sentence[j]) == 0 )
			{
				State ns = new State(lhs, rhs[a].addDotLast(), j, j + 1, s);
				charts[j + 1].addState(ns);
			}
		}
	}

	/**************************************************************************
	 * completer()
	 *   After this function completes, any state in the i-th chart for which
	 *   the string after the dot matches the current state's LHS will be added 
	 *   to the j-th chart with the dot moved to the right.
	 *************************************************************************/
	private void completer(State s)
	{
		String lhs = s.getLHS();
		
		for ( int a = 0; a < charts[s.getI()].size(); a++ )
		{
			State st = charts[s.getI()].getState(a);
			String after = st.getAfterDot();
			if ( after != null && lhs.compareTo(after) == 0 )
			{
				State ns = new State(st.getLHS(), st.getRHS().moveDot(),
									 st.getI(), s.getJ(), s);
				charts[s.getJ()].addState(ns);
			}
		}
	}
}