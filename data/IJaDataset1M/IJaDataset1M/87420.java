package artofillusion.object;

import artofillusion.*;
import artofillusion.animation.*;
import artofillusion.math.*;
import artofillusion.ui.*;
import buoy.event.*;
import buoy.widget.*;
import java.io.*;

/** DirectionalLight represents a distant light source which emits light in one direction
    from outside the scene. */
public class DirectionalLight extends Light {

    static BoundingBox bounds;

    static WireframeMesh mesh;

    static final int SEGMENTS = 8;

    static {
        double sine[] = new double[SEGMENTS];
        double cosine[] = new double[SEGMENTS];
        Vec3 vert[];
        int i, from[], to[];
        bounds = new BoundingBox(-0.15, 0.15, -0.15, 0.15, -0.15, 0.25);
        for (i = 0; i < SEGMENTS; i++) {
            sine[i] = Math.sin(i * 2.0 * Math.PI / SEGMENTS);
            cosine[i] = Math.cos(i * 2.0 * Math.PI / SEGMENTS);
        }
        vert = new Vec3[SEGMENTS * 4];
        from = new int[SEGMENTS * 4];
        to = new int[SEGMENTS * 4];
        for (i = 0; i < SEGMENTS; i++) {
            vert[i] = new Vec3(0.15 * cosine[i], 0.15 * sine[i], -0.15);
            vert[i + SEGMENTS] = new Vec3(0.15 * cosine[i], 0.15 * sine[i], 0.0);
            vert[i + 2 * SEGMENTS] = new Vec3(0.15 * cosine[i], 0.15 * sine[i], 0.05);
            vert[i + 3 * SEGMENTS] = new Vec3(0.15 * cosine[i], 0.15 * sine[i], 0.25);
            from[i] = i;
            to[i] = (i + 1) % SEGMENTS;
            from[i + SEGMENTS] = i;
            to[i + SEGMENTS] = i + SEGMENTS;
            from[i + 2 * SEGMENTS] = i + SEGMENTS;
            to[i + 2 * SEGMENTS] = (i + 1) % SEGMENTS + SEGMENTS;
            from[i + 3 * SEGMENTS] = i + 2 * SEGMENTS;
            to[i + 3 * SEGMENTS] = i + 3 * SEGMENTS;
        }
        mesh = new WireframeMesh(vert, from, to);
    }

    public DirectionalLight(RGBColor theColor, float theIntensity) {
        setParameters(theColor.duplicate(), theIntensity, false, 0.5f);
    }

    public Object3D duplicate() {
        return new DirectionalLight(color, intensity);
    }

    public void copyObject(Object3D obj) {
        DirectionalLight lt = (DirectionalLight) obj;
        setParameters(lt.color.duplicate(), lt.intensity, lt.ambient, lt.decayRate);
    }

    public BoundingBox getBounds() {
        return bounds;
    }

    public void setSize(double xsize, double ysize, double zsize) {
    }

    public void getLight(RGBColor light, float distance) {
        light.copy(color);
        light.scale(intensity);
    }

    public boolean canSetTexture() {
        return false;
    }

    public WireframeMesh getWireframeMesh() {
        return mesh;
    }

    public boolean isEditable() {
        return true;
    }

    public DirectionalLight(DataInputStream in, Scene theScene) throws IOException, InvalidObjectException {
        super(in, theScene);
        short version = in.readShort();
        if (version != 0) throw new InvalidObjectException("");
        setParameters(new RGBColor(in), in.readFloat(), false, 0.0f);
        bounds = new BoundingBox(-0.15, 0.15, -0.15, 0.15, -0.15, 0.25);
    }

    public void writeToFile(DataOutputStream out, Scene theScene) throws IOException {
        super.writeToFile(out, theScene);
        out.writeShort(0);
        color.writeToFile(out);
        out.writeFloat(intensity);
    }

    public void edit(EditingWindow parent, ObjectInfo info, Runnable cb) {
        final Widget patch = color.getSample(50, 30);
        ValueField intensityField = new ValueField(intensity, ValueField.NONE);
        RGBColor oldColor = color.duplicate();
        final BFrame parentFrame = parent.getFrame();
        patch.addEventLink(MouseClickedEvent.class, new Object() {

            void processEvent() {
                new ColorChooser(parentFrame, Translate.text("lightColor"), color);
                patch.setBackground(color.getColor());
            }
        });
        ComponentsDialog dlg = new ComponentsDialog(parentFrame, Translate.text("editDirectionalLightTitle"), new Widget[] { patch, intensityField }, new String[] { Translate.text("Color"), Translate.text("Intensity") });
        if (!dlg.clickedOk()) {
            color.copy(oldColor);
            return;
        }
        setParameters(color, (float) intensityField.getValue(), ambient, decayRate);
        cb.run();
    }

    public Keyframe getPoseKeyframe() {
        return new DirectionalLightKeyframe(color, intensity);
    }

    public void applyPoseKeyframe(Keyframe k) {
        DirectionalLightKeyframe key = (DirectionalLightKeyframe) k;
        setParameters(key.color.duplicate(), key.intensity, ambient, 0.5f);
    }

