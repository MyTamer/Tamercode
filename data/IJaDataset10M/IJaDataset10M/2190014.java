package org.j3d.geom;

import org.j3d.util.HashSet;
import org.j3d.util.ObjectArray;

/**
 * Utility routines for triangulating arbitrary polygons.
 * <p>
 * The concave triangulation routine is designed for small numbers of vertices
 * as it uses a much simpler, but slower algorithm that is O(kn) where k is the
 * number of concave vertices, rather than the more efficient, but much more
 * complex to implement Seidel's Algorithm. A summary of the implementation can
 * be found
 * <a href="http://www.mema.ucl.ac.be/~wu/FSA2716-2002/project.html">here</a>.
 * <p>
 * If at any time an error is detected in the input geometry, the return value
 * of the triangulation methods will be a negative value. The number will still
 * indicate the number of triangles successfully created for the return, but the
 * negative is used to indicate an error occurred that could not allow for any
 * more triangulation to take place.
 * <p>
 *
 * Seidel's algorithm is described here:
 * http://www.cs.unc.edu/~dm/CODE/GEM/chapter.html
 *
 * @author Justin Couch
 * @version $Revision: 1.3 $
 */
class EarCutTriangulator {

    /** The default size of the polygon values */
    private static final int DEFAULT_POLY_SIZE = 6;

    /** Cache of polygon vertex structures for efficiency */
    private static ObjectArray vertexCache;

    /** Set of concave vertices for this polygon */
    private HashSet concaveVertices;

    /** Normal of the face being processed now */
    private float[] faceNormal;

    /** The current 2D coordinate list that we work from */
    private float[] working2dCoords;

    /** Array for reading out the concave vertices from the hashset */
    private PolyVertex[] tmpArray;

    /**
     * Static construct.
     */
    static {
        vertexCache = new ObjectArray();
    }

    /**
     * Construct a new instance of the triangulation utilities. Assumes a
     * default max polygon size of 6 vertices.
     */
    EarCutTriangulator() {
        this(DEFAULT_POLY_SIZE);
    }

    /**
     * Construct a new instance of the triangulation utilities with a given
     * maximum polygon size hint. A number of internal structures are created
     * and this hint is used to ensure that the structures are large enough and
     * don't need to be dynamically re-created during the triangulation
     * process.
     *
     * @param size Hint to the maximum size of polygon to deal with
     */
    EarCutTriangulator(int size) {
        concaveVertices = new HashSet(size);
        faceNormal = new float[3];
        working2dCoords = new float[6];
        tmpArray = new PolyVertex[size];
    }

