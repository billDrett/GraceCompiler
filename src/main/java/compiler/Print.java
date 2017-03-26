package compiler;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;
/**
 * Created by bill on 26/03/17.
 */
public class Print extends DepthFirstAdapter
{

    @Override
    public void inAFuncDefProgram(AFuncDefProgram node) {
        System.out.printf("Defining A Function\n");
    }

    @Override
    public void inACommd(ACommd node) {
        System.out.printf("Command \n");
    }

    @Override
    public void inABlockBlock(ABlockBlock node) {
        System.out.printf("Block \n");
    }

    @Override
    public void inASemiStmt(ASemiStmt node) {
        System.out.printf("Semicolon Statement \n");
    }

    @Override
    public void inAAssignmentStmt(AAssignmentStmt node) {
        System.out.printf("Assignment \n");
    }

    @Override
    public void inABlockStmt(ABlockStmt node) {
        System.out.printf("Block Statement\n");
    }

    @Override
    public void inAFuncCallStmt(AFuncCallStmt node) {
        System.out.printf("Function Call\n");
    }

    @Override
    public void inAIfStmt(AIfStmt node) {
        System.out.printf("If statement\n");
    }

    @Override
    public void inAWhileStmt(AWhileStmt node) {
        System.out.printf("While Statement \n");
    }

    @Override
    public void inAReturnStmt(AReturnStmt node) {
        System.out.printf("Return \n");
    }

    @Override
    public void inAWithoutElseIfStmt(AWithoutElseIfStmt node) {
        System.out.printf("Without Else If Statement \n");
    }

    @Override
    public void inAWithElseIfStmt(AWithElseIfStmt node) {
        System.out.printf("With Else If Statement \n");
    }

    @Override
    public void inASemiStmtWithElse(ASemiStmtWithElse node) {
        System.out.printf("A semi statement with Else\n");
    }

    @Override
    public void inAAssignmentStmtWithElse(AAssignmentStmtWithElse node){
        System.out.printf("Assignment Statement With Else\n");
    }

    @Override
    public void inABlockStmtWithElse(ABlockStmtWithElse node) {
        System.out.printf("Block Statement With Else\n");
    }

    @Override
    public void inAFuncCallStmtWithElse(AFuncCallStmtWithElse node) {
        System.out.printf("Function Call Statement with Else\n");
    }

    @Override
    public void inAIfElseStmtWithElse(AIfElseStmtWithElse node) {
        System.out.printf("If Else Statement With Else\n");
    }

    @Override
    public void inAWhileStmtWithElse(AWhileStmtWithElse node) {
        System.out.printf("While Statement With Else\n");
    }

    @Override
    public void inAReturnStmtWithElse(AReturnStmtWithElse node) {
        System.out.printf("Return Statement With Else\n");
    }

    @Override
    public void inAFactorExpr(AFactorExpr node) {
        System.out.printf("Factor Expression\n");
    }

    @Override
    public void inAPlusExpr(APlusExpr node) {
        System.out.printf("Plus Expression\n");
    }

    @Override
    public void inAMinusExpr(AMinusExpr node) {
        System.out.printf("Minus Expression\n");
    }

    @Override
    public void inAExprParFactor(AExprParFactor node) {
        System.out.printf("Expression Par Factor\n");
    }

    @Override
    public void inAMultFactor(AMultFactor node) {
        System.out.printf("Mult Factor\n");
    }

    @Override
    public void inADivFactor(ADivFactor node) {
        System.out.printf("Division Factor\n");
    }

    @Override
    public void inAModFactor(AModFactor node) {
        System.out.printf("Module Factor\n");
    }

    @Override
    public void inATermExprPar(ATermExprPar node) {
        System.out.printf("Term Expression Par\n");
    }

    @Override
    public void inAParExprPar(AParExprPar node) {
        System.out.printf("Par Expr Par\n");
    }

    @Override
    public void inATermTermSign(ATermTermSign node) {
        System.out.printf("Term Term Sign\n");
    }

    @Override
    public void inASignPlusTermSign(ASignPlusTermSign node) {
        System.out.printf("Sign Plus Term Sign\n");
    }

    @Override
    public void inASignMinusTermSign(ASignMinusTermSign node) {
        System.out.printf("Sign Minus Term Sign\n");
    }

    @Override
    public void inAIntConstTerm(AIntConstTerm node) {
        System.out.printf("Int Const Term\n");
    }

    @Override
    public void inACharConstTerm(ACharConstTerm node) {
        System.out.printf("Char Const Term\n");
    }

    @Override
    public void inALValueTerm(ALValueTerm node) {
        System.out.printf("LValue Term\n");
    }

    @Override
    public void inAFuncCallTerm(AFuncCallTerm node) {
        System.out.printf("FunctinCall Term\n");
    }

    @Override
    public void inACondCond(ACondCond node) {
        System.out.printf("Cond Cond\n");
    }

    @Override
    public void inACondOrCond(ACondOrCond node) {
        System.out.printf("Cond Or Cond\n");
    }

