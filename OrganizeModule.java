/**
 * Write a description of class OrganizeModule here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;

public class OrganizeModule
{
    static File aPCSDir = new File("C:\\Users\\jackt\\OneDrive - Pinellas County Schools\\APCS Course Files");
    static Scanner in = new Scanner(System.in);
    
    private static File getSubDir(File dir, String dirName)
    {
        return new File(dir.getAbsolutePath() + "\\" + dirName);
    }
    
    private static String twoDigitNumString(String numString)
    {
        if(numString.length() == 1)
            return "0" + numString;
        return numString;
    }
    private static String twoDigitNumString(int numString)
    {
        return twoDigitNumString(numString + "");
    }
    private static String ask(String question, String defaultAnswer)
    {
        String defaultAnswerString = (" <" + defaultAnswer + "> " );
        String formattedQuestion = question.trim().substring(0, question.length()-1) + defaultAnswerString + question.substring(question.length()-1) + " ";
        System.out.print(formattedQuestion);
        String response = in.nextLine();
        if(response.equals(""))
        {
            System.out.println("  Submitted Default: " + defaultAnswer);
            return defaultAnswer;
        }
        return response;
    }
    
    public static void main(String[] args) throws IOException
    {
        
        
        //Finds Latest Module Number
        int latestModNum = 0;
        for(String fileName :  aPCSDir.list())
        {
            if(fileName.substring(0, 6).equals("Module"))
                latestModNum = Integer.parseInt(fileName.substring(7));
        }
        
        int nextModNum = latestModNum + 1;
        String nextModString = twoDigitNumString(nextModNum);
        
        nextModString = ask("Enter Module Number:", nextModString);
        nextModString = twoDigitNumString(nextModString);
        
        File nextModDir = getSubDir(aPCSDir, "Module " + nextModString);
        nextModDir.mkdir();
        
        getSubDir(nextModDir, "Mod" + nextModString + " Assignments").mkdir();
        getSubDir(nextModDir, "Mod" + nextModString + " Documents").mkdir();
        getSubDir(nextModDir, "Mod" + nextModString + " Lessons").mkdir();
    }
}
