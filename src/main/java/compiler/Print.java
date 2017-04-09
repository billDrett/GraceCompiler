package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

import java.util.Stack;

public class Print extends DepthFirstAdapter
{
    private Stack<String> st = new Stack<String>();
    private int depth =0;
    private int condDepth =0;

    public String stringSpaces()
    {
        String newString = "";
        for(int i=0; i<depth; i++)
            newString = newString + "  ";
        return newString;
    }

    @Override
    public void inABlockBlock(ABlockBlock node) {
        System.out.println(stringSpaces()+"{");
        depth++;
    }

    @Override
    public void outABlockBlock(ABlockBlock node) {
        depth--;
        System.out.println(stringSpaces()+"}");
    }

    @Override
    public void outAAssignmentStmt(AAssignmentStmt node) {
        System.out.println(stringSpaces()+"Assignment: ");
        System.out.println(stringSpaces()+"\t--Name : "+node.getLValue());
        System.out.println(stringSpaces()+"\t--Value: "+st.pop());
    }

    @Override
    public void outAFuncCallStmt(AFuncCallStmt node) {
        System.out.println(stringSpaces()+"Function Call: "+st.pop());
    }

    @Override
    public void inAWhileStmt(AWhileStmt node) {
        System.out.println(stringSpaces()+"Statement: While");
        System.out.println(stringSpaces()+"--Enter While Statement");
        System.out.print(stringSpaces()+"While "); //condition
    }

    @Override
    public void outAWhileStmt(AWhileStmt node) {

        System.out.println(stringSpaces()+"--Exit While Statement");
    }
/*
    @Override
    public void inAWhileStmtWithElse(AWhileStmtWithElse node) {
        System.out.println(stringSpaces()+"Statement: While_ with_else");
        System.out.println(stringSpaces()+"--Enter While_ with_else Statement");
        System.out.print(stringSpaces()+"While_with_else "); //condition
    }

    @Override
    public void outAWhileStmtWithElse(AWhileStmtWithElse node){

        System.out.println(stringSpaces()+"--Exit While_with_else Statement");
    }

*/
    @Override
    public void inAReturnStmt(AReturnStmt node) {
        System.out.println(stringSpaces()+"Return");
    }

    @Override
    public void inAWithoutElseIfStmt(AWithoutElseIfStmt node) {
        System.out.println(stringSpaces()+"--Enter IfWithoutElse Statement");
        System.out.print(stringSpaces()+"-IfWithoutElse "); //condition
    }

    @Override
    public void outAWithoutElseIfStmt(AWithoutElseIfStmt node) {
        System.out.println(stringSpaces()+"-IfStatement: "+node.getStmt());
        System.out.println(stringSpaces()+"--Exit IfWithoutElse Statement");
    }


    @Override
    public void inAWithElseIfStmt(AWithElseIfStmt node) {
        System.out.println(stringSpaces()+"--Enter IfWithElse Statement");
        System.out.println(stringSpaces()+"Statement: If with Else");
        System.out.println(stringSpaces()+"-If Statement:   "+node.getStmtWithElse());
        System.out.println(stringSpaces()+"-Else Statement: "+node.getStmt());

        System.out.print(stringSpaces()+"-IfWithElse "); //condition
    }

    @Override
    public void outAWithElseIfStmt(AWithElseIfStmt node) {

        System.out.println(stringSpaces()+"--Exit IfWithElse Statement");
    }

    @Override
    public void inAHeaderHeader(AHeaderHeader node){
        System.out.println(stringSpaces()+"Fuction: ");
        System.out.println(stringSpaces()+"\t--Name       : "+ node.getIdentifier());
        System.out.println(stringSpaces()+"\t--Parameters : "+ node.getFunctionParameters());
        System.out.println(stringSpaces()+"\t--Return Type: "+ node.getRetType());
    }

    @Override
    public void inAFuncDefLocalDefinition(AFuncDefLocalDefinition node){
        System.out.println(stringSpaces()+"Local Fuction definition");
    }

    @Override
    public void inAFuncDeclLocalDefinition(AFuncDeclLocalDefinition node){
        System.out.println(stringSpaces()+"Local Fuction declaration");
    }

    @Override
    public void inAVarVariableDefinition(AVarVariableDefinition node){
        String cids = (node.getCommaIdentifier()).toString();
        System.out.println(stringSpaces()+"Variable(s) Definition: ");
        System.out.print(stringSpaces()+"\t--Name(s): "+ node.getIdentifier());
        if ((node.getCommaIdentifier()).toString() != "[]")
            for(int i=1; i<cids.length()-1; i++)
               System.out.print(cids.charAt(i));
        System.out.println(stringSpaces()+"\n\t--varType: "+ node.getType());
    }

    @Override
    public void inAFuncDeclFunctionDeclaration(AFuncDeclFunctionDeclaration node){
        System.out.println(stringSpaces()+"Fuction declaration");
    }

    /******************************************************************************/

    @Override
    public void inAPlusExpr(APlusExpr node){

    }

    @Override
    public void outAPlusExpr(APlusExpr node){
        String rightChild = st.pop();
        String newString = "("+ st.pop()+" + "+rightChild+")";
        st.push(newString);

    }

    @Override
    public void inAMinusExpr(AMinusExpr node){

    }

    @Override
    public void outAMinusExpr(AMinusExpr node){
        String rightChild = st.pop();
        String newString = "("+ st.pop()+" - "+rightChild+")";
        st.push(newString);

    }

