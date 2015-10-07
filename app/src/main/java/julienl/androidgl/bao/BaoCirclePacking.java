package julienl.androidgl.bao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import julienl.androidgl.geometry.Circle;
import julienl.androidgl.geometry.Shape;
import julienl.androidgl.quadtree.QuadTree;

/**
 * Created by JulienL on 10/7/2015.
 */
public class BaoCirclePacking {

	protected BaoStack mstack;
	protected int mlastindex;
	protected QuadTree mquadtree;
	protected BaoPattern mbaopattern;
	protected double mside;


	public BaoCirclePacking(ArrayList<Shape> boundaries, ArrayList<BaoNode> nodes, BaoPattern baopattern, double side, QuadTree quadtree) {
		mstack = new BaoStack(nodes);
		mlastindex = mstack.lastindex();
		mquadtree = quadtree == null ? new QuadTree() : quadtree;
		mbaopattern = baopattern;
		mside = side;
		if (boundaries != null) {
			mquadtree.adds( boundaries );
		}
		for (BaoNode node : nodes) {
			node.packing(this);
		}
	}

	public BaoNode computenextnode(QuadTree quadtree, BaoNode node2, BaoNode node1, double newr, int index, double iside) {
		Circle newcircle = Circle.circles2tangentout(node2,node1,newr,iside);
		if (newcircle != null && !quadtree.isColliding(newcircle)) {
			BaoNode newbaonode = new BaoNode(this,newcircle,node2.colorindex() + 1, index + 1);
			return newbaonode;
		}
		return null;
	}

	public static ArrayList<BaoNode> findnewother(QuadTree quadtree, BaoNode lastnode, ArrayList<BaoNode> excludednodes, double newr) {
		Circle bigcircle = lastnode.scale( (lastnode.r() + newr * 2.1) / lastnode.r() );
		ArrayList<Shape> scollidings = quadtree.collidings(bigcircle);
		ArrayList<BaoNode> bcollidings = new ArrayList<BaoNode>();
		for (Shape scol : scollidings) {
			if (scol != null && scol instanceof BaoNode) {
				BaoNode bcoll = (BaoNode)scol;
				if (bcoll.index() >= 0 ) {
					bcollidings.add(bcoll);
				}
			}
		}
		bcollidings.removeAll(excludednodes);
		
		Collections.sort(bcollidings, new Comparator<BaoNode>() {
			@Override
			public int compare(BaoNode node1, BaoNode node2) {
				return (new Integer(node1.index())).compareTo(new Integer(node2.index()));
			}
		});
		return bcollidings;
	}


	public void iter() {
		iter(1);
	}

	public void iter(int niter) {
		for (int iiter = 0; iiter < niter; iiter++) {
			if (iiter % 1000 == 0) {
				// System.println("iiter"+iiter);
			}

			ArrayList<BaoNode> lastseed = mstack.lastseed();
			BaoNode lastnode  = lastseed.get(0);
			BaoNode othernode = lastseed.get(1);
			double newr       = mbaopattern.next().radius();

			BaoNode newbaonode = null;
			newbaonode = computenextnode(mquadtree,lastnode,othernode,newr,mstack.newindex(),mside);
			if (newbaonode == null) {
				othernode = mstack.nextothernode(othernode);
				newbaonode = computenextnode(mquadtree,lastnode,othernode,newr,mstack.newindex(),mside);
				
				if (newbaonode == null) {
					ArrayList<BaoNode> collidings = BaoCirclePacking.findnewother( mquadtree, lastnode, mstack.excludednodes(othernode), newr);
					for (BaoNode cothernode: collidings) {
						othernode = cothernode;
						newbaonode = computenextnode(mquadtree,lastnode,cothernode,newr,mstack.newindex(),mside);
						if (newbaonode != null) {
							break;
						}
					}
				}
			}

			if (newbaonode == null) {
				lastnode.notfound(true);
				mlastindex = mstack.rewindtofreenode(mlastindex);
			} else {
				othernode.retouch(true);
				mquadtree.add(newbaonode);
				mbaopattern.draw(newbaonode,newbaonode.colorindex());
				mlastindex = mstack.stack(newbaonode,othernode);
			}

			if (mlastindex < 1) {
				break;
			}
		}
	}
}
