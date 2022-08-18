import java.util.Scanner;

/**
 * Write a description of class UserInput here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class UserInput
{
    static Scanner kb = new Scanner(System.in);
    
    public static String ask(String question, String defaultAnswer)
    {
        String formattedQuestion = question + " <" + defaultAnswer + ">";
        String response = ask(formattedQuestion);
        if(response.isEmpty())
        {
            System.out.println("  Submitted Default: " + defaultAnswer);
            return defaultAnswer;
        }
        return response;
    }
    public static int ask(String question, int defaultAnswer) {
        String response = ask(question, defaultAnswer + "");
        while(true) {
            try {
                int num = Integer.parseInt(response);
                return num;
            }
            catch(NumberFormatException e) {
                ask("Input not recognized. Please enter in an integer", defaultAnswer);
            }
        }
    }
    public static String ask(String question)
    {
        System.out.print(question + ": ");
        return kb.nextLine();
    }
    
    public static boolean askYN(String question, boolean defaultAnswer)
    {
        String formattedQuestion = question + " (Y/N)";
        return ask(formattedQuestion, (defaultAnswer? "Y" : "N")).equalsIgnoreCase("Y");
    }
    public static boolean askYN(String question, String defaultAnswer)
    {
        return askYN(question, defaultAnswer.equalsIgnoreCase("Y"));
    }
    
    public static int askMenuOptions(String question, String[] options, int defaultIndex)
    {
        System.out.println(question);
        for(int i = 0; i < options.length; i++)
        {
            System.out.println("  [" + Formatter.numToLetter(i + 1) + "] " + options[i]);
        }
        String defaultOption = Formatter.numToLetter(defaultIndex+1)+"";
        String response = ask("Please select a menu option", defaultOption);
        while(true) {
            if(response.trim().length() == 1) {
                int optionIndex = Formatter.letterToNum(response)-1;
                if(optionIndex >= 0 && optionIndex < options.length) {
                    return optionIndex;
                }
            }
            response = ask("Input not recognized. Please re-enter option letter (A-" + Formatter.numToLetter(options.length) + ")", defaultOption);
        }
    }
    public static int askMenuOptions(String question, String[] options)
    {
        return askMenuOptions(question, options, 0);
    }
    public static int[] askMulitMenuOptions(String question, String[] options, int defaultIndex) {
        System.out.println(question);
        for(int i = 0; i < options.length; i++)
        {
            System.out.println("  [" + Formatter.numToLetter(i + 1) + "] " + options[i]);
        }
        String defaultOption = Formatter.numToLetter(defaultIndex+1)+"";
        String response = ask("You may select one or multiple menu options (ABC)", defaultOption);
        while(true) {
            if(Formatter.isLettersString(response.trim())) {
                boolean validLetters = true;
                
                int[] selectedIndexes = new int[response.length()];
                String[] selectedLetters = response.split("");
                for(int i = 0; i < selectedLetters.length; i++) {
                    selectedIndexes[i] = Formatter.letterToNum(selectedLetters[i])-1;
                    if(selectedIndexes[i] < 0 && selectedIndexes[i] >= options.length) {
                        validLetters = false;
                        break;
                    }
                }
                if(validLetters) {
                    return selectedIndexes;
                }
            }
            response = ask("Input not recognized. Please re-enter option letter(s) (A-" + Formatter.numToLetter(options.length) + ")", defaultOption);
        }
    }
}
