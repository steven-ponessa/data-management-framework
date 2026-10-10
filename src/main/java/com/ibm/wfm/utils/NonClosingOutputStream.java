package com.ibm.wfm.utils;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class NonClosingOutputStream
        extends FilterOutputStream {

    public NonClosingOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void close() throws IOException {
        /*
         * Flush the wrapped stream but do not close it.
         */
        flush();
    }
}