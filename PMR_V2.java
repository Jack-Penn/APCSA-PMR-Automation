/**
 * Write a description of class PMR_V2 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Desktop;

public class PMR_V2
{
    static Scanner kb = new Scanner(System.in);
    static int SCREEN_CHARACTERS = 85;
    static Desktop desktop = Desktop.getDesktop();
    
    private static File getSubDir(File dir, String dirName)
    {
        return new File(dir.getAbsolutePath() + "\\" + dirName);
    }
    
    private static String getFileExtension(String fileName)
    {
        String[] fileParts = fileName.split("\\.");
        return fileParts[fileParts.length-1];
    }
    private static String getFileName(String fileName)
    {
        String[] fileParts = fileName.split("\\.");
        return fileParts[0];
    }
    
    private static String readFileAsString (File file) throws IOException
    {
        Scanner fileIn = new Scanner(file);
        
        String res = fileIn.nextLine();
        while(fileIn.hasNextLine())
            res += "\n"+fileIn.nextLine();
        return res;
    }
    private static void copyFile(File file, File copyFile) throws IOException
    {
        PrintWriter outFile = new PrintWriter(copyFile);
        outFile.print(readFileAsString(file));
        outFile.close();
    }
    
    private static String ask(String question, String defaultAnswer)
    {
        String defaultAnswerString = (" <" + defaultAnswer + "> " );
        question = question.trim();
        String formattedQuestion = question.substring(0, question.length()-1) + defaultAnswerString + question.substring(question.length()-1) + " ";
        System.out.print(formattedQuestion);
        String response = kb.nextLine();
        if(response.equals(""))
        {
            System.out.println("  Submitted Default: " + defaultAnswer);
            return defaultAnswer;
        }
        return response;
    }
    private static String ask(String question)
    {
        System.out.print(question.trim() + " ");
        return kb.nextLine();
    }
    private static boolean askYN(String question, String defaultAnswer)
    {
        question = question.substring(0, question.length()-1) + " (Y/N) " + question.substring(question.length()-1);
        return ask(question, defaultAnswer).equalsIgnoreCase("y");
    }
    private static int askMenuOptions(String question, String[] options)
    {
        for(int i = 0; i < options.length; i++)
        {
            System.out.println("  [" + (char)(65 + i) + "] " + options[i]);
        }
        return (int)(ask(question, "A").toUpperCase().charAt(0))-65;
    }
    
    private static String twoDigitNumString(String numString)
    {
        if(numString.length() == 1)
            return "0" + numString;
        return numString;
    }
    
    private static String stringToWhitespace(String str)
    {
        return " ".repeat(str.length());
    }
    private static String wrapWordString(String str, String indentation, int lineCharacters)
    {
        str = String.join(" ", str.split("\\n"));
        str = String.join(" ", str.split("\\s\\s+"));
        
        if(str.length() <= lineCharacters) 
            return str;
        
        String res = "";
        int start = 0;
        int end;
        for(end = lineCharacters-indentation.length(); end < str.length(); end+= lineCharacters - indentation.length()) {
            while(str.charAt(end) != ' ') 
                end--;
            res += str.substring(start, end) + "\n" + indentation;
            start = end+1;
        }
        res+= str.substring(start);
        return res;
    }
    private static String useStartAsWrapIndent(String start, String str, int lineCharacters)
    {
        return wrapWordString(start + str, stringToWhitespace(start), lineCharacters);
    }
    
    public static void main(String[] args) throws IOException
    {
        File aPCSDir = new File("C:\\Users\\jackt\\OneDrive - Pinellas County Schools\\APCS Course Files");
        
        //Retrieves Project Directory
        File projectDir = new File("");
        {
            //Finds Latest Module
            File latestModFolder = new File("");
            for(String fileName :  aPCSDir.list())
            {
                if(fileName.substring(0, 6).equals("Module"))
                    latestModFolder = getSubDir(aPCSDir, fileName);
            }
            String latestModNumString = latestModFolder.getName().substring(7);
            
            //Finds Latest Project in Module
            File latestModAssignmentsDir = getSubDir(latestModFolder, "Mod" + latestModNumString + " Assignments");
            String[] projectNames = latestModAssignmentsDir.list();
            File latestProject = getSubDir(latestModAssignmentsDir, projectNames[projectNames.length-1]);
            String latestProjectNumString = latestProject.getName().substring(0, 5);
            
            //Asks for Project String
            String projectNumString = ask("Enter Project String:", latestProjectNumString + " Assignment");
            if(projectNumString.equals(latestProjectNumString + " Assignment"))
            {
                projectDir = latestProject;
            }
            else
            {
                //Parses Specified Project String
                String[] projectNumStringParts = projectNumString.split("\\.");
                String modNumString = twoDigitNumString(projectNumStringParts[0]);
                projectNumString = modNumString + "." + twoDigitNumString(projectNumStringParts[1]);
                
                File modDir = getSubDir(aPCSDir, "Module " + modNumString);
                
                //Searches for Project String in Assignments and Lessons Folders
                //Searches Assignments Folder
                File assignmentsDir = getSubDir(modDir, "Mod" + modNumString + " Assignments");
                File assignmentsProjectDir = new File("");
                for(String projectName :  assignmentsDir.list())
                {
                    if(projectName.substring(0,5).equals(projectNumString))
                    {
                        assignmentsProjectDir = getSubDir(assignmentsDir, projectName);
                        break;
                    }
                }
                //Searches Lessons Folder
                File lessonsDir = getSubDir(modDir, "Mod" + modNumString + " Lessons");
                File lessonsProjectDir = new File("");
                for(String projectName :  lessonsDir.list())
                {
                    if(projectName.substring(0,5).equals(projectNumString))
                    {
                        lessonsProjectDir = getSubDir(lessonsDir, projectName);
                        break;
                    }
                }
                
                boolean isInAssignments = !assignmentsProjectDir.getName().equals("");
                boolean isInLessons = !lessonsProjectDir.getName().equals("");
                if(isInAssignments && isInLessons)
                {
                    if(askYN("Project String found in both Assignments and Lessons Folder. Would you like to Use the one in Assignments?", "Y"))
                    {
                        projectDir = assignmentsProjectDir;
                    }
                    else
                    {
                        projectDir = lessonsProjectDir;
                    }
                }
                else if(isInAssignments)
                {
                    projectDir = assignmentsProjectDir;
                }
                else if(isInLessons)
                {
                    projectDir = lessonsProjectDir;
                }
                else
                {
                    System.out.println("Error: Project String \"" + projectNumString + "\" not found");
                }
            }
            System.out.println("Using Project: " + projectDir.getName());
            System.out.println();
        }
        
        //Retrieves Java File
        File javaFile;
        {
            //Finds Java Files
            ArrayList<String> javaFileList = new ArrayList<>();
            for(String fileName : projectDir.list())
            {
                if(getFileExtension(fileName).equals("java"))
                {
                    javaFileList.add(fileName);
                }
            }
            
            int javaFileIndex = 0;
            if(javaFileList.size() > 1)
            {
                System.out.println("Multiple Classes Detected.");
                String[] javaFileArray = javaFileList.toArray(new String[javaFileList.size()]);
                javaFileIndex = askMenuOptions("Enter the Letter corresponding to the class you would like to choose:", javaFileArray);
            }
            javaFile = getSubDir(projectDir, javaFileList.get(javaFileIndex));
            
            System.out.println("Using class: " + javaFile.getName());
            System.out.println();
        }
        
        if(askYN("Before we begin, would you like to open the project?", "y"))
        {
            desktop.open(getSubDir(projectDir, "package.bluej"));
            System.out.println();
        }
        
        //Creates PMR
        if(askYN("Would you like to create a PMR for the project?", "Y"))
        {
            System.out.println();
            String projectTitleString = projectDir.getName() + " / " + javaFile.getName();
            
            //Reads Purpose of Project from java File
            Scanner inJavaFile = new Scanner(javaFile);
            String purposeOfProjectString = "";
            while(inJavaFile.hasNextLine())
            {
                String line = inJavaFile.nextLine();
                if(line.length() > 3 && line.substring(0, 3).equals(" * ") && line.charAt(3) != '@')
                {
                    purposeOfProjectString += line.substring(3) + " ";
                }
                else if(purposeOfProjectString.length() > 0)
                {
                    break;
                }
                
            }
            purposeOfProjectString = purposeOfProjectString.trim();
            
            //Reads Author from java File
            String authorString = "";
            while(inJavaFile.hasNextLine())
            {
                String line = inJavaFile.nextLine();
                if(line.length() > 11 && line.substring(0,11).equals(" * @author "))
                {
                    authorString = line.substring(11);
                    break;
                }
            }
            
            //Reads Date from java File
            String dateString = "";
            String fileDateString = "";
            while(inJavaFile.hasNextLine())
            {
                String line = inJavaFile.nextLine();
                if(line.length() > 12 && line.substring(0,12).equals(" * @version "))
                {
                    fileDateString = line.substring(12);
                    break;
                }
            }
            String todayDateString = (new SimpleDateFormat("MM/dd/yyyy")).format(new Date());
            if(fileDateString.equals(todayDateString))
            {
                dateString = todayDateString;
            }
            else {
                if(askYN(wrapWordString("The Version String in the Java file \"" + fileDateString + "\" and today's date \"" + todayDateString + "\" appear to not match. Would you like to use Today's date in your PMR?", "", SCREEN_CHARACTERS), "Y"))
                {
                    dateString = todayDateString;
                    if(askYN(wrapWordString("Since they don't match, would you like to update the @version of \"" + fileDateString + "\" in " + javaFile.getName() + " to \"" + todayDateString + "\"", "", SCREEN_CHARACTERS), "Y"))
                    {
                        //updates @version of java file 
                        String javaFileString = readFileAsString(javaFile);
                        javaFileString = javaFileString.replaceAll("@version .+", "@version " + dateString);
                        
                        //Temp file backup
                        //copyFile(javaFile, getSubDir(projectDir, "tempcopy.java"));
                        
                        PrintWriter outJavaFile = new PrintWriter(javaFile);
                        outJavaFile.print(javaFileString);
                        outJavaFile.close();
                        
                        System.out.println("@version updated to " + dateString);
                    }
                }
                else
                {
                    dateString = fileDateString;
                }
            }
            inJavaFile.close();
            System.out.println();
            
            String plusesString = ask(useStartAsWrapIndent("<+s>: ", "<Reflect on what went well on the project. What did you learn that was new or surprised you?>", SCREEN_CHARACTERS));
            String minusesString = ask(useStartAsWrapIndent("<-s>: ", "<Where did you struggle? Did you have any bugs (logic errors) or was there a syntax error that you had to figure out?>", SCREEN_CHARACTERS));
            String futureReflectionString = ask(useStartAsWrapIndent("In the future: ", "<Did what you learn make you think differently about problem solving? Now that you have learned a new skill, are there certain kinds of problems that you would approach differently?", SCREEN_CHARACTERS));
            System.out.println();
            
            String pMRString = String.join("\n",
                "PROJECT TITLE: " + projectTitleString,
                "PURPOSE OF PROJECT: " + wrapWordString(purposeOfProjectString, stringToWhitespace("PURPOSE OF PROJECT: "), SCREEN_CHARACTERS),
                "DATE: " + dateString,
                "AUTHOR: " + authorString,
                "",
                "**************************** P M R *********************************************",
                "",
                useStartAsWrapIndent("<+s>: ", plusesString, SCREEN_CHARACTERS),
                "",
                useStartAsWrapIndent("<-s>: ", minusesString, SCREEN_CHARACTERS),
                "********************************************************************************",
                useStartAsWrapIndent("In the future: ", futureReflectionString, SCREEN_CHARACTERS)
            );
            
            System.out.println("Creating PMR");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            System.out.println(pMRString);
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            
            if(askYN("Write PMR to README.TXT?", "Y")) {
                File outFile = getSubDir(projectDir, "README.TXT");
                PrintWriter outPMR = new PrintWriter(outFile);
                outPMR.print(pMRString);
                outPMR.close();
                
                System.out.println("PMR written to README.TXT");
                desktop.open(outFile);
            }
        }
        System.out.println();
        
        
        
        //Academic Integrity ***********************************************************
        if(askYN("Create Academic Integrity? ", "Y")) {
            String aIFileName = getFileName(javaFile.getName()) + ".txt";
            copyFile(javaFile, getSubDir(projectDir, aIFileName));
            
            System.out.println("Academic Integrity file created");
        }
        System.out.println();
    }
}
