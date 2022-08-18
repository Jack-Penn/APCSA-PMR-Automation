import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Write a description of class Module here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Module
{
    public File dir;
    public int modNum;
    
    public Module() {
        this.dir = new File("");
        this.modNum = 0;
    }
    public Module(File dir) {
        this.dir = dir;
        this.modNum = Integer.parseInt(dir.getName().substring(7));
    }
    public Module(File dir, int num) {
        this.dir = dir;
        this.modNum = num;
    }
    
    private File getModSubdir(String dirSuffix) {
        return FileHandler.getSubDir(this.dir, "Mod" + Formatter.twoDigitNumString(this.modNum) + " " + dirSuffix);
    }
    
    public Project[] listProjects(String modSubdirName) {
        ArrayList<Project> modProjects = new ArrayList<Project>();
        File modSubdir = getModSubdir(modSubdirName);
        for( String name: modSubdir.list()) {
            File dir = FileHandler.getSubDir(modSubdir, name);
            if(dir.isDirectory()) {
                boolean isAssignment = modSubdirName.equals("Assignments");
                modProjects.add(new Project(dir, this, isAssignment));
            }
        }
        return modProjects.toArray(Project[]::new);
    }
    public Project[] listProjects() {
        Project[] assignmentProjects =  listProjects("Assignments");
        Project[] lessonProjects =  listProjects("Lessons");
        
        Project[] allProjects = new Project[assignmentProjects.length + lessonProjects.length];
        for(int i = 0; i < assignmentProjects.length; i++) {
            allProjects[i] = assignmentProjects[i];
        }
        for(int i = 0; i < lessonProjects.length; i++) {
            allProjects[assignmentProjects.length + i] = lessonProjects[i];
        }
        return allProjects;
    }
    
    public Project getProject(int lessonNum, String modSubdir) {
        for(Project project :  listProjects(modSubdir))
        {
            if(project.lessonNum == lessonNum)
                return project;
        }
        return new Project();
    }
    public Project[] getProject(int lessonNum) {
        ArrayList<Project> matchingProjects = new ArrayList<Project>();
        for(Project project :  listProjects())
        {
            if(project.lessonNum == lessonNum)
                matchingProjects.add(project);
        }
        return matchingProjects.toArray(Project[]::new);
    }
    
    public Project getLatestProject(String modSubdir) {
        Project latestProject = new Project();
        Date latestProjectDate = new Date(0);
        for(Project project : listProjects(modSubdir)) {
            Date projectDate = new Date(project.dir.lastModified());
            if(latestProjectDate.before(projectDate)) {
                latestProject = project;
                latestProjectDate = projectDate;
            }
        }
        return latestProject;
    }
    public Project getLatestProject() {
        return getLatestProject("Assignments");
    }
}
