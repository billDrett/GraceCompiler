package compiler;

import com.sun.org.apache.regexp.internal.RE;
import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.util.*;

public class Print extends DepthFirstAdapter
{
    private SymbolTable symTable = new SymbolTable();
    private ArrayList<String> fparVars = new ArrayList<>();
    private LinkedList<Integer> dimList = new LinkedList<Integer>();
    private LinkedList<Record> fParam = new LinkedList<>();
    private String tmpVarType;
    private Stack<RecType> typeStack = new Stack<>();
    private Stack<String> returnStatement = new Stack<>();
    boolean returnFound =false;

    @Override
    public void inAProgram(AProgram node){
        System.out.println("In program");
    }

    @Override
    public void outAProgram(AProgram node){
        System.out.println("Type Stack contains "+typeStack.size()+" elements");
        for(RecType recType : typeStack)
        {
            System.out.println("\t"+recType.getType()+" "+recType.getDimensions());
        }
        System.out.println("Out program");
    }

    @Override
    public void inAFunDefinition(AFunDefinition node) {
        System.out.println("In FunDefinition");
        symTable.enter();
    }

    @Override
    public void outAFunDefinition(AFunDefinition node) {
        System.out.println("Out FunDefinition");
        String returnType;

        symTable.printALl();
        symTable.exit();

        returnType = returnStatement.pop();
        if(!returnFound && !returnType.equals("nothing")) //return not found and the type return type was int or char
        {
            System.out.println("Error no return statement found");
        }

        returnFound = false;
    }

    @Override
    public void inASemiStatement(ASemiStatement node) {
        System.out.println("In SemiStatement");
    }
    @Override
    public void inAAssignStatement(AAssignStatement node) {
        System.out.println("In AssignStatement");
    }

    @Override
    public void outAAssignStatement(AAssignStatement node) {
        System.out.println("Out AssignStatement");
        RecType leftType, rightType, recType;

        System.out.println(typeStack.size());

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(rightType.getType().trim().equals(leftType.getType().trim()) && rightType.getDimensions() == leftType.getDimensions())
        {
            System.out.println("Compatible types");
        }
        else
        {
            System.out.println("Conflict in assignment: Cannot convert "+rightType.getType().trim()+" "+rightType.getDimensions()+ " to "+leftType.getType().trim()+" "+leftType.getDimensions());
        }
    }

    @Override
    public void inABlockStatement(ABlockStatement node) {
      //  System.out.println("In BlockStatement");
    }

    @Override
    public void inAFuncCallStatement(AFuncCallStatement node) {
      //  System.out.println("In FuncCallStatement");
    }

    @Override
    public void outAIfWithoutElseStatement(AIfWithoutElseStatement node) {
        System.out.println("Out IfWithoutElseStatement");
        RecType recType = typeStack.pop();

        if(recType.getType().equals("bool"))
        {
            System.out.println("Condition ifWithoutElse is Correct");
        }
        else
        {
            System.out.println("Conflict in ifWithoutElse Condition :"+recType.getType());
        }

        System.out.println("Condition is "+recType.getType());

    }

    @Override
    public void outAIfWithElseStatement(AIfWithElseStatement node) {
        System.out.println("Out IfWithElseStatement");
        RecType recType = typeStack.pop();

        if(recType.getType().equals("bool"))
        {
            System.out.println("Condition IfWithElseStatement is Correct");
        }
        else
        {
            System.out.println("Conflict in IfWithElseStatement Condition :"+recType.getType());
        }

        System.out.println("Condition is "+recType.getType());
    }

    @Override
    public void outAWhileStatement(AWhileStatement node) {
        System.out.println("Out WhileStatement");
        RecType recType = typeStack.pop();

        if(recType.getType().equals("bool"))
        {
            System.out.println("Condition WhileStatement is Correct");
        }
        else
        {
            System.out.println("Conflict in WhileStatement Condition :"+recType.getType());
        }

        System.out.println("Condition is "+recType.getType());
    }

