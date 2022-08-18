
/**
 * Write a description of class Formatter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Formatter
{
    public static String twoDigitNumString(String numString)
    {
        if(numString.length() == 1)
            return "0" + numString;
        return numString;
    }
    public static String twoDigitNumString(int num)
    {
        return twoDigitNumString(num+"");
    }
    
    public static boolean isLettersString(String str) {
        for(int i = 0; i < str.length(); i++) {
            if(!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static int letterToNum(String letter) {
        return letter.toUpperCase().charAt(0) - 64;
    }
    public static char numToLetter(int num) {
        return (char)(64 + num);
    }
    
    public static String stringToWhitespace(String str)
    {
        return " ".repeat(str.length());
    }
    public static String wrapWordString(String str, String indentation, int lineCharacters)
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
    public static String useStartAsWrapIndent(String start, String str, int lineCharacters)
    {
        return wrapWordString(start + str, stringToWhitespace(start), lineCharacters);
    }
}
