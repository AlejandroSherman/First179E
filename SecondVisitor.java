import syntaxtree.*;
import visitor.GJDepthFirst;

public class SecondVisitor extends GJDepthFirst<AbstractTable,AbstractTable> {
    TypeDictionary dictionary = new TypeDictionary();
    String inClass;
    String inMethod;
    Boolean isReturning = false;
    Boolean isVarReturning = false;
    String exRetType;
    String returnName;

    @Override
    public AbstractTable visit(MainClass n, AbstractTable argTable){
        ClassTable classTable = argTable.Global.classes.get(n.f1.f0.tokenImage);
        n.f0.accept(this, classTable);
        n.f1.accept(this, classTable);
        n.f2.accept(this, classTable);

        MethodTable methodTable = classTable.methods.get("main");
        n.f4.accept(this, methodTable);
        n.f5.accept(this, methodTable);
        n.f6.accept(this, methodTable);
        n.f7.accept(this, methodTable);
        n.f8.accept(this, methodTable);
        n.f9.accept(this, methodTable);
        n.f10.accept(this, methodTable);
        n.f11.accept(this, methodTable);
        n.f12.accept(this, methodTable);
        n.f13.accept(this, methodTable);
        n.f14.accept(this, methodTable);
        n.f15.accept(this, methodTable);
        n.f16.accept(this, classTable);

        inClass = "Main";
        inMethod = "main";

        return null;
    }
 
    @Override
    public AbstractTable visit(AssignmentStatement n, AbstractTable argTable){
        String leftSideName = n.f0.f0.tokenImage;

        //System.out.println("Class: " + inClass + "    Method: " + inMethod);
        //System.out.println("Left hand side var: " + leftSideName);

        ClassTable cTable = argTable.Global.classes.get(inClass);
        MethodTable mTable = cTable.methods.get(inMethod);

        try { 
            if(mTable.locals.get(leftSideName) == null) {
                boolean isFound = false;
                //Check parameters
                for (String paramsName: mTable.params.keySet()) {
                    //System.out.println(leftSideName + " vs " + paramsName);
                    if(leftSideName == paramsName){
                        isFound = true;
                        //System.out.println("I was called");
                    }
                }

                //Check class
                if(!isFound){
                    for (String varsName: cTable.vars.keySet()) {
                        //System.out.println(leftSideName + " vs " + varsName);
                        if(leftSideName == varsName){
                            isFound = true;
                            //System.out.println("I was called");
                        }
                    }

                    //Check super class(es)
                    if(!isFound){
                        String superClass = cTable.super_class;
                        ClassTable tempTable;
                        while (superClass != null){
                            tempTable = argTable.Global.classes.get(superClass);
                            if(tempTable.vars.get(leftSideName) != null) isFound = true;
                            superClass = tempTable.super_class;
                        }
                    }
                }
                if(!isFound) throw new Exception("ERROR - " + leftSideName + "does not exist in scope.");
            }
        }
        catch(Exception e){
            //System.out.println("Class: " + inClass + "    Method: " + inMethod);
            //System.out.println("Left hand side var: " + leftSideName);
            System.out.println("Type error");
            System.exit(1);
        }

        return null;
    }

    @Override
    public AbstractTable visit(MethodDeclaration n, AbstractTable argTable) {
        inMethod = n.f2.f0.tokenImage;
        ClassTable cTable = argTable.Global.classes.get(inClass);
        MethodTable mTable = cTable.methods.get(inMethod);

        //System.out.println("called for method: " + inMethod);
        
        AbstractTable _ret=null;
        n.f0.accept(this, argTable);
        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);
        n.f3.accept(this, argTable);
        n.f4.accept(this, argTable);
        n.f5.accept(this, argTable);
        n.f6.accept(this, argTable);
        n.f7.accept(this, argTable);
        n.f8.accept(this, argTable);
        n.f9.accept(this, argTable);

        isReturning = true;
        exRetType = mTable.returnType;

        n.f10.accept(this, argTable);
        n.f11.accept(this, argTable);
        n.f12.accept(this, argTable);
        return _ret;
    }



    @Override
    public AbstractTable visit(PrimaryExpression n, AbstractTable argu) {
        AbstractTable _ret=null;
        
        if(isReturning) {
            String returningType = n.f0.choice.getClass().getSimpleName();
            //Find type for indentifiers
            if(returningType.equals("Identifier") || returningType.equals("ThisExpression")){
                //Returning a varaiable or a class
                //System.out.println("Needed type      " + return_Type);
                //System.out.println(returningType + " vs " + exRetType);
            }
            else {
                //System.out.println(returningType + " vs " + exRetType);
                //System.out.println("checking " + inMethod);
                returningType = dictionary.getRealType(returningType);
                exRetType = dictionary.getRealType(exRetType);
                //System.out.println(returningType + " vs " + exRetType);

                if(returningType!=null && exRetType!=null){
                    try{
                        if(!(returningType.equals(exRetType))) throw new Exception("ERROR - Invalid return type.");
                    }
                    catch(Exception e){
                        System.out.println("Type error");
                        System.exit(1);
                    }   
                } 
            }
            isReturning = false;
        }

        n.f0.accept(this, argu);

        return _ret;
     }

//Everything below this line is redoing first pass stuff to get class and method names
/**************************************************************************************/
    @Override
    public AbstractTable visit(ClassDeclaration n, AbstractTable argTable) {
        inClass = n.f1.f0.tokenImage;
        AbstractTable _ret=null;
        n.f0.accept(this, argTable);
        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);
        n.f3.accept(this, argTable);
        n.f4.accept(this, argTable);
        n.f5.accept(this, argTable);
        return _ret;
    }

}  

