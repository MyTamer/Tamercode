package visad.bom;

import visad.*;
import visad.java3d.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.util.Enumeration;
import java.rmi.*;
import javax.media.j3d.*;

/**
   PointManipulationRendererJ3D is the VisAD class for direct
   manipulation of single points
*/
public class PointManipulationRendererJ3D extends DirectManipulationRendererJ3D {

    private RealType x = null;

    private RealType y = null;

    private RealTupleType xy = null;

    private int mouseModifiersMask = 0;

    private int mouseModifiersValue = 0;

    private BranchGroup branch = null;

    private BranchGroup group = null;

    /** this DirectManipulationRenderer is quite different - it does not
      render its data, but only place values into its DataReference
      on right mouse button press;
      it uses xarg and yarg to determine spatial ScalarMaps */
    public PointManipulationRendererJ3D(RealType xarg, RealType yarg) {
        this(xarg, yarg, 0, 0);
    }

    /** xarg and yarg determine spatial ScalarMaps;
      mmm and mmv determine whehter SHIFT or CTRL keys are required -
      this is needed since this is a greedy DirectManipulationRenderer
      that will grab any right mouse click (that intersects its 2-D
      sub-manifold)
      @arg mmm - "Mouse Modifier Mask", matches the modifiers we want plus all that we don't want
      @arg mmv - "Mouse Modifier Value", equals the subset of mask that we want to match
 */
    public PointManipulationRendererJ3D(RealType xarg, RealType yarg, int mmm, int mmv) {
        super();
        x = xarg;
        y = yarg;
        mouseModifiersMask = mmm;
        mouseModifiersValue = mmv;
    }

    /** don't render - just return BranchGroup for scene graph to
      render rectangle into */
    public synchronized BranchGroup doTransform() throws VisADException, RemoteException {
        branch = new BranchGroup();
        branch.setCapability(BranchGroup.ALLOW_DETACH);
        branch.setCapability(Group.ALLOW_CHILDREN_READ);
        branch.setCapability(Group.ALLOW_CHILDREN_WRITE);
        branch.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        if (!getIsDirectManipulation()) {
            throw new BadDirectManipulationException(getWhyNotDirect() + ": DirectManipulationRendererJ3D.doTransform");
        }
        setBranch(branch);
        return branch;
    }

    /** for use in drag_direct */
    private transient DataDisplayLink link = null;

    private transient DataReference ref = null;

    private transient ScalarMap xmap = null;

    private transient ScalarMap ymap = null;

    float[] default_values;

    /** arrays of length one for inverseScaleValues */
    private float[] f = new float[1];

    private float[] d = new float[1];

    private float[] value = new float[2];

    /** explanation for invalid use of DirectManipulationRenderer */
    private String whyNotDirect = null;

    /** dimension of direct manipulation
      (always 2 for PointManipulationRendererJ3D) */
    private int directManifoldDimension = 2;

    /** spatial DisplayTupleType other than
      DisplaySpatialCartesianTuple */
    private DisplayTupleType tuple;

    private CoordinateSystem tuplecs;

    private int xindex = -1;

    private int yindex = -1;

    private int otherindex = -1;

    private float othervalue;

    private byte red, green, blue;

    private float[][] first_x;

    private float[][] last_x;

    private float[][] clast_x;

    private float cum_lon;

    /** possible values for whyNotDirect */
    private static final String xandyNotMatch = "x and y spatial domains don't match";

    private static final String xandyNotSpatial = "x and y must be mapped to spatial";

    private boolean stop = false;

