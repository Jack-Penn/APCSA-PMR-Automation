import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Write a description of class Program here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Program
{
    public File dir;
    public Project project;
    public String name;
    public File integrityFile;
    
    public String purposeString;
    public String authorString;
    public String dateString;
    
    public Variable[] variables;
    public Method[] methods;
    public Constructor[] constructors;
    
    public Program(File dir, Project project) throws java.io.FileNotFoundException {
        this.dir = dir;
        this.project = project;
        this.name = dir.getName();
        this.integrityFile = FileHandler.getSubDir(this.project.dir, FileHandler.getFileName(this.name) + ".txt");
    }
    
    public void createIntegrity() throws java.io.IOException {
        FileHandler.copyFile(this.dir, this.integrityFile);
    }
    
    public void retrieveDocInfo() throws java.io.FileNotFoundException {
        Scanner inFile = new Scanner(dir);
        this.purposeString = "";
        String line = inFile.nextLine();
        while(inFile.hasNextLine() && line.indexOf("@") == -1)
        {
            int indentIndex = line.indexOf(" *");
            if(indentIndex >= 0)
            {
                this.purposeString += line.substring(indentIndex+2).trim() + " ";
            }
            /*
            else if(this.purposeString.length() > 0)
            {
                break;
            }
            */
            line = inFile.nextLine();
        }
        System.out.println(this.name + ":");
        while(inFile.hasNextLine())
        {
            int labelIndex = line.indexOf("@author");
            if(labelIndex >= 0) {
                this.authorString = line.substring(labelIndex+7).trim();
                break;
            }
            line = inFile.nextLine();
        }
        while(inFile.hasNextLine())
        {
            int labelIndex = line.indexOf("@version");
            if(labelIndex >= 0)
            {
                this.dateString = line.substring(labelIndex+8).trim();
                break;
            }
            line = inFile.nextLine();
        }
    }
    
    public void setInfo(String labelString, String str) throws java.io.IOException {
        String fileString = FileHandler.readFileAsString(this.dir);
        fileString = fileString.replaceAll("@" + labelString + ".+", "@" + labelString + " " + str);
        FileHandler.writeFile(fileString, this.dir);
    }
    
    public void retrieveDiagramInfo() throws java.io.FileNotFoundException {
        Scanner inFile = new Scanner(dir);
        String line = inFile.nextLine();
        
        ArrayList<Variable> programVars = new ArrayList<Variable>();
        ArrayList<Method> programMethods = new ArrayList<Method>();
        ArrayList<Constructor> programConstructors = new ArrayList<Constructor>();
        while(inFile.hasNextLine())
        {
            if(line.startsWith("    p")) {
                line = line.trim();
                if(line.contains("(")) { //determines has function signature
                    //determines if constructor or method counting function modifiers
                    String[] parts = line.split(" ");
                    int i;
                    for(i = 0; i < parts.length; i++) {
                        if(parts[i].contains("(")) { //finds function name
                            break;
                        }
                    }
                    if(i > 1) {//class method
                        programMethods.add(new Method(line));
                    }else { //function identified as constructor
                        programConstructors.add(new Constructor(line));
                    }
                }else { //parses as variable
                    String[] vars = line.split(",");
                    Variable baseVar = new Variable(vars[0]);
                    programVars.add(baseVar);
                    for(int i = 1; i < vars.length; i++) {
                        if(vars[i].endsWith(";")) { //removes trailing semicolon
                            vars[i] = vars[i].substring(0, vars[i].length()-1);
                        }
                        programVars.add(new Variable(vars[i].trim(), baseVar.type, baseVar.access));
                    }
                }
            }
            line = inFile.nextLine();
        }
        
        variables = programVars.toArray(Variable[]::new);
        System.out.println("Variables");
        for(Variable var: variables) {
            System.out.println(" " + var.toSymbolString());
        }
        methods = programMethods.toArray(Method[]::new);
        System.out.println("Methods");
        for(Method method: methods) {
            System.out.println(" " + method.toSymbolString());
        }
        constructors = programConstructors.toArray(Constructor[]::new);
        System.out.println("Constructors");
        for(Constructor constructor: constructors) {
            System.out.println(" " + constructor.toSymbolString());
        }
    }
}
