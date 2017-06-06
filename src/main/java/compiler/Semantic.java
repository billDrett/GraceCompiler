package compiler;

import com.sun.org.apache.regexp.internal.RE;
import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.util.*;

public class Semantic extends DepthFirstAdapter
{
    private SymbolTable symTable = new SymbolTable();
    private ArrayList<String> fparVars = new ArrayList<>();
    private LinkedList<Integer> dimList = new LinkedList<>();
    private LinkedList<Record> fParam = new LinkedList<>();
    private String tmpVarType;
    private Stack<RecType> typeStack = new Stack<>();
    private Stack<String> returnStatement = new Stack<>();
    //private LinkedList<Record> currentFunctionHeader = new LinkedList<>();
    boolean returnFound =false;
    boolean functDefinition = true;
    VisitorIR intermediateCode = new VisitorIR(symTable);
    Error error = new Error();
    Record extrFunction;
    private int funCounter = 0;

    Semantic()
    {
        LinkedList<Integer> dimList = new LinkedList<>();
        dimList.addLast(0); //no dimensions given

        //insert all the library functions at the symbol table
        insertFunction("puti", "nothing", new RecordParam("n", "int", false, 0), null);
        insertFunction("putc", "nothing", new RecordParam("c", "char", false, 0), null);

        insertFunction("puts", "nothing", new RecordParamArray("s", "char", dimList, true, 0), null);

        insertFunction("geti", "int", null, null);
        insertFunction("getc", "char", null, null);

        Record rec1Gets = new RecordParam("n", "int", false, 0);
        Record rec2Gets = new RecordParamArray("s", "char", dimList, true, 0);
        insertFunction("geti", "nothing", rec1Gets, rec2Gets);

        insertFunction("abs", "int", new RecordParam("n", "int", false, 0), null);
        insertFunction("ord", "int", new RecordParam("c", "char", false, 0), null);
        insertFunction("chr", "char", new RecordParam("n", "int", false, 0), null);

        insertStrFunction("strlen", "int", "char", null);
        insertStrFunction("strcmp", "int", "char", "char");
        insertStrFunction("strcpy", "nothing", "char", "char");
        insertStrFunction("strcat", "nothing", "char", "char");
    }

    //insert a library function at the symbol table
    void insertFunction(String name, String returnType, Record rec1, Record rec2)
    {
        RecordFunction newFunctRecord;
        LinkedList<Record> functParameters = new LinkedList<>();

        if(rec1 != null)
        {
            functParameters.addLast(rec1);
        }

        if(rec2 != null)
        {
            functParameters.addLast(rec2);
        }

        newFunctRecord = new RecordFunction(name, returnType, functParameters, 0, 0); //create function record
        newFunctRecord.setDefined(true);
        symTable.insert(newFunctRecord);

    }

    //inserts the str fuction at the symbol table, all the parameters are with ref and type of array with one dimension.
    void insertStrFunction(String name, String returnType, String rec1Type, String rec2Type)
    {
        LinkedList<Integer> dimList = new LinkedList<>();
        dimList.addLast(0); //no dimensions given
        Record rec1 = null, rec2= null;

        if(rec1Type != null)
        {
            rec1 = new RecordParamArray("s1", "char", dimList, true, 0);
        }

        if(rec2Type != null)
        {
            rec2 = new RecordParamArray("s2", "char", dimList, true, 0);
        }

        insertFunction(name, returnType, rec1, rec2);
    }

    @Override
    public void outAProgram(AProgram node)
    {
        intermediateCode.printIntermidiateCode();
    }

    @Override
    public void inAFunDefinition(AFunDefinition node) {
        functDefinition = true;
    }


