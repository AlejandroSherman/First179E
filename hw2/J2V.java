//Cody Steimle (SID: 862137374)
//Alejandro Sherman (SID: 862062898)
//Steven Carrasco (SID: 862060283)

import syntaxtree.Node;
import java.io.InputStream;

public class J2V {

    public static void main(String [] args) throws Exception {
       
        InputStream fileStream = System.in;
        new MiniJavaParser(fileStream);
        CompleteTable completeTable = new CompleteTable();
        FirstVisitor firstVisitor = new FirstVisitor(); // Global Symbol Table. Map for classes and map for methods.
        VTableVisitor vtableVisitor = new VTableVisitor();
        TranslatorVisitor translator = new TranslatorVisitor();
        try {
            Node root = MiniJavaParser.Goal();   // does the parsing.
            root.accept(firstVisitor, completeTable);  // fills symbol table
            root.accept(vtableVisitor, completeTable);  // fills vtables
            root.accept(translator, completeTable);  // fills vtables
            completeTable.GlobalVTables.printTables();
            completeTable.GlobalCodeGen.printFuncs(completeTable.GlobalVTables.vtables.size());
            //System.out.println("success");
        }
        catch(Exception e) {
            System.out.println("error");
            System.exit(1);
        }   
    }

}
