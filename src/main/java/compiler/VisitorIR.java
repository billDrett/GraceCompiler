package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.util.*;

/**
 * Created by bill on 17/05/17.
 */

class RecordLValue
{
    private Record record;
    private Iterator<Integer> currentDimension;
    private String addressIndex;

    public RecordLValue(Record rec)
    {
        record = rec;
        addressIndex = null;
        if(record instanceof RecordArray)
        {
            System.out.println("REcords Arraay");
            RecordArray recArray = (RecordArray) record;
            currentDimension = recArray.getDimensions().iterator();
            currentDimension.next();
        }
        else
        {
            currentDimension = null;
        }
    }

    public Record getRecord()
    {
        if(record instanceof RecordArray)
        {
            return ((RecordArray) record);
        }
        return record;
    }

    public int getDimension()
    {
        return currentDimension.next();
    }

    public String getAddressIndex()
    {
        return addressIndex;
    }

    public void setAddressIndex(String newAddressIndex)
    {
        addressIndex = newAddressIndex;
    }

}



public class VisitorIR extends DepthFirstAdapter{
    private LinkedList<Integer> dimList = new LinkedList<>();

    QuadList quadList = new QuadList();
    SymbolTable symTable = new SymbolTable();
    String extrChild;
    Stack<RecordLValue> stackLValue = new Stack<>();
    boolean idFound;
    private String tmpVarType;
    Condition extrCond;

