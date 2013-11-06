/******************************************************************************
 * author: Breanna Ammons
 * project: EarleyParser with parse trees
 * 
 * RHS
 *   The RHS contains an array of terms and potentially has a dot.
 *   We chose the '@' character to represent the dot. If this becomes a problem, 
 *   the static DOT can be changed.
 * 
 *****************************************************************************/

package earleyparser;

public class RHS
{
	// The terms that make up the RHS. This may contain a DOT.
	private String[] terms;

	// Members that describe if the dot is present in the terms, and its index.
	private boolean hasDot = false;
	private int dot = -1;

	// A static character that describes what we are currently using as the DOT.
	private final static String DOT = "@";

	public RHS(String[] t)
	{
		terms = t;
		for ( int i = 0; i < terms.length; i++ )
		{
			if ( terms[i].compareTo(DOT) == 0 ) {
				dot = i;
				hasDot = true;
				break;
			}
		}
	}

	public String[] getTerms()
	{
		return terms;
	}

	public String getPriorToDot()
	{
		if ( hasDot && dot > 0 )
			return terms[dot - 1];
		return "";
	}

	public String getAfterDot()
	{
		if ( hasDot && dot < terms.length - 1 )
			return terms[dot + 1];
		return "";
	}

	public int getDotPos()
	{
		return dot;
	}

	public boolean hasDot()
	{
		return hasDot;
	}

	public boolean isDotLast()
	{
		if ( hasDot )
			return (dot == terms.length - 1);
		return false;
	}

	public boolean isDotFirst()
	{
		if ( hasDot )
			return (dot == 0);
		return false;
	}

	/**************************************************************************
	 * addDot()
	 *   This adds a dot to the beginning of the term array. 
	 *   There is an assumption that this RHS does not already contain a dot.
	 *************************************************************************/
	public RHS addDot()
	{
		String[] t = new String[terms.length + 1];
		t[0] = DOT;

		for ( int i = 1; i < t.length; i++ )
			t[i] = terms[i - 1];

		return new RHS(t);
	}

	/**************************************************************************
	 * addDotLast()
	 *   This a dot to the end of the term array.
	 *   There is an assumption that this RHS does not already contain a dot.
	 *************************************************************************/
	public RHS addDotLast()
	{
		String[] t = new String[terms.length + 1];
		
		for ( int i = 0; i < t.length - 1; i++ )
			t[i] = terms[i];
		
		t[t.length - 1] = DOT;
		
		return new RHS(t);
	}

	/**************************************************************************
	 * moveDot()
	 *   This moves the dot one term to the right. If the dot is already last, 
	 *   it does nothing to the dot.
	 *************************************************************************/
	public RHS moveDot()
	{
		if ( isDotLast() )
			return new RHS(terms);
		
		String[] t = new String[terms.length];
		for ( int i = 0; i < t.length; i++ )
		{
			if ( terms[i].compareTo(DOT) == 0 ) {
				t[i] = terms[i + 1];
				t[i + 1] = DOT;
				i++;
			}
			else
				t[i] = terms[i];
		}
		
		return new RHS(t);
	}

	/**************************************************************************
	 * equals()
	 *   This is an over-ride of the equals function. It tests that the arrary 
	 *   terms is the same and that the dot is in the same place.
	 *************************************************************************/
	@Override
	public boolean equals(Object o)
	{
		if ( o == null )
			return false;
		
		if ( o.getClass() != this.getClass() )
			return false;

		RHS rhs = (RHS) o;

		if ( terms.length != rhs.terms.length )
			return false;
		
		if ( hasDot != rhs.hasDot )
			return false;
		
		if ( dot != rhs.dot )
			return false;
		
		for ( int i = 0; i < terms.length; i++ )
		{
			if ( terms[i].compareTo(rhs.terms[i]) != 0 )
				return false;
		}

		return true;
	}

	/**************************************************************************
	 * toString()
	 *   This is an over-ride of the toString function. It prints the the array
	 *   of terms in a readable way.
	 *************************************************************************/
	@Override
	public String toString()
	{
		StringBuffer out = new StringBuffer();

		for ( int i = 0; i < terms.length - 1; i++ )
			out.append(terms[i] + " ");

		out.append(terms[terms.length - 1]);

		return out.toString();
	}
}