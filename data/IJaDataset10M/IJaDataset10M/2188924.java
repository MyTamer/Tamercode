package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import java.nio.*;

public final class EXTTextureSharedExponent {

    /**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 * TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 * RenderbufferStorageEXT:
	 */
    public static final int GL_RGB9_E5_EXT = 0x8c3d;

    /**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 * TexImage1D, TexImage2D, GetTexImage, TexImage3D, TexSubImage1D,
	 * TexSubImage2D, TexSubImage3D, GetHistogram, GetMinmax,
	 * ConvolutionFilter1D, ConvolutionFilter2D, ConvolutionFilter3D,
	 * GetConvolutionFilter, SeparableFilter2D, GetSeparableFilter,
	 * ColorTable, ColorSubTable, and GetColorTable:
	 */
    public static final int GL_UNSIGNED_INT_5_9_9_9_REV_EXT = 0x8c3e;

    /**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameterfv and
	 * GetTexLevelParameteriv:
	 */
    public static final int GL_TEXTURE_SHARED_SIZE_EXT = 0x8c3f;

    private EXTTextureSharedExponent() {
    }
}
