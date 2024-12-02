package com.dcx.jfoss.fra.api;

import java.io.File;
import java.io.Serializable;

public class FileName
        implements Serializable, Comparable<FileName> {
    private static final long serialVersionUID = -8049495208401580297L;

    private File file = null;

    public FileName(String fileName) {
        this.file = new File(fileName);
    }

    public String getName() {
        return this.file.getName();
    }

    public int compareTo(FileName o) {
        if (o == null || o.getFileName() == null) {
            throw new NullPointerException("Null argument in comparison");
        }
        return getFileName().compareTo(o.getFileName());
    }


    protected String getFileName() {
        return this.file.getAbsolutePath();
    }


    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof FileName) {
            result = getFileName().equals(((FileName) o).getFileName());
        }
        return result;
    }


    public int hashCode() {
        return this.file.hashCode();
    }
}