    /** This will be called whenever a new pose track is created for this object.  It allows
      the object to configure the track by setting its graphable values, subtracks, etc. */
    public void configurePoseTrack(PoseTrack track) {
        track.setGraphableValues(new String[] { "Intensity", "Decay Rate" }, new double[] { intensity, decayRate }, new double[][] { { -Double.MAX_VALUE, Double.MAX_VALUE }, { 0.0, Double.MAX_VALUE } });
    }

    public void editKeyframe(EditingWindow parent, Keyframe k, ObjectInfo info) {
        final DirectionalLightKeyframe key = (DirectionalLightKeyframe) k;
        final Widget patch = key.color.getSample(50, 30);
        ValueField intensityField = new ValueField(key.intensity, ValueField.NONE);
        RGBColor oldColor = key.color.duplicate();
        final BFrame parentFrame = parent.getFrame();
        patch.addEventLink(MouseClickedEvent.class, new Object() {

            void processEvent() {
                new ColorChooser(parentFrame, Translate.text("lightColor"), key.color);
                patch.setBackground(key.color.getColor());
            }
        });
        ComponentsDialog dlg = new ComponentsDialog(parentFrame, Translate.text("editDirectionalLightTitle"), new Widget[] { patch, intensityField }, new String[] { Translate.text("Color"), Translate.text("Intensity") });
        if (!dlg.clickedOk()) {
            key.color.copy(oldColor);
            return;
        }
        key.intensity = (float) intensityField.getValue();
    }

    public static class DirectionalLightKeyframe implements Keyframe {

        public RGBColor color;

        public float intensity;

        public DirectionalLightKeyframe(RGBColor color, float intensity) {
            this.color = color.duplicate();
            this.intensity = intensity;
        }

        public Keyframe duplicate() {
            return new DirectionalLightKeyframe(color, intensity);
        }

        public Keyframe duplicate(Object owner) {
            return duplicate();
        }

        public double[] getGraphValues() {
            return new double[] { intensity };
        }

        public void setGraphValues(double values[]) {
            intensity = (float) values[0];
        }

        public Keyframe blend(Keyframe o2, double weight1, double weight2) {
            DirectionalLightKeyframe k2 = (DirectionalLightKeyframe) o2;
            RGBColor c = new RGBColor(weight1 * color.getRed() + weight2 * k2.color.getRed(), weight1 * color.getGreen() + weight2 * k2.color.getGreen(), weight1 * color.getBlue() + weight2 * k2.color.getBlue());
            return new DirectionalLightKeyframe(c, (float) (weight1 * intensity + weight2 * k2.intensity));
        }

        public Keyframe blend(Keyframe o2, Keyframe o3, double weight1, double weight2, double weight3) {
            DirectionalLightKeyframe k2 = (DirectionalLightKeyframe) o2, k3 = (DirectionalLightKeyframe) o3;
            RGBColor c = new RGBColor(weight1 * color.getRed() + weight2 * k2.color.getRed() + weight3 * k3.color.getRed(), weight1 * color.getGreen() + weight2 * k2.color.getGreen() + weight3 * k3.color.getGreen(), weight1 * color.getBlue() + weight2 * k2.color.getBlue() + weight3 * k3.color.getBlue());
            return new DirectionalLightKeyframe(c, (float) (weight1 * intensity + weight2 * k2.intensity + weight3 * k3.intensity));
        }

        public Keyframe blend(Keyframe o2, Keyframe o3, Keyframe o4, double weight1, double weight2, double weight3, double weight4) {
            DirectionalLightKeyframe k2 = (DirectionalLightKeyframe) o2, k3 = (DirectionalLightKeyframe) o3, k4 = (DirectionalLightKeyframe) o4;
            RGBColor c = new RGBColor(weight1 * color.getRed() + weight2 * k2.color.getRed() + weight3 * k3.color.getRed() + weight4 * k4.color.getRed(), weight1 * color.getGreen() + weight2 * k2.color.getGreen() + weight3 * k3.color.getGreen() + weight4 * k4.color.getGreen(), weight1 * color.getBlue() + weight2 * k2.color.getBlue() + weight3 * k3.color.getBlue() + weight4 * k4.color.getBlue());
            return new DirectionalLightKeyframe(c, (float) (weight1 * intensity + weight2 * k2.intensity + weight3 * k3.intensity + weight4 * k4.intensity));
        }

        public boolean equals(Keyframe k) {
            if (!(k instanceof DirectionalLightKeyframe)) return false;
            DirectionalLightKeyframe key = (DirectionalLightKeyframe) k;
            return (key.color.equals(color) && key.intensity == intensity);
        }

        public void writeToStream(DataOutputStream out) throws IOException {
            color.writeToFile(out);
            out.writeFloat(intensity);
            out.writeFloat(0.0f);
        }

        public DirectionalLightKeyframe(DataInputStream in, Object parent) throws IOException {
            this(new RGBColor(in), in.readFloat());
            in.readFloat();
        }
    }
}