    @Override
    public void caseAFunDefinition(AFunDefinition node)
    {
        Record localFunct;
        inAFunDefinition(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }

        localFunct = extrFunction; //keep the header of the function
        symTable.enter(); //create a scope for the parameters and local variables


        for(Record rec: fParam) //push all function parameters in the symbol table
        {
            if(!symTable.insert(rec))
            {
                error.varDeclaration(rec);
            }
        }

        fParam.clear(); //empty the list for the next function header

        {
            List<PLocalDef> copy = new ArrayList<PLocalDef>(node.getLocalDef());
            for(PLocalDef e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PStatement> copy = new ArrayList<PStatement>(node.getStatement());
            for(PStatement e : copy)
            {
                e.apply(this);
            }
        }

        extrFunction = localFunct;
        outAFunDefinition(node);

        if(!error.getErrorFound()) //there wasnt any semantic error
        {
            intermediateCode.caseAFunDefinition(node);
        }
        else //semantic error found, delete the intermediateCode
        {
            intermediateCode.clearIR();
        }

        symTable.exit(); //function block is finished, delete the current scope

    }


    @Override
    public void outAFunDefinition(AFunDefinition node) {
        String returnType;
        Record currentHeader;

        returnType = returnStatement.pop();
        if(!returnFound && !returnType.equals("nothing")) //return not found and the type return type was int or char
        {
            error.returnStatement(extrFunction);
        }

        symTable.checkDefinedFunct(error); //check for all functions of the scope if they are defined

        returnFound = false;
    }

    @Override
    public void outAAssignStatement(AAssignStatement node) {
        RecType leftType, rightType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        //check if the assigment is the same type and they are literals int/char
        if(!(rightType.getType().equals(leftType.getType()) && rightType.getDimensions() == leftType.getDimensions()) && rightType.getDimensions() == 0)
        {
            error.varAssignment(leftType, rightType);
        }
    }

    //in functionStatement the functioncall return type must be nothing
    @Override
    public void outAFuncCallStatement(AFuncCallStatement node)
    {
        RecType recType = typeStack.pop();

        if(!recType.getType().equals("nothing"))
        {
            error.nothingFunctCall(recType);
        }
    }

    @Override
    public void outAIfWithoutElseStatement(AIfWithoutElseStatement node) {
        RecType recType = typeStack.pop();

        if(!(recType.getType().equals("bool")))
        {
            error.condStatement(recType);
        }
    }

    @Override
    public void outAIfWithElseStatement(AIfWithElseStatement node) {
        RecType recType = typeStack.pop();

        if(!(recType.getType().equals("bool")))
        {
            error.condStatement(recType);
        }
    }

    @Override
    public void outAWhileStatement(AWhileStatement node) {
        RecType recType = typeStack.pop();

        if(!(recType.getType().equals("bool")))
        {
            error.condStatement(recType);
        }
    }

    @Override
    public void outAReturnStatement(AReturnStatement node) {
        String returnType = returnStatement.pop();

        if(node.getExpression() == null) //empty return, the return type of function must be nothing
        {
            if(!(returnType.equals("nothing")))
            {
                error.returnDiffType(returnType);
            }
        }
        else //non empty return, must be the same type as return type of function
        {
            RecType recType = typeStack.pop();

            if(recType.getDimensions() != 0) //must be literal int/char
            {
                error.returnDiffType(recType, returnType);
            }
            else if (!recType.getType().equals(returnType)) //same type
            {
                error.returnDiffType(recType, returnType);
            }
        }
        returnStatement.push(returnType); //put back function return type, there might be more than one return statements
        returnFound = true;

    }

    @Override
    public void inAFunDeclLocalDef(AFunDeclLocalDef node) {
        functDefinition = false;
    }

    @Override
    public void outAFunDeclLocalDef(AFunDeclLocalDef node) {
        returnStatement.pop();
        returnFound = false;
    }

    @Override
    public void outAOrCondition(AOrCondition node) {
        RecType rightType, leftType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(!(rightType.getType().equals("bool") && leftType.getType().equals("bool"))) //must be bool
        {
            error.conditionOR(leftType, rightType);
        }

        recType = new RecType(node.getRight().toString(), "bool", 0, rightType.getLine());
        typeStack.push(recType);
    }

    @Override
    public void outAAndCondition(AAndCondition node) {
        RecType rightType, leftType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if(!(rightType.getType().equals("bool") && leftType.getType().equals("bool"))) //must be bool
        {
            error.conditionAND(leftType, rightType);
        }

        recType = new RecType(node.getRight().toString(), "bool", 0, rightType.getLine());
        typeStack.push(recType);
    }

    @Override
    public void outANotCondition(ANotCondition node) {
        RecType tempType, recType;
        tempType = typeStack.pop();

        if(!(tempType.getType().equals("bool"))) //must be bool
        {
            error.conditionNOT(tempType);
        }

        recType = new RecType(node.getCondition().toString(), "bool", 0, tempType.getLine());
        typeStack.push(recType);

    }

    @Override
    public void outARelatCondition(ARelatCondition node) {
        RecType rightType, leftType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        //must be same type and not pointers
        if(!((rightType.getType().equals(leftType.getType())) && rightType.getDimensions() == 0 && leftType.getDimensions() == 0))
        {
            error.conditionRelat(leftType, rightType, node.getSymbol().toString().trim());
        }

        recType = new RecType(node.getRight().toString(), "bool", 0, rightType.getLine());
        typeStack.push(recType);

    }

    public void arithmeticOperation(String oper)
    {
        RecType leftType, rightType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        //the operation must be between integers
        if(rightType.getType().equals("char"))
        {
            error.exprOperationType(rightType, oper);
        }
        else if(rightType.getDimensions() != 0)
        {
            error.exprOperationArray(rightType, oper);
        }

        if(leftType.getType().equals("char"))
        {
            error.exprOperationType(leftType, oper);
        }
        else if(leftType.getDimensions() != 0)
        {
            error.exprOperationArray(leftType, oper);
        }

        recType = new RecType(leftType.getVarName(), "int", 0, leftType.getLine()); //result type is int
        typeStack.push(recType);
    }
    @Override
    public void outAPlusExpression(APlusExpression node) {
        arithmeticOperation("+");
    }

    @Override
    public void outAMinusExpression(AMinusExpression node) {
        arithmeticOperation("-");
    }

    @Override
    public void outAMultExpression(AMultExpression node) {
        arithmeticOperation("*");
    }

    @Override
    public void outADivExpression(ADivExpression node) {
        arithmeticOperation("div");
    }

    @Override
    public void outAModExpression(AModExpression node) {
        arithmeticOperation("mod");
    }

    @Override
    public void inAIntExpression(AIntExpression node) {
        RecType recType = new RecType(node.getNumber().toString(), "int", 0, node.getNumber().getLine()); //type of child is int

        typeStack.push(recType);
    }

    @Override
    public void outAPosExpression(APosExpression node) {
        RecType tempType, recType;

        tempType = typeStack.pop();

        //the operation must be between integers
        if(tempType.getType().equals("char"))
        {
            error.exprOperationType(tempType, "pos +");
        }
        else if(tempType.getDimensions() != 0)
        {
            error.exprOperationArray(tempType, "pos +");
        }

        recType = new RecType(node.getExpression().toString(), "int", 0, tempType.getLine());
        typeStack.push(recType);
    }

    @Override
    public void outANegExpression(ANegExpression node) {
        RecType tempType, recType;

        tempType = typeStack.pop();

        //the operation must be between integers
        if(tempType.getType().equals("char"))
        {
            error.exprOperationType(tempType, "neg -");
        }
        else if(tempType.getDimensions() != 0)
        {
            error.exprOperationArray(tempType, "neg +");
        }

        recType = new RecType(node.getExpression().toString(), "int", 0, tempType.getLine());
        typeStack.push(recType);
    }

    @Override
    public void inACharExpression(ACharExpression node) {
        RecType recType = new RecType(node.getConstChar().toString(), "char", 0, node.getConstChar().getLine()); //type of child is char

        typeStack.push(recType);
    }

    @Override
    public void outAHeader(AHeader node) {
        Record recFunct, tmpRec;
        AFparDefinition Apar;
        boolean functMatch;

        recFunct = new RecordFunction(node.getIdentifier().toString().trim(), node.getGeneralType().toString().trim(), fParam, node.getIdentifier().getLine(), funCounter);
        funCounter++; //create next uniqueId

        if(symTable.getCurrentDepth() ==1) //main function
        {
            if(!node.getGeneralType().toString().trim().equals("nothing"))
            {
                error.mainFunctType(recFunct);
            }

            if(fParam.size() != 0)
            {
                error.mainFunctArgs(recFunct);
            }

        }

        if(functDefinition) ((RecordFunction)recFunct).setDefined(true); //if its a declaration set it true

        //insert the function record at the symbol table
        if(!symTable.insert(recFunct)) //name already exists
        {
            tmpRec = symTable.lookup(recFunct.getName()); //find if its a function declaration

            if(tmpRec instanceof RecordFunction) //must be a function
            {
                RecordFunction recInTable = (RecordFunction) tmpRec;
                RecordFunction recFunction = (RecordFunction) recFunct;

                if(functDefinition && recInTable.getDefined() == false) //must have function declaration and the function is only defined
                {
                    functMatch = functionMatch(recFunction, recInTable); //check if function have the same parameters

                    if(functMatch)
                    {
                        recInTable.setDefined(true);
                    }
                    else
                    {
                        error.functMatch(recInTable, recFunction);
                    }
                }
                else
                {
                    error.functReDefinition(recFunction);
                }
            }
            else
            {
                error.functDeclarVar(recFunct, recFunct.getLine());
            }
        }


        returnStatement.push(node.getGeneralType().toString().trim());
    }

    //checks if two function have the same return type and parameters
    private boolean functionMatch(RecordFunction recFunct1, RecordFunction recFunct2)
    {
        Iterator<Record> iter1, iter2;
        Record rec1, rec2;

        if(!recFunct1.getType().equals(recFunct2.getType())) //differnt return type
        {
            return false;
        }

        if(recFunct1.getFparameters().size() != recFunct2.getFparameters().size()) //differnt number of parameters
        {
            return false;
        }

        iter1 = recFunct1.getFparameters().iterator();
        iter2 = recFunct2.getFparameters().iterator();
        while (iter1.hasNext())
        {
            rec1 = iter1.next();
            rec2 = iter2.next();

            if(rec1 instanceof RecordParam && rec2 instanceof RecordParam)
            {
                RecordParam recParam1, recParam2;

                recParam1 = (RecordParam) rec1;
                recParam2 = (RecordParam) rec2;
                if(!recParam1.getType().equals(recParam2.getType()) || recParam1.getReference() != recParam2.getReference()) return false; //different type of argument or reference

            }
            else if (rec1 instanceof RecordParamArray && rec2 instanceof RecordParamArray)
            {
                RecordParamArray recParam1, recParam2;
                Iterator<Integer> dimIter2;
                int dim2;

                recParam1 = (RecordParamArray) rec1;
                recParam2 = (RecordParamArray) rec2;

                if(!recParam1.getType().equals(recParam2.getType())) return false; //different type of array

                if(recParam1.getDimensions().size() != recParam2.getDimensions().size()) return false; //different size of array

                dimIter2 = recParam2.getDimensions().listIterator();
                for(Integer dim1 : recParam1.getDimensions())
                {
                    dim2 = dimIter2.next();
                    if(dim1 != dim2) return false; //arrays must have same dimensions
                }

            }
            else //different type of argument, one is array and other is literal
            {
                return false;
            }

        }

        return true;
    }

    @Override
    public void outAFparDefinition(AFparDefinition node) {
        AVarIdentifier aVarId;
        Record tmpRec;
        boolean ref;

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
            aVarId = (AVarIdentifier) varId;

            if(dimList.isEmpty()) //no dimensions found, so its a literal
            {
                tmpRec = new RecordParam(varId.toString().trim(), tmpVarType, ref, aVarId.getIdentifier().getLine());
            }
            else
            {
                tmpRec = new RecordParamArray(varId.toString().trim(), tmpVarType, dimList, ref, aVarId.getIdentifier().getLine());
            }

            fParam.addLast(tmpRec); //put parameter at the current function parameters list
        }
        dimList.clear();
    }