    @Override
    public void outAReturnStatement(AReturnStatement node) {
        System.out.println("Out ReturnStatement");
        String returnType = returnStatement.pop();

        if(node.getExpression() == null)
        {
            if(returnType.equals("nothing"))
            {
                System.out.println("The return is correct "+returnType);
            }
            else
            {
                System.out.println("Error the return has different type "+returnType+ " not nothing");
            }
        }
        else
        {
            RecType recType = typeStack.pop();
            if(recType.getDimensions() != 0)
            {
                System.out.println("Error the return has dimensions");
            }
            else if (!recType.getType().equals(returnType))
            {
                System.out.println("Error the return has different type "+recType.getType());
            }
            else
            {
                System.out.println("The return is correct "+returnType);
            }
        }
        returnStatement.push(returnType);
        returnFound = true;

    }

    @Override
    public void inAFunDefLocalDef(AFunDefLocalDef node) {
    //    System.out.println("In FunDefLocalDef");
    }
    @Override
    public void outAFunDeclLocalDef(AFunDeclLocalDef node) {
        System.out.println("Out FunDeclLocalDef");
        returnStatement.pop();
        returnFound = false;
    }

    @Override
    public void inAVarDefLocalDef(AVarDefLocalDef node) {
      //  System.out.println("In AVarDefLocalDef");
    }

    @Override
    public void outAOrCondition(AOrCondition node) {
        System.out.println("Out AOrCondition "+ node.getLeft()+" or "+node.getRight());

        RecType rightType, leftType, recType;
        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(rightType.getType().equals("bool") && leftType.getType().equals("bool"))
        {
            System.out.println("Condition Or is Correct");
        }
        else
        {
            System.out.println("Conflict in Or Condition :"+rightType.getType()+" "+leftType.getType());
        }

        recType = new RecType("bool", 0);
        typeStack.push(recType);
    }

    @Override
    public void outAAndCondition(AAndCondition node) {
        System.out.println("Out AAndCondition "+ node.getLeft()+" and "+node.getRight());

        RecType rightType, leftType, recType;
        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(rightType.getType().equals("bool") && leftType.getType().equals("bool"))
        {
            System.out.println("Condition And is Correct");
        }
        else
        {
            System.out.println("Conflict in And Condition :"+rightType.getType()+" "+leftType.getType());
        }

        recType = new RecType("bool", 0);
        typeStack.push(recType);
    }

    @Override
    public void outANotCondition(ANotCondition node) {
        System.out.println("Out ANotCondition "+ " not "+node.getCondition());

        RecType tempType, recType;
        tempType = typeStack.pop();

        if(tempType.getType().equals("bool"))
        {
            System.out.println("Condition Not is Correct");
        }
        else
        {
            System.out.println("Conflict in Not Condition :"+tempType.getType());
        }

        recType = new RecType("bool", 0);
        typeStack.push(recType);
    }

    @Override
    public void inARelatCondition(ARelatCondition node) {
      //  System.out.println("In ARelatCondition "+node.getLeft()+" "+node.getSymbol()+ ""+node.getRight());
    }

    @Override
    public void outARelatCondition(ARelatCondition node) {
        System.out.println("Out ARelatCondition "+node.getLeft()+" "+node.getSymbol()+ ""+node.getRight());

        RecType rightType, leftType, recType;
        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if((rightType.getType().equals(leftType.getType())) && rightType.getDimensions() == 0 && leftType.getDimensions() == 0)
        {
            System.out.println("Condition Relat is Correct");
        }
        else
        {
            System.out.println("Conflict in Condition Relat :"+rightType.getType()+" "+rightType.getDimensions()+" _ "+leftType.getType()+" "+leftType.getDimensions());
        }

        recType = new RecType("bool", 0);
        typeStack.push(recType);

    }
    @Override
    public void inAEqualRelationOper(AEqualRelationOper node) {
      //  System.out.println("In AEqualRelationOper");
    }