    public void backPatch(List<Integer> backList, int newLabel) //backList is a true or false list for u cocksuckers
    {
        Quad currentQuad;
        for(int label :backList)
        {
            currentQuad = quadList.getQuad(label);
            System.out.println("backpatch "+label+" "+newLabel);
            currentQuad.setOpt3(Integer.toString(newLabel));
        }
    }

    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getPProgram().apply(this);
        node.getEOF().apply(this);
        outStart(node);
        System.out.println("END");
        quadList.printAll();
    }

    public void inAProgram(AProgram node)
    {
        defaultIn(node);
    }

    public void outAProgram(AProgram node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAProgram(AProgram node)
    {
        inAProgram(node);
        if(node.getFunDefinition() != null)
        {
            node.getFunDefinition().apply(this);
        }
        outAProgram(node);
    }

    public void inAFunDefinition(AFunDefinition node)
    {
        defaultIn(node);
    }

    public void outAFunDefinition(AFunDefinition node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunDefinition(AFunDefinition node)
    {
        inAFunDefinition(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
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
        outAFunDefinition(node);
    }



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


    public void inASemiStatement(ASemiStatement node)
    {
        defaultIn(node);
    }

    public void outASemiStatement(ASemiStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASemiStatement(ASemiStatement node)
    {
        inASemiStatement(node);
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outASemiStatement(node);
    }

    public void inAAssignStatement(AAssignStatement node)
    {
        defaultIn(node);
    }

    public void outAAssignStatement(AAssignStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAssignStatement(AAssignStatement node)
    {
        inAAssignStatement(node);
        if(node.getLvalue() != null)
        {
            node.getLvalue().apply(this);
        }
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }
        outAAssignStatement(node);
    }

    public void inABlockStatement(ABlockStatement node)
    {
        defaultIn(node);
    }

    public void outABlockStatement(ABlockStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABlockStatement(ABlockStatement node)
    {
        inABlockStatement(node);
        {
            List<PStatement> copy = new ArrayList<PStatement>(node.getStatement());
            for(PStatement e : copy)
            {
                e.apply(this);
            }
        }
        outABlockStatement(node);
    }

    public void inAFuncCallStatement(AFuncCallStatement node)
    {
        defaultIn(node);
    }

    public void outAFuncCallStatement(AFuncCallStatement node)
    {
        defaultOut(node);
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

    public void inAIfWithoutElseStatement(AIfWithoutElseStatement node)
    {
        defaultIn(node);
    }

    public void outAIfWithoutElseStatement(AIfWithoutElseStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIfWithoutElseStatement(AIfWithoutElseStatement node)
    {
        inAIfWithoutElseStatement(node);
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }
        if(node.getStatement() != null)
        {
            node.getStatement().apply(this);
        }
        outAIfWithoutElseStatement(node);
    }

    public void inAIfWithElseStatement(AIfWithElseStatement node)
    {
        defaultIn(node);
    }

    public void outAIfWithElseStatement(AIfWithElseStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIfWithElseStatement(AIfWithElseStatement node)
    {
        inAIfWithElseStatement(node);
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }
        if(node.getId1() != null)
        {
            node.getId1().apply(this);
        }
        if(node.getId2() != null)
        {
            node.getId2().apply(this);
        }
        outAIfWithElseStatement(node);
    }

    public void inAWhileStatement(AWhileStatement node)
    {
        defaultIn(node);
    }

    public void outAWhileStatement(AWhileStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAWhileStatement(AWhileStatement node)
    {
        inAWhileStatement(node);
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }
        if(node.getStatement() != null)
        {
            node.getStatement().apply(this);
        }
        outAWhileStatement(node);
    }

    public void inAReturnStatement(AReturnStatement node)
    {
        defaultIn(node);
    }

    public void outAReturnStatement(AReturnStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAReturnStatement(AReturnStatement node)
    {
        inAReturnStatement(node);
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }
        outAReturnStatement(node);
    }

    public void inAFunDefLocalDef(AFunDefLocalDef node)
    {
        defaultIn(node);
    }

    public void outAFunDefLocalDef(AFunDefLocalDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunDefLocalDef(AFunDefLocalDef node)
    {
        inAFunDefLocalDef(node);
        if(node.getFunDefinition() != null)
        {
            node.getFunDefinition().apply(this);
        }
        outAFunDefLocalDef(node);
    }

    public void inAFunDeclLocalDef(AFunDeclLocalDef node)
    {
        defaultIn(node);
    }

    public void outAFunDeclLocalDef(AFunDeclLocalDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunDeclLocalDef(AFunDeclLocalDef node)
    {
        inAFunDeclLocalDef(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        outAFunDeclLocalDef(node);
    }

    public void inAVarDefLocalDef(AVarDefLocalDef node)
    {
        defaultIn(node);
    }

    public void outAVarDefLocalDef(AVarDefLocalDef node)
    {
        defaultOut(node);
    }


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

            if(!symTable.insert(tmpRec)) //check if variable already exists
            {
                System.out.println("Error variable already declared in the same scope");
            }

        }
        dimList.clear();
        System.out.println();
    }

    @Override
    public void caseAVarDefLocalDef(AVarDefLocalDef node)
    {
        inAVarDefLocalDef(node);
        if(node.getVarDefinition() != null)
        {
            node.getVarDefinition().apply(this);
        }
        outAVarDefLocalDef(node);
    }

    public void inAOrCondition(AOrCondition node)
    {
        defaultIn(node);
    }

    public void outAOrCondition(AOrCondition node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAOrCondition(AOrCondition node)
    {
        inAOrCondition(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAOrCondition(node);
    }

    public void inAAndCondition(AAndCondition node)
    {
        defaultIn(node);
    }

    public void outAAndCondition(AAndCondition node)
    {
        defaultOut(node);
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

    public void inANotCondition(ANotCondition node)
    {
        defaultIn(node);
    }

    public void outANotCondition(ANotCondition node)
    {
        defaultOut(node);
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

    public void inAParCondition(AParCondition node)
    {
        defaultIn(node);
    }

    public void outAParCondition(AParCondition node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAParCondition(AParCondition node)
    {
        inAParCondition(node);
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }
        outAParCondition(node);
    }

    public void inARelatCondition(ARelatCondition node)
    {
        defaultIn(node);
    }

    public void outARelatCondition(ARelatCondition node)
    {
        defaultOut(node);
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

        operator = node.getSymbol().toString();

        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }

        rightChild = extrChild;

        extrCond = new Condition();

        extrCond.addLabelTrue(quadList.NextQuad());
        quadList.GenQuad(leftChild, operator, rightChild, "*");

        extrCond.addLabelFalse(quadList.NextQuad());
        quadList.GenQuad("jump", "-", "-", "*");

        outARelatCondition(node);
    }

    public void inAEqualRelationOper(AEqualRelationOper node)
    {
        defaultIn(node);
    }

    public void outAEqualRelationOper(AEqualRelationOper node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAEqualRelationOper(AEqualRelationOper node)
    {
        inAEqualRelationOper(node);
        if(node.getEqual() != null)
        {
            node.getEqual().apply(this);
        }
        outAEqualRelationOper(node);
    }

    public void inANEqualRelationOper(ANEqualRelationOper node)
    {
        defaultIn(node);
    }

    public void outANEqualRelationOper(ANEqualRelationOper node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANEqualRelationOper(ANEqualRelationOper node)
    {
        inANEqualRelationOper(node);
        if(node.getNEqual() != null)
        {
            node.getNEqual().apply(this);
        }
        outANEqualRelationOper(node);
    }

    public void inALessRelationOper(ALessRelationOper node)
    {
        defaultIn(node);
    }

    public void outALessRelationOper(ALessRelationOper node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALessRelationOper(ALessRelationOper node)
    {
        inALessRelationOper(node);
        if(node.getLess() != null)
        {
            node.getLess().apply(this);
        }
        outALessRelationOper(node);
    }

    public void inALessEqualRelationOper(ALessEqualRelationOper node)
    {
        defaultIn(node);
    }

    public void outALessEqualRelationOper(ALessEqualRelationOper node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALessEqualRelationOper(ALessEqualRelationOper node)
    {
        inALessEqualRelationOper(node);
        if(node.getLessEqual() != null)
        {
            node.getLessEqual().apply(this);
        }
        outALessEqualRelationOper(node);
    }

    public void inAGreaterRelationOper(AGreaterRelationOper node)
    {
        defaultIn(node);
    }

    public void outAGreaterRelationOper(AGreaterRelationOper node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAGreaterRelationOper(AGreaterRelationOper node)
    {
        inAGreaterRelationOper(node);
        if(node.getGreater() != null)
        {
            node.getGreater().apply(this);
        }
        outAGreaterRelationOper(node);
    }

    public void inAGreaterEqualRelationOper(AGreaterEqualRelationOper node)
    {
        defaultIn(node);
    }

    public void outAGreaterEqualRelationOper(AGreaterEqualRelationOper node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAGreaterEqualRelationOper(AGreaterEqualRelationOper node)
    {
        inAGreaterEqualRelationOper(node);
        if(node.getGreaterEqual() != null)
        {
            node.getGreaterEqual().apply(this);
        }
        outAGreaterEqualRelationOper(node);
    }

    public void inAExprList(AExprList node)
    {
        defaultIn(node);
    }

    public void outAExprList(AExprList node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAExprList(AExprList node)
    {
        inAExprList(node);
        {
            List<PExpression> copy = new ArrayList<PExpression>(node.getExprs());
            for(PExpression e : copy)
            {
                e.apply(this);
            }
        }
        outAExprList(node);
    }

    public void inAPlusExpression(APlusExpression node)
    {
        defaultIn(node);
    }

    public void outAPlusExpression(APlusExpression node)
    {
        defaultOut(node);
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

    public void inAMinusExpression(AMinusExpression node)
    {
        defaultIn(node);
    }

    public void outAMinusExpression(AMinusExpression node)
    {
        defaultOut(node);
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

    public void inAMultExpression(AMultExpression node)
    {
        defaultIn(node);
    }

    public void outAMultExpression(AMultExpression node)
    {
        defaultOut(node);
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

    public void inADivExpression(ADivExpression node)
    {
        defaultIn(node);
    }

    public void outADivExpression(ADivExpression node)
    {
        defaultOut(node);
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

    public void inAModExpression(AModExpression node)
    {
        defaultIn(node);
    }

    public void outAModExpression(AModExpression node)
    {
        defaultOut(node);
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

    public void inAPosExpression(APosExpression node)
    {
        defaultIn(node);
    }

    public void outAPosExpression(APosExpression node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPosExpression(APosExpression node)
    {
        inAPosExpression(node);
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }
        outAPosExpression(node);
    }

    public void inANegExpression(ANegExpression node)
    {
        defaultIn(node);
    }

    public void outANegExpression(ANegExpression node)
    {
        defaultOut(node);
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

    public void inAIntExpression(AIntExpression node)
    {
        defaultIn(node);
    }

    public void outAIntExpression(AIntExpression node)
    {
        defaultOut(node);
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

    public void inACharExpression(ACharExpression node)
    {
        defaultIn(node);
    }

    public void outACharExpression(ACharExpression node)
    {
        defaultOut(node);
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

    public void inAValExpression(AValExpression node)
    {
        defaultIn(node);
    }

    public void outAValExpression(AValExpression node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAValExpression(AValExpression node)
    {
        RecordLValue recordLValue;
        String tmpValue, newValue;

        inAValExpression(node);
        if(node.getLvalue() != null)
        {
            node.getLvalue().apply(this);
        }

        recordLValue = stackLValue.pop();
        System.out.println("name is "+recordLValue.getRecord().getName());
        if(recordLValue.getRecord() instanceof RecordArray)             //RecordArray
        {
            System.out.println("Its a RecordARray!");
            tmpValue = recordLValue.getAddressIndex();

            newValue = quadList.NewTemp("pointer");
            quadList.GenQuad("array", recordLValue.getRecord().getName(), "[" +tmpValue+"]", newValue);
            extrChild = newValue;
        }
        else
        {
            System.out.println("Its a Record!");
            extrChild = recordLValue.getRecord().getName();
        }

        outAValExpression(node);
    }

    public void inAFunExpression(AFunExpression node)
    {
        defaultIn(node);
    }

    public void outAFunExpression(AFunExpression node)
    {
        defaultOut(node);
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

    public void inAHeader(AHeader node)
    {
        defaultIn(node);
    }

    public void outAHeader(AHeader node)
    {
        defaultOut(node);
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
        outAHeader(node);
    }

    public void inAFparDefinition(AFparDefinition node)
    {
        defaultIn(node);
    }

    public void outAFparDefinition(AFparDefinition node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFparDefinition(AFparDefinition node)
    {
        inAFparDefinition(node);
        if(node.getReference() != null)
        {
            node.getReference().apply(this);
        }
        {
            List<PVarIdentifier> copy = new ArrayList<PVarIdentifier>(node.getVarIdentifier());
            for(PVarIdentifier e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getFparType() != null)
        {
            node.getFparType().apply(this);
        }
        outAFparDefinition(node);
    }

    public void inAVarDefinition(AVarDefinition node)
    {
        defaultIn(node);
    }

  /*  public void outAVarDefinition(AVarDefinition node)
    {
        defaultOut(node);
    }
*/
    @Override
    public void caseAVarDefinition(AVarDefinition node)
    {
        inAVarDefinition(node);
        {
            List<PVarIdentifier> copy = new ArrayList<PVarIdentifier>(node.getVarIdentifier());
            for(PVarIdentifier e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getVarType() != null)
        {
            node.getVarType().apply(this);
        }
        outAVarDefinition(node);
    }

    public void inAVarIdentifier(AVarIdentifier node)
    {
        defaultIn(node);
    }

    public void outAVarIdentifier(AVarIdentifier node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAVarIdentifier(AVarIdentifier node)
    {
        inAVarIdentifier(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        outAVarIdentifier(node);
    }

    public void inAFparType(AFparType node)
    {
        defaultIn(node);
    }

    public void outAFparType(AFparType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFparType(AFparType node)
    {
        inAFparType(node);
        if(node.getGeneralType() != null)
        {
            node.getGeneralType().apply(this);
        }
        if(node.getEmptyBr() != null)
        {
            node.getEmptyBr().apply(this);
        }
        {
            List<PConstIntBr> copy = new ArrayList<PConstIntBr>(node.getConstIntBr());
            for(PConstIntBr e : copy)
            {
                e.apply(this);
            }
        }
        outAFparType(node);
    }

    /*public void inAVarType(AVarType node)
    {
        defaultIn(node);
    }*/

    public void outAVarType(AVarType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAVarType(AVarType node)
    {
        inAVarType(node);
        if(node.getGeneralType() != null)
        {
            node.getGeneralType().apply(this);
        }
        {
            List<PConstIntBr> copy = new ArrayList<PConstIntBr>(node.getConstIntBr());
            for(PConstIntBr e : copy)
            {
                e.apply(this);
            }
        }
        outAVarType(node);
    }

    public void inAIntGeneralType(AIntGeneralType node)
    {
        defaultIn(node);
    }

    public void outAIntGeneralType(AIntGeneralType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntGeneralType(AIntGeneralType node)
    {
        inAIntGeneralType(node);
        if(node.getInt() != null)
        {
            node.getInt().apply(this);
        }
        outAIntGeneralType(node);
    }

    public void inACharGeneralType(ACharGeneralType node)
    {
        defaultIn(node);
    }

    public void outACharGeneralType(ACharGeneralType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseACharGeneralType(ACharGeneralType node)
    {
        inACharGeneralType(node);
        if(node.getChar() != null)
        {
            node.getChar().apply(this);
        }
        outACharGeneralType(node);
    }

    public void inANothGeneralType(ANothGeneralType node)
    {
        defaultIn(node);
    }

    public void outANothGeneralType(ANothGeneralType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANothGeneralType(ANothGeneralType node)
    {
        inANothGeneralType(node);
        if(node.getNothing() != null)
        {
            node.getNothing().apply(this);
        }


        outANothGeneralType(node);
    }

    public void inAConstIntBr(AConstIntBr node)
    {
        defaultIn(node);
    }

    public void outAConstIntBr(AConstIntBr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAConstIntBr(AConstIntBr node)
    {
        inAConstIntBr(node);
        if(node.getNumber() != null)
        {
            node.getNumber().apply(this);
        }
        outAConstIntBr(node);
    }

    public void inAEmptyBr(AEmptyBr node)
    {
        defaultIn(node);
    }

    public void outAEmptyBr(AEmptyBr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAEmptyBr(AEmptyBr node)
    {
        inAEmptyBr(node);
        outAEmptyBr(node);
    }

    public void inAFunctionCall(AFunctionCall node)
    {
        defaultIn(node);
    }

    public void outAFunctionCall(AFunctionCall node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunctionCall(AFunctionCall node)
    {
        String tmpName;
        inAFunctionCall(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }

        tmpName = node.getIdentifier().toString().trim();
        quadList.GenQuad("call", "-", "-", tmpName);

        if(node.getExprList() != null)
        {
            node.getExprList().apply(this);
        }
        outAFunctionCall(node);

        extrChild = tmpName;
    }

    public void inAIdLvalue(AIdLvalue node)
    {
        defaultIn(node);
    }

    public void outAIdLvalue(AIdLvalue node)
    {
        defaultOut(node);
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

        RecordLValue lVar = new RecordLValue(symTable.lookup(node.getIdentifier().toString().trim()));
        stackLValue.push(lVar);
        idFound = true;

        outAIdLvalue(node);
    }

    public void inAStrLvalue(AStrLvalue node)
    {
        defaultIn(node);
    }

    public void outAStrLvalue(AStrLvalue node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAStrLvalue(AStrLvalue node)
    {
        inAStrLvalue(node);
        if(node.getConstString() != null)
        {
            node.getConstString().apply(this);
        }
        outAStrLvalue(node);
    }

    public void inAValLvalue(AValLvalue node)
    {
        defaultIn(node);
    }

    public void outAValLvalue(AValLvalue node)
    {
        defaultOut(node);
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
        //leftChild = extrChild;
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

            tmpValue1 = quadList.NewTemp("int");
            quadList.GenQuad("*", oldValue, Integer.toString(currentDim), tmpValue1);

            tmpValue2 = quadList.NewTemp("int");
            quadList.GenQuad("+", tmpValue1, extrChild, tmpValue2);

            recValue.setAddressIndex(tmpValue2);
            stackLValue.push(recValue);
        }
        else
        {
            recValue = stackLValue.pop();
            recValue.setAddressIndex(extrChild);
            stackLValue.push(recValue);
        }

        outAValLvalue(node);
    }
}