    @Override
    public void outAVarDefinition(AVarDefinition node) {
        AVarIdentifier aVarId;
        Record tmpRec;

        for(PVarIdentifier varId: node.getVarIdentifier())
        {
            aVarId = (AVarIdentifier) varId;
            if(dimList.isEmpty())
            {
                tmpRec = new Record(varId.toString().trim(), tmpVarType, aVarId.getIdentifier().getLine());
            }
            else
            {
                tmpRec = new RecordArray(varId.toString().trim(), tmpVarType, dimList, aVarId.getIdentifier().getLine());
            }

            if(!symTable.insert(tmpRec)) //check if variable already exists
            {
                error.varDeclaration(tmpRec);
            }

        }
        dimList.clear();
    }

    @Override
    public void inAFparType(AFparType node) {
        int tmpInteger;
        tmpVarType = new String(node.getGeneralType().toString().trim());

        if(node.getEmptyBr() != null) dimList.addLast(0);

        for(PConstIntBr arrayDimensions:node.getConstIntBr()) //add all the dimensions of the array in the dimList
        {
            tmpInteger = Integer.parseInt(arrayDimensions.toString().trim());
            dimList.addLast(tmpInteger);
        }
    }

    @Override
    public void inAVarType(AVarType node) {
        int tmpInteger;
        tmpVarType = new String(node.getGeneralType().toString().trim());

        for(PConstIntBr arrayDimensions:node.getConstIntBr()) //add all the dimensions of the array in the dimList
        {
            tmpInteger = Integer.parseInt(arrayDimensions.toString().trim());
            dimList.addLast(tmpInteger);
        }
    }

