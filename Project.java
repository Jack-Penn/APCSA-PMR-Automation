import java.io.File;
import java.util.ArrayList;
import java.awt.Desktop;
import java.io.IOException;

/**
 * Write a description of class Project here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Project
{
    static Desktop desktop = Desktop.getDesktop();
    
    File dir;
    Module module;
    PMR PMR;
    String name;
    int lessonNum;
    boolean isAssignment;
    
    public Project() {
    }
    public Project(File dir, Module mod) {
        this.dir = dir;
        this.module = mod;
        this.PMR = new PMR(this);
        this.name = dir.getName();
        this.lessonNum = Integer.parseInt(name.substring(3, 5));
        this.isAssignment = dir.getParentFile().getName().endsWith("Assignments");
    }
    public Project(File dir, Module mod, boolean isAssignment) {
        this.dir = dir;
        this.module = mod;
        this.PMR = new PMR(this);
        this.name = dir.getName();
        try {
            this.lessonNum = Integer.parseInt(name.substring(3, 5));
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            this.lessonNum = 0;
        }
        this.isAssignment = isAssignment;
    }
    
    public void open() throws java.io.IOException {
        desktop.open(FileHandler.getSubDir(this.dir, "package.bluej"));
    }
    
    public Program[] listPrograms() throws IOException{
        File[] javaFiles = FileHandler.listExtensionFiles(this.dir, "java");
        Program[] programs = new Program[javaFiles.length];
        for(int i = 0; i < javaFiles.length; i++) {
            programs[i] = new Program(javaFiles[i], this);
        }
        return programs;
    }
}
