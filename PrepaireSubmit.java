import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Write a description of class PrepaireSubmit here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PrepaireSubmit
{    
    static Project selectedProject;
    
    private static void selectProject() {
        Project latestProject = APCSADir.getLatestProject();
        int optionNum = UserInput.askMenuOptions("How would you like to select a project folder?", new String[]{
            "Use Latest Assignment (" + latestProject.name + ")",
            "Specify Lesson Number",
            "Search Directory"
        });
        
        switch (optionNum) {
            case 0:
                selectedProject = latestProject;
                return;
            case 1:
                {
                    while(true) {
                        String projectString = UserInput.ask("Enter a project string (ex: 5.1)");
                        Project[] matchingProjects = APCSADir.getProjects(projectString);
                        switch(matchingProjects.length) {
                            case 0:
                                String[] parts = projectString.split("\\.");
                                int modNum = Integer.parseInt(parts[0]);
                                int lessonNum = Integer.parseInt(parts[1]);
                                System.out.println("Module " + modNum + " lesson " + lessonNum + " project not found");
                                System.out.println("Please try again");
                                break;
                            case 1:
                                selectedProject = matchingProjects[0];
                                return;
                            default:
                                selectedProject = selectFromMatchingProjects(matchingProjects);
                                return;
                        }
                    }
                }
                
            case 2:
                {
                    int latestModNum = latestProject.module.modNum;
                    int modNum = UserInput.ask("Choose a module to search (1-" +  latestModNum + ")", latestModNum);
                    Module selectedMod = APCSADir.getModule(modNum);
                    Project modLatestProject = selectedMod.getLatestProject();
                    
                    int letterIndex = 1;
                    System.out.println("Choose a Project");
                    System.out.println("  Assignments");
                    for(Project project : selectedMod.listProjects("Assignments")) {
                        System.out.println("   [" + Formatter.numToLetter(letterIndex)+ "] " + project.name);
                        letterIndex++;
                    }
                    System.out.println("  Lessons");
                    for(Project project : selectedMod.listProjects("Lessons")) {
                        System.out.println("   [" + Formatter.numToLetter(letterIndex) + "] " + project.name);
                        letterIndex++;
                    }
                    String response = UserInput.ask("Please select a menu option or enter project string", modLatestProject.name);
                    response = response.trim();
                    
                    //Retrieves project from and validates response
                    while(true) {
                        //handles default menu option
                        if(response.equals(modLatestProject.name)) {
                            selectedProject = modLatestProject;
                            return;
                        }
                        //handles menu letter input
                        if(Formatter.isLettersString(response) && response.length() == 1) {
                            int optionIndex = Formatter.letterToNum(response)-1;
                            if(optionIndex >= 0 && optionIndex < letterIndex) {
                                selectedProject = selectedMod.listProjects()[optionIndex];
                                return;
                            }
                        }else { //handles project string input
                            try {
                                Double.parseDouble(response); //tests if number string entered in
                                String selectedProjectString = response;
                                try { //tests if module number included
                                    Integer.parseInt(response);
                                    selectedProjectString = selectedMod.modNum + "." + response;
                                } catch(NumberFormatException e) {}
                                Project[] matchingProjects = APCSADir.getProjects(selectedProjectString);
                                selectedProject = selectFromMatchingProjects(matchingProjects);
                                return;
                            } catch(NumberFormatException e) {}
                        }
                        
                        response = UserInput.ask("Input not recognized. Please re-enter option letter (A-" + Formatter.numToLetter(letterIndex) + ") or project string", modLatestProject.name);
                    }
                }
        }
    }
    
    private static Project selectFromMatchingProjects(Project[] matchingProjects) {
        String[] projectOptions = new String[matchingProjects.length];
        for(int i = 0; i < matchingProjects.length; i++) {
            Project project = matchingProjects[i];
            projectOptions[i] = project.name + " " + (project.isAssignment? "(Assignment)" : "(Lesson)");
        }
        int projectIndex = UserInput.askMenuOptions("It appears that the project string matches multiple projects", projectOptions);
        return matchingProjects[projectIndex];
    }
    
    private static Program[] selectJavaPrograms(String prompt) throws IOException {
        Program[] projPrograms = selectedProject.listPrograms();
        String[] programOptions = new String[1+projPrograms.length];
        programOptions[0] = "All Java Files";
        for(int i = 0; i < projPrograms.length; i++) {
            programOptions[i+1] = projPrograms[i].name;
        }
        if(projPrograms.length > 1) {
            int[] selectedIndexes = UserInput.askMulitMenuOptions(prompt, programOptions, 0);
            System.out.println();
            
            if(selectedIndexes[0] == 0) {
                return projPrograms;
            }else {
                Program[] selectedPrograms = new Program[selectedIndexes.length];
                for(int i = 0; i < selectedIndexes.length; i++) {
                    selectedPrograms[i] = projPrograms[selectedIndexes[i]-1];
                }
                return selectedPrograms;
            }
        }else {
            return projPrograms;
        }
    }
    
    public static void main(String[] args) throws IOException
    {  
        System.out.println("Welcome to PrepaireSubmit!");
        System.out.println();
        
        selectProject();
        System.out.println();
        
        int optionNum = 0;
        while(optionNum != 5) {
            System.out.println("Using project " + selectedProject.name);
            optionNum = UserInput.askMenuOptions("Select what you'd like to do", new String[]{
                "Create PMR",
                "Create Academic Integrity .txt",
                "Create Class Diagram",
                "Open Selected Project",
                "Choose New Project",
                "Exit",
            });
            System.out.println();
            
            switch (optionNum) {
                case 0: //Create PMR
                    {
                        //Select Java Programs
                        Program[] selectedPrograms = selectJavaPrograms("Select which programs the PMR is for?");
                        String[] selectedProgramNames = new String[selectedPrograms.length];
                        for(int i = 0; i < selectedPrograms.length; i++) {
                            selectedPrograms[i].retrieveDocInfo();
                            selectedProgramNames[i] = selectedPrograms[i].name;
                        }
                        
                        //Project Title
                        String projectTitleString = selectedProject.name + " / " + String.join(", ", selectedProgramNames);
                        System.out.println("PROJECT TITLE: " + projectTitleString);
                        
                        //Project Purpose
                        String projectPurposeString;
                        String[] purposeOptions = new String[selectedPrograms.length+1];
                        purposeOptions[0] = "Write New";
                        for(int i = 0; i < selectedPrograms.length; i++) {
                            purposeOptions[i+1] = "(" + selectedPrograms[i].name +")" + selectedPrograms[i].purposeString;
                        }
                        int selectedPurposeOption = UserInput.askMenuOptions("Select a string to use a the project's purpose", purposeOptions, 0);
                        switch(selectedPurposeOption) {
                            case 0:
                                projectPurposeString = UserInput.ask("PURPOSE OF PROJECT");
                                break;
                            default:
                                projectPurposeString = selectedPrograms[selectedPurposeOption-1].purposeString;
                                System.out.println("PURPOSE OF PROJECT: " + projectPurposeString);
                                break;
                        }
                        
                        //Project Version
                        String projectDateString = new SimpleDateFormat("M/d/yy").format(new Date());
                        System.out.println("DATE: " + projectDateString);
                        ArrayList<Program> unmatchingDatePrograms = new ArrayList<Program>();
                        for(Program program : selectedPrograms) {
                            if(!program.dateString.equals(projectDateString)) {
                                unmatchingDatePrograms.add(program);
                            }
                        }
                        if(unmatchingDatePrograms.size() > 0) {
                            System.out.println("The @version date strings in the Java file(s) do not all match today's date \"" + projectDateString + "\"");
                            if(UserInput.askYN("Would you like to update the dates in these files?", true)) {
                                for(Program program : unmatchingDatePrograms) {
                                    program.setInfo("version", projectDateString);
                                }
                                System.out.println(unmatchingDatePrograms.size() + " file(s) @version info updated");
                            }
                        }
                        
                        //Project Author
                        String AUTHOR_NAME = "Jack Penn";
                        int updatedAuthors = 0;
                        for(Program program : selectedPrograms) {
                            if(!program.authorString.equals(AUTHOR_NAME)) {
                                program.setInfo("author", AUTHOR_NAME);
                                updatedAuthors++;
                            }
                        }
                        if(updatedAuthors > 0) {
                            System.out.println(updatedAuthors + " file(s) @author info updated to " + AUTHOR_NAME);
                        }
                        
                        int SCREEN_CHARACTERS = 85;
                        String posString = UserInput.ask(Formatter.useStartAsWrapIndent("<+s>: ", "<Reflect on what went well on the project. What did you learn that was new or surprised you?>", SCREEN_CHARACTERS));
                        String negString = UserInput.ask(Formatter.useStartAsWrapIndent("<-s>: ", "<Where did you struggle? Did you have any bugs (logic errors) or was there a syntax error that you had to figure out?>", SCREEN_CHARACTERS));
                        String futureReflectionString = UserInput.ask(Formatter.useStartAsWrapIndent("In the future: ", "<Did what you learn make you think differently about problem solving? Now that you have learned a new skill, are there certain kinds of problems that you would approach differently?", SCREEN_CHARACTERS));
                        System.out.println();
                        
                        //Combines PMR info into string
                        String PMRString = String.join("\n",
                            "PROJECT TITLE: " + projectTitleString,
                            Formatter.useStartAsWrapIndent("PURPOSE OF PROJECT: ", projectPurposeString, SCREEN_CHARACTERS),
                            "DATE: " + projectDateString,
                            "AUTHOR: " + AUTHOR_NAME,
                            "",
                            "**************************** P M R *********************************************",
                            "",
                            Formatter.useStartAsWrapIndent("<+s>: ", posString, SCREEN_CHARACTERS),
                            "",
                            Formatter.useStartAsWrapIndent("<-s>: ", negString, SCREEN_CHARACTERS),
                            "********************************************************************************",
                            Formatter.useStartAsWrapIndent("In the future: ", futureReflectionString, SCREEN_CHARACTERS)
                        );
                        
                        System.out.println();
                        System.out.println(PMRString);
                        
                        //Write PMR
                        if(UserInput.askYN("Write PMR to README.TXT?", true)) {
                            selectedProject.PMR.writeToFile(PMRString);
                            System.out.println("PMR written to README.TXT");
                            if(UserInput.askYN("Would you like to open the PMR file?", true)) {
                                selectedProject.PMR.open();
                            }
                        }
                        
                        //Academic Integrity
                        if(UserInput.askYN("Create Academic Integrity files for selected programs?", true)) {
                            for(Program program : selectedPrograms) {
                                program.createIntegrity();
                            }
                            System.out.println(selectedPrograms.length + " Academic Integrity files created/overwritten");
                        }
                        break;
                    }
                    
                case 1: //Create Academic Integrity .txt
                    {
                        Program[] programs = selectedProject.listPrograms();
                        
                        String[] javaFileOptions = new String[1 + programs.length];
                        javaFileOptions[0] = "All Java Files";
                        for(int i = 0; i < programs.length; i++) {
                            boolean alreadyExists = programs[i].integrityFile.exists();
                            javaFileOptions[i+1] = (alreadyExists ? "✅" : "❌") + " " + programs[i].name;
                        }
                        
                        int[] selectedIndexes = UserInput.askMulitMenuOptions("Which file(s) would you like create an Academic Integrity file for?", javaFileOptions, 0);
                        
                        Program[] selectedPrograms;
                        if(selectedIndexes[0] == 0) {
                            //All Java File Option Selected
                            ArrayList<Program> notAlreadyExistsPrograms = new ArrayList<Program>();
                            String alreadyExistsString = "(i.e. ";
                            for(Program program :  programs)
                            {
                                if(program.integrityFile.exists()) {
                                    alreadyExistsString += program.integrityFile.getName() + ", ";
                                }else {
                                    notAlreadyExistsPrograms.add(program);
                                }
                            }
                            alreadyExistsString = alreadyExistsString.substring(0, alreadyExistsString.length()-2);
                            alreadyExistsString += ")";
                            
                            int alreadyExistsCount = programs.length - notAlreadyExistsPrograms.size();
                            if(alreadyExistsCount != 0
                               && UserInput.askYN("Would you like to overwrite already created Integrity files " + alreadyExistsString, true)) {
                                selectedPrograms = programs;
                            }else {
                                selectedPrograms = notAlreadyExistsPrograms.toArray(Program[]::new);
                            }
                        }else {
                            selectedPrograms = new Program[selectedIndexes.length];
                            for(int i = 0; i < selectedIndexes.length; i++) {
                                selectedPrograms[i] = programs[i-1];
                            } 
                        }
                        
                        int alreadyExistsCount = 0;
                        for(Program program : selectedPrograms) {
                            if(program.integrityFile.exists()) {
                                alreadyExistsCount++;
                            }
                            program.createIntegrity();
                        }
                        int notAlreadyExistsCount = selectedPrograms.length - alreadyExistsCount;
                        System.out.println("Overwrote " + alreadyExistsCount + " and created " + notAlreadyExistsCount + " Academic Integrity files");
                        break;
                    }
                case 2: //Create Class Diagram
                    {
                        Program[] selectedPrograms = selectJavaPrograms("Select programs for the Class Diagram");
                        for(Program program : selectedPrograms) {
                            String projectName = FileHandler.getFileName(program.name);
                            program.retrieveDiagramInfo();
                        }
                        break;
                    }
                case 3: //Open Selected Project
                    selectedProject.open();
                    System.out.println("Opening project in bluej editor");
                    break;
                case 4: //Choose New Project
                    selectProject();
                    break;
                case 5: //Exit
                    System.out.println("Thank you for using this app!");
                    break;
            }
            System.out.println();
        }
    }
}
