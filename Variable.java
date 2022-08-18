
/**
 * Write a description of class Variable here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Variable
{
    public String name;
    public String type;
    public Access access;
    
    
    public Variable(String name, String type, Access access) {
        this.name = name;
        this.type = type;
        this.access = access;
    }
    public Variable(String signature) {
        String[] parts = signature.trim().split(" ");
        if(parts.length == 3) {
            this.access = Access.parseAccess(parts[0].trim());
            this.type = parts[1].trim();
            this.name = parts[2].trim();
            if(this.name.endsWith(";")) { //removes trailing semicolon
                this.name = this.name.substring(0, this.name.length()-1);
            }
        }else {
            System.err.println("Something weird happened with Variable class sig: " + signature);
        }
    }
    
    public String toString() {
        return access.getAccess() + " " + this.type + " " + this.name;
    }
    public String toSymbolString() {
        return access.getSymbol() + " " + this.type + " " + this.name;
    }
}
