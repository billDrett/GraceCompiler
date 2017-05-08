package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.util.*;

public class Print extends DepthFirstAdapter
{
    private SymbolTable symTable = new SymbolTable();
    private Deque<Double> stack = new ArrayDeque<>();
    private ArrayList<String> fparVars = new ArrayList<>();
    private LinkedList<Integer> dimList = new LinkedList<Integer>();
    private LinkedList<Record> fParam = new LinkedList<>();
    private Stack<String> type = new Stack<>();
    private String tmpVarType;

    @Override
    public void inAProgram(AProgram node){
        System.out.println("In program");
    }

    @Override
    public void inAFunDefinition(AFunDefinition node) {
        System.out.println("In FunDefinition");
        symTable.enter();
    }

    @Override
    public void outAFunDefinition(AFunDefinition node) {
        System.out.println("Out FunDefinition");
        symTable.printALl();
        symTable.exit();
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
    public void inABlockStatement(ABlockStatement node) {
        System.out.println("In BlockStatement");
    }

    @Override
    public void inAFuncCallStatement(AFuncCallStatement node) {
        System.out.println("In FuncCallStatement");
    }

    @Override
    public void inAIfWithoutElseStatement(AIfWithoutElseStatement node) {
        System.out.println("In IfWithoutElseStatement");
    }
    @Override
    public void inAIfWithElseStatement(AIfWithElseStatement node) {
        System.out.println("In IfWithElseStatement");
    }
    @Override
    public void inAWhileStatement(AWhileStatement node) {
        System.out.println("In WhileStatement");
    }
    @Override
    public void inAReturnStatement(AReturnStatement node) {
        System.out.println("In ReturnStatement");
    }

    @Override
    public void inAFunDefLocalDef(AFunDefLocalDef node) {
        System.out.println("In FunDefLocalDef");
    }
    @Override
    public void inAFunDeclLocalDef(AFunDeclLocalDef node) {
        System.out.println("In FunDeclLocalDef");
    }
    @Override
    public void inAVarDefLocalDef(AVarDefLocalDef node) {
        System.out.println("In AVarDefLocalDef");
    }
    @Override
    public void inAOrCondition(AOrCondition node) {
        System.out.println("In AOrCondition "+ node.getLeft()+" or "+node.getRight());
    }

    @Override
    public void inAAndCondition(AAndCondition node) {
        System.out.println("In AAndCondition "+ node.getLeft()+" and "+node.getRight());
    }
    @Override
    public void inANotCondition(ANotCondition node) {
        System.out.println("In ANotCondition "+ " not "+node.getCondition());
    }

    @Override
    public void inARelatCondition(ARelatCondition node) {
        System.out.println("In ARelatCondition "+node.getLeft()+" "+node.getSymbol()+ ""+node.getRight());
    }
    @Override
    public void inAEqualRelationOper(AEqualRelationOper node) {
        System.out.println("In AEqualRelationOper");
    }

    @Override
    public void inANEqualRelationOper(ANEqualRelationOper node) {
        System.out.println("In ANEqualRelationOper");
    }
    @Override
    public void inALessRelationOper(ALessRelationOper node) {
        System.out.println("In ALessRelationOper");
    }

    @Override
    public void inALessEqualRelationOper(ALessEqualRelationOper node) {
        System.out.println("In ALessEqualRelationOper");
    }
    @Override
    public void inAGreaterRelationOper(AGreaterRelationOper node) {
        System.out.println("In AGreaterRelationOper");
    }

    @Override
    public void inAGreaterEqualRelationOper(AGreaterEqualRelationOper node) {
        System.out.println("In AGreaterEqualRelationOper");
    }

    @Override
    public void inAExprList(AExprList node){
        System.out.println("In ExprList");
        for (PExpression expr: node.getExprs())
        {
            System.out.println(expr+" ");
        }
    }

    @Override
    public void outAPlusExpression(APlusExpression node) {
        System.out.println("Out APlusExpression "+ node.getLeft()+" + " + node.getRight());
        String leftType, rightType;

        rightType = type.pop();
        leftType = type.pop();

        System.out.println("Right "+rightType+".");
        System.out.println("Left "+leftType);
        if(rightType.equals("char") || leftType.equals("char"))
        {
            System.out.println("Confict in plus expression: type char");
        }

        type.push("int");
    }

    @Override
    public void outAMinusExpression(AMinusExpression node) {
        System.out.println("Out AMinusExpression "+ node.getLeft()+" - " + node.getRight());
        String leftType, rightType;

        rightType = type.pop();
        leftType = type.pop();

        if(rightType.equals("char") || leftType.equals("char"))
        {
            System.out.println("Confict in minus expression: type char");
        }

        type.push("int");
    }
    @Override
    public void outAMultExpression(AMultExpression node) {
        System.out.println("Out AMultExpression "+ node.getLeft()+" * " + node.getRight());
        String leftType, rightType;

        rightType = type.pop();
        leftType = type.pop();

        if(rightType.equals("char") || leftType.equals("char"))
        {
            System.out.println("Confict in mult expression: type char");
        }

        type.push("int");
    }

    @Override
    public void outADivExpression(ADivExpression node) {
        System.out.println("Out ADivExpression "+ node.getLeft()+" div " + node.getRight());
        String leftType, rightType;

        rightType = type.pop();
        leftType = type.pop();

        if(rightType.equals("char") || leftType.equals("char"))
        {
            System.out.println("Confict in div expression: type char");
        }

        type.push("int");
    }
    @Override
    public void outAModExpression(AModExpression node) {
        System.out.println("Out AModExpression "+ node.getLeft()+" mod " + node.getRight());
        String leftType, rightType;

        rightType = type.pop();
        leftType = type.pop();

        if(rightType.equals("char") || leftType.equals("char"))
        {
            System.out.println("Confict in mod expression: type char");
        }

        type.push("int");
    }

    @Override
    public void inAIntExpression(AIntExpression node) {
        System.out.println("In AIntExpression: "+node.getNumber());

        type.push("int");
    }
    @Override
    public void inACharExpression(ACharExpression node) {
        System.out.println("In ACharExpression: "+node.getConstChar());

        type.push("char");
    }

    @Override
    public void inAPosExpression(APosExpression node) {
        System.out.println("In APosExpression: +"+node.getExpression());
    }
    @Override
    public void inANegExpression(ANegExpression node) {
        System.out.println("In ANegExpression: -"+node.getExpression());
    }

    @Override
    public void inAValExpression(AValExpression node) {
        System.out.println("In AValExpression: "+node.getLvalue());


    }

    @Override
    public void outAValExpression(AValExpression node) {
        System.out.println("Out AValExpression: "+node.getLvalue());

    }
    @Override
    public void inAFunExpression(AFunExpression node) {
        System.out.println("In AFunExpression");
    }

    @Override
    public void inAHeader(AHeader node) {
        AFparDefinition Apar;
        System.out.println("IN Function has name "+ node.getIdentifier());
        for(PFparDefinition pexr:node.getFparDefinition())
        {
            Apar= (AFparDefinition) pexr;
            System.out.println(Apar.getFparType());
        }

    }

    @Override
    public void outAHeader(AHeader node) {
        Record recFunct;
        AFparDefinition Apar;

        System.out.println("OUt Function has name "+ node.getIdentifier());
        for(PFparDefinition pexr:node.getFparDefinition())
        {
            Apar= (AFparDefinition) pexr;
            System.out.println(Apar.getFparType());
        }

        System.out.println("SO");
        for(String myString : fparVars)
        {
            System.out.println(myString);
        }

        recFunct = new RecordFunction(node.getIdentifier().toString().trim(), node.getGeneralType().toString().trim(), "Function", fParam);
        symTable.insert(recFunct);

        fParam.clear();
    }

    @Override
    public void outAFparDefinition(AFparDefinition node) {
        Record tmpRec;
        boolean ref;
        System.out.println("In AFparDefinition "+node.getReference()+" number of parameters "+node.getVarIdentifier().size()+ " ");

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
/*
    @Override
    public void inAVarDefinition(AVarDefinition node) {

        System.out.print("In AVarDefinition type "+node.getVarType()+ " number of parameters "+node.getVarIdentifier().size()+" Variables ");
        for(PVarIdentifier Pexr: node.getVarIdentifier())
        {
            tmpRec
            symTable.insert()
            System.out.print(Pexr+" ");
        }
        System.out.println();
    }
*/
    @Override
    public void outAVarDefinition(AVarDefinition node) {
        Record tmpRec;

        System.out.print("In AVarDefinition type "+node.getVarType()+ " number of parameters "+node.getVarIdentifier().size()+" Variables ");
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
            System.out.println("array Size " + arrayDimensions);
        }
    }

    @Override
    public void inAIntGeneralType(AIntGeneralType node) {
        System.out.println("In AIntGeneralType");
    }
    @Override
    public void inACharGeneralType(ACharGeneralType node) {
        System.out.println("In ACharGeneralType");
    }

    @Override
    public void inANothGeneralType(ANothGeneralType node) {
        System.out.println("In ANothGeneralType");
    }
    @Override
    public void inAConstIntBr(AConstIntBr node) {
        System.out.println("In AConstIntBr");
    }

    @Override
    public void inAEmptyBr(AEmptyBr node) {
        System.out.println("In AEmptyBr");
    }

    @Override
    public void inAFunctionCall(AFunctionCall node) {
        System.out.print("In AFunctionCall: Name: "+ node.getIdentifier()+ " number of Expressions "+node.getExprList());
        System.out.println();
    }

    @Override
    public void inAIdLvalue(AIdLvalue node) {
        System.out.println("In AIdLvalue : ");
        Record tmpRec;

        tmpRec = symTable.lookup(node.getIdentifier().toString().trim());

        if(tmpRec == null)
        {
            System.out.println("Error undefined variable "+node.getIdentifier());
            type.push("Null");
            return;
        }

        type.push(tmpRec.getType());
        System.out.println("Var name "+ tmpRec.getName()+" "+ tmpRec.getType());
    }

    @Override
    public void inAStrLvalue(AStrLvalue node) {
        System.out.println("In AStrLvalue: "+node.getConstString());

        type.push("char");
    }

    @Override
    public void inAValLvalue(AValLvalue node) {

    }

    @Override
    public void outAValLvalue(AValLvalue node) {
        System.out.println("In AValLvalue");
        String leftType, rightType;

        rightType = type.pop();
        System.out.println("Right "+rightType);
       /* leftType = type.pop();
        System.out.println("Left "+leftType);
        */

        if(rightType != "int")
        {
            System.out.println("Error array index must be int");
        }


    }
}
