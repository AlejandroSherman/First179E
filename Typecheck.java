import java.io.InputStream;
import syntaxtree.*;

public class Typecheck {
    public static void main(String[] args) {

        InputStream fileStream = System.in; //passes in file from command line piping (java Typecheck < [filename].java)
        new MiniJavaParser(fileStream); //MiniJavaParser is a singleton

        FirstVisitor first = new FirstVisitor(); //creates the first visitor
        GlobalTable Global = new GlobalTable();  //creates the Global symbol table
        //Need to create the second Visitor creation

        try {
            Node root = MiniJavaParser.Goal();
            //root.accept(first, Global); //Currently have no clue why this doesn't accept the FirstVisitor
            //root.accept(second, Global);
            System.out.println("Program type checked successfully");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //SymbolTable testTable = new SymbolTable("a","int");
        //System.out.println(testTable.tempTable);

        //termination check
        //System.out.println("Got to the end of main.");
    }
}