    /**
     * Triangulate a concave polygon using an indexed coordinates array. The
     * start index is the index into the indexArray for the first vertex of the
     * polygon. The coordinates are not required to be a closed polygon as the
     * algorithm will automatically close it. The output array is the new indexes
     * to use
     * <p>
     * If an error occurs, the result will be negative number of triangles.
     *
     * @param coords The coordinates of the face
     * @param startIndex The index of the first coordinate in the face
     * @param numVertex  The number of vertices to read from the list
     * @param firstNormalIndex The first position of the normalIndex array
     * @param normalIndex The index of normals for each coordinate
     * @param firstColorIndex The first position of the colorIndex array
     * @param colorIndex The index of color for each coordinate
     * @param firstTexCoordIndex The first position of the texCoordIndex array
     * @param texCoordIndex The index of textureCoordinates for each coordinate
     * @param coordOutput The array to copy the coord index values to
     * @param normalOutput The array to copy the normal index values to
     * @param colorOutput The array to copy the color index values to
     * @param texCoordOutput The array to copy the texCoord index values to
     * @param normal The normal to this face these vertices are a part of
     * @return The number of triangles in the output array
     */
    public int triangulateConcavePolygon(float[] coords, int startIndex, int numVertex, int[] coordIndex, int firstNormalIndex, int[] normalIndex, int firstColorIndex, int[] colorIndex, int firstTexCoordIndex, int[] texCoordIndex, int[] coordOutput, int[] normalOutput, int[] colorOutput, int[] texCoordOutput, float[] normal) {
        if (numVertex < 3) return 0; else if (numVertex == 3) {
            coordOutput[0] = coordIndex[startIndex];
            coordOutput[1] = coordIndex[startIndex + 1];
            coordOutput[2] = coordIndex[startIndex + 2];
            if (normalOutput != null) {
                normalOutput[0] = normalIndex[firstNormalIndex];
                normalOutput[1] = normalIndex[firstNormalIndex + 1];
                normalOutput[2] = normalIndex[firstNormalIndex + 2];
            }
            if (colorOutput != null) {
                colorOutput[0] = colorIndex[firstColorIndex];
                colorOutput[1] = colorIndex[firstColorIndex + 1];
                colorOutput[2] = colorIndex[firstColorIndex + 2];
            }
            if (texCoordOutput != null) {
                texCoordOutput[0] = texCoordIndex[firstTexCoordIndex];
                texCoordOutput[1] = texCoordIndex[firstTexCoordIndex + 1];
                texCoordOutput[2] = texCoordIndex[firstTexCoordIndex + 2];
            }
            return 1;
        }
        faceNormal[0] = normal[0];
        faceNormal[1] = normal[1];
        faceNormal[2] = normal[2];
        if (numVertex > tmpArray.length) tmpArray = new PolyVertex[numVertex];
        int index = coordIndex[startIndex];
        PolyVertex first = newVertex();
        first.x = coords[index * 3];
        first.y = coords[index * 3 + 1];
        first.z = coords[index * 3 + 2];
        first.vertexIndex = index;
        if (colorIndex != null) first.colorIndex = colorIndex[firstColorIndex];
        if (normalIndex != null) first.normalIndex = normalIndex[firstNormalIndex];
        if (texCoordIndex != null) first.texCoordIndex = texCoordIndex[firstTexCoordIndex];
        if (!isConvexVertex(coords, coordIndex[startIndex + numVertex - 1] * 3, coordIndex[startIndex] * 3, coordIndex[startIndex + 1] * 3, faceNormal)) concaveVertices.add(first);
        PolyVertex current = first;
        PolyVertex prev = first;
        int inc = 1;
        for (int i = startIndex + 1; i < startIndex + numVertex - 1; i++) {
            index = coordIndex[i];
            current = newVertex();
            current.x = coords[index * 3];
            current.y = coords[index * 3 + 1];
            current.z = coords[index * 3 + 2];
            current.vertexIndex = index;
            if (colorIndex != null) current.colorIndex = colorIndex[firstColorIndex + inc];
            if (normalIndex != null) current.normalIndex = normalIndex[firstNormalIndex + inc];
            if (texCoordIndex != null) current.texCoordIndex = texCoordIndex[firstTexCoordIndex + inc];
            if (!isConvexVertex(coords, coordIndex[i - 1] * 3, coordIndex[i] * 3, coordIndex[i + 1] * 3, faceNormal)) concaveVertices.add(current);
            current.prev = prev;
            prev.next = current;
            prev = current;
            inc++;
        }
        index = coordIndex[startIndex + numVertex - 1];
        if ((coords[index * 3] == coords[coordIndex[startIndex] * 3]) && (coords[index * 3 + 1] == coords[coordIndex[startIndex] * 3 + 1]) && (coords[index * 3 + 2] == coords[coordIndex[startIndex] * 3 + 2])) {
            current.next = first;
            first.prev = current;
        } else {
            PolyVertex last = newVertex();
            last.x = coords[index * 3];
            last.y = coords[index * 3 + 1];
            last.z = coords[index * 3 + 2];
            last.vertexIndex = index;
            if (colorIndex != null) last.colorIndex = colorIndex[firstColorIndex + numVertex - 1];
            if (normalIndex != null) last.normalIndex = normalIndex[firstNormalIndex + numVertex - 1];
            if (texCoordIndex != null) last.texCoordIndex = texCoordIndex[firstTexCoordIndex + numVertex - 1];
            if (!isConvexVertex(coords, coordIndex[startIndex + numVertex - 2] * 3, coordIndex[startIndex + numVertex - 1] * 3, coordIndex[startIndex] * 3, faceNormal)) concaveVertices.add(last);
            first.prev = last;
            last.next = first;
            last.prev = current;
            current.next = last;
        }
        return triangulate(first, coordOutput, normalOutput, colorOutput, texCoordOutput);
    }

