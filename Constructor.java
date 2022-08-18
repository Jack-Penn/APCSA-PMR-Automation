
/**
 * Write a description of class Method here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Constructor
{
    public String name;
    public Access access;
    public String parameters;
    
    public Constructor(String name, Access access, String parameters) {
        this.name = name;
        this.access = access;
        this.parameters = parameters;
    }
    public Constructor(String header) {
        String[] parts = header.trim().split(" ");
        this.access = Access.parseAccess(parts[0].trim());
        this.name = parts[1].trim().replaceAll("\\(.+", "");
        int sigIndex = header.indexOf("(");
        this.parameters = header.substring(sigIndex).replaceAll("\\).+", ")");
    }
    
    public String toString() {
        return access.getAccess() + " " + this.name + " " + this.parameters;
    }
    public String toSymbolString() {
        return access.getSymbol() + " " + this.name + " " + this.parameters;
    }
}
