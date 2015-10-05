package julienl.androidgl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JulienL on 10/5/2015.
 */
public class Quad {

	private BBox mbbox;
	private Quad[] msubquads;
	private HashMap<Integer,ArrayList<Shape>> mshapemap;
	private int mmaxshapenumber;

	public Quad(BBox bbox) {
		mbbox = bbox;
		msubquads = null;
		mshapemap = new HashMap<Integer,ArrayList<Shape>>();
		mmaxshapenumber = 10;
	}

	public Shape[] shapes() {
		ArrayList<Shape> result = new ArrayList();
		if (msubquads != null) {
            for (Quad subquad:msubquads) {
                for (Shape subshape: subquad.shapes()) {
                    result.add(subshape);
                }
            }
		}

        for (ArrayList<Shape> shapes: mshapemap.values()) {
            for (Shape shape: shapes) {
                result.add(shape);
            }
        }

        // TODO: to test
        Shape[] typeresult = new Shape[1];
        return result.toArray(typeresult);
	}

	public Boolean intersect(Shape shape) {
		return mbbox.intersect(shape.bbox());
	}

	public void add(Shape shape, int push) {
		if (intersect(shape)) {
			mbbox = BBox.bbunion(mbbox,shape.bbox());
			insert(shape,push);
		} else {
			addwithoutcheck(shape,push);
		}
	}

	public void pop(int push) {
		// TODO
	}

	public void addwithoutcheck(Shape shape, int push) {
		if (msubquads == null) {
			insert(shape,push);
		} else {
			dispatch(shape, push);
		}
	}

	public void dispatch(Shape shape, int push) {
		if (msubquads != null) {
			for (Quad subquad: msubquads) {
				if (subquad.intersect(shape)) {
					subquad.addwithoutcheck(shape,push);
				}
			}
		}
	}

	public int nshapes() {
		int result = 0;
		for (ArrayList<Shape> shapes : mshapemap.values()) {
			result += shapes.size();
		}
		return result;
	}
	
	public Shape[] ownshapes() {
		ArrayList<Shape> result = new ArrayList<Shape>();
        for (ArrayList<Shape> shapes: mshapemap.values()) {
            for (Shape shape: shapes) {
                result.add(shape);
            }
        }

        Shape[] typeresult = new Shape[1];
		return result.toArray(typeresult);
	}

	public void insert(Shape shape, int push) {
		if (!mshapemap.containsKey(push)) {
			mshapemap.put(push, new ArrayList<Shape>());
		}
		
		for (Shape c: ownshapes()) {
			if (shape.intersect(c)) {
				mmaxshapenumber += 1;
			}
		}

		mshapemap.get(push).add(shape);

		if (nshapes() > mmaxshapenumber) {
			split();
		}
	}

	public void split() {
		msubquads = new Quad[4];
		BBox[] subbboxes = mbbox.split4();
		for (int i = 0; i < subbboxes.length; i++) {
			msubquads[i] = new Quad(subbboxes[i]);
		}

		for (int push: mshapemap.keySet()) {
			for (Shape shape : mshapemap.get(push)) {
				dispatch(shape,push);
			}
		}

		mshapemap.clear();
	}
    
	public ArrayList<Shape> mayintersect(Shape newshape) {
		ArrayList<Shape> result = new ArrayList<Shape>();
		if (intersect(newshape)) {
			if (msubquads != null) {
				for (Quad subquad : msubquads) {
					if (subquad.intersect(newshape)) {
						ArrayList<Shape> subresult = subquad.mayintersect(newshape);
						for (Shape shape : subresult) {
							result.add(shape);
						}
					}
				}
			} else {
				for (int push : mshapemap.keySet()) {
					for (Shape shape : mshapemap.get(push)) {
						result.add(shape);
					}
				}
			}
		}
		return result;
	}

	public BBox bbox() {
		return mbbox;
	}
}
