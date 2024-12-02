package com.dcx.jfoss.fra.api;

import java.io.*;

public interface JCAFileAdapterConnection extends Serializable, Closeable {
    String getPathSeparator();

    String getSeparator();

    boolean canRead(String paramString);

    boolean canWrite(String paramString);

    boolean createNewFile(String paramString) throws IOException;

    boolean delete(String paramString);

    void deleteOnExit(String paramString);

    String getParent(String paramString);

    boolean isDirectory(String paramString);

    boolean isFile(String paramString);

    boolean isHidden(String paramString);

    long lastModified(String paramString);

    long length(String paramString);

    String[] list(String paramString);

    String[] list(String paramString, FileFilter paramFileFilter);

    FileName[] listFiles(String paramString);

    FileName[] listFiles(String paramString, FileFilter paramFileFilter);

    boolean mkdir(String paramString);

    boolean mkdirs(String paramString);

    boolean rename(String paramString1, String paramString2);

    boolean setLastModified(String paramString, long paramLong);

    boolean setReadOnly(String paramString);

    InputStream getInputStream(String paramString) throws IOException;

    OutputStream getOutputStream(String paramString, boolean paramBoolean) throws IOException;

    boolean isFileAccessLocked();

    boolean isClosed();
}