    @Override
    public void outAFunctionCall(AFunctionCall node) {
        RecordFunction recFunct;
        Record recParam, tmpFunct;
        RecType recordType, paramType;
        Iterator<Record> iterRec;

        boolean referable, errorFound = false;
        AExprList arguments = null;
        Iterator<PExpression> iterReferenceAllowed;
        int numberOfArguments=0;

        String functName;
        int currentLine = node.getIdentifier().getLine();

        tmpFunct = symTable.lookup(node.getIdentifier().toString().trim());


        if (node.getExprList() != null) //check if function call has arguments
        {
            arguments = (AExprList)  node.getExprList();
            numberOfArguments = arguments.getExprs().size();
        }

        if(tmpFunct == null) //if function doesnt exists
        {
            error.undeclaredFunct(node.getIdentifier().toString(), currentLine);

            recordType = new RecType(null, "null", 0, currentLine);
            while (numberOfArguments > 0) //clean arguments
            {
                typeStack.pop();
                numberOfArguments--;
            }

            typeStack.push(recordType);
            return;
        }

        if(tmpFunct instanceof RecordFunction) //record must be function
        {
            recFunct = (RecordFunction) tmpFunct;
        }
        else
        {
            error.callFunctionVarInsted(tmpFunct, currentLine);

            recordType = new RecType(null, "null", 0, currentLine);
            while (numberOfArguments > 0) //clean arguments
            {
                typeStack.pop();
                numberOfArguments--;
            }

            typeStack.push(recordType);
            return;
        }

        functName = recFunct.getName();

        if(recFunct.getFparameters().size() != numberOfArguments) //if not the same parameters
        {
            error.callFunctionArguments(recFunct, currentLine);
            recordType = new RecType(functName, recFunct.getType(), 0, currentLine);
            while (numberOfArguments > 0) //clean arguments
            {
                typeStack.pop();
                numberOfArguments--;
            }

            typeStack.push(recordType);
            return;
        }

        if(numberOfArguments != 0) //check if arguments are the same type
        {
            iterRec = recFunct.getFparameters().descendingIterator();
            iterReferenceAllowed = arguments.getExprs().descendingIterator();
            for(int i = numberOfArguments; i > 0; i--) //gets the arguments type in reverse
            {
                paramType = typeStack.pop(); //gets last argument type

                recParam = iterRec.next();
                if(iterReferenceAllowed.next() instanceof AValExpression) //lvalue can be referenced
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
                    if(recParameter.getReference() == true && referable == false) //we need a reference but its not lvalue
                    {
                        errorFound = true;
                        error.callFunctionParamRef(i, currentLine);
                    }

                    if(!paramType.getType().equals(recParam.getType()) || paramType.getDimensions() !=0) //different type
                    {
                        errorFound = true;
                        error.callFunctionParamType(i, currentLine, recParam, 0, paramType.getType(),paramType.getDimensions());
                    }
                }
                else if (recParam instanceof RecordArray)
                {
                    RecordParamArray recParameter = (RecordParamArray) recParam;
                    if(recParameter.getReference() == true && referable == false)  //we need a reference but its not lvalue
                    {
                        errorFound = true;
                        error.callFunctionParamRef(i, currentLine);
                    }

                    if(!paramType.getType().equals(recParam.getType()) || paramType.getDimensions() != recParameter.getDimensions().size()) //different type
                    {
                        errorFound = true;
                        error.callFunctionParamType(i, currentLine, recParam, recParameter.getDimensions().size(), paramType.getType(),paramType.getDimensions());
                    }
                }
            }

            if(errorFound)
            {
                error.callFunctionArguments(recFunct, currentLine);
            }
        }