    @Override
    public void inANEqualRelationOper(ANEqualRelationOper node) {
      //  System.out.println("In ANEqualRelationOper");
    }
    @Override
    public void inALessRelationOper(ALessRelationOper node) {
    //    System.out.println("In ALessRelationOper");
    }

    @Override
    public void inALessEqualRelationOper(ALessEqualRelationOper node) {
      //  System.out.println("In ALessEqualRelationOper");
    }
    @Override
    public void inAGreaterRelationOper(AGreaterRelationOper node) {
      //  System.out.println("In AGreaterRelationOper");
    }

    @Override
    public void inAGreaterEqualRelationOper(AGreaterEqualRelationOper node) {
      //  System.out.println("In AGreaterEqualRelationOper");
    }

    @Override
    public void inAExprList(AExprList node){
      //  System.out.println("In ExprList");
    }

    @Override
    public void outAExprList(AExprList node){
      //  System.out.println("Out ExprList");
    }


    @Override
    public void inAPlusExpression(APlusExpression node) {
    //    System.out.println("In APlusExpression ");
    }

    @Override
    public void outAPlusExpression(APlusExpression node) {
        System.out.println("Out APlusExpression "+ node.getLeft()+" + " + node.getRight());
        RecType leftType, rightType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(rightType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in plus expression: type char");
        }
        else if(rightType.getDimensions() != 0)
        {
            System.out.println("Conflict in plus expression: type array");
        }

        if(leftType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in plus expression: type char");
        }
        else if(leftType.getDimensions() != 0)
        {
            System.out.println("Conflict in plus expression: type array");
        }

        recType = new RecType("int", 0);
        typeStack.push(recType);
    }

    @Override
    public void inAMinusExpression(AMinusExpression node) {
      //  System.out.println("In AMinusExpression ");
    }

    @Override
    public void outAMinusExpression(AMinusExpression node) {
        System.out.println("Out AMinusExpression "+ node.getLeft()+" - " + node.getRight());
        RecType leftType, rightType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(rightType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in minus expression: type char");
        }
        else if(rightType.getDimensions() != 0)
        {
            System.out.println("Conflict in minus expression: type array");
        }

        if(leftType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in minus expression: type char");
        }
        else if(leftType.getDimensions() != 0)
        {
            System.out.println("Conflict in minus expression: type array");
        }

        recType = new RecType("int", 0);
        typeStack.push(recType);
    }


    @Override
    public void inAMultExpression(AMultExpression node) {
      //  System.out.println("In AMultExpression");
    }

    @Override
    public void outAMultExpression(AMultExpression node) {
        System.out.println("Out AMultExpression "+ node.getLeft()+" * " + node.getRight());
        RecType leftType, rightType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(rightType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in mult expression: type char");
        }
        else if(rightType.getDimensions() != 0)
        {
            System.out.println("Conflict in mult expression: type array");
        }

        if(leftType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in mult expression: type char");
        }
        else if(leftType.getDimensions() != 0)
        {
            System.out.println("Conflict in mult expression: type array");
        }

        recType = new RecType("int", 0);
        typeStack.push(recType);
    }


    @Override
    public void inADivExpression(ADivExpression node) {
      //  System.out.println("In ADivExpression");
    }

    @Override
    public void outADivExpression(ADivExpression node) {
        System.out.println("Out ADivExpression "+ node.getLeft()+" div " + node.getRight());
        RecType leftType, rightType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(rightType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in div expression: type char");
        }
        else if(rightType.getDimensions() != 0)
        {
            System.out.println("Conflict in div expression: type array");
        }

        if(leftType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in div expression: type char");
        }
        else if(leftType.getDimensions() != 0)
        {
            System.out.println("Conflict in div expression: type array");
        }

        recType = new RecType("int", 0);
        typeStack.push(recType);
    }

    @Override
    public void inAModExpression(AModExpression node) {
      //  System.out.println("In AModExpression");
    }

