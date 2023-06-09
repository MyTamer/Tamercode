package gnu.javax.imageio.bmp;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import javax.imageio.IIOImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

public class EncodeRLE8 extends BMPEncoder {

    protected BMPInfoHeader infoHeader;

    protected BMPFileHeader fileHeader;

    protected long offset;

    /**
   * RLE control codes
   */
    private static final byte ESCAPE = (byte) 0;

    private static final byte EOL = (byte) 0;

    private static final byte EOB = (byte) 1;

    private static final byte DELTA = (byte) 2;

    /**
   * Constructs an instance of this class.
   * 
   * @param fh - the file header to use.
   * @param ih - the info header to use.
   */
    public EncodeRLE8(BMPFileHeader fh, BMPInfoHeader ih) {
        super();
        fileHeader = fh;
        infoHeader = ih;
        offset = BMPFileHeader.SIZE + BMPInfoHeader.SIZE;
    }

    /**
   * The image encoder.
   * 
   * @param o - the image output stream
   * @param streamMetadata - metadata associated with this stream, or
   * null
   * @param image - an IIOImage containing image data.
   * @param param - image writing parameters, or null
   * @exception IOException if a write error occurs
   */
    public void encode(ImageOutputStream o, IIOMetadata streamMetadata, IIOImage image, ImageWriteParam param) throws IOException {
        int size;
        int value;
        int j;
        int rowCount;
        int rowIndex;
        int lastRowIndex;
        int[] bitmap;
        size = (infoHeader.biWidth * infoHeader.biHeight) - 1;
        rowCount = 1;
        rowIndex = size - infoHeader.biWidth;
        lastRowIndex = rowIndex;
        ByteBuffer buf = ByteBuffer.allocate(size);
        try {
            bitmap = new int[infoHeader.biWidth * infoHeader.biHeight];
            PixelGrabber pg = new PixelGrabber((BufferedImage) image.getRenderedImage(), 0, 0, infoHeader.biWidth, infoHeader.biHeight, bitmap, 0, infoHeader.biWidth);
            pg.grabPixels();
            for (j = 0; j < size; j++) {
                value = bitmap[rowIndex];
                buf.put((byte) (value & 0xFF));
                if (rowCount == infoHeader.biWidth) {
                    rowCount = 1;
                    rowIndex = lastRowIndex - infoHeader.biWidth;
                    lastRowIndex = rowIndex;
                } else rowCount++;
                rowIndex++;
            }
            buf.flip();
            o.write(uncompress(infoHeader.biWidth, infoHeader.biHeight, buf));
        } catch (Exception wb) {
            wb.printStackTrace();
        }
    }

    /**
   * Uncompresses the image stored in the buffer.
   * 
   * @param w - the width of the image
   * @param h - the height of the image
   * @param buf - the ByteBuffer containing the pixel values.
   * @return byte array containing the uncompressed image
   * @throws IOException if an error is encountered while reading
   * buffer.
   */
    private byte[] uncompress(int w, int h, ByteBuffer buf) throws IOException {
        byte[] cmd = new byte[2];
        byte[] data = new byte[w * h];
        int offIn = 0;
        int x = 0, y = 0;
        try {
            while ((x + y * w) < w * h) {
                try {
                    buf.get(cmd);
                } catch (BufferUnderflowException e) {
                    throw new IOException("Error reading compressed data.");
                }
                if (cmd[0] == ESCAPE) {
                    switch(cmd[1]) {
                        case EOB:
                            return data;
                        case EOL:
                            x = 0;
                            y++;
                            break;
                        case DELTA:
                            try {
                                buf.get(cmd);
                            } catch (BufferUnderflowException e) {
                                throw new IOException("Error reading compressed data.");
                            }
                            int dx = cmd[0] & (0xFF);
                            int dy = cmd[1] & (0xFF);
                            x += dx;
                            y += dy;
                            break;
                        default:
                            int length = cmd[1] & (0xFF);
                            int copylength = length;
                            length += (length & 1);
                            byte[] run = new byte[length];
                            try {
                                buf.get(run);
                            } catch (BufferUnderflowException e) {
                                throw new IOException("Error reading compressed data.");
                            }
                            System.arraycopy(run, 0, data, (x + w * (h - y - 1)), copylength);
                            x += copylength;
                            break;
                    }
                } else {
                    int length = cmd[0] & (0xFF);
                    for (int i = 0; i < length; i++) data[(h - y - 1) * w + x++] = cmd[1];
                }
            }
            return data;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BMPException("Invalid RLE data.");
        }
    }
}
