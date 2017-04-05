package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class Print extends DepthFirstAdapter
{

    @Override
    public void inABlockBlock(ABlockBlock node) {
        System.out.println("Block in");
    }

    @Override
    public void outABlockBlock(ABlockBlock node) {
        System.out.println("Block out");
    }

    @Override
    public void inAAssignmentStmt(AAssignmentStmt node) {
        System.out.println("Assignment: ");
        System.out.println("\t-name : "+node.getLValue());
        System.out.println("\t-value: "+node.getExpr());
    }

    @Override
    public void inAWhileStmt(AWhileStmt node) {
        System.out.println("Statement: While");
        System.out.println("-Condition: "+"("+node.getCond()+")");
        System.out.println("--Enter while Statement");
    }

    @Override
    public void outAWhileStmt(AWhileStmt node) {
        System.out.println("--Exit while Statement");
    }

    @Override
    public void inAReturnStmt(AReturnStmt node) {
        System.out.printf("Return \n");
    }

    @Override
    public void inAWithoutElseIfStmt(AWithoutElseIfStmt node) {
        System.out.println("Statement: If without Else");
        System.out.println("-Condition: "+"("+node.getCond()+")");
        System.out.println("-IfStatement: "+node.getStmt());
        System.out.println("--EnterIfWithoutElseStatement");
    }

    @Override
    public void outAWithoutElseIfStmt(AWithoutElseIfStmt node) {
        System.out.println("--ExitIfWithoutElseStatement");
    }

    @Override
    public void inAWithElseIfStmt(AWithElseIfStmt node) {
        System.out.println("Statement: If with Else");
        System.out.println("-Condition:      "+"("+node.getCond()+")");
        System.out.println("-If Statement:   "+node.getStmtWithElse());
        System.out.println("-Else Statement: "+node.getStmt());
        System.out.println("--EnterIfWithElseStatement");
    }

    @Override
    public void outAWithElseIfStmt(AWithElseIfStmt node) {
        System.out.println("--ExitIfWithElseStatement");
    }

    @Override
    public void inAHeaderHeader(AHeaderHeader node){
        System.out.println("Fuction: ");
        System.out.println("\t-Name       : "+ node.getIdentifier());
        System.out.println("\t-Parameters : "+ node.getFunctionParameters());
        System.out.println("\t-return Type: "+ node.getRetType());
    }

    @Override
    public void inAFuncDefLocalDefinition(AFuncDefLocalDefinition node){
        System.out.println("Local Fuction definition");
    }

    @Override
    public void inAFuncDeclLocalDefinition(AFuncDeclLocalDefinition node){
        System.out.println("Local Fuction declaration");
    }

    @Override
    public void inAVarVariableDefinition(AVarVariableDefinition node){
        String cids = (node.getCommaIdentifier()).toString();
        System.out.println("Variable(s) Definition: ");
        System.out.print("\t-Name(s): "+ node.getIdentifier());
        if ((node.getCommaIdentifier()).toString() != "[]")
            for(int i=1; i<cids.length()-1; i++)
               System.out.print(cids.charAt(i));
        System.out.println("\n\t-varType: "+ node.getType());
    }

    @Override
    public void inAFuncDeclFunctionDeclaration(AFuncDeclFunctionDeclaration node){
        System.out.println("Fuction declaration");
    }

    @Override
    public void inAFuncCallFuncCall(AFuncCallFuncCall node){
        System.out.println("Fuction call");
        System.out.println("\t-Name      : "+node.getIdentifier());
        System.out.println("\t-Parameters: "+node.getFuncCallExpr());
    }
}
