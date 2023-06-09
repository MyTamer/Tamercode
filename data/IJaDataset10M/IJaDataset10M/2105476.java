package artofillusion.animation.distortion;

import artofillusion.math.*;
import artofillusion.procedural.*;
import artofillusion.object.*;

public class CustomDistortion implements Distortion {

    private Procedure proc;

    private int procVersion;

    PointInfo point;

    private double weight;

    private Mat4 preTransform, postTransform;

    private Distortion previous;

    public CustomDistortion(Procedure proc, int procVersion, PointInfo point, double weight, Mat4 preTransform, Mat4 postTransform) {
        this.proc = proc;
        this.procVersion = procVersion;
        this.point = point;
        this.weight = weight;
        this.preTransform = preTransform;
        this.postTransform = postTransform;
    }

    /** Set another distortion which should be applied before this one.
      This allows Distortions to be chained. */
    public void setPreviousDistortion(Distortion previous) {
        this.previous = previous;
    }

    /** Determine whether this distortion is identical to another one. */
    public boolean isIdenticalTo(Distortion d) {
        if (!(d instanceof CustomDistortion)) return false;
        CustomDistortion s = (CustomDistortion) d;
        if (previous != null && !previous.isIdenticalTo(s.previous)) return false;
        if (previous == null && s.previous != null) return false;
        if (weight != s.weight || proc != s.proc || procVersion != s.procVersion) return false;
        if (point.t != s.point.t) return false;
        if (point.param == null) {
            if (s.point.param != null) return false;
        } else {
            if (s.point.param == null || point.param.length != s.point.param.length) return false;
            for (int i = 0; i < point.param.length; i++) if (point.param[i] != s.point.param[i]) return false;
        }
        if (preTransform == s.preTransform && postTransform == s.postTransform) return true;
        return (preTransform != null && preTransform.equals(s.preTransform) && postTransform != null && postTransform.equals(s.postTransform));
    }

    /** Create a duplicate of this object. */
    public Distortion duplicate() {
        CustomDistortion d = new CustomDistortion(proc, procVersion, point, weight, preTransform, postTransform);
        if (previous != null) d.previous = previous.duplicate();
        return d;
    }

    /** Apply the Distortion, and return a transformed mesh. */
    public Mesh transform(Mesh obj) {
        if (previous != null) obj = previous.transform(obj);
        Mesh newmesh = (Mesh) obj.duplicate();
        MeshVertex[] vert = newmesh.getVertices();
        Vec3 newvert[] = new Vec3[vert.length];
        OutputModule output[] = proc.getOutputModules();
        double w2 = 1.0 - weight;
        for (int i = 0; i < newvert.length; i++) {
            newvert[i] = vert[i].r;
            if (preTransform != null) preTransform.transform(newvert[i]);
            point.x = newvert[i].x;
            point.y = newvert[i].y;
            point.z = newvert[i].z;
            proc.initForPoint(point);
            if (output[0].inputConnected(0)) newvert[i].x = weight * output[0].getAverageValue(0, 0.0) + w2 * newvert[i].x;
            if (output[1].inputConnected(0)) newvert[i].y = weight * output[1].getAverageValue(0, 0.0) + w2 * newvert[i].y;
            if (output[2].inputConnected(0)) newvert[i].z = weight * output[2].getAverageValue(0, 0.0) + w2 * newvert[i].z;
            if (postTransform != null) postTransform.transform(newvert[i]);
        }
        newmesh.setVertexPositions(newvert);
        return newmesh;
    }
}