    @Override
    public void outAModExpression(AModExpression node) {
        System.out.println("Out AModExpression "+ node.getLeft()+" mod " + node.getRight());
        RecType leftType, rightType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(rightType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in mod expression: type char");
        }
        else if(rightType.getDimensions() != 0)
        {
            System.out.println("Conflict in mod expression: type array");
        }

        if(leftType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in mod expression: type char");
        }
        else if(leftType.getDimensions() != 0)
        {
            System.out.println("Conflict in mod expression: type array");
        }

        recType = new RecType("int", 0);
        typeStack.push(recType);
    }

    @Override
    public void inAIntExpression(AIntExpression node) {
        System.out.println("In AIntExpression: "+node.getNumber());

        RecType recType = new RecType("int", 0);
        typeStack.push(recType);
    }

    @Override
    public void inAPosExpression(APosExpression node) {
        System.out.println("In APosExpression: +"+node.getExpression());
        RecType tempType, recType;

        tempType = typeStack.pop();

        if(tempType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in mod expression: type char");
        }
        else if(tempType.getDimensions() != 0)
        {
            System.out.println("Conflict in mod expression: type array");
        }

        recType = new RecType("int", 0);
        typeStack.push(recType);
    }

    @Override
    public void inANegExpression(ANegExpression node) {
        System.out.println("In ANegExpression: -"+node.getExpression());
        RecType tempType, recType;

        tempType = typeStack.pop();

        if(tempType.getType().trim().equals("char"))
        {
            System.out.println("Conflict in mod expression: type char");
        }
        else if(tempType.getDimensions() != 0)
        {
            System.out.println("Conflict in mod expression: type array");
        }

        recType = new RecType("int", 0);
        typeStack.push(recType);
    }

    @Override
    public void inACharExpression(ACharExpression node) {
        System.out.println("In ACharExpression: "+node.getConstChar());

        RecType recType = new RecType("char", 0);
        typeStack.push(recType);
    }

    @Override
    public void inAValExpression(AValExpression node) {
        System.out.println("In AValExpression: "+node.getLvalue());
    }

    @Override
    public void outAValExpression(AValExpression node) {
        System.out.println("Out AValExpression");
    }

    @Override
    public void inAFunExpression(AFunExpression node) {
      //  System.out.println("In AFunExpression");
    }

    @Override
    public void inAHeader(AHeader node) {
        AFparDefinition Apar;
        System.out.println("In Header: Function has name "+ node.getIdentifier());
        for(PFparDefinition pexr:node.getFparDefinition())
        {
            Apar= (AFparDefinition) pexr;
            System.out.println(Apar.getFparType());
        }
    }

    @Override
    public void outAHeader(AHeader node) {
        System.out.println("Out header: Function has name "+ node.getIdentifier());
        Record recFunct;
        AFparDefinition Apar;

        for(PFparDefinition pexr:node.getFparDefinition())
        {
            Apar= (AFparDefinition) pexr;
            System.out.println(Apar.getFparType());
        }

        for(String myString : fparVars)
        {
            System.out.println(myString);
        }

        recFunct = new RecordFunction(node.getIdentifier().toString().trim(), node.getGeneralType().toString().trim(), "Function", fParam);
        symTable.insert(recFunct);
        returnStatement.push(node.getGeneralType().toString().trim());
        fParam.clear();
    }

    @Override
    public void inAFparDefinition(AFparDefinition node) {
      //  System.out.println("In AFparDefinition");
    }

    @Override
    public void outAFparDefinition(AFparDefinition node) {
        Record tmpRec;
        boolean ref;
        System.out.println("Out AFparDefinition "+node.getReference()+" number of parameters "+node.getVarIdentifier().size()+ " ");

        if(node.getReference() == null)
        {
            ref = false;
        }
        else
        {
            ref = true;
        }

        for(PVarIdentifier varId: node.getVarIdentifier())
        {
            if(dimList.isEmpty())
            {
                tmpRec = new RecordParam(varId.toString().trim(), tmpVarType, "functParam", ref);
            }
            else
            {
                tmpRec = new RecordParamArray(varId.toString().trim(), tmpVarType, "functParamArray", dimList, ref);
            }

            symTable.insert(tmpRec);
            fParam.addLast(tmpRec);
        }
        dimList.clear();
    }