    /**
     * Triangulate a concave polygon in the given array. The array is a flat
     * array of coordinates of [...Xn, Yn, Zn....] values. The start index is
     * the index into the array of the X component of the first item, while the
     * endIndex is the index into the array of the X component of the last item.
     * The coordinates are not required to be a closed polygon as the algorithm
     * will automatically close it. The output array is indexes into the
     * original array (including compensating for the 3 index values per
     * coordinate)
     * <p>
     * If an error occurs, the result will be negative number of triangles.
     *
     * @param coords The coordinates of the face
     * @param startIndex The index of the first coordinate in the face
     * @param numVertex  The number of vertices to read from the list
     * @param firstNormalIndex The first position of the normalIndex array
     * @param firstColorIndex The index of color for each coordinate
     * @param firstTexCoordIndex The first position of the texCoordIndex array
     * @param coordOutput The array to copy the coord index values to
     * @param normalOutput The array to copy the normal index values to
     * @param colorOutput The array to copy the color index values to
     * @param texCoordOutput The array to copy the texCoord index values to
     * @param normal The normal to this face these vertices are a part of
     * @return The number of triangles in the output array
     */
    public int triangulateConcavePolygon(float[] coords, int startIndex, int numVertex, int firstNormalIndex, int firstColorIndex, int firstTexCoordIndex, int[] coordOutput, int[] normalOutput, int[] colorOutput, int[] texCoordOutput, float[] normal) {
        if (numVertex < 3) return 0; else if (numVertex == 3) {
            coordOutput[0] = startIndex;
            coordOutput[1] = startIndex + 3;
            coordOutput[2] = startIndex + 6;
            if (normalOutput != null) {
                normalOutput[0] = firstNormalIndex;
                normalOutput[1] = firstNormalIndex + 3;
                normalOutput[2] = firstNormalIndex + 6;
            }
            if (colorOutput != null) {
                colorOutput[0] = firstColorIndex;
                colorOutput[1] = firstColorIndex + 3;
                colorOutput[2] = firstColorIndex + 6;
            }
            if (texCoordOutput != null) {
                texCoordOutput[0] = firstTexCoordIndex;
                texCoordOutput[1] = firstTexCoordIndex + 3;
                texCoordOutput[2] = firstTexCoordIndex + 6;
            }
            return 1;
        }
        faceNormal[0] = normal[0];
        faceNormal[1] = normal[1];
        faceNormal[2] = normal[2];
        if (numVertex > tmpArray.length) tmpArray = new PolyVertex[numVertex];
        PolyVertex first = newVertex();
        first.x = coords[startIndex];
        first.y = coords[startIndex + 1];
        first.z = coords[startIndex + 2];
        first.vertexIndex = startIndex;
        first.colorIndex = firstColorIndex;
        first.normalIndex = firstNormalIndex;
        first.texCoordIndex = firstTexCoordIndex;
        if (!isConvexVertex(coords, startIndex + (numVertex - 1) * 3, startIndex, startIndex + 3, faceNormal)) concaveVertices.add(first);
        PolyVertex current = first;
        PolyVertex prev = first;
        int vtx = startIndex + 3;
        int inc = 3;
        for (int i = 1; i < numVertex - 1; i++) {
            current = newVertex();
            current.x = coords[vtx];
            current.y = coords[vtx + 1];
            current.z = coords[vtx + 2];
            current.vertexIndex = vtx;
            current.colorIndex = firstColorIndex + inc;
            current.normalIndex = firstNormalIndex + inc;
            current.texCoordIndex = firstTexCoordIndex + inc;
            if (!isConvexVertex(coords, vtx - 3, vtx, vtx + 3, faceNormal)) concaveVertices.add(current);
            current.prev = prev;
            prev.next = current;
            prev = current;
            vtx += 3;
            inc += 3;
        }
        if ((coords[vtx] == coords[startIndex]) && (coords[vtx + 1] == coords[startIndex + 1]) && (coords[vtx + 2] == coords[startIndex + 2])) {
            current.next = first;
            first.prev = current;
        } else {
            PolyVertex last = newVertex();
            last.x = coords[vtx];
            last.y = coords[vtx + 1];
            last.z = coords[vtx + 2];
            last.vertexIndex = vtx;
            last.colorIndex = firstColorIndex + inc;
            last.normalIndex = firstNormalIndex + inc;
            last.texCoordIndex = firstTexCoordIndex + inc;
            if (!isConvexVertex(coords, vtx - 3, vtx, startIndex, faceNormal)) concaveVertices.add(last);
            first.prev = last;
            last.next = first;
            last.prev = current;
            current.next = last;
        }
        return triangulate(first, coordOutput, normalOutput, colorOutput, texCoordOutput);
    }

    /**
     * Clean up the internal cache and reduce it to zero.
     */
    public void clearCachedObjects() {
        vertexCache.clear();
    }

