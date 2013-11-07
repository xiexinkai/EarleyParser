/******************************************************************************
 * author: Breanna Ammons
 * project: EarleyParser with parse trees
 * 
 * ParseTree
 *   The ParseTree is a particualr parse of a sentence. There is a static 
 *   function that will take a grammar and a chart set produced from the 
 *   grammar and will return a vector of ParseTrees. This vector will be empty
 *   if there were not any possible parses, and will contain potentially more 
 *   than one if the sentence is ambiguous.
 * 
 *   As it stands, the ParseTree class only has two public functins. One is the 
 *   static getTree() that will create a vector of ParseTrees. The other is 
 *   toString() so that you can print the trees.
 * 
 *****************************************************************************/
package earleyparser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Vector;

public class ParseTree
{
	private PTNode root;
	
	// The stateList is a stack that describes the state tree we have been 
	// navigating to determine the parseTree.
	private Deque<State> stateList;

	static private int ID = 0;
	static private Chart[] charts;
	static private Grammar grammar;

	final static private String tab = "\t";

	/**************************************************************************
	 * All of the constructors for ParseTree are private.  The intended way to 
	 *  get a ParseTree is to use the static funciton getTree()
	 *************************************************************************/
	private ParseTree()
	{
		root = null;
		stateList = new ArrayDeque<State>();
	}

	private ParseTree(String s)
	{
		root = new PTNode(s, null);
		stateList = new ArrayDeque<State>();
	}

	private ParseTree(String s, State st)
	{
		root = new PTNode(s, null);
		stateList = new ArrayDeque<State>();
		stateList.addFirst(st);
	}

	private ParseTree(PTNode r)
	{
		root = r;
		stateList = new ArrayDeque<State>();
	}

	private ParseTree getParent()
	{
		if ( root.Parent == null )
			return null;
		return new ParseTree(root.Parent);
	}

	private ParseTree getChild(int i)
	{
		return new ParseTree(root.getChild(i));
	}

	/**************************************************************************
	 * addChild()
	 *   If the tree does not have a root yet, this child will become the root.
	 *   Otherwise it will be added to the root's children.
	 *************************************************************************/
	private void addChild(String v)
	{
		if ( root == null )
			root = new PTNode(v, null);
		else
			root.addChild(new PTNode(v, root));
	}

	/**************************************************************************
	 * copy()
	 *   This creates a deep copy of the root and the state list. This is used
	 *   when the path we were taking to get to a parse tree has more then one
	 *   viable source from our current state.
	 *************************************************************************/
	private ParseTree copy()
	{
		ParseTree t = new ParseTree();
		t.root = root.copy();
		t.stateList.addAll(this.stateList);
		return t;
	}

	/**************************************************************************
	 * getNodeI()
	 *   Do a recursive search to find the node with id i. This assumes that 
	 *   there exists a node with such an id.
	 *************************************************************************/
	private ParseTree getNodeI(int i)
	{
		PTNode p = root;

		while ( p.id != i )
		{
			for ( int a = 0; a < p.Children.size(); a++ )
			{
				if ( i >= ((PTNode) p.Children.get(a)).id )
				{
					p = (PTNode) p.Children.get(a);
					break;
				}
			}
		}

		return new ParseTree(p);
	}

	/**************************************************************************
	 * getRootID()
	 *   Returns the id associated with the root.
	 *************************************************************************/
	private int getRootID()
	{
		return root.id;
	}
	
	/**********************************************************************
	 * equals()
	 *   This is an over-ride of the equals function. Two ParseTrees are equal
	 *   if thier roots have the same value and all of the children have the 
	 *   same value.
	 *********************************************************************/
	@Override
	public boolean equals(Object obj)
	{
		if ( obj == null )
			return false;

		if ( obj.getClass() != this.getClass() )
			return false;

		ParseTree pt = (ParseTree) obj;

		return this.root.equals(pt.root);
	}
		