    @Override
    public void inAVarDefinition(AVarDefinition node) {
      //  System.out.print("In AVarDefinition type");
    }

    @Override
    public void outAVarDefinition(AVarDefinition node) {
        Record tmpRec;

        System.out.print("Out AVarDefinition type "+node.getVarType()+ " number of parameters "+node.getVarIdentifier().size()+" Variables ");
        for(PVarIdentifier varId: node.getVarIdentifier())
        {
            if(dimList.isEmpty())
            {
                tmpRec = new Record(varId.toString().trim(), tmpVarType, "Variable");
            }
            else
            {
                tmpRec = new RecordArray(varId.toString().trim(), tmpVarType, "Array", dimList);
            }

            symTable.insert(tmpRec);

        }
        dimList.clear();
        System.out.println();
    }

    @Override
    public void inAVarIdentifier(AVarIdentifier node) {
        System.out.println("In AVarIdentifier");
    }

    @Override
    public void inAFparType(AFparType node) {
        System.out.println("In AFparType "+node.getEmptyBr()+" ");
        int tmpInteger;
        tmpVarType = new String(node.getGeneralType().toString().trim());

        if(node.getEmptyBr() != null) dimList.addLast(0);

        for(PConstIntBr arrayDimensions:node.getConstIntBr())
        {
            tmpInteger = Integer.parseInt(arrayDimensions.toString().trim());
            dimList.addLast(tmpInteger);
            System.out.println("array Size " + arrayDimensions);
        }
    }

    @Override
    public void inAVarType(AVarType node) {
        System.out.println("In AVarType");
        int tmpInteger;
        tmpVarType = new String(node.getGeneralType().toString().trim());

        for(PConstIntBr arrayDimensions:node.getConstIntBr())
        {
            tmpInteger = Integer.parseInt(arrayDimensions.toString().trim());
            dimList.addLast(tmpInteger);
        }
    }

    @Override
    public void inAIntGeneralType(AIntGeneralType node) {
      //  System.out.println("In AIntGeneralType");
    }

    @Override
    public void inACharGeneralType(ACharGeneralType node) {
      //  System.out.println("In ACharGeneralType");
    }

    @Override
    public void inANothGeneralType(ANothGeneralType node) {
      //  System.out.println("In ANothGeneralType");
    }
    @Override
    public void inAConstIntBr(AConstIntBr node) {
      //  System.out.println("In AConstIntBr");
    }

    @Override
    public void inAEmptyBr(AEmptyBr node) {
      //  System.out.println("In AEmptyBr");
    }

    @Override
    public void inAFunctionCall(AFunctionCall node) {
        System.out.print("In AFunctionCall: Name: "+ node.getIdentifier()+ " number of Expressions "+node.getExprList());
    }

