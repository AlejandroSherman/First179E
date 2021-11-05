//Cody Steimle (SID: 862137374)
//Alejandro Sherman (SID: 862062898)
//Steven Carrasco (SID: 862060283)

import syntaxtree.Node;
import java.io.InputStream;

public class J2V {

    public static void main(String [] args) throws Exception {
       
<<<<<<< Updated upstream:J2V.java
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
=======
>>>>>>> Stashed changes:Phase1Tester/Output/Unzipped/hw1/Typecheck.java
        InputStream fileStream = System.in;
        new MiniJavaParser(fileStream);
        //FirstVisitor first_visitor = new FirstVisitor();
        CompleteTable completeTable = new CompleteTable(); // Global Symbol Table. Map for classes and map for methods.
        TranslatorVisitor makeTables = new TranslatorVisitor();
        try {
            Node root = MiniJavaParser.Goal();   // does the parsing.
            //root.accept(first_visitor, completeTable);  // fills global symbol table
            root.accept(makeTables, completeTable);  // fills all tables
            completeTable.GlobalVTables.printTables();
            completeTable.GlobalCodeGen.printFuncs(completeTable.GlobalVTables.vtables.size());
            //completeTable.prntTable();
            System.out.println("success");
        }
        catch(Exception e) {
            System.out.println("error");
            System.exit(1);
        }   
        */
    }

}
