package com.kayak.pms.opFlow.engine.autoload;

import java.io.File;

/**
 * Created by daniel on 09/04/2017.
 */
public class ConfFile {
    private File file;
    private Long lastModified;

    public ConfFile(File file, Long lastModified) {
        this.file = file;
        this.lastModified = lastModified;
    }

    public ConfFile() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }
}
