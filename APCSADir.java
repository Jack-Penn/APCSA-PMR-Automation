import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;

/**
 * Write a description of class SearchDir here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class APCSADir
{
    static File baseDir = new File("C:\\Users\\jackt\\OneDrive - Pinellas County Schools\\APCS Course Files");
    
    //Finds Latest Module
    public static Module[] listModules() {
        ArrayList<Module> modDirs = new ArrayList<Module>();
        for(String fileName :  baseDir.list())
        {
            if(fileName.substring(0, 6).equals("Module"))
                modDirs.add(new Module(FileHandler.getSubDir(baseDir, fileName)));
        }
        return modDirs.toArray(Module[]::new);
    }
    
    public static Module getModule(int num) {
        for(Module mod :  listModules())
        {
            if(mod.modNum == num)
                return mod;
        }
        return new Module();
    }
    
    
    public static Project getLatestProject() {
        Module latestMod = new Module();
        
        //Finds Latest Non-empty Module
        for(Module mod : listModules()) {
            if(mod.listProjects().length != 0 && mod.modNum > latestMod.modNum)
                latestMod = mod;
        }
            
        return latestMod.getLatestProject();
    }
    
    public static Project[] getProjects(String projectString) {
        //Parses Specified Project String
        String[] parts = projectString.split("\\.");
        int modNum = Integer.parseInt(parts[0]);
        int lessonNum = Integer.parseInt(parts[1]);
        
        Module projectMod = getModule(modNum);
        return projectMod.getProject(lessonNum);
    }
}