    @Override
    public void inACondCondAnd(ACondCondAnd node) {
        System.out.printf("Cond Cond And \n");
    }

    @Override
    public void inACondAndCondAnd(ACondAndCondAnd node) {
        System.out.printf("Cond And Cond And\n");
    }

    @Override
    public void inACondCondNot(ACondCondNot node) {
        System.out.printf("Cond Cond Not\n");
    }

    @Override
    public void inACondNotCondNot(ACondNotCondNot node) {
        System.out.printf("Cond Not Cond Not\n");
    }

    @Override
    public void inAExprCondPar(AExprCondPar node) {
        System.out.printf("Expr Cond Par\n");
    }

    @Override
    public void inACondCondPar(ACondCondPar node) {
        System.out.printf("Cond Cond Par\n");
    }

    @Override
    public void inACondRelatCondRelat(ACondRelatCondRelat node) {
        System.out.printf("Cond Relat Cond Relat \n");
    }

    @Override
    public void inAEqualRelatOper(AEqualRelatOper node) {
        System.out.printf("Cond Relat Oper\n");
    }

    @Override
    public void inANEqualRelatOper(ANEqualRelatOper node) {
        System.out.printf("NEqual Relat Oper\n");
    }

    @Override
    public void inALessRelatOper(ALessRelatOper node){
        System.out.println("Less operator in condition");
    }

    @Override
    public void inALessEqualRelatOper(ALessEqualRelatOper node){
        System.out.println("Less_equal operator in condition");
    }

    @Override
    public void inAGreaterRelatOper(AGreaterRelatOper node){
        System.out.println("Greater operator in condition");
    }

    @Override
    public void inAGreaterEqualRelatOper(AGreaterEqualRelatOper node){
        System.out.println("Greater_equal operator in condition");
    }

    @Override
    public void inAFunctionDefinition(AFunctionDefinition node){
        System.out.println("Fuction Definition");
    }

    @Override
    public void inAHeaderHeader(AHeaderHeader node){
        System.out.println("Fuction header");
    }

    @Override
    public void inAFparFunctionParameters(AFparFunctionParameters node){
        System.out.println("Fuction parameter");
    }

    @Override
    public void inASemiFparSemiFunctionParameters(ASemiFparSemiFunctionParameters node){
        System.out.println("Semi fuction parameter");
    }

    @Override
    public void inAFparDefFparametersDefinition(AFparDefFparametersDefinition node){
        System.out.println("Fuction parameters definition");
    }

    @Override
    public void inACommaIdCommaIdentifier(ACommaIdCommaIdentifier node){
        System.out.println("Comma identifier");
    }

    @Override
    public void inADataRetType(ADataRetType node) {
        System.out.println("Return type data");
    }

    @Override
    public void inANothRetType(ANothRetType node){
        System.out.println("Return type nothing");
    }

    @Override
    public void inAIntTypeDataType(AIntTypeDataType node){
        System.out.println("Int datatype");
    }

    @Override
    public void inACharTypeDataType(ACharTypeDataType node){
        System.out.println("Char datatype");
    }

    @Override
    public void inATypeType(ATypeType node){
        System.out.println("Type definition");
    }

    @Override
    public void inATypeFparType(ATypeFparType node){
        System.out.println("Fpar Type");
    }

    @Override
    public void inAArrayTypeConstIntBrackets(AArrayTypeConstIntBrackets node){
        System.out.println("Array type const int brackets");
    }

    @Override
    public void inAEmptyEmptyBrackets(AEmptyEmptyBrackets node){
        System.out.println("Empty Brackets");
    }

    @Override
    public void inAFuncDefLocalDefinition(AFuncDefLocalDefinition node){
        System.out.println("Fuction definition local");
    }

    @Override
    public void inAFuncDeclLocalDefinition(AFuncDeclLocalDefinition node){
        System.out.println("Fuction declaration local");
    }

    @Override
    public void inAVarDefLocalDefinition(AVarDefLocalDefinition node){
        System.out.println("Variable definition local");
    }

    @Override
    public void inAVarVariableDefinition(AVarVariableDefinition node){
        System.out.println("Variable definiton");
    }

    @Override
    public void inAFuncDeclFunctionDeclaration(AFuncDeclFunctionDeclaration node){
        System.out.println("Fuction declaration");
    }

    @Override
    public void inAFuncCallFuncCall(AFuncCallFuncCall node){
        System.out.println("Fuction call");
    }

    @Override
    public void inAExprCommaExprFuncCallExpr(AExprCommaExprFuncCallExpr node){
        System.out.println("Fuction call expration");
    }

    @Override
    public void inACommaExprCommaExpr(ACommaExprCommaExpr node){
        System.out.println("A comma Expr ");
    }

    @Override
    public void inAIdLValue(AIdLValue node){
        System.out.println("L_value id");
    }

    @Override
    public void inAStringLiteralLValue(AStringLiteralLValue node){
        System.out.println("L_value string");
    }

    @Override
    public void inALValueLValue(ALValueLValue node){
        System.out.println("L_value array");
    }


}