	/**************************************************************************
	 * parseTree()
	 *   Helper function that takes the current ParseTree and the state we are
	 *   currently inspecting.
	 * 
	 *   ParseTree tree is the entire tree that we are working on building up. 
	 *   ParseTree child is the current node we are building up that is a 
	 *   child of ParseTree tree.
	 *   
	 *   The tree was started from the right-most leaf. We build up the tree
	 *   by adding parents (which becomes the root) and adding all their 
	 *   children. 
	 *   
	 *   An important part of creating the parse tree is determining what is 
	 *   the the correct source state. This implementation uses the stateList
	 *   for this purpose. The initial state that is in the stateList is
	 *   "$ -> S @". 
	 *************************************************************************/
	static private Vector<ParseTree> parseTree(ParseTree tree, ParseTree child, State currentState)
	{
		Vector<ParseTree> trees = new Vector<ParseTree>();

		String[] startTerms = { "@", "S" };
		RHS startRHS = new RHS(startTerms);
		State start = new State("$", startRHS, 0, 0, null);

		// If the current state is the start state, we are done. 
		if ( currentState.equals(start) )
		{
			// The only state in the stateList, the currentState and the start
			// state are all same.
			tree.stateList.removeFirst();
			trees.add(tree);
			return trees;
		}

		RHS rhs = currentState.getRHS();
		boolean addedLHS = false;
		boolean alreadyRemoved = false;

		if ( rhs.isDotLast() )
		{
			// This is the currently left-most child of the current node we are
			// working on. 
			child.addChild(currentState.getLHS()); 
			addedLHS = true;
		}
		
		// If the top state on the stack is the same as the currentState (with 
		//  a dot change) then we remove it. This means that we have backwards
		//  parsed all of the term that is between the dots.
		if ( ! rhs.isDotLast() && 
			 tree.stateList.peek().getRHS().equals(currentState.getRHS().moveDot()) )
		{
			tree.stateList.removeFirst();
		}
		
		tree.stateList.addFirst(currentState);

		Vector<State> srcs = currentState.getSources();

		if ( grammar.isPartOfSpeech(currentState.getLHS()) )
		{
			// The currentState is a POS and we don't need these on the stack.
			//  The stack contains states that are not completely backwards 
			//  parsed yet and this one will be complete.
			tree.stateList.removeFirst();
			String[] terms = currentState.getRHS().getTerms();
			child.getChild(0).addChild(terms[0]);

			// Select the correct source (the one with the correct i)
			if ( srcs.size() == 1 )
				currentState = (State) srcs.get(0);
			else
			{
				State posState = tree.stateList.peekFirst();

				for ( int i = 0; i < srcs.size(); i++ )
				{
					// If the state we are currently looking at, matches the 
					//  the top state in the stateList (with the dot moved 
					//  one term) then it is the correct source.
					currentState = (State) srcs.get(i);
					State moveCurrent = new State(currentState.getLHS(), 
												  currentState.getRHS().moveDot(), 
												  currentState.getI(), 
												  currentState.getJ() + 1, null);
					if ( moveCurrent.equals(posState) )
						break;
				}
			}
			
			srcs = currentState.getSources();
			rhs = currentState.getRHS();
			tree.stateList.removeFirst();

			// If the state is not completely parsed, add it to the stateList.
			if ( currentState.getRHS().getDotPos() > 0 )
				tree.stateList.addFirst(currentState);
			
			alreadyRemoved = true;
			addedLHS = false;
		}

		// If the dot is first, the state is completely backwards parsed now.
		//  If the state has not already been removed from the stateList, do
		//  so now.
		if ( rhs.isDotFirst() && ! alreadyRemoved )
			tree.stateList.removeFirst();

		// For every source of the updated currentState, we may need to attempt a
		//  backwards parse.
		for ( int i = 0; i < srcs.size(); i++ )
		{
			ParseTree treeCopy = tree.copy();
			ParseTree childCopy = treeCopy.getNodeI(child.getRootID());
			ParseTree nextChild;

			if ( rhs.isDotFirst() )
				nextChild = childCopy.getParent();
			else if ( addedLHS )
				nextChild = childCopy.getChild(0);
			else
				nextChild = childCopy;
			
			State nextState = (State) srcs.get(i);
			String lhs = nextState.getLHS();

			// When the sources list for a state was created during parsing, all of the 
			//  potential trees were mixed together. This means that some of the 
			//  sources are not valid for the particular parse tree we are creating.
			// The conditions were it could be a source are:
			//    1) The LHS of the potential source is the same as the term prior
			//       to the dot. This source was produced by the predictor step.
			//    2) We have just completed parsing the term prior to the dot. This source
			//       was produced by the completer step. 
			//
			// The scanner step does not need to be handled here due to being handled
			//  when we were handling the POS.
			if ( currentState.getRHS().getPriorToDot().compareTo(lhs) == 0 || 
				 ( tree.stateList.peek().getRHS().equals(nextState.getRHS().moveDot()) &&
				   tree.stateList.peek().getLHS().compareTo(nextState.getLHS()) == 0 ) )
			{
				trees.addAll(parseTree(treeCopy, nextChild, nextState));
			}
		}

		return trees;
	}

	/**************************************************************************
	 * getTree()
	 *   This starts the parsing of the grammar and charts. It also removes any
	 *   duplicate trees that might have been produced during the parsing.
	 *   
	 *   This is one of the two functions that are public and is the correct 
	 *   to get a ParseTree.
	 *   
	 *   Bug:if last state in charts is not $->S @, this function cannot return any result.
	 *   Bug fixed by @author xxk
	**************************************************************************/
	static public Vector<ParseTree> getTree(Grammar g, Chart[] c)
	{
		charts = c;
		grammar = g;
		

		/*
		 * bug fixed
		 */
		Chart lastC = charts[charts.length - 1];
		Vector<ParseTree> trees = new Vector<ParseTree>();
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
		boolean hasTree=k>0 ? true : false;
		// If there was a successful parse, find all of the possible parse trees.
		if ( hasTree )
		{ 
			Vector<State> srcs = parse.getSources();
			for ( int i = 0; i < srcs.size(); i++ )
			{
				// Find all the trees that could come from this source.
				State s = (State) srcs.get(i);
				ParseTree pt = new ParseTree(parse.getLHS(), parse);
				trees.addAll(parseTree(pt, pt, s));
			}
		}

		// Remove any duplicate trees.
		Vector<ParseTree> noDups = new Vector<ParseTree>();
		for ( int i = 0; i < trees.size(); i++ )
		{
			ParseTree pt = (ParseTree) trees.get(i);
			if ( ! noDups.contains(pt) )
				noDups.add(pt);
		}

		return noDups;
	}

