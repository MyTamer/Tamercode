package net.claribole.zvtm.layout.delaunay;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

/**
 * A 2D Delaunay Triangulation (DT) with incremental site insertion.
 * This is not the fastest way to build a DT, but it's a reasonable way
 * to build the DT incrementally and it makes a nice interactive display.
 * There are several O(n log n) methods, but they require that either (1) 
 * the sites are all known initially or (2) the sites are inserted in random
 * order.
 * 
 * @author Paul Chew
 * 
 * Created July 2005.  Derived from an earlier, messier version.
 */
public class DelaunayTriangulation extends Triangulation<Pnt> {

    private Simplex<Pnt> mostRecent = null;

    public boolean debug = false;

    /**
     * Constructor.
     * All sites must fall within the initial triangle.
     * @param triangle the initial triangle
     */
    public DelaunayTriangulation(Simplex<Pnt> triangle) {
        super(triangle);
        mostRecent = triangle;
    }

    /**
     * Locate the triangle with point (a Pnt) inside (or on) it.
     * @param point the Pnt to locate
     * @return triangle (Simplex<Pnt>) that holds the point; null if no such triangle
     */
    public Simplex<Pnt> locate(Pnt point) {
        Simplex<Pnt> triangle = mostRecent;
        if (!this.contains(triangle)) triangle = null;
        Set<Simplex<Pnt>> visited = new HashSet<Simplex<Pnt>>();
        while (triangle != null) {
            if (visited.contains(triangle)) {
                System.out.println("Warning: Caught in a locate loop");
                break;
            }
            visited.add(triangle);
            Pnt corner = point.isOutside(triangle.toArray(new Pnt[0]));
            if (corner == null) return triangle;
            triangle = this.neighborOpposite(corner, triangle);
        }
        System.out.println("Warning: Checking all triangles for " + point);
        for (Simplex<Pnt> tri : this) {
            if (point.isOutside(tri.toArray(new Pnt[0])) == null) return tri;
        }
        System.out.println("Warning: No triangle holds " + point);
        return null;
    }

    /**
     * Place a new point site into the DT.
     * @param site the new Pnt
     * @return set of all new triangles created
     */
    public Set<Simplex<Pnt>> delaunayPlace(Pnt site) {
        Set<Simplex<Pnt>> newTriangles = new HashSet<Simplex<Pnt>>();
        Set<Simplex<Pnt>> oldTriangles = new HashSet<Simplex<Pnt>>();
        Set<Simplex<Pnt>> doneSet = new HashSet<Simplex<Pnt>>();
        Queue<Simplex<Pnt>> waitingQ = new LinkedList<Simplex<Pnt>>();
        if (debug) System.out.println("Locate");
        Simplex<Pnt> triangle = locate(site);
        if (triangle == null || triangle.contains(site)) return newTriangles;
        if (debug) System.out.println("Cavity");
        waitingQ.add(triangle);
        while (!waitingQ.isEmpty()) {
            triangle = waitingQ.remove();
            if (site.vsCircumcircle(triangle.toArray(new Pnt[0])) == 1) continue;
            oldTriangles.add(triangle);
            for (Simplex<Pnt> tri : this.neighbors(triangle)) {
                if (doneSet.contains(tri)) continue;
                doneSet.add(tri);
                waitingQ.add(tri);
            }
        }
        if (debug) System.out.println("Create");
        for (Set<Pnt> facet : Simplex.boundary(oldTriangles)) {
            facet.add(site);
            newTriangles.add(new Simplex<Pnt>(facet));
        }
        if (debug) System.out.println("Update");
        this.update(oldTriangles, newTriangles);
        if (!newTriangles.isEmpty()) mostRecent = newTriangles.iterator().next();
        return newTriangles;
    }

    /**
     * Main program; used for testing.
     */
    public static void main(String[] args) {
        Simplex<Pnt> tri = new Simplex<Pnt>(new Pnt(-10, 10), new Pnt(10, 10), new Pnt(0, -10));
        System.out.println("Triangle created: " + tri);
        DelaunayTriangulation dt = new DelaunayTriangulation(tri);
        System.out.println("DelaunayTriangulation created: " + dt);
        dt.delaunayPlace(new Pnt(0, 0));
        dt.delaunayPlace(new Pnt(1, 0));
        dt.delaunayPlace(new Pnt(0, 1));
        System.out.println("After adding 3 points, the DelaunayTriangulation is a " + dt);
        dt.printStuff();
    }
}