    /**
     * Perform the triangulation process now based on the vertex listing.
     *
     * @param first The first vertex of the polygon
     * @param output The array to copy the index values to to describe
     * @return The number of triangles in the output array
     */
    private int triangulate(PolyVertex first, int[] coordOutput, int[] normalOutput, int[] colorOutput, int[] texCoordOutput) {
        int output_index = 0;
        int cnt = 0;
        int maxCnt = (coordOutput.length / 3);
        maxCnt *= maxCnt;
        PolyVertex current = first.next.next;
        while (current != first) {
            PolyVertex prev_vtx = current.prev;
            boolean is_tri = false;
            cnt++;
            if (cnt > maxCnt) {
                System.out.println("Endless loop in Triangulation detected");
                System.out.println("Possible causes: ");
                System.out.println("  The normals are not correct");
                System.out.println("  Non-planar polygons");
                System.out.println("  Self-intersecting polygons");
                System.out.println("  Degenerate polygons with all coincident vertices");
                concaveVertices.clear();
                return -output_index / 3;
            }
            is_tri = isTriangle(current);
            if (isEar(prev_vtx) && !is_tri) {
                if (isCoincident(prev_vtx)) {
                    prev_vtx.prev.next = current;
                    current.prev = prev_vtx.prev;
                    if (prev_vtx == first) current = current.next;
                    prev_vtx.next = null;
                    prev_vtx.prev = null;
                    freeVertex(prev_vtx);
                    continue;
                }
                coordOutput[output_index] = current.prev.prev.vertexIndex;
                coordOutput[output_index + 1] = current.prev.vertexIndex;
                coordOutput[output_index + 2] = current.vertexIndex;
                if (normalOutput != null) {
                    normalOutput[output_index] = current.prev.prev.normalIndex;
                    normalOutput[output_index + 1] = current.prev.normalIndex;
                    normalOutput[output_index + 2] = current.normalIndex;
                }
                if (colorOutput != null) {
                    colorOutput[output_index] = current.prev.prev.colorIndex;
                    colorOutput[output_index + 1] = current.prev.colorIndex;
                    colorOutput[output_index + 2] = current.colorIndex;
                }
                if (texCoordOutput != null) {
                    texCoordOutput[output_index] = current.prev.prev.texCoordIndex;
                    texCoordOutput[output_index + 1] = current.prev.texCoordIndex;
                    texCoordOutput[output_index + 2] = current.texCoordIndex;
                }
                output_index += 3;
                prev_vtx.prev.next = current;
                current.prev = prev_vtx.prev;
                if (concaveVertices.contains(current) && isConvexVertex(current.prev, current, current.next)) concaveVertices.remove(current);
                if (concaveVertices.contains(prev_vtx) && isConvexVertex(prev_vtx.prev, prev_vtx, prev_vtx.next)) concaveVertices.remove(current);
                if (prev_vtx == first) current = current.next;
                prev_vtx.next = null;
                prev_vtx.prev = null;
                freeVertex(prev_vtx);
            } else if (is_tri) {
                coordOutput[output_index] = current.prev.prev.vertexIndex;
                coordOutput[output_index + 1] = current.prev.vertexIndex;
                coordOutput[output_index + 2] = current.vertexIndex;
                if (normalOutput != null) {
                    normalOutput[output_index] = current.prev.prev.normalIndex;
                    normalOutput[output_index + 1] = current.prev.normalIndex;
                    normalOutput[output_index + 2] = current.normalIndex;
                }
                if (colorOutput != null) {
                    colorOutput[output_index] = current.prev.prev.colorIndex;
                    colorOutput[output_index + 1] = current.prev.colorIndex;
                    colorOutput[output_index + 2] = current.colorIndex;
                }
                if (texCoordOutput != null) {
                    texCoordOutput[output_index] = current.prev.prev.texCoordIndex;
                    texCoordOutput[output_index + 1] = current.prev.texCoordIndex;
                    texCoordOutput[output_index + 2] = current.texCoordIndex;
                }
                output_index += 3;
                PolyVertex p = current.next;
                if (p.next != current) {
                    PolyVertex p1 = p.next;
                    p1.next = null;
                    p1.prev = null;
                    freeVertex(p1);
                }
                p.next = null;
                p.prev = null;
                freeVertex(p);
                current.next = null;
                current.prev = null;
                freeVertex(current);
                current = null;
                break;
            } else {
                current = current.next;
            }
        }
        concaveVertices.clear();
        return (output_index / 3);
    }

