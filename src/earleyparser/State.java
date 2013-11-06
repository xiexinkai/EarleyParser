/******************************************************************************
 * author: Breanna Ammons
 * project: EarleyParser with parse trees
 * 
 * State
 *   The state is a LHS, a RHS, i and j that describe the section of the 
 *   sentence we are considering, and the sources of this state. The state is
 *   mostly a container class for all of the individual parts. It can compare 
 *   itself to other states and will print nicely.
 * 
 *   If additional information needed to be associated with the state, this 
 *   would potentially be the place to add it.
 * 
 *****************************************************************************/
package earleyparser;

import java.util.Vector;

public class State
{
	// The left-hand side non-terminal that led to this state.
	private String lhs;

	// This will contain a 'dot'.
	private RHS rhs;

	// Indices that describe the words being considered in the sentence.
	private int i,  j;

	// Contains the states that produced this one.
	private Vector<State> srcs;

	public State(String lhs, RHS rhs, int i, int j, State src)
	{
		this.lhs = lhs;
		this.rhs = rhs;
		this.i = i;
		this.j = j;
		this.srcs = new Vector<State>();
		if ( src != null )
			this.srcs.add(src);
	}

	public String getLHS()
	{
		return lhs;
	}

	public RHS getRHS()
	{
		return rhs;
	}

	public int getI()
	{
		return i;
	}

	public int getJ()
	{
		return j;
	}

	public Vector<State> getSources()
	{
		return srcs;
	}

	public String getPriorToDot()
	{
		return rhs.getPriorToDot();
	}

	public String getAfterDot()
	{
		return rhs.getAfterDot();
	}

	public boolean isDotLast()
	{
		return rhs.isDotLast();
	}

	public void addSources(State s)
	{
		srcs.addAll(s.srcs);
	}

	@Override
	/**************************************************************************
	 * equals()
	 *   This is an over-ride of the equals function. It tests that the LHS, 
	 *   RHS, the i, and the j are equivalent. We do not test that the sources 
	 *   are the same. During the forward and the backward parsing we do not 
	 *   care if the sources are different.
	 *************************************************************************/
	public boolean equals(Object o)
	{
		if ( o == null )
			return false;
		
		if ( o.getClass() != this.getClass() )
			return false;
		
		State s = (State) o;

		if ( lhs.compareTo(s.lhs) != 0 )
			return false;
		
		if ( ! rhs.equals(s.rhs) )
			return false;
		
		if ( i != s.i )
			return false;
		
		if ( j != s.j )
			return false;

		return true;
	}

	/**************************************************************************
	 * toString()
	 *   This is an over-ride of the toString function. It prints the state in
	 *   a way to make it more readable and useable during debugging.
	 *************************************************************************/
	@Override
	public String toString()
	{
		StringBuffer out = new StringBuffer();

		out.append(lhs + "\t-> ");
		out.append(rhs);
		out.append("\t[" + i + ", " + j + "]");

		return out.toString();
	}
}