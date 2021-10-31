//Cody Steimle (SID: 862137374)
//Alejandro Sherman (SID: 862062898)
//Steven Carrasco (SID: 862060283)

import syntaxtree.Node;
import java.io.InputStream;

public class J2V {

    public static void main(String [] args) throws Exception {
       
        InputStream fileStream = System.in;
        new MiniJavaParser(fileStream);
        FirstVisitor first_visitor = new FirstVisitor();
        CompleteTable completeTable = new CompleteTable(); // Global Symbol Table. Map for classes and map for methods.
        MakeVTables makeTables = new MakeVTables();
        try {
            Node root = MiniJavaParser.Goal();   // does the parsing.
            //root.accept(first_visitor, completeTable);  // fills global symbol table
            root.accept(makeTables, completeTable);  // fills all tables
            //completeTable.prntTable();
            completeTable.GlobalVTables.printTables();
            System.out.println("success");
        }
        catch(Exception e) {
            System.out.println("error");
            System.exit(1);
        }   


       //Below is Phase 1 code
        /*
        //read in file via <
        InputStream fileStream = System.in;
        new MiniJavaParser(fileStream);
        FirstVisitor first_visitor = new FirstVisitor();
        SecondVisitor second_visitor = new SecondVisitor();
        CompleteTable completeTable = new CompleteTable(); // Global Symbol Table. Map for classes and map for methods.
        try {
            Node root = MiniJavaParser.Goal();   // does the parsing.
            root.accept(first_visitor, completeTable);  // fills global symbol table.
            //completeTable.prntTable();
            root.accept(second_visitor, completeTable); // does the type checking.
            System.out.println("Program type checked successfully");
        }
        catch(Exception e) {
            System.out.println("Type error");
            System.exit(1);
        }   
        */
    }

}
