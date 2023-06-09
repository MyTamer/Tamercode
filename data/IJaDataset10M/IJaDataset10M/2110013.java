package javax.media.ding3d;

/**
 * The IndexedLineArray object draws the array of vertices as individual
 * line segments.  Each pair of vertices defines a line to be drawn.
 */
public class IndexedLineArray extends IndexedGeometryArray {

    /**
     * Package scoped default constructor.
     */
    IndexedLineArray() {
    }

    /**
     * Constructs an empty IndexedLineArray object using the specified
     * parameters.
     *
     * @param vertexCount
     * see {@link GeometryArray#GeometryArray(int,int)}
     * for a description of this parameter.
     *
     * @param vertexFormat
     * see {@link GeometryArray#GeometryArray(int,int)}
     * for a description of this parameter.
     *
     * @param indexCount
     * see {@link IndexedGeometryArray#IndexedGeometryArray(int,int,int)}
     * for a description of this parameter.
     *
     * @exception IllegalArgumentException if vertexCount is less than 1,
     * or indexCount is less than 2, or indexCount is <i>not</i>
     * a multiple of 2
     * ;<br>
     * See {@link GeometryArray#GeometryArray(int,int)}
     * for more exceptions that can be thrown
     */
    public IndexedLineArray(int vertexCount, int vertexFormat, int indexCount) {
        super(vertexCount, vertexFormat, indexCount);
        if (vertexCount < 1) throw new IllegalArgumentException(Ding3dI18N.getString("IndexedLineArray0"));
        if (indexCount < 2 || ((indexCount % 2) != 0)) throw new IllegalArgumentException(Ding3dI18N.getString("IndexedLineArray1"));
    }

    /**
     * Constructs an empty IndexedLineArray object using the specified
     * parameters.
     *
     * @param vertexCount
     * see {@link GeometryArray#GeometryArray(int,int,int,int[])}
     * for a description of this parameter.
     *
     * @param vertexFormat
     * see {@link GeometryArray#GeometryArray(int,int,int,int[])}
     * for a description of this parameter.
     *
     * @param texCoordSetCount
     * see {@link GeometryArray#GeometryArray(int,int,int,int[])}
     * for a description of this parameter.
     *
     * @param texCoordSetMap
     * see {@link GeometryArray#GeometryArray(int,int,int,int[])}
     * for a description of this parameter.
     *
     * @param indexCount
     * see {@link IndexedGeometryArray#IndexedGeometryArray(int,int,int,int[],int)}
     * for a description of this parameter.
     *
     * @exception IllegalArgumentException if vertexCount is less than 1,
     * or indexCount is less than 2, or indexCount is <i>not</i>
     * a multiple of 2
     * ;<br>
     * See {@link GeometryArray#GeometryArray(int,int,int,int[])}
     * for more exceptions that can be thrown
     *
     * @since Java 3D 1.2
     */
    public IndexedLineArray(int vertexCount, int vertexFormat, int texCoordSetCount, int[] texCoordSetMap, int indexCount) {
        super(vertexCount, vertexFormat, texCoordSetCount, texCoordSetMap, indexCount);
        if (vertexCount < 1) throw new IllegalArgumentException(Ding3dI18N.getString("IndexedLineArray0"));
        if (indexCount < 2 || ((indexCount % 2) != 0)) throw new IllegalArgumentException(Ding3dI18N.getString("IndexedLineArray1"));
    }

    /**
     * Constructs an empty IndexedLineArray object using the specified
     * parameters.
     *
     * @param vertexCount
     * see {@link GeometryArray#GeometryArray(int,int,int,int[],int,int[])}
     * for a description of this parameter.
     *
     * @param vertexFormat
     * see {@link GeometryArray#GeometryArray(int,int,int,int[],int,int[])}
     * for a description of this parameter.
     *
     * @param texCoordSetMap
     * see {@link GeometryArray#GeometryArray(int,int,int,int[],int,int[])}
     * for a description of this parameter.
     *
     * @param vertexAttrCount
     * see {@link GeometryArray#GeometryArray(int,int,int,int[],int,int[])}
     * for a description of this parameter.
     *
     * @param vertexAttrSizes
     * see {@link GeometryArray#GeometryArray(int,int,int,int[],int,int[])}
     * for a description of this parameter.
     *
     * @param indexCount
     * see {@link IndexedGeometryArray#IndexedGeometryArray(int,int,int,int[],int,int[],int)}
     * for a description of this parameter.
     *
     * @exception IllegalArgumentException if vertexCount is less than 1,
     * or indexCount is less than 2, or indexCount is <i>not</i>
     * a multiple of 2
     * ;<br>
     * See {@link GeometryArray#GeometryArray(int,int,int,int[],int,int[])}
     * for more exceptions that can be thrown
     *
     * @since Java 3D 1.4
     */
    public IndexedLineArray(int vertexCount, int vertexFormat, int texCoordSetCount, int[] texCoordSetMap, int vertexAttrCount, int[] vertexAttrSizes, int indexCount) {
        super(vertexCount, vertexFormat, texCoordSetCount, texCoordSetMap, vertexAttrCount, vertexAttrSizes, indexCount);
        if (vertexCount < 1) throw new IllegalArgumentException(Ding3dI18N.getString("IndexedLineArray0"));
        if (indexCount < 2 || ((indexCount % 2) != 0)) throw new IllegalArgumentException(Ding3dI18N.getString("IndexedLineArray1"));
    }

    /**
     * Creates the retained mode IndexedLineArrayRetained object that this
     * IndexedLineArray object will point to.
     */
    void createRetained() {
        this.retained = new IndexedLineArrayRetained();
        this.retained.setSource(this);
    }

    /**
     * @deprecated replaced with cloneNodeComponent(boolean forceDuplicate)
     */
    public NodeComponent cloneNodeComponent() {
        IndexedLineArrayRetained rt = (IndexedLineArrayRetained) retained;
        int texSetCount = rt.getTexCoordSetCount();
        int[] texMap = null;
        int vertexAttrCount = rt.getVertexAttrCount();
        int[] vertexAttrSizes = null;
        if (texSetCount > 0) {
            texMap = new int[rt.getTexCoordSetMapLength()];
            rt.getTexCoordSetMap(texMap);
        }
        if (vertexAttrCount > 0) {
            vertexAttrSizes = new int[vertexAttrCount];
            rt.getVertexAttrSizes(vertexAttrSizes);
        }
        IndexedLineArray l = new IndexedLineArray(rt.getVertexCount(), rt.getVertexFormat(), texSetCount, texMap, vertexAttrCount, vertexAttrSizes, rt.getIndexCount());
        l.duplicateNodeComponent(this);
        return l;
    }
}