	/**************************************************************************
	 * toString()
	 *   This is an over-ride of the toString function. It prints the tree 
	 *   in a way to make it more readable. It uses prettyPrint() on the root
	 *   node to do this. For debugging purposes, the call to prettyPrint() 
	 *   could be changed to use PTNode::toString() 
	**************************************************************************/
	@Override
	public String toString()
	{
		if ( root == null )
			return "";

		StringBuffer out = new StringBuffer();
		out.append(root.prettyPrint(""));

		return out.toString();
	}

	/**************************************************************************
	 * PTNode
	 *   This is a helper class. It is the nodes of the parse tree that we 
	 *   will be constructing. It contains two methods for printing, and can 
	 *   make deep copies of a node and all of its children.
	 *************************************************************************/
	private class PTNode
	{
		Vector<PTNode> Children;
		PTNode Parent;
		String Value;
		int id;

		public PTNode(String v, PTNode p)
		{
			Children = new Vector<PTNode>();
			Parent = p;
			Value = v;
			id = ID++;
		}

		public PTNode(String v, PTNode p, int i)
		{
			Children = new Vector<PTNode>();
			Parent = p;
			Value = v;
			id = i;
		}

		public void addChild(PTNode c)
		{
			Children.add(0, c);
		}

		public PTNode getChild(int i)
		{
			return (PTNode) Children.get(i);
		}

		/**********************************************************************
		 * copy()
		 *   This will make a new PTNode with the same value and a copy of 
		 *   all of the children.
		**********************************************************************/
		public PTNode copy()
		{
			PTNode n = new PTNode(Value, null, id);
			for ( int i = Children.size() - 1; i >= 0; i-- )
			{
				PTNode nc = ((PTNode) Children.get(i)).copy();
				n.addChild(nc);
				nc.Parent = n;
			}
			return n;
		}

		/**********************************************************************
		 * prettyPrint()
		 *   This printing attempts to make a tree-like structure when it 
		 *   prints. It will place the value at the offset passed in, and then
		 *   prettyPrint each of its children offset a bit more. 
		 * 
		 *   Here is an example:
		 *    1
		 *      2
		 *        4
		 *        5
		 *      3
		 *        6
		 *          8
		 *        7
		**********************************************************************/
		public String prettyPrint(String offset)
		{
			StringBuffer out = new StringBuffer();
			out.append(offset + Value + "\n");

			for ( int i = 0; i < Children.size(); i++ )
			{
				PTNode c = (PTNode) Children.get(i);
				out.append(c.prettyPrint(offset + tab));
			}

			return out.toString();
		}

		/**********************************************************************
		 * toString()
		 *   This is an over-ride of the toString function. It prints the node 
		 *   in a way to make it more readable.
		 * 
		 *   This printing is for debugging purposes. If the tree needs to be 
		 *   displayed in a "prettier" way, use prettyPrint().
		**********************************************************************/
		@Override
		public String toString()
		{
			if ( Children.size() == 0 )
				return Value;
			
			StringBuffer out = new StringBuffer();			
			out.append(Value + "<");

			for ( int i = 0; i < Children.size() - 1; i++ )
				out.append((PTNode) Children.get(i) + ",");

			if ( Children.size() > 0 )
				out.append((PTNode) Children.get(Children.size() - 1));

			out.append(">");
			return out.toString();
		}

		/**********************************************************************
		 * equals()
		 *   This is an over-ride of the equals function. It tests that 
		 *   equivalence of the value at the node and the values of all the 
		 *   children nodes recursively. Two nodes are only equal if they and 
		 *   ALL of their childen are equal.
		 *********************************************************************/
		@Override
		public boolean equals(Object obj)
		{
			if ( obj == null )
				return false;
			
			if ( obj.getClass() != this.getClass() )
				return false;

			PTNode pt = (PTNode) obj;

			if ( this.Value.compareTo(pt.Value) != 0 )
				return false;
			
			if ( this.Children.size() != pt.Children.size() )
				return false;

			for ( int i = 0; i < this.Children.size(); i++ )
			{
				PTNode thisC = (PTNode) this.Children.get(i);
				PTNode ptC = (PTNode) pt.Children.get(i);
				if ( !thisC.equals(ptC) )
					return false;
			}

			return true;
		}
	}
}