    @Override
    public void inAMultFactor(AMultFactor node){

    }

    @Override
    public void outAMultFactor(AMultFactor node){
        String rightChild = st.pop();
        String newString = "("+ st.pop()+" * "+rightChild+")";
        st.push(newString);

    }

    @Override
    public void inADivFactor(ADivFactor node){

    }

    @Override
    public void outADivFactor(ADivFactor node){
        String rightChild = st.pop();
        String newString = "("+ st.pop()+" div "+rightChild+")";
        st.push(newString);

    }

    @Override
    public void outAModFactor(AModFactor node){
        String rightChild = st.pop();
        String newString = "("+ st.pop()+" mod "+rightChild+")";
        st.push(newString);
    }


    @Override
    public void outAParExprPar(AParExprPar node){

    }

    @Override
    public void inATermTermSign(ATermTermSign node){

    }

    @Override
    public void outATermTermSign(ATermTermSign node){
        String newString = "term::"+ st.pop();
        st.push(newString);
    }

    @Override
    public void inASignPlusTermSign(ASignPlusTermSign node){

    }

    @Override
    public void outASignPlusTermSign(ASignPlusTermSign node){
        String newString = "term:: +"+ st.pop();
        st.push(newString);
    }

    @Override
    public void inASignMinusTermSign(ASignMinusTermSign node){

    }

    @Override
    public void outASignMinusTermSign(ASignMinusTermSign node){
        String newString = "term:: -"+ st.pop();
        st.push(newString);
    }

    @Override
    public void inAIntConstTerm(AIntConstTerm node){

    }

    @Override
    public void outAIntConstTerm(AIntConstTerm node){
        String newString = "constInt::"+ node.getNumber();
        st.push(newString);
    }

    @Override
    public void inACharConstTerm(ACharConstTerm node){

    }

    @Override
    public void outACharConstTerm(ACharConstTerm node){
        String newString = "constChar::"+ node.getConstChar();
        st.push(newString);
    }

    @Override
    public void inALValueTerm(ALValueTerm node){

    }

    @Override
    public void outALValueTerm(ALValueTerm node){
        String newString = "lvalue::"+ node.getLValue();
        st.push(newString);
    }

    @Override
    public void outALValueLValue(ALValueLValue node)
    {
        st.pop();
    }

    @Override
    public void inAFuncCallTerm(AFuncCallTerm node){

    }

    @Override
    public void outAFuncCallTerm(AFuncCallTerm node){
        String newString = "funcCall::"+ st.pop();
        st.push(newString);
    }

    @Override
    public void inAFuncCallFuncCall(AFuncCallFuncCall node){

    }

    @Override
    public void outAFuncCallFuncCall(AFuncCallFuncCall node){
        String newString;
        if (node.getFuncCallExpr() == null)
            newString = "Id::"+node.getIdentifier()+"NoParameters::( )";
        else
            newString = "Id::"+node.getIdentifier()+"Parameters::("+ st.pop()+")";
        st.push(newString);
    }

    @Override
    public void inAExprCommaExprFuncCallExpr(AExprCommaExprFuncCallExpr node){

    }

    @Override
    public void outAExprCommaExprFuncCallExpr(AExprCommaExprFuncCallExpr node){
        String newString = st.pop();
        st.push(newString);
    }

    @Override
    public void inACommaExprCommaExpr(ACommaExprCommaExpr node){

    }

    @Override
    public void outACommaExprCommaExpr(ACommaExprCommaExpr node){
        String rightChild = st.pop();
        String newString = st.pop()+" , "+rightChild;
        st.push(newString);
    }

    /******************************************************************************/

    @Override
    public void inACondCond(ACondCond node){
        condDepth++;
    }

    @Override
    public void outACondCond(ACondCond node){
        condDepth--;
        if(condDepth == 0) {
            System.out.println("Condition:: " + st.pop());
        }
    }

    @Override
    public void inACondOrCond(ACondOrCond node){
        condDepth++;
    }

    @Override
    public void outACondOrCond(ACondOrCond node){
        condDepth--;

        String rightChild = st.pop();
        String newString = "("+ st.pop()+" or "+rightChild+")";
        if (condDepth == 0) {
            System.out.println("Condition:: "+newString);
        }
        else
        {
            st.push(newString);
        }

    }

    @Override
    public void inACondCondAnd(ACondCondAnd node){

    }

    @Override
    public void inACondAndCondAnd(ACondAndCondAnd node){

    }

    @Override
    public void outACondAndCondAnd(ACondAndCondAnd node){
        String rightChild = st.pop();
        String newString = "("+ st.pop()+" and "+rightChild+")";
        st.push(newString);
    }

    @Override
    public void inACondCondNot(ACondCondNot node){

    }


    @Override
    public void inACondNotCondNot(ACondNotCondNot node){

    }

    @Override
    public void outACondNotCondNot(ACondNotCondNot node){
        String newString = "( not "+ st.pop()+")";
        st.push(newString);
    }

    @Override
    public void inAExprCondPar(AExprCondPar node){

    }

    @Override
    public void inACondCondPar(ACondCondPar node){

    }
    
    @Override
    public void inACondRelatCondRelat(ACondRelatCondRelat node){

    }

    @Override
    public void outACondRelatCondRelat(ACondRelatCondRelat node){
        String rightChild = st.pop();
        String newString = "("+ st.pop()+node.getRelatOper()+rightChild+")";
        st.push(newString);
    }

}
