import java.util.Scanner;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Write a description of class FileHandler here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class FileHandler
{
    static Scanner kb = new Scanner(System.in);
    static int SCREEN_CHARACTERS = 85;
    static Desktop desktop = Desktop.getDesktop();
    
    public static File getSubDir(File dir, String dirName)
    {
        return new File(dir.getAbsolutePath() + "\\" + dirName);
    }
    
    public static String getFileExtension(String fileName)
    {
        String[] fileParts = fileName.split("\\.");
        return fileParts[fileParts.length-1];
    }
    
    public static File[] listExtensionFiles(File dir, String extension)
    {
        ArrayList<File> javaFileList = new ArrayList<File>();
        for(String fileName : dir.list())
        {
            if(getFileExtension(fileName).equals(extension))
            {
                javaFileList.add(getSubDir(dir, fileName));
            }
        }
        return javaFileList.toArray(File[]::new);
    }
    
    public static String getFileName(String fileName)
    {
        String[] fileParts = fileName.split("\\.");
        return fileParts[0];
    }
    
    public static String readFileAsString (File file) throws IOException
    {
        Scanner fileIn = new Scanner(file);
        
        String res = fileIn.nextLine();
        while(fileIn.hasNextLine())
            res += "\n"+fileIn.nextLine();
        return res;
    }
    public static void copyFile(File file, File copyFile) throws IOException
    {
        writeFile(readFileAsString(file), copyFile);
    }
    public static void writeFile(String str, File file) throws IOException
    {
        PrintWriter outFile = new PrintWriter(file);
        outFile.print(str);
        outFile.close();
    }
    
    public static boolean fileIsEmpty(File file) {
        return file.getPath() == "";
    }
}
