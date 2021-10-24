import syntaxtree.*;
import visitor.GJDepthFirst;

public class SecondVisitor extends GJDepthFirst<AbstractTable,AbstractTable> {
    TypeDictionary dictionary = new TypeDictionary();
    String inClass;
    String inMethod;
    Boolean isReturning = false;
    String exRetType;
    //Boolean isAssign = false;
    Boolean isAssignLHS = false;
    Boolean isAssignRHS = false;
    String lhsType;
    String rhsName;

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
        isAssignLHS = true;

        //System.out.println("Class: " + inClass + "    Method: " + inMethod);
        //System.out.println("Left hand side var: " + leftSideName);

        ClassTable cTable = argTable.Global.classes.get(inClass);
        MethodTable mTable = cTable.methods.get(inMethod);

        //Checking if the LHS variable exisits
        try {
            //Check locals
            if(mTable.locals.get(leftSideName) == null) {
                boolean isFound = false;

                //Check parameters
                for (String paramsName: mTable.params.keySet()) {
                    //System.out.println(leftSideName + " vs " + paramsName);
                    if(leftSideName == paramsName){
                        isFound = true;
                        lhsType = mTable.params.get(leftSideName).dataType;
                        //System.out.println("Name: " + leftSideName + "  Type: " + lhsType);
                    }
                }

                //Check class
                if(!isFound){
                    for (String varsName: cTable.vars.keySet()) {
                        //System.out.println(leftSideName + " vs " + varsName);
                        if(leftSideName == varsName){
                            isFound = true;
                            lhsType = cTable.vars.get(leftSideName).dataType;
                            //System.out.println("Name: " + leftSideName + "  Type: " + lhsType);
                        }
                    }

                    //Check super class(es)
                    if(!isFound){
                        String superClass = cTable.super_class;
                        ClassTable tempTable;
                        while (superClass != null){
                            tempTable = argTable.Global.classes.get(superClass);
                            if(tempTable.vars.get(leftSideName) != null) {
                                isFound = true;
                                lhsType = tempTable.vars.get(leftSideName).dataType;
                                //System.out.println("Name: " + leftSideName + "  Type: " + lhsType);
                            }
                            superClass = tempTable.super_class;
                        }
                    }
                }
                if(!isFound) throw new Exception("ERROR - " + leftSideName + "does not exist in scope.");
            }
            else {
                lhsType = mTable.locals.get(leftSideName);
                //System.out.println("Name: " + leftSideName + "  Type: " + lhsType);
            }
        }
        catch(Exception e){
            //System.out.println("Type error on: " + leftSideName + " = " + rhsName + ",  LHS not found  " + inClass + "->" + inMethod);
            System.out.println("Type error");
            System.exit(1);
        }

        n.f0.accept(this, argTable);
        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);

        //Checking RHS variable
        //System.out.println("Checking the RHS");
        if (!lhsType.equals("Identifier")){ //Ignoring classes            
            try {
                //System.out.println("Left hand side var: " + leftSideName);
                //System.out.println("Right hand side var: " + rhsName);
                boolean isFound = false;

                //Check for counter or raw data
                if(rhsName.equals("Int") || rhsName.equals("Bool")) isFound = true;

                //Ignoring classes for now
                else if(lhsType.equals("Identifier")){
                    //System.out.println(leftSideName + " is of type Class" );
                    //leftSideName = rhsName;
                    isFound = true;
                }

                //Check Locals
                else if(mTable.locals.get(rhsName) == null) {

                    //Check parameters
                    for (String paramsName: mTable.params.keySet()) {
                        if(rhsName == paramsName){
                            isFound = true;
                            lhsType = mTable.params.get(rhsName).dataType;
                        }
                    }

                    //Check class vars
                    if(!isFound){
                        for (String varsName: cTable.vars.keySet()) {
                            if(rhsName == varsName){
                                isFound = true;
                                lhsType = cTable.vars.get(rhsName).dataType;
                            }
                        }

                        //Check class methods for "this"
                        if(cTable.methods.get(rhsName) != null) {
                            isFound = true;
                        }

                        //Check super class(es)
                        if(!isFound){
                            String superClass = cTable.super_class;
                            ClassTable tempTable;
                            while (superClass != null){
                                tempTable = argTable.Global.classes.get(superClass);
                                if(tempTable.vars.get(rhsName) != null) {
                                    isFound = true;
                                    lhsType = tempTable.vars.get(rhsName).dataType;
                                }

                                //Check super class(es) methods for "this"
                                if(cTable.methods.get(rhsName) != null) {
                                    isFound = true;
                                }

                                superClass = tempTable.super_class;
                            }
                        }
                    }

                    //Else not found
                    if(!isFound) throw new Exception("ERROR - " + rhsName + "does not exist in scope.");
                }

                //Else is found
                else {
                    isFound = true;
                    //System.out.println("Type OKAY: " + leftSideName + " = " + rhsName + ",  found in  " + inClass + "->" + inMethod);
                }

            }
            catch(Exception e){
                //System.out.println("Type error on: " + leftSideName + " = " + rhsName + ",  RHS not found  " + inClass + "->" + inMethod);
                System.out.println("Type error");
                System.exit(1);
            }
        }
        //System.out.println('\n');

        n.f3.accept(this, argTable);

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

        isReturning = false;

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
        }
        if(isAssignRHS){
            //System.out.println(n.f0.choice.getClass().getSimpleName());
            String rhsType = n.f0.choice.getClass().getSimpleName();
            if(rhsType.equals("IntegerLiteral")) rhsName = "Int";
            else if (rhsType.equals("TrueLiteral")) rhsName = "Bool";
            else if (rhsType.equals("FalseLiteral")) rhsName = "Bool";
        }

        n.f0.accept(this, argu);

        return _ret;
    }

    @Override
    public AbstractTable visit(Identifier n, AbstractTable argu) {
        AbstractTable _ret=null;
 
        if(isReturning) {
            //System.out.println("returning var" + n.f0.tokenImage);
            String returnName = n.f0.tokenImage;

            //Check locals for return type locals
            ClassTable cTable = argu.Global.classes.get(inClass);
            MethodTable mTable = cTable.methods.get(inMethod);

            //Ignore classes for now
            if(exRetType.equals("Identifier")){

            }
            else if(mTable.locals.get(returnName) != null) {
                String returnType = mTable.locals.get(returnName);
                returnType = dictionary.getRealType(returnType);
                exRetType = dictionary.getRealType(exRetType);

                try{
                    if(!returnType.equals(exRetType)) throw new Exception("ERROR - Invalid return type.");
                }
                catch(Exception e){
                    //System.out.println(returnType + " != " + exRetType);
                    System.out.println("Type error");
                    System.exit(1);
                }   
            }
            
        }

        if(isAssignLHS) {
            //System.out.println(lhsType + " vs " + n.f0.choice.getClass().getSimpleName());
            //System.out.println("LHS name: " + n.f0.tokenImage);
            //System.out.println("LHS ("+ n.f0.tokenImage+") in " + inClass + "->"+ inMethod);
            isAssignLHS = false;  
            isAssignRHS = true;          
        }
        else if(isAssignRHS){
            //System.out.println("RHS name: " + n.f0.tokenImage);
            rhsName = n.f0.tokenImage;   
            if(rhsName.equals("i")) rhsName = "Int";
            //if(rhsName.equals("val")) System.out.println("val found in " + inClass + "->"+ inMethod);
            //System.out.println("RHS ("+ n.f0.tokenImage+") in " + inClass + "->"+ inMethod);
            isAssignRHS = false;
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

