package compiler;

/**
 * Created by bill on 30/05/17.
 */
public class Error
{
    private boolean errorFound = false;

    public void varDeclaration(Record rec)
    {
        errorFound =true;
        System.out.println("[line:"+rec.getLine()+"]:Error variable "+ rec.getName()+ " already declared in the same scope");
    }

    public void varUndefined(String varName, int line)
    {
        errorFound =true;
        System.out.println("[line:"+line+"]:Error variable "+ varName+ " is undeclared");
    }

    public void returnStatement(Record rec)
    {
        errorFound =true;
        System.out.println("[line:"+rec.getLine()+"]:Error in function "+ rec.getName()+ " no return statement");
    }

    public void returnDiffType(String type)
    {
        errorFound =true;
        System.out.println("[line:???]:Error return type expects "+type+", but you gave nothing");
    }

    public void returnDiffType(RecType recType, String type)
    {
        errorFound =true;
        System.out.println("[line:"+recType.getLine()+"]:Error return type expects "+type+", but you gave "+recType.getType()+" "+recType.getDimensions());
    }

    public void varAssignment(RecType leftType, RecType rightType)
    {
        errorFound =true;
        System.out.println("[line:"+leftType.getLine()+"]:Error cannot convert "+rightType.getType().trim()+" "+rightType.getDimensions()+ " to "+leftType.getType().trim()+" "+leftType.getDimensions());
    }

    public void nothingFunctCall(RecType recType)
    {
        errorFound =true;
        System.out.println("[line:"+recType.getLine()+"]:Error in function call statement "+ recType.getVarName()+ " type must be nothing");
    }

    public void undeclaredFunct(String name, int line)
    {
        errorFound =true;
        System.out.println("[line:"+line+"]:Error call to function with name "+ name+ " but function is not declared or defined");
    }

    public void callFunctionVarInsted(Record rec, int line)
    {
        errorFound =true;
        System.out.println("[line:"+line+"]:Error call for function with name "+ rec.getName()+ " but its variable instead, declared in line:"+rec.getLine());
    }

    public void callFunctionArguments(RecordFunction rec, int line)
    {
        errorFound =true;
        System.out.print("[line:"+line+"]:Error call for function with name "+ rec.getName()+ " but has different number of argument function header is ");
        rec.printHeader();
    }

    public void callFunctionParamRef(int parameterPos, int line)
    {
        errorFound =true;
        System.out.println("[line:"+line+"]:Error the "+parameterPos +" parameter must be lvalue");
    }

    public void callFunctionParamType(int parameterPos, int line, Record rec, int expectedDimensions, String type, int dimensions)
    {
        errorFound =true;
        System.out.println("[line:"+line+"]:Error the "+parameterPos +" parameter must be "+rec.getType()+" dimensions "+expectedDimensions+"instead of type "+type+" dimensions "+dimensions);
    }

    public void mainFunctType(Record rec)
    {
        errorFound =true;
        System.out.println("[line:"+rec.getLine()+"]:Error main function must be nothing");
    }

    public void mainFunctArgs(Record rec)
    {
        errorFound =true;
        System.out.println("[line:"+rec.getLine()+"]:Error main function must have 0 arguments");
    }

    public void arrayIndex(String type , int line)
    {
        errorFound =true;
        System.out.println("[line:"+line+"]:Error array index must be int not "+type);
    }

    public void condStatement(RecType recType)
    {
        errorFound =true;
        System.out.println("[line:"+recType.getLine()+"]:Error return type expects boolean, but you gave "+recType.getType()+" "+recType.getDimensions());
    }

    public void conditionAND(RecType leftType, RecType rightType)
    {
        errorFound =true;
        System.out.println("[line:"+leftType.getLine()+"]:Error operation AND expects 2 booleans, but you gave "+leftType.getType()+" "+leftType.getDimensions()+" AND "+rightType.getType()+" "+rightType.getDimensions());
    }

    public void conditionOR(RecType leftType, RecType rightType)
    {
        errorFound =true;
        System.out.println("[line:"+leftType.getLine()+"]:Error operation OR expects 2 booleans, but you gave "+leftType.getType()+" "+leftType.getDimensions()+" OR "+rightType.getType()+" "+rightType.getDimensions());
    }

    public void conditionNOT(RecType recType)
    {
        errorFound =true;
        System.out.println("[line:"+recType.getLine()+"]:Error operation NOT expects boolean, but you gave NOT "+recType.getType()+" "+recType.getDimensions());
    }

    public void conditionRelat(RecType leftType, RecType rightType, String operator)
    {
        errorFound =true;
        System.out.println("[line:"+leftType.getLine()+"]:Error operation "+operator+" expects same types, but you gave "+leftType.getType()+" "+leftType.getDimensions()+" "+operator+" "+rightType.getType()+" "+rightType.getDimensions());
    }

    public void exprOperationType(RecType recType, String operator)
    {
        errorFound =true;
        System.out.println("[line:"+recType.getLine()+"]:Error operation "+operator+" expects int, but you gave char type "+recType.getType()+" "+recType.getDimensions());
    }

    public void exprOperationArray(RecType recType, String operator)
    {
        errorFound =true;
        System.out.println("[line:"+recType.getLine()+"]:Error operation "+operator+" expects int, but you gave array type "+recType.getType()+" "+recType.getDimensions());
    }

    public void functNotDefined(RecordFunction rec)
    {
        errorFound =true;
        System.out.println("[line:"+rec.getLine()+"]:Error function "+rec.getName()+" not defined ");
    }

    public void functMatch(RecordFunction recInTable, RecordFunction recFunct)
    {
        errorFound =true;
        System.out.print("[line:"+recFunct.getLine()+"]:Error function "+recFunct.getName()+" already exists but with header: ");
        recInTable.printHeader();
    }

    public void functReDefinition(RecordFunction rec)
    {
        errorFound =true;
        System.out.println("[line:"+rec.getLine()+"]:Error function "+rec.getName()+" already defined/declared");
    }

    public void functDeclarVar(Record rec, int line)
    {
        errorFound =true;
        System.out.println("[line:"+line+"]:Error call for function with name "+ rec.getName()+ " but its variable instead");
    }


    public boolean getErrorFound()
    {
        return errorFound;
    }
}
