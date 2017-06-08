package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.util.*;

public class VisitorIR extends DepthFirstAdapter{
    QuadList quadList;
    SymbolTable symTable;
    String extrChild;
    Stack<RecordLValue> stackLValue = new Stack<>();
    boolean idFound;
    private String tmpVarType;
    Condition extrCond;
    boolean getValueOfAddress;
    RecordFunction extrRecordFunction;


    public VisitorIR(SymbolTable symbolTable)
    {
        symTable = symbolTable;
        quadList = new QuadList(symTable);

    }

    public void clearIR()
    {
        quadList.clearIntermidateCode();
    }

    public void backPatch(List<Integer> backList, int newLabel) //backList is a true or false list for u cocksuckers
    {
        Quad currentQuad;
        for(int label :backList)
        {
            currentQuad = quadList.getQuad(label);
            currentQuad.setOpt3(Integer.toString(newLabel));
        }
    }

    //prints all the quads
    public void printIntermidiateCode()
    {
        quadList.printAll();
    }
    public QuadList getQuadList()
    {
        return quadList;
    }

    @Override
    public void caseAFunDefinition(AFunDefinition node)
    {
        RecordFunction functionRec;
        String functName ="";
        inAFunDefinition(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
            functionRec = (RecordFunction) symTable.lookup(extrChild);
            functName = functionRec.getFixedName();
        }

        //dont visit local definitions

        //unit
        quadList.GenQuad("unit", functName, "-", "-");

        {
            List<PStatement> copy = new ArrayList<PStatement>(node.getStatement());
            for(PStatement e : copy)
            {
                e.apply(this);
            }
        }
        //
        quadList.GenQuad("endu", functName, "-", "-");
        outAFunDefinition(node);

    }

    @Override
    public void caseAAssignStatement(AAssignStatement node)
    {
        RecordLValue recordLValue;
        String tmpValue, newValue, arrayName;

        String leftChild, rightChild;
        inAAssignStatement(node);
        if(node.getLvalue() != null)
        {
            node.getLvalue().apply(this);
        }

        recordLValue = stackLValue.pop();

        if(recordLValue instanceof StringLiteral)
        {
            arrayName = ((StringLiteral) recordLValue).getLiteralId();
            createArrayQuad(recordLValue, arrayName); //create quad for the string literal
        }
        else if(recordLValue.getRecord() instanceof RecordArray)             //RecordArray
        {
            createArrayQuad(recordLValue, recordLValue.getRecord().getName()); //create quad for array
        }
        else //Record
        {
            extrChild = recordLValue.getRecord().getName();
        }

        leftChild = extrChild;
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }



        rightChild = extrChild;