    /**
     * Find out if this is an ear.
     *
     * @param p The vertex to test for being an ear
     * @return true if this is an ear
     */
    private boolean isEar(PolyVertex p) {
        int num_concaves = concaveVertices.size();
        if (num_concaves == 0) return true;
        if (isConvexVertex(p.prev, p, p.next)) {
            tmpArray = (PolyVertex[]) concaveVertices.toArray(tmpArray);
            boolean found_vertex = false;
            for (int i = 0; i < num_concaves; i++) {
                PolyVertex cp = tmpArray[i];
                if ((cp != p && cp != p.prev && cp != p.next) && isPointInTriangle(cp, p.prev, p, p.next)) {
                    if (cp.x == p.x && cp.y == p.y && cp.z == p.z) continue;
                    if (cp.x == p.prev.x && cp.y == p.prev.y && cp.z == p.prev.z) continue;
                    if (cp.x == p.next.x && cp.y == p.next.y && cp.z == p.next.z) continue;
                    found_vertex = true;
                    break;
                }
            }
            return !found_vertex;
        } else return false;
    }

    /**
     * Check to see if the polygon pointed to by p is a triangle. The
     * triangle test is just following the next pointer for 3 jumps. If
     * any of those 3 jumps comes back to the starting vertex then it
     * is a triangle. Otherwise there are more than 3 vertices in the
     * polygon, so it can't be a triangle.
     *
     * @param p The starting point of the polygon to test
     * @return true if the polygon is a triangle
     */
    private boolean isTriangle(PolyVertex p) {
        return (p.next == p) || (p.next.next == p) || (p.next.next.next == p);
    }

    /**
     * Alternate version of the convex checking based on the PolyVertex
     * structures. Equivalent to the public method.
     *
     * @param coords The array to read coodinate values from
     * @param p0 The index of the previous vertex to the one in question
     * @param p The index of the vertex being tested
     * @param p1 The index after the vertex after the one in question
     * @param The normal to this face these vertices are a part of
     * @return true if this is a convex vertex, false for concave
     */
    private boolean isConvexVertex(PolyVertex p0, PolyVertex p, PolyVertex p1) {
        float x1 = p.x - p0.x;
        float y1 = p.y - p0.y;
        float z1 = p.z - p0.z;
        float x2 = p1.x - p.x;
        float y2 = p1.y - p.y;
        float z2 = p1.z - p.z;
        float cross_x = y1 * z2 - z1 * y2;
        float cross_y = z1 * x2 - x1 * z2;
        float cross_z = x1 * y2 - y1 * x2;
        float dot = cross_x * faceNormal[0] + cross_y * faceNormal[1] + cross_z * faceNormal[2];
        return dot >= 0;
    }

    /**
     * Check to see if this vertex is a concave vertex or convex. It assumes
     * a right-handed coordinate system and counter-clockwise ordering of the
     * vertices. The coordinate array is assumed to be flat with vertex
     * values stored [ ... Xn, Yn, Zn, ...] with the index values provided
     * assuming that flattened structure (ie Pi = n, Pi+1 = n + 3). The turn
     * direction is given by n . (a x b) <= 0 (always want right turns).
     *
     * @param coords The array to read coodinate values from
     * @param p0 The index of the previous vertex to the one in question
     * @param p The index of the vertex being tested
     * @param p1 The index after the vertex after the one in question
     * @param normal The normal to this face these vertices are a part of
     * @return true if this is a convex vertex, false for concave
     */
    private static boolean isConvexVertex(float[] coords, int p0, int p, int p1, float[] normal) {
        float x1 = coords[p] - coords[p0];
        float y1 = coords[p + 1] - coords[p0 + 1];
        float z1 = coords[p + 2] - coords[p0 + 2];
        float x2 = coords[p1] - coords[p];
        float y2 = coords[p1 + 1] - coords[p + 1];
        float z2 = coords[p1 + 2] - coords[p + 2];
        float cross_x = y1 * z2 - z1 * y2;
        float cross_y = z1 * x2 - x1 * z2;
        float cross_z = x1 * y2 - y1 * x2;
        float dot = cross_x * normal[0] + cross_y * normal[1] + cross_z * normal[2];
        return dot >= 0;
    }

