//Cody Steimle (SID: 862137374)
//Alejandro Sherman (SID: 862062898)
//Steven Carrasco (SID: 862060283)

import syntaxtree.Node;
import java.io.InputStream;

public class J2V {

    public static void main(String [] args) throws Exception {
       
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
    }

}