    public void checkDirect() throws VisADException, RemoteException {
        setIsDirectManipulation(false);
        DisplayImpl display = getDisplay();
        DataDisplayLink[] Links = getLinks();
        if (Links == null || Links.length == 0) {
            link = null;
            return;
        }
        link = Links[0];
        ref = link.getDataReference();
        default_values = link.getDefaultValues();
        xmap = null;
        ymap = null;
        Vector scalar_map_vector = display.getMapVector();
        Enumeration en = scalar_map_vector.elements();
        while (en.hasMoreElements()) {
            ScalarMap map = (ScalarMap) en.nextElement();
            ScalarType real = map.getScalar();
            if (real.equals(x)) {
                DisplayRealType dreal = map.getDisplayScalar();
                DisplayTupleType t = dreal.getTuple();
                if (t != null && (t.equals(Display.DisplaySpatialCartesianTuple) || (t.getCoordinateSystem() != null && t.getCoordinateSystem().getReference().equals(Display.DisplaySpatialCartesianTuple)))) {
                    xmap = map;
                    xindex = dreal.getTupleIndex();
                    if (tuple == null) {
                        tuple = t;
                    } else if (!t.equals(tuple)) {
                        whyNotDirect = xandyNotMatch;
                        return;
                    }
                }
            }
            if (real.equals(y)) {
                DisplayRealType dreal = map.getDisplayScalar();
                DisplayTupleType t = dreal.getTuple();
                if (t != null && (t.equals(Display.DisplaySpatialCartesianTuple) || (t.getCoordinateSystem() != null && t.getCoordinateSystem().getReference().equals(Display.DisplaySpatialCartesianTuple)))) {
                    ymap = map;
                    yindex = dreal.getTupleIndex();
                    if (tuple == null) {
                        tuple = t;
                    } else if (!t.equals(tuple)) {
                        whyNotDirect = xandyNotMatch;
                        return;
                    }
                }
            }
        }
        if (xmap == null || ymap == null) {
            whyNotDirect = xandyNotSpatial;
            return;
        }
        xy = new RealTupleType(x, y);
        otherindex = 3 - (xindex + yindex);
        DisplayRealType dreal = (DisplayRealType) tuple.getComponent(otherindex);
        int index = getDisplay().getDisplayScalarIndex(dreal);
        othervalue = (index > 0) ? default_values[index] : (float) dreal.getDefaultValue();
        if (Display.DisplaySpatialCartesianTuple.equals(tuple)) {
            tuple = null;
            tuplecs = null;
        } else {
            tuplecs = tuple.getCoordinateSystem();
        }
        directManifoldDimension = 2;
        setIsDirectManipulation(true);
    }

    private int getDirectManifoldDimension() {
        return directManifoldDimension;
    }

    public String getWhyNotDirect() {
        return whyNotDirect;
    }

    public void addPoint(float[] x) throws VisADException {
    }

    public CoordinateSystem getDisplayCoordinateSystem() {
        return tuplecs;
    }

    /** set spatialValues from ShadowType.doTransform */
    public synchronized void setSpatialValues(float[][] spatial_values) {
    }

    /** check if ray intersects sub-manifold
      @return float, 0 - hit, Float.MAX_VALUE - no hit
  */
    public synchronized float checkClose(double[] origin, double[] direction) {
        int mouseModifiers = getLastMouseModifiers();
        if ((mouseModifiers & mouseModifiersMask) != mouseModifiersValue) {
            return Float.MAX_VALUE;
        }
        try {
            float r = findRayManifoldIntersection(true, origin, direction, tuple, otherindex, othervalue);
            if (r == r) {
                return 0.0f;
            } else {
                return Float.MAX_VALUE;
            }
        } catch (VisADException ex) {
            return Float.MAX_VALUE;
        }
    }

    public void stop_direct() {
        stop = true;
    }

    private static final int EDGE = 20;

    private static final float EPS = 0.005f;

    public synchronized void drag_direct(VisADRay ray, boolean first, int mouseModifiers) {
        if (ref == null) return;
        if (!first) return;
        double[] origin = ray.position;
        double[] direction = ray.vector;
        try {
            float r = findRayManifoldIntersection(true, origin, direction, tuple, otherindex, othervalue);
            if (r != r) {
                if (group != null) group.detach();
                return;
            }
            float[][] xx = { { (float) (origin[0] + r * direction[0]) }, { (float) (origin[1] + r * direction[1]) }, { (float) (origin[2] + r * direction[2]) } };
            if (tuple != null) xx = tuplecs.fromReference(xx);
            first_x = xx;
            cum_lon = 0.0f;
            clast_x = xx;
            Vector vect = new Vector();
            f[0] = xx[xindex][0];
            d = xmap.inverseScaleValues(f);
            Real rr = new Real(x, d[0]);
            Unit overrideUnit = xmap.getOverrideUnit();
            Unit rtunit = x.getDefaultUnit();
            if (overrideUnit != null && !overrideUnit.equals(rtunit) && !RealType.Time.equals(x)) {
                double dval = overrideUnit.toThis((double) d[0], rtunit);
                rr = new Real(x, dval, overrideUnit);
            }
            String valueString = rr.toValueString();
            vect.addElement(x.getName() + " = " + valueString);
            f[0] = xx[yindex][0];
            d = ymap.inverseScaleValues(f);
            rr = new Real(y, d[0]);
            overrideUnit = ymap.getOverrideUnit();
            rtunit = y.getDefaultUnit();
            if (overrideUnit != null && !overrideUnit.equals(rtunit) && !RealType.Time.equals(y)) {
                double dval = overrideUnit.toThis((double) d[0], rtunit);
                rr = new Real(y, dval, overrideUnit);
            }
            valueString = rr.toValueString();
            valueString = new Real(y, d[0]).toValueString();
            vect.addElement(y.getName() + " = " + valueString);
            double[] dd = new double[2];
            f[0] = first_x[xindex][0];
            d = xmap.inverseScaleValues(f);
            dd[0] = d[0];
            f[0] = first_x[yindex][0];
            d = ymap.inverseScaleValues(f);
            dd[1] = d[0];
            RealTuple rt = new RealTuple(xy, dd);
            ref.setData(rt);
        } catch (VisADException e) {
            System.out.println("drag_direct " + e);
            e.printStackTrace();
        } catch (RemoteException e) {
            System.out.println("drag_direct " + e);
            e.printStackTrace();
        }
    }

