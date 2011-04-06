package org.richfaces.demo.tree.adaptors;

import java.util.List;

import javax.inject.Named;
import javax.faces.bean.RequestScoped;

@Named
@RequestScoped
public class FileSystemBean {
    private static final String SRC_PATH = "/WEB-INF";

    private List<FileSystemNode> srcRoots;

    public synchronized List<FileSystemNode> getSourceRoots() {
        if (srcRoots == null) {
            srcRoots = new FileSystemNode(SRC_PATH).getDirectories();
        }

        return srcRoots;
    }
}