    /**
     * Special case version of the general ray-polygon intersection test
     * that is used to work out if the suspect point is inside or outside the
     * triangle defined by p0, p and p1.
     */
    private boolean isPointInTriangle(PolyVertex suspect, PolyVertex p0, PolyVertex p, PolyVertex p1) {
        int i, j;
        double abs_nrm_x = (faceNormal[0] >= 0) ? faceNormal[0] : -faceNormal[0];
        double abs_nrm_y = (faceNormal[1] >= 0) ? faceNormal[1] : -faceNormal[1];
        double abs_nrm_z = (faceNormal[2] >= 0) ? faceNormal[2] : -faceNormal[2];
        int dom_axis;
        if (abs_nrm_x > abs_nrm_y) dom_axis = 0; else dom_axis = 1;
        if (dom_axis == 0) {
            if (abs_nrm_x < abs_nrm_z) dom_axis = 2;
        } else if (abs_nrm_y < abs_nrm_z) {
            dom_axis = 2;
        }
        switch(dom_axis) {
            case 0:
                working2dCoords[5] = p1.z - suspect.z;
                working2dCoords[4] = p1.y - suspect.y;
                working2dCoords[3] = p.z - suspect.z;
                working2dCoords[2] = p.y - suspect.y;
                working2dCoords[1] = p0.z - suspect.z;
                working2dCoords[0] = p0.y - suspect.y;
                break;
            case 1:
                working2dCoords[5] = p1.z - suspect.z;
                working2dCoords[4] = p1.x - suspect.x;
                working2dCoords[3] = p.z - suspect.z;
                working2dCoords[2] = p.x - suspect.x;
                working2dCoords[1] = p0.z - suspect.z;
                working2dCoords[0] = p0.x - suspect.x;
                break;
            case 2:
                working2dCoords[5] = p1.y - suspect.y;
                working2dCoords[4] = p1.x - suspect.x;
                working2dCoords[3] = p.y - suspect.y;
                working2dCoords[2] = p.x - suspect.x;
                working2dCoords[1] = p0.y - suspect.y;
                working2dCoords[0] = p0.x - suspect.x;
                break;
        }
        int sh;
        int nsh;
        float dist;
        int crossings = 0;
        if (working2dCoords[1] < 0.0) sh = -1; else sh = 1;
        for (i = 0; i < 3; i++) {
            j = (i + 1) % 3;
            int i_u = i * 2;
            int j_u = j * 2;
            int i_v = i * 2 + 1;
            int j_v = j * 2 + 1;
            if (working2dCoords[j_v] < 0.0) nsh = -1; else nsh = 1;
            if (sh != nsh) {
                if ((working2dCoords[i_u] > 0.0) && (working2dCoords[j_u] > 0.0)) {
                    crossings++;
                } else if ((working2dCoords[i_u] > 0.0) || (working2dCoords[j_u] > 0.0)) {
                    dist = working2dCoords[i_u] - (working2dCoords[i_v] * (working2dCoords[j_u] - working2dCoords[i_u])) / (working2dCoords[j_v] - working2dCoords[i_v]);
                    if (dist > 0) crossings++;
                }
                sh = nsh;
            }
        }
        return ((crossings % 2) == 1);
    }

    /**
     * Check to see if the triangle described by this apex triangle is actually
     * a degenerate one. The check is a simple cross product looking for a
     * value of zero as the result.
     *
     * @param vtx The pointer to the vertex to test
     * @return true if it is degenerate, false otherwise
     */
    private boolean isCoincident(PolyVertex vtx) {
        float x1 = vtx.x - vtx.prev.x;
        float y1 = vtx.y - vtx.prev.y;
        float z1 = vtx.z - vtx.prev.z;
        float x2 = vtx.x - vtx.next.x;
        float y2 = vtx.y - vtx.next.y;
        float z2 = vtx.z - vtx.next.z;
        float cross_x = y1 * z2 - z1 * y2;
        float cross_y = z1 * x2 - x1 * z2;
        float cross_z = x1 * y2 - y1 * x2;
        return (cross_x == 0) && (cross_y == 0) && (cross_z == 0);
    }

    /**
     * Fetch a new entry object from the cache. If there are none, create a
     * new one.
     *
     * @return an available entry object
     */
    private static PolyVertex newVertex() {
        PolyVertex ret_val = null;
        synchronized (vertexCache) {
            ret_val = (vertexCache.size() == 0) ? new PolyVertex() : (PolyVertex) vertexCache.remove(0);
        }
        return ret_val;
    }

    /**
     * Release this entry back to the cache. Assumes that the entry has been
     * freed of all the links and value before the call.
     *
     * @param e The entry to put back in the list
     */
    private static void freeVertex(PolyVertex e) {
        synchronized (vertexCache) {
            vertexCache.add(e);
        }
    }
}
