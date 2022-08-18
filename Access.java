
/**
 * Write a description of class Access here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public enum Access {
    PRIVATE, PUBLIC, PROTECTED;
    
    public String getAccess() {
        switch(this) {
            case PRIVATE:
                return "private";
            case PUBLIC:
                return "public";
            case PROTECTED:
                return "protected";
        }
        return "";
    }
    
    public String getSymbol() {
        switch(this) {
            case PRIVATE:
                return "-";
            case PUBLIC:
                return "+";
            case PROTECTED:
                return "#";
        }
        return "";
    }
    
    public static Access parseAccess(String str) {
        switch(str) {
            case "private":
                return PRIVATE;
            case "public":
                return PUBLIC;
            case "protected":
                return PROTECTED;
        }
        return PUBLIC;
    }
    
    
}