        recordType = new RecType(functName, recFunct.getType(), 0, currentLine); //functions cant return arrays only int or char
        typeStack.push(recordType);
    }

    @Override
    public void inAIdLvalue(AIdLvalue node) {
        Record tmpRec;
        RecType rectype;
        String varName;
        int currentLine, arraySize=0;

        varName = node.getIdentifier().toString().trim();
        currentLine = node.getIdentifier().getLine();
        tmpRec = symTable.lookup(varName);

        if(tmpRec == null) //check if variable is defined
        {
            error.varUndefined(varName, currentLine);
            rectype = new RecType(varName, "null", 0, currentLine);
            typeStack.push(rectype);
            return;
        }

        if (tmpRec instanceof RecordArray) //if its array keep its dimensions
        {
            RecordArray recArray = (RecordArray) tmpRec;
            arraySize = recArray.getDimensions().size(); //keep number of dimensions
        }

        rectype = new RecType(varName, tmpRec.getType().trim(), arraySize, currentLine);
        typeStack.push(rectype);
    }

    @Override
    public void inAStrLvalue(AStrLvalue node) {
        RecType rectype = new RecType(node.getConstString().toString(), "char", 1, node.getConstString().getLine()); //string literal is a char array

        typeStack.push(rectype);
    }

    @Override
    public void outAValLvalue(AValLvalue node) {
        RecType rightType, leftType, recType;

        rightType = typeStack.pop();
        leftType = typeStack.pop();

        if (!rightType.getType().equals("int") || rightType.getDimensions() !=0) //index of the array must be int
        {
            error.arrayIndex(rightType.getType(), rightType.getLine());
        }
        leftType.setDimensions(leftType.getDimensions()-1); //dimensions of the current array are decreased by one
        typeStack.push(leftType);
    }

}
