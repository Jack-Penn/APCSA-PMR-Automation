
/**
 * Write a description of class Method here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Method
{
    public String name;
    public boolean isStatic;
    public String returnType;
    public Access access;
    public String parameters;
    
    public Method(String name, String returnType, Access access, String parameters) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.access = access;
    }
    public Method(String header) {
        String[] parts = header.trim().split(" ");
        this.access = Access.parseAccess(parts[0].trim());
        this.returnType = parts[1].trim();
        if(parts[2].equals("static")) { //static method
            this.isStatic = true;
            this.name = parts[3];
        }else {
            this.isStatic = false;
            this.name = parts[2];
        }
        this.name = this.name.trim().replaceAll("\\(.+", "");
        int sigIndex = header.indexOf("(");
        this.parameters = header.substring(sigIndex).replaceAll("\\).+", ")");
        
        //System.out.println("Parsed:\""+ this.toString() + "\"   Header:\"" + header + "\"");
    }
    
    public String toString() {
        return access.getAccess() + " " + (isStatic? "static": "") + this.returnType + " " + this.name + " " + this.parameters;
    }
    public String toSymbolString() {
        return access.getSymbol() + " " + (isStatic? "static": "") + this.returnType + " " + this.name + " " + this.parameters;
    } 
}