    @Override
    public void outAFunctionCall(AFunctionCall node) {
        RecordFunction recFunct;
        Record recParam;
        RecType recordType, paramType;
        Iterator<Record> iterRec;

        boolean referable;
        AExprList arguments = null;
        Iterator<PExpression> iterReferenceAllowed;
        int numberOfArguments=0;


        System.out.print("out AFunctionCall: Name: "+ node.getIdentifier()+ " number of Expressions "+node.getExprList());

        recFunct = (RecordFunction)symTable.lookup(node.getIdentifier().toString().trim());

        if (node.getExprList() != null)
        {
            arguments = (AExprList)  node.getExprList();
            numberOfArguments = arguments.getExprs().size();
        }

        if(recFunct == null) //if function doesnt exists
        {
            System.out.println("Error function undeclared "+numberOfArguments+" "+typeStack.size());
            recordType = new RecType("null", 0);
            while (numberOfArguments > 0)
            {
                typeStack.pop();
                numberOfArguments--;
            }

            typeStack.push(recordType);
            return;
        }

        if(recFunct.getFparameters().size() != numberOfArguments) //if not the same parameters
        {
            System.out.println("Error wrong number of Arguments");
            recordType = new RecType(recFunct.getType(), 0);
            while (numberOfArguments > 0)
            {
                typeStack.pop();
                numberOfArguments--;
            }

            typeStack.push(recordType);
            return;
        }

        if(numberOfArguments != 0)
        {
            iterRec = recFunct.getFparameters().descendingIterator();
            iterReferenceAllowed = arguments.getExprs().descendingIterator();
            for(int i = numberOfArguments; i > 0; i--) //gets the arguments type in reverse
            {
                paramType = typeStack.pop(); //gets last argument type

                recParam = iterRec.next();
                if(iterReferenceAllowed.next() instanceof AValExpression)
                {
                    referable = true;
                }
                else
                {
                    referable = false;
                }

                if(recParam instanceof RecordParam)
                {
                    RecordParam recParameter = (RecordParam) recParam;
                    if(recParameter.getReference() == true && referable == false)
                    {
                        System.out.println("Error the "+i+" argument must be lvalue");
                    }

                    if(!paramType.getType().equals(recParam.getType()) || paramType.getDimensions() !=0)
                    {
                        System.out.println("Error the "+i+" argument must be "+recParam.getType()+"and dimensions are "+ paramType.getDimensions());
                    }
                }
                else if (recParam instanceof RecordArray)
                {
                    RecordParamArray recParameter = (RecordParamArray) recParam;
                    if(recParameter.getReference() == true && referable == false)
                    {
                        System.out.println("Error the "+i+" argument must be lvalue");
                    }

                    if(!paramType.getType().equals(recParam.getType()) || paramType.getDimensions() != recParameter.getDimensions().size())
                    {
                        System.out.println("Error the "+i+" argument must be "+recParam.getType()+" and "+recParameter.getDimensions().size()+" dimensions");
                    }
                }
            }
        }

        recordType = new RecType(recFunct.getType(), 0); //functions cant return arrays only int or char
        typeStack.push(recordType);

        System.out.println("functionCall is fine");
    }

    @Override
    public void inAIdLvalue(AIdLvalue node) {
        System.out.println("In AIdLvalue");
        Record tmpRec;
        RecType rectype;
        int arraySize=0;

        tmpRec = symTable.lookup(node.getIdentifier().toString().trim());

        if(tmpRec == null)
        {
            System.out.println("Error undefined variable "+node.getIdentifier());
            rectype = new RecType("null", 0);
            typeStack.push(rectype);
            return;
        }

        if (tmpRec instanceof RecordArray)
        {
            System.out.println("Here");
            RecordArray recArray = (RecordArray) tmpRec;
            System.out.println("Size "+recArray.getDimensions().size());
            arraySize = recArray.getDimensions().size();
        }

        rectype = new RecType(tmpRec.getType().trim(), arraySize);
        typeStack.push(rectype);

        System.out.println("rectype type "+ rectype.getType()+" "+ arraySize);
    }

    @Override
    public void inAStrLvalue(AStrLvalue node) {
      //  System.out.println("In AStrLvalue: "+node.getConstString());

        RecType rectype = new RecType("char", 1);
        typeStack.push(rectype);
    }

    @Override
    public void outAStrLvalue(AStrLvalue node) {
    //    System.out.println("Out AStrLvalue");
    }

    @Override
    public void inAValLvalue(AValLvalue node) {
    //  System.out.println("In AValLvalue");
    }

    @Override
    public void outAValLvalue(AValLvalue node) {
        System.out.println("Out AValLvalue");
        RecType rightType, leftType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if (!rightType.getType().equals("int") || rightType.getDimensions() !=0)
        {
            System.out.println("Error array index must be int");
        }
        leftType.setDimensions(leftType.getDimensions()-1);
        typeStack.push(leftType);
    }

}
