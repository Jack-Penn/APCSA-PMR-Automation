import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;  
import java.util.Date; 
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.nio.file.StandardCopyOption;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;


/**
 * Write a description of class PMR here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PMR
{// instance variables - replace the example below with your own
    static Scanner scanner;
    static String baseUri;
    
    public static void main(String[] args) throws IOException
    {
        scanner = new Scanner(System.in);
        
        baseUri = new File(System.getProperty("user.dir")).getParent(); //stores uir of base AP CSA folder
        
        System.out.println("NOTE: Typing \"exit\" into any submission filed will stop the process.");
        System.out.println();
        
        //Project Locator **************************************
        File projectDir = new File("");
            
        String projectSearchDirUri;
        String lessonName;
        if(!askYN("Would you like to specify a project string", true)) {
            System.out.println();
            
            // Asks Module number
            String modNum = toTwoDigitNum(ask("What Module number?", "1"));
            String modDirName = "Module " + modNum;
            System.out.println("Accessing \"" + modDirName + "\"");
             
            // Asks Assignment/Lesson
            String projectTypeName = "Mod" + modNum + " " + ask("Assignment or Lesson?", "Assignment") + "s";
           
            projectSearchDirUri = baseUri + "\\" + modDirName + "\\" + projectTypeName + "\\";
            System.out.println("Searching \"" + getLocalPath(projectSearchDirUri) + "\"");
            
            // Finds Lesson Project
            File[] possibleLessonFiles = new File(projectSearchDirUri).listFiles();
            String[] possibleLessonsList = new String[possibleLessonFiles.length];
            for(int i = 0; i < possibleLessonFiles.length; i++) {
                possibleLessonsList[i] = possibleLessonFiles[i].getAbsolutePath().substring(projectSearchDirUri.length(),projectSearchDirUri.length() + 5);
            }
            System.out.println("Found Lessons: " + String.join(", ", possibleLessonsList));
            
            // Asks Lesson Number
            String lessonNum = toTwoDigitNum(ask("Which lesson number?", possibleLessonsList[possibleLessonsList.length-1].substring(3)));
            lessonName = modNum + "." + lessonNum;
              
        }else {
            String projectString = ask("Project String: ", "1.13");
            System.out.println();
            
            String inputedLessonName;
            boolean specifiedType = !Character.isDigit(projectString.charAt(0));
            String[] projectParts = (specifiedType ? projectString.substring(1) : projectString).split("\\.");
            
            String modNum = toTwoDigitNum(projectParts[0]);
            String modDirName = "Module " + modNum;
            
            String lessonNum = toTwoDigitNum(projectParts[1]);
            lessonName = modNum + "." + lessonNum;
            
            if(specifiedType) {
                String projectTypeName = "Mod" + modNum + " " + (Character.toLowerCase(projectString.charAt(0)) == 'a' ? "Assignments" : "Lessons");
                projectSearchDirUri = baseUri + "\\" + modDirName + "\\" + projectTypeName + "\\";
            }else {
                String commonUri = baseUri + "\\" + modDirName + "\\Mod" + modNum + " ";
                String assignmentsSearchUri =  commonUri + "Assignments\\";
                String lessonsSearchUri =  commonUri + "Lessons\\";

                //Assignments Search
                File foundAssignmentProject = null;
                for(File lessonDir: new File(assignmentsSearchUri).listFiles()) {
                    String dirLessonName = lessonDir.getName().substring(0,5);
                    if(dirLessonName.equals(lessonName)) {
                        foundAssignmentProject = lessonDir;
                        break;
                    }
                }
                    
                if(foundAssignmentProject == null) {
                    projectSearchDirUri = lessonsSearchUri;
                    System.out.println("Trying Lessons Folder");
                }else {
                    //Lessons Search
                    File foundLessonProject = null;
                    for(File lessonDir: new File(lessonsSearchUri).listFiles()) {
                        String dirLessonName = lessonDir.getName().substring(0,5);
                        if(dirLessonName.equals(lessonName)) {
                            foundLessonProject = lessonDir;
                            break;
                        }
                    }
                    
                    if(foundLessonProject == null) {
                        projectSearchDirUri = assignmentsSearchUri;
                        System.out.println("Trying Assignments Folder");
                    }else {
                        System.out.println("Specified lesson number found in both assignments and lessons");
                        projectSearchDirUri = askYN("Use the one in Assignments?", true) ? assignmentsSearchUri : lessonsSearchUri;
                    }
                }
            }
        }
            
            
        // Retrieves Lesson's Project Folder;
        File[] lessonsList = new File(projectSearchDirUri).listFiles();
        for(File file: lessonsList) {
            String fileLessonName = file.getName().substring(0,5);
            if(fileLessonName.equals(lessonName)) {
                projectDir = file;
                break;
            }
        }
            
        System.out.println("Using: " + projectDir.getName());
        System.out.println();
            
        // Finds Project Folder's java classes
        File[] projectJavaFiles = findFilesOfExtension(projectDir, "java");
        // Retrieves Class Names
        String[] projectJavaFileNames = new String[projectJavaFiles.length];
        for(var i = 0;  i < projectJavaFiles.length; i++) {
            projectJavaFileNames[i] = projectJavaFiles[i].getName();
        }
        String javaFileName = projectJavaFiles[0].getName();
        // Asks for Class File Name
        if(projectJavaFiles.length > 1) {
            System.out.println("Found: " + String.join(", ", projectJavaFileNames));
            javaFileName = ask("Which java file?", javaFileName);
        }
        File javaFile = new File(projectDir.getPath() + "//" + javaFileName);
        System.out.println("Using: " + javaFileName);
        System.out.println();
            
        System.out.println("Final Local URI: " + getLocalPath(javaFile));
        System.out.println("======================================================================================================================================");
        System.out.println();
            
            
        //PMR ***********************************************************
        if(askYN("create PMR?", true)) {
            System.out.println("************************************** Creating PMR ******************************************");
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");   
                
            String projectClassTitle = projectDir.getName() + " / " + javaFile.getName();
                
            //Retrieve Purpose of Program
            String javaFileContent = Files.readString(Path.of(javaFile.getPath()));
            // ^ \* 
            String[] purposeLines = Pattern.compile("^ \\* [^@\n]+", Pattern.MULTILINE)
                                            .matcher(javaFileContent)
                                            .results()
                                            .map(MatchResult::group)
                                            .toArray(String[]::new);
            for(int i = 0; i < purposeLines.length; i++) {
                purposeLines[i] = String.join("", purposeLines[i].substring(2).split("\n"));
                if(purposeLines[i].charAt(purposeLines[i].length()-1) == ' ') {
                    purposeLines[i] = purposeLines[i].substring(0, purposeLines[i].length()-1);
                }
            }
            String programPurpose = String.join(" ", purposeLines);

            String PMR = "PROJECT TITLE: "  + projectClassTitle;
            System.out.println(PMR);
            
            
            programPurpose = wrapWordString(String.join(" ", ask("PURPOSE OF PROJECT: ", programPurpose)), "                    ", 90);
            
            String tPMR = String.join("\n",
                "DATE: " + (new SimpleDateFormat("MM/dd/yyyy")).format(new Date()),
                "AUTHOR: Jack Penn",
                "",
                "**************************** P M R *********************************************",
                ""
            );
            System.out.println(tPMR);
            PMR = String.join("\n",
                PMR,
                "PURPOSE OF PROJECT: " + programPurpose,
                tPMR
            );
                
            String pluses = ask("<+s>: <Reflect on what went well on the project. What did you learn that was new or surprised you?>");
            String plusesText = wrapWordString(pluses, "      ", 90);
            String minuses = ask("<-s>: <Where did you struggle? Did you have any bugs (logic errors) or was there a syntax error that \nyou had to figure out?>");
            String minusesText = wrapWordString(minuses, "      ", 90);
            System.out.println("*******************************************************************************");
            String futureReflection = ask("In the future: <Did what you learn make you think differently about problem solving? Now that you have \n learned a new skill, are there certain kinds of problems that you would approach \ndifferently?");
            String futureReflectionText = wrapWordString(futureReflection, "               ", 90);
                
            PMR+= String.join("\n",
                "",
                "<+s>: " + plusesText,
                "",
                "<-s>: " + minusesText,
                "*******************************************************************************",
                "In the future: " + futureReflectionText
            );
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
            
            System.out.println("************************************** Final PMR ******************************************");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            System.out.println(PMR);
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
                
            if(askYN("Write PMR to README.TXT?", true)) {
                String readmeFilePath = projectDir.getPath() + "\\" + "README.TXT";
                BufferedWriter writer = new BufferedWriter(new FileWriter(readmeFilePath));
                writer.write(PMR);
                writer.close();
                System.out.println("File Written!");
                System.out.println();
            }
        }
        System.out.println();
            
        //Academic Integrity ***********************************************************
        if(askYN("Create Academic Integrity? ", true)) {
            String javaPath = javaFile.getPath();
            Path txtPath = Paths.get(javaPath.substring(0, javaPath.length()-4) + "txt");
            System.out.println("Creating as: " + getLocalPath(txtPath.toString()));
            
            Files.copy(javaFile.toPath(), txtPath);

            System.out.println("Academic Integrity File Created!");
        }
        System.out.println();

        System.out.println("Program Exiting");
    }
    
    private static String ask(String question, String defaultAnswer)
    {
        String formattedDefault = wrapWordString(defaultAnswer, " ".repeat(question.length()), 76);
        System.out.println(question + (defaultAnswer.equals("") ? "" : (" <" + formattedDefault + ">")));
        String line = scanner.nextLine();
        
        if(line.equals("exit")) {
            System.out.println("Program Exiting");
            System.exit(0);
        }
        if(line.equals("")) {
            System.out.println("Submitted: " + defaultAnswer);
            return defaultAnswer;
        }
        return line;
    }
    private static String ask(String question) {return ask(question, "");}
    
    
    private static boolean askYN(String question, boolean defaultAnswer)
    {
        String answer = ask(question + " " + "(y/n)", defaultAnswer ? "y" : "n");
        return answer.toLowerCase().equals("y");
    }

    private static String wrapWordString(String str, String indentation, int lineCharacters) {
        str = String.join(" ", str.split("\\n"));
        str = String.join(" ", str.split("\\s\\s+"));
        if(str.length() <= lineCharacters) return str;
        
        String res = "";
        int start = 0;
        int end;
        for(end = lineCharacters-indentation.length(); end < str.length(); end+= lineCharacters - indentation.length()) {
            while(str.charAt(end) != ' ') end--;
            res += str.substring(start, end) + "\n" + indentation;
            start = end+1;
        }
        res+= str.substring(start);
        
        return res;
    }

    private static String toTwoDigitNum(String num) {
        if(num.length() == 1) {
            return "0" + num;
        }
        return num;
    }
    
    private static File[] findFilesOfExtension(File searchDir, String extension)
    {
        File[] files = searchDir.listFiles();
        files = Arrays.stream(files).filter(file -> file.getName().split("\\.", 2)[1].equals(extension)).toArray(File[]::new);
        return files;
    }
    
    private static String getLocalPath(String path)
    {
        return path.substring(baseUri.length());
    }
    private static String getLocalPath(File file) {return getLocalPath(file.getPath());}
}
