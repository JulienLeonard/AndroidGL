package julienl.androidgl.bao;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by JulienL on 10/7/2015.
 */
public class BaoStack {

	protected ArrayList<BaoNode> mnodes;
	protected BaoNode mlastnode;
	protected BaoNode mlastlastnode;
	protected BaoNode mothernode;
	protected BaoNode mlast3node;
	protected double mprevdirection;

	public BaoStack(ArrayList<BaoNode> nodes) {
		mnodes = nodes;
		mlastnode      = nodes.get(nodes.size()-1);
		mlastlastnode  = nodes.get(nodes.size()-2);
		mothernode     = nodes.get(nodes.size()-2);
		mlast3node     = null;
		mprevdirection = -1.0;
	}

	public void set(ArrayList<BaoNode> nodes, int lastindex) {
		mlastnode     = mprevdirection > 0.0 ? nodes.get(lastindex)   : nodes.get(lastindex);
		mlastlastnode = mprevdirection > 0.0 ? nodes.get(lastindex-1) : nodes.get(lastindex+1);
		mothernode    = mlastlastnode;
		mlast3node    = null;
	}

	public int lastindex() {
		return mnodes.size() -1;
	}

	public int stack(BaoNode newnode, BaoNode othernode) {
		mnodes.add(newnode);
		mlast3node    = mlastlastnode;
		mlastlastnode = mlastnode;
		mlastnode     = newnode;
		mothernode    = othernode;
		return lastindex();
	}

	public ArrayList<BaoNode> lastnodes() {
		ArrayList<BaoNode> result = new ArrayList<BaoNode>();
		result.add(mlastnode);
		result.add(mlastlastnode);
		result.add(mlast3node);
		return result;
	}

	public ArrayList<BaoNode> lastseed() {
		ArrayList<BaoNode> result = new ArrayList<BaoNode>();
		result.add(mlastnode);
		result.add(mothernode);
		return result;
	}

	public ArrayList<BaoNode> excludednodes(BaoNode othernode) {
		ArrayList<BaoNode> result = new ArrayList<BaoNode>();
		result.addAll(lastnodes());
		result.add(othernode);
		return result;
	}

	public Pair<BaoNode,Integer> findlastfreenode(ArrayList<BaoNode> nodes, int lastindex) {
		BaoNode freenode = nodes.get(lastindex);
		while ((freenode.retouch() || freenode.notfound()) && lastindex > 0) {
			lastindex += -1;
			freenode = nodes.get(lastindex);
		}
		Pair<BaoNode,Integer> result = Pair.create(freenode,lastindex);
		return result;
	}

	public int rewindtofreenode(int lastindex) {
		Pair<BaoNode,Integer> freenodelastindex = findlastfreenode(mnodes,lastindex);
		int newlastindex = freenodelastindex.second;
		set(mnodes,newlastindex);
		return newlastindex;
	}

	public ArrayList<BaoNode> nodes() {
		return mnodes;
	}

	public int newindex() {
		return mnodes.size();
	}

	public BaoNode node(int index) {
		for (BaoNode node: mnodes) {
			if (node.index() == index) {
				return node;
			}
		}
		return null;
	}

	public BaoNode nextothernode(BaoNode othernode) {
		int nodeindex = mprevdirection > 0.0 ? othernode.index() + 1 : othernode.index() - 1;
		return node(nodeindex);
	}

	public void switchstack() {
		mlastnode      = mlastnode;
		mothernode     = mlastlastnode;
		mlastlastnode  = null;
		mlast3node     = null;
		mprevdirection = -mprevdirection;
	}
}