        quadList.GenQuad(":=", rightChild, "-", leftChild);
        outAAssignStatement(node);
    }


    @Override
    public void caseAFuncCallStatement(AFuncCallStatement node)
    {
        inAFuncCallStatement(node);
        if(node.getFunctionCall() != null)
        {
            node.getFunctionCall().apply(this);
        }
        outAFuncCallStatement(node);
    }

    @Override
    public void caseAIfWithoutElseStatement(AIfWithoutElseStatement node)
    {
        Condition condition;
        inAIfWithoutElseStatement(node);
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }

        condition = extrCond;
        backPatch(condition.getTrueList(), quadList.NextQuad());

        if(node.getStatement() != null)
        {
            node.getStatement().apply(this);
        }

        backPatch(condition.getFalseList(), quadList.NextQuad());
        outAIfWithoutElseStatement(node);
    }

    @Override
    public void caseAIfWithElseStatement(AIfWithElseStatement node)
    {
        Condition condition;
        ArrayList<Integer> jumpList = new ArrayList<>();
        inAIfWithElseStatement(node);
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }

        condition = extrCond;
        backPatch(condition.getTrueList(), quadList.NextQuad());

        //if statements
        if(node.getId1() != null)
        {
            node.getId1().apply(this);
        }

        jumpList.add(quadList.NextQuad());
        quadList.GenQuad("jump", "-", "-", "*");

        backPatch(condition.getFalseList(), quadList.NextQuad());
        //else statements
        if(node.getId2() != null)
        {
            node.getId2().apply(this);
        }

        backPatch(jumpList, quadList.NextQuad());

        outAIfWithElseStatement(node);
    }

    @Override
    public void caseAWhileStatement(AWhileStatement node)
    {
        Condition condition;
        int startLabel;

        inAWhileStatement(node);

        startLabel = quadList.NextQuad();
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }
        condition = extrCond;
        backPatch(condition.getTrueList(), quadList.NextQuad());


        if(node.getStatement() != null)
        {
            node.getStatement().apply(this);
        }

        quadList.GenQuad("jump", "-", "-", Integer.toString(startLabel));
        backPatch(condition.getFalseList(), quadList.NextQuad());

        outAWhileStatement(node);
    }

    @Override
    public void caseAReturnStatement(AReturnStatement node)
    {
        inAReturnStatement(node);
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }

        quadList.GenQuad(":=", extrChild, "-", "$$");

        outAReturnStatement(node);
    }

    @Override
    public void caseAOrCondition(AOrCondition node)
    {
        Condition leftCond, rightCond;

        inAOrCondition(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }

        leftCond = extrCond;
        backPatch(leftCond.getFalseList(), quadList.NextQuad());

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        rightCond = extrCond;

        extrCond = new Condition();
        extrCond.mergeLists(leftCond.getTrueList(), true);
        extrCond.mergeLists(rightCond.getTrueList(), true);

        extrCond.mergeLists(rightCond.getFalseList(), false);

        outAOrCondition(node);
    }

    @Override
    public void caseAAndCondition(AAndCondition node)
    {
        Condition leftCond, rightCond;

        inAAndCondition(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }

        leftCond = extrCond;
        backPatch(leftCond.getTrueList(), quadList.NextQuad());

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        rightCond = extrCond;

        extrCond = new Condition();
        extrCond.mergeLists(leftCond.getFalseList(), false);
        extrCond.mergeLists(rightCond.getFalseList(), false);

        extrCond.mergeLists(rightCond.getTrueList(), true);

        outAAndCondition(node);
    }

    @Override
    public void caseANotCondition(ANotCondition node)
    {
        Condition currentCond;
        inANotCondition(node);
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }

        //currentCond = extrCond;
        extrCond.swapLists();

        outANotCondition(node);
    }

    @Override
    public void caseARelatCondition(ARelatCondition node)
    {
        String leftChild, rightChild, tmpValue, operator;

        inARelatCondition(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }

        leftChild = extrChild;

        if(node.getSymbol() != null)
        {
            node.getSymbol().apply(this);
        }

        operator = node.getSymbol().toString().trim();

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        rightChild = extrChild;

        extrCond = new Condition();

        extrCond.addLabelTrue(quadList.NextQuad());
        quadList.GenQuad(operator, leftChild, rightChild, "*");

        extrCond.addLabelFalse(quadList.NextQuad());
        quadList.GenQuad("jump", "-", "-", "*");

        outARelatCondition(node);
    }


    @Override
    public void caseAExprList(AExprList node)
    {
        RecordFunction currentFunct = extrRecordFunction;
        Iterator<Record> iterRec = currentFunct.getFparameters().descendingIterator();
        Iterator<String> iterParam;
        LinkedList<String> allParameters = new LinkedList<>();
        //String tmpVar;
        Record tmpParam;
        String paramName;
        String paraMode;


        inAExprList(node);
        {
            List<PExpression> copy = new ArrayList<PExpression>(node.getExprs());
            for(PExpression e : copy)
            {
                e.apply(this);
                allParameters.add(extrChild); //add the parameters temp value with the right order
            }

            //we want all the parameters of the function to be in order
            for(iterParam = allParameters.descendingIterator(); iterParam.hasNext();)
            {
                tmpParam = iterRec.next();
                paramName = iterParam.next();

                if(tmpParam instanceof RecordParam)
                {
                    RecordParam param = (RecordParam)tmpParam;
                    if(param.getReference()) paraMode = "R";
                    else paraMode = "V";
                }
                else //Array
                {
                    paraMode = "R";
                }

                quadList.GenQuad("par", paramName, paraMode, "-");
            }
        }
        outAExprList(node);
    }

    @Override
    public void caseAPlusExpression(APlusExpression node)
    {
        String leftChild, rightChild, tmpValue;
        inAPlusExpression(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        leftChild = extrChild;

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        rightChild = extrChild;

        tmpValue = quadList.NewTemp("int");
        quadList.GenQuad("+", leftChild, rightChild, tmpValue);

        extrChild = tmpValue;

        outAPlusExpression(node);
    }

    @Override
    public void caseAMinusExpression(AMinusExpression node)
    {
        String leftChild, rightChild, tmpValue;
        inAMinusExpression(node);

        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        leftChild = extrChild;

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        rightChild = extrChild;

        tmpValue = quadList.NewTemp("int");
        quadList.GenQuad("-", leftChild, rightChild, tmpValue);

        extrChild = tmpValue;

        outAMinusExpression(node);
    }

    @Override
    public void caseAMultExpression(AMultExpression node)
    {
        String leftChild, rightChild, tmpValue;
        inAMultExpression(node);

        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        leftChild = extrChild;

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        rightChild = extrChild;

        tmpValue = quadList.NewTemp("int");
        quadList.GenQuad("*", leftChild, rightChild, tmpValue);

        extrChild = tmpValue;

        outAMultExpression(node);
    }

    @Override
    public void caseADivExpression(ADivExpression node)
    {
        String leftChild, rightChild, tmpValue;
        inADivExpression(node);

        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        leftChild = extrChild;

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        rightChild = extrChild;

        tmpValue = quadList.NewTemp("int");
        quadList.GenQuad("/", leftChild, rightChild, tmpValue);

        extrChild = tmpValue;

        outADivExpression(node);
    }

    @Override
    public void caseAModExpression(AModExpression node)
    {
        String leftChild, rightChild, tmpValue;
        inAModExpression(node);

        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        leftChild = extrChild;

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        rightChild = extrChild;

        tmpValue = quadList.NewTemp("int");
        quadList.GenQuad("%", leftChild, rightChild, tmpValue);

        extrChild = tmpValue;

        outAModExpression(node);
    }

    @Override
    public void caseANegExpression(ANegExpression node)
    {
        String rightChild, tmpValue;
        inANegExpression(node);
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }

        rightChild = extrChild;
        tmpValue = quadList.NewTemp("int");
        quadList.GenQuad("-", "0", rightChild, tmpValue);

        extrChild = tmpValue;

        outANegExpression(node);
    }

    @Override
    public void caseAIntExpression(AIntExpression node)
    {
        inAIntExpression(node);
        if(node.getNumber() != null)
        {
            node.getNumber().apply(this);
        }

        extrChild = node.getNumber().toString().trim();

        outAIntExpression(node);
    }

    @Override
    public void caseACharExpression(ACharExpression node)
    {
        inACharExpression(node);
        if(node.getConstChar() != null)
        {
            node.getConstChar().apply(this);
        }
        outACharExpression(node);
    }

    @Override
    public void caseAValExpression(AValExpression node)
    {
        RecordLValue recordLValue;
        String arrayName, tmpValue, newValue;

        inAValExpression(node);
        if(node.getLvalue() != null)
        {
            node.getLvalue().apply(this);
        }

        recordLValue = stackLValue.pop(); //get lvalue record

        if(recordLValue instanceof StringLiteral)
        {
            arrayName = ((StringLiteral) recordLValue).getLiteralId();
            createArrayQuad(recordLValue, arrayName);
        }
        else if(recordLValue.getRecord() instanceof RecordArray)             //RecordArray
        {
            createArrayQuad(recordLValue, recordLValue.getRecord().getName());
        }
        else
        {
            extrChild = recordLValue.getRecord().getName();
        }

        outAValExpression(node);
    }

    //create an array/StringLiteral Quad
    public void createArrayQuad(RecordLValue recordLValue, String arrayName)
    {
        String tmpValue, newValue;
        tmpValue = recordLValue.getAddressIndex();

        newValue = quadList.NewTemp("pointer");

        if(recordLValue.getAddressIndex() != null)
        {
            quadList.GenQuad("array", arrayName, tmpValue, newValue);
            if(recordLValue.emptyDimension()) //all dimension of the array were used so dereference the pointer
            {
                extrChild = "["+newValue+"]";
            }
            else //use address
            {
                extrChild = newValue;
            }
        }
        else
        {
            extrChild = arrayName;
        }
    }

    @Override
    public void caseAFunExpression(AFunExpression node)
    {
        RecordFunction recordFunction;
        String tmpName, tmpType;
        inAFunExpression(node);
        if(node.getFunctionCall() != null)
        {
            node.getFunctionCall().apply(this);
        }

        outAFunExpression(node);
    }

    @Override
    public void caseAHeader(AHeader node)
    {
        inAHeader(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        {
            List<PFparDefinition> copy = new ArrayList<PFparDefinition>(node.getFparDefinition());
            for(PFparDefinition e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getGeneralType() != null)
        {
            node.getGeneralType().apply(this);
        }

        extrChild = node.getIdentifier().toString().trim(); //keep the name of the function
        outAHeader(node);
    }

    @Override
    public void caseAFunctionCall(AFunctionCall node)
    {
        String functionName, tmpVar="";
        RecordFunction localRecordFunction;
        inAFunctionCall(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }

        functionName = node.getIdentifier().toString().trim();

        localRecordFunction = (RecordFunction)symTable.lookup(functionName);
        extrRecordFunction = localRecordFunction;

        if(node.getExprList() != null)
        {
            node.getExprList().apply(this);
        }

        if(! localRecordFunction.getType().equals("nothing")) //if its returning values generate par, RET quad
        {
            tmpVar = quadList.NewTemp(localRecordFunction.getType());
            quadList.GenQuad("par", "RET", tmpVar, "-");
        }


        quadList.GenQuad("call", "-", "-", localRecordFunction.getFixedName());
        outAFunctionCall(node);

        extrChild = tmpVar;
    }

    @Override
    public void caseAIdLvalue(AIdLvalue node)
    {
        inAIdLvalue(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }

        //extrChild = node.getIdentifier().toString().trim();

        RecordLValue lVar = new RecordLValue(symTable.lookup(node.getIdentifier().toString().trim())); //find variable at the symbol table
        stackLValue.push(lVar);
        idFound = true;

        outAIdLvalue(node);
    }

    @Override
    public void caseAStrLvalue(AStrLvalue node)
    {
        String tmpVar;

        inAStrLvalue(node);
        if(node.getConstString() != null)
        {
            node.getConstString().apply(this);
        }

        tmpVar = quadList.NewTemp("pointerStr");
        quadList.GenQuad(":=", node.getConstString().toString().trim(), "-", tmpVar);

        RecordLValue lVar = new StringLiteral(tmpVar);
        stackLValue.push(lVar);
        idFound = true;


        outAStrLvalue(node);
    }

    @Override
    public void caseAValLvalue(AValLvalue node)
    {
        String leftChild, rightChild, oldValue, tmpValue1, tmpValue2;
        RecordLValue recValue;
        boolean tmpFound;
        int currentDim;

        inAValLvalue(node);
        if(node.getLvalue() != null)
        {
            node.getLvalue().apply(this);
        }

        tmpFound = idFound;
        idFound = false;

        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }

        if(!tmpFound) //do calculations
        {
            recValue = stackLValue.pop();
            oldValue = recValue.getAddressIndex();
            currentDim = recValue.getDimension();

            //find its address, array serialization
            tmpValue1 = quadList.NewTemp("int");
            quadList.GenQuad("*", oldValue, Integer.toString(currentDim), tmpValue1);

            tmpValue2 = quadList.NewTemp("int");
            quadList.GenQuad("+", tmpValue1, extrChild, tmpValue2);

            recValue.setAddressIndex(tmpValue2);
            stackLValue.push(recValue);
        }
        else //if we are at the first index of the array we dont do calculations
        {
            recValue = stackLValue.pop();
            recValue.setAddressIndex(extrChild);
            stackLValue.push(recValue);
        }

        outAValLvalue(node);
    }
}
