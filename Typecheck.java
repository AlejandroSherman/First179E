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
            //root.accept(first, table);
            //root.accept(second, table);
            System.out.println ("Program type checked successfully");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //SymbolTable testTable = new SymbolTable("a","int");
        //System.out.println(testTable.tempTable);

        //termination check
        System.out.println ("Got to the end of main.");
    }
}