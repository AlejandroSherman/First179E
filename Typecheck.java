//import SymbolTable;
import java.io.InputStream;
import syntaxtree.*;
import visitor.*;

public class Typecheck {
    public static void main(String[] args) {

        InputStream fileStream = System.in; //passes in file from command line piping (java Typecheck < [filename].java)
        new MiniJavaParser(fileStream); //MiniJavaParser is a singleton

        //Need to create the first visitor
        //Need to create the Global symbol table
        //Need to create the second Visitor creation

        try {
            Node root = MiniJavaParser.Goal();
            //Scope environment = new Scope(root);
            //root.accept(new FirstVisitor(), environment);
            //root.accept(new SecondVisitor(), environment);
            System.out.println ("Program type checked successfully");

        } catch (ParseException e) {
            //e.printStackTrace();
            System.out.println("Type error");
        }

        //SymbolTable testTable = new SymbolTable("a","int");
        //System.out.println(testTable.tempTable);

        //termination check
        //System.out.println ("Got to the end of main.");
    }
}