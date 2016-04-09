package julienl.androidgl.bao;

import android.util.Log;

import java.util.ArrayList;

import julienl.androidgl.geometry.Shape;
import julienl.androidgl.quadtree.QuadTree;

/**
 * Created by JulienL on 10/8/2015.
 */
public class BaoCirclePackingSwitch extends BaoCirclePacking {

	protected double mlastside;

    public BaoCirclePackingSwitch(ArrayList<Shape> boundaries, ArrayList<BaoNode> nodes, BaoPatternSwitch baopattern, double side, QuadTree quadtree) {
		super(boundaries,nodes,baopattern,side,quadtree);
		mlastside = 0.0;

	}

	public static BaoCirclePackingSwitch New(ArrayList<Shape> boundaries, ArrayList<BaoNode> nodes, BaoPatternSwitch baopattern, double side, QuadTree quadtree) {
		return new BaoCirclePackingSwitch(boundaries,nodes,baopattern,side,quadtree);
	}


	public void iter(int niter) {

        Log.v("BaoCirclePackingSwitch","nnodes" + mstack.nodes().size());

		for (int iiter = 0 ; iiter < niter; iiter++ ) {
			double newside = ((BaoPatternSwitch)mbaopattern.next()).side();
			if (!(newside == mlastside || mlastside == 0)) {
				mstack.switchstack();
				Log.v("BaoCirclePackingSwitch","switch stack " + newside);
			}

			ArrayList<BaoNode> lastseed = mstack.lastseed();
			BaoNode lastnode  = lastseed.get(0);
			BaoNode othernode = lastseed.get(1);
			double newr       = mbaopattern.next().radius();

			BaoNode newbaonode = null;
			newbaonode = computenextnode(mquadtree,lastnode,othernode,newr,mstack.newindex(),newside);
			if (newbaonode == null) {
				if (othernode != null) {
                    Log.v("BaoCirclePackingSwitch","user othernode ");
					othernode = mstack.nextothernode(othernode);
					newbaonode = computenextnode(mquadtree, lastnode, othernode, newr, mstack.newindex(), newside);
				}
				
				if (newbaonode == null) {
                    Log.v("BaoCirclePackingSwitch","user collidings ");
					ArrayList<BaoNode> collidings = BaoCirclePacking.findnewother( mquadtree, lastnode, mstack.excludednodes(othernode), newr);
					for (BaoNode cothernode: collidings) {
						othernode = cothernode;
						newbaonode = computenextnode(mquadtree,lastnode,cothernode,newr,mstack.newindex(),newside);
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
			
			mlastside = newside;
		}


	}
}
