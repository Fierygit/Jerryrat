package blog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/6 20:46
 * 组合模式
 */

public class PostFile {
    private String fileName;
    private boolean isDir;
    private String info;
    private List<PostFile> child = new ArrayList<>();

    PostFile(String name, boolean isDir){
        this.fileName = name;
        this.isDir = isDir;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setChild(List<PostFile> child) {
        this.child = child;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isDir() {
        return isDir;
    }

    public List<PostFile> getChild() {
        return child;
    }
}
