import java.io.File;
import java.awt.Desktop;

/**
 * Write a description of class PMR here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PMR
{
    static Desktop desktop = Desktop.getDesktop();
    
    File dir;
    Project project;
    
    
    
    public PMR(File dir, Project project) {
        this.dir = dir;
        this.project = project;
    }
    public PMR(Project project) {
        this.project = project;
        this.dir = FileHandler.getSubDir(this.project.dir, "README.TXT");
    }
    
    public void writeToFile(String str) throws java.io.IOException {
        FileHandler.writeFile(str, this.dir);  
    }
    //String projectTitleString = projectDir.getName() + " / " + javaFile.getName();
    public void open() throws java.io.IOException {
        desktop.open(this.dir);
    }
}