    public Object clone() {
        return new PointManipulationRendererJ3D(x, y, mouseModifiersMask, mouseModifiersValue);
    }

    private static final int N = 64;

    /** test PointManipulationRendererJ3D */
    public static void main(String args[]) throws VisADException, RemoteException {
        RealType x = RealType.getRealType("x");
        RealType y = RealType.getRealType("y");
        RealTupleType xy = new RealTupleType(x, y);
        RealType c = RealType.getRealType("c");
        FunctionType ft = new FunctionType(xy, c);
        DisplayImpl display = new DisplayImplJ3D("display1");
        if (args.length == 0 || args[0].equals("z")) {
            display.addMap(new ScalarMap(x, Display.XAxis));
            display.addMap(new ScalarMap(y, Display.YAxis));
        } else if (args[0].equals("x")) {
            display.addMap(new ScalarMap(x, Display.YAxis));
            display.addMap(new ScalarMap(y, Display.ZAxis));
        } else if (args[0].equals("y")) {
            display.addMap(new ScalarMap(x, Display.XAxis));
            display.addMap(new ScalarMap(y, Display.ZAxis));
        } else if (args[0].equals("radius")) {
            display.addMap(new ScalarMap(x, Display.Longitude));
            display.addMap(new ScalarMap(y, Display.Latitude));
        } else if (args[0].equals("lat")) {
            display.addMap(new ScalarMap(x, Display.Longitude));
            display.addMap(new ScalarMap(y, Display.Radius));
        } else if (args[0].equals("lon")) {
            display.addMap(new ScalarMap(x, Display.Latitude));
            display.addMap(new ScalarMap(y, Display.Radius));
        } else {
            display.addMap(new ScalarMap(x, Display.Longitude));
            display.addMap(new ScalarMap(y, Display.Latitude));
        }
        display.addMap(new ScalarMap(c, Display.RGB));
        Integer2DSet fset = new Integer2DSet(xy, N, N);
        FlatField field = new FlatField(ft, fset);
        float[][] values = new float[1][N * N];
        int k = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                values[0][k++] = (i - N / 2) * (j - N / 2);
            }
        }
        field.setSamples(values);
        DataReferenceImpl field_ref = new DataReferenceImpl("field");
        field_ref.setData(field);
        display.addReference(field_ref);
        RealTuple dummy_rt = new RealTuple(xy, new double[] { Double.NaN, Double.NaN });
        final DataReferenceImpl ref = new DataReferenceImpl("rt");
        ref.setData(dummy_rt);
        int m = (args.length > 1) ? InputEvent.CTRL_MASK : 0;
        display.addReferences(new PointManipulationRendererJ3D(x, y, m, m), ref);
        CellImpl cell = new CellImpl() {

            public void doAction() throws VisADException, RemoteException {
                RealTuple rt = (RealTuple) ref.getData();
                double dx = ((Real) rt.getComponent(0)).getValue();
                double dy = ((Real) rt.getComponent(1)).getValue();
                if (dx == dx && dy == dy) {
                    System.out.println("point (" + dx + ", " + dy + ")");
                }
            }
        };
        cell.addReference(ref);
        JFrame frame = new JFrame("test PointManipulationRendererJ3D");
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentY(JPanel.TOP_ALIGNMENT);
        panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        frame.getContentPane().add(panel);
        panel.add(display.getComponent());
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
