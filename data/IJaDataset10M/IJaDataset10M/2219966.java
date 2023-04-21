package org.apache.axis.utils;

import java.io.OutputStream;
import java.io.IOException;

public class TeeOutputStream extends OutputStream {

    private OutputStream left;

    private OutputStream right;

    public TeeOutputStream(OutputStream left, OutputStream right) {
        this.left = left;
        this.right = right;
    }

    public void close() throws IOException {
        left.close();
        right.close();
    }

    public void flush() throws IOException {
        left.flush();
        right.flush();
    }

    public void write(byte[] b) throws IOException {
        left.write(b);
        right.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        left.write(b, off, len);
        right.write(b, off, len);
    }

    public void write(int b) throws IOException {
        left.write(b);
        right.write(b);
    }
}