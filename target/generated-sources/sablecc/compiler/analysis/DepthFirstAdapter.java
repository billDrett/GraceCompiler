/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.analysis;

import java.util.*;
import compiler.node.*;

public class DepthFirstAdapter extends AnalysisAdapter
{
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
        node.getPExpr().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }

    public void inATermExpr(ATermExpr node)
    {
        defaultIn(node);
    }

    public void outATermExpr(ATermExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATermExpr(ATermExpr node)
    {
        inATermExpr(node);
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        outATermExpr(node);
    }

    public void inAAddExpr(AAddExpr node)
    {
        defaultIn(node);
    }

    public void outAAddExpr(AAddExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAddExpr(AAddExpr node)
    {
        inAAddExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getPlus() != null)
        {
            node.getPlus().apply(this);
        }
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        outAAddExpr(node);
    }

    public void inASubExpr(ASubExpr node)
    {
        defaultIn(node);
    }

    public void outASubExpr(ASubExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASubExpr(ASubExpr node)
    {
        inASubExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getMinus() != null)
        {
            node.getMinus().apply(this);
        }
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        outASubExpr(node);
    }

    public void inAFactorTerm(AFactorTerm node)
    {
        defaultIn(node);
    }

    public void outAFactorTerm(AFactorTerm node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFactorTerm(AFactorTerm node)
    {
        inAFactorTerm(node);
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        outAFactorTerm(node);
    }

    public void inAMultTerm(AMultTerm node)
    {
        defaultIn(node);
    }

    public void outAMultTerm(AMultTerm node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMultTerm(AMultTerm node)
    {
        inAMultTerm(node);
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        if(node.getMult() != null)
        {
            node.getMult().apply(this);
        }
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        outAMultTerm(node);
    }

    public void inADivisionTerm(ADivisionTerm node)
    {
        defaultIn(node);
    }

    public void outADivisionTerm(ADivisionTerm node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADivisionTerm(ADivisionTerm node)
    {
        inADivisionTerm(node);
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        if(node.getDivision() != null)
        {
            node.getDivision().apply(this);
        }
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        outADivisionTerm(node);
    }

    public void inADivTerm(ADivTerm node)
    {
        defaultIn(node);
    }

    public void outADivTerm(ADivTerm node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADivTerm(ADivTerm node)
    {
        inADivTerm(node);
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        if(node.getDiv() != null)
        {
            node.getDiv().apply(this);
        }
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        outADivTerm(node);
    }

    public void inAModTerm(AModTerm node)
    {
        defaultIn(node);
    }

    public void outAModTerm(AModTerm node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAModTerm(AModTerm node)
    {
        inAModTerm(node);
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        if(node.getMod() != null)
        {
            node.getMod().apply(this);
        }
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        outAModTerm(node);
    }

    public void inANumberFactor(ANumberFactor node)
    {
        defaultIn(node);
    }

    public void outANumberFactor(ANumberFactor node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANumberFactor(ANumberFactor node)
    {
        inANumberFactor(node);
        if(node.getNumber() != null)
        {
            node.getNumber().apply(this);
        }
        outANumberFactor(node);
    }

    public void inAParensFactor(AParensFactor node)
    {
        defaultIn(node);
    }

    public void outAParensFactor(AParensFactor node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAParensFactor(AParensFactor node)
    {
        inAParensFactor(node);
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getRPar() != null)
        {
            node.getRPar().apply(this);
        }
        outAParensFactor(node);
    }

    public void inAProgProgram(AProgProgram node)
    {
        defaultIn(node);
    }

    public void outAProgProgram(AProgProgram node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAProgProgram(AProgProgram node)
    {
        inAProgProgram(node);
        if(node.getFuncDef() != null)
        {
            node.getFuncDef().apply(this);
        }
        outAProgProgram(node);
    }

    public void inAFuncDefFuncDef(AFuncDefFuncDef node)
    {
        defaultIn(node);
    }

    public void outAFuncDefFuncDef(AFuncDefFuncDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncDefFuncDef(AFuncDefFuncDef node)
    {
        inAFuncDefFuncDef(node);
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
        if(node.getBlock() != null)
        {
            node.getBlock().apply(this);
        }
        outAFuncDefFuncDef(node);
    }

    public void inAHeaderHeader(AHeaderHeader node)
    {
        defaultIn(node);
    }

    public void outAHeaderHeader(AHeaderHeader node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAHeaderHeader(AHeaderHeader node)
    {
        inAHeaderHeader(node);
        if(node.getFunction() != null)
        {
            node.getFunction().apply(this);
        }
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        if(node.getFparDefinition() != null)
        {
            node.getFparDefinition().apply(this);
        }
        if(node.getRPar() != null)
        {
            node.getRPar().apply(this);
        }
        if(node.getColon() != null)
        {
            node.getColon().apply(this);
        }
        if(node.getRetType() != null)
        {
            node.getRetType().apply(this);
        }
        outAHeaderHeader(node);
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
        if(node.getFparDef() != null)
        {
            node.getFparDef().apply(this);
        }
        {
            List<PSemiFparDef> copy = new ArrayList<PSemiFparDef>(node.getSemiFparDef());
            for(PSemiFparDef e : copy)
            {
                e.apply(this);
            }
        }
        outAFparDefinition(node);
    }

    public void inASemiFparDef(ASemiFparDef node)
    {
        defaultIn(node);
    }

    public void outASemiFparDef(ASemiFparDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASemiFparDef(ASemiFparDef node)
    {
        inASemiFparDef(node);
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        if(node.getFparDef() != null)
        {
            node.getFparDef().apply(this);
        }
        outASemiFparDef(node);
    }

    public void inAFparDefFparDef(AFparDefFparDef node)
    {
        defaultIn(node);
    }

    public void outAFparDefFparDef(AFparDefFparDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFparDefFparDef(AFparDefFparDef node)
    {
        inAFparDefFparDef(node);
        if(node.getReference() != null)
        {
            node.getReference().apply(this);
        }
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        {
            List<PCommaIdentifier> copy = new ArrayList<PCommaIdentifier>(node.getCommaIdentifier());
            for(PCommaIdentifier e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getColon() != null)
        {
            node.getColon().apply(this);
        }
        if(node.getFparType() != null)
        {
            node.getFparType().apply(this);
        }
        outAFparDefFparDef(node);
    }

    public void inACommaIdentifier(ACommaIdentifier node)
    {
        defaultIn(node);
    }

    public void outACommaIdentifier(ACommaIdentifier node)
    {
        defaultOut(node);
    }

    @Override
    public void caseACommaIdentifier(ACommaIdentifier node)
    {
        inACommaIdentifier(node);
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        outACommaIdentifier(node);
    }

    public void inAIntTypeDataType(AIntTypeDataType node)
    {
        defaultIn(node);
    }

    public void outAIntTypeDataType(AIntTypeDataType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntTypeDataType(AIntTypeDataType node)
    {
        inAIntTypeDataType(node);
        if(node.getInt() != null)
        {
            node.getInt().apply(this);
        }
        outAIntTypeDataType(node);
    }

    public void inACharTypeDataType(ACharTypeDataType node)
    {
        defaultIn(node);
    }

    public void outACharTypeDataType(ACharTypeDataType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseACharTypeDataType(ACharTypeDataType node)
    {
        inACharTypeDataType(node);
        if(node.getChar() != null)
        {
            node.getChar().apply(this);
        }
        outACharTypeDataType(node);
    }

    public void inATypeType(ATypeType node)
    {
        defaultIn(node);
    }

    public void outATypeType(ATypeType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATypeType(ATypeType node)
    {
        inATypeType(node);
        if(node.getDataType() != null)
        {
            node.getDataType().apply(this);
        }
        {
            List<PTypeStruct> copy = new ArrayList<PTypeStruct>(node.getTypeStruct());
            for(PTypeStruct e : copy)
            {
                e.apply(this);
            }
        }
        outATypeType(node);
    }

    public void inATypeStruct(ATypeStruct node)
    {
        defaultIn(node);
    }

    public void outATypeStruct(ATypeStruct node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATypeStruct(ATypeStruct node)
    {
        inATypeStruct(node);
        if(node.getLBkt() != null)
        {
            node.getLBkt().apply(this);
        }
        if(node.getIntConst() != null)
        {
            node.getIntConst().apply(this);
        }
        if(node.getRBkt() != null)
        {
            node.getRBkt().apply(this);
        }
        outATypeStruct(node);
    }

    public void inADataRetType(ADataRetType node)
    {
        defaultIn(node);
    }

    public void outADataRetType(ADataRetType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADataRetType(ADataRetType node)
    {
        inADataRetType(node);
        if(node.getDataType() != null)
        {
            node.getDataType().apply(this);
        }
        outADataRetType(node);
    }

    public void inANothRetType(ANothRetType node)
    {
        defaultIn(node);
    }

    public void outANothRetType(ANothRetType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANothRetType(ANothRetType node)
    {
        inANothRetType(node);
        if(node.getNothing() != null)
        {
            node.getNothing().apply(this);
        }
        outANothRetType(node);
    }

    public void inAFparFparType(AFparFparType node)
    {
        defaultIn(node);
    }

    public void outAFparFparType(AFparFparType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFparFparType(AFparFparType node)
    {
        inAFparFparType(node);
        if(node.getDataType() != null)
        {
            node.getDataType().apply(this);
        }
        if(node.getLrbkt() != null)
        {
            node.getLrbkt().apply(this);
        }
        {
            List<PTypeStruct> copy = new ArrayList<PTypeStruct>(node.getTypeStruct());
            for(PTypeStruct e : copy)
            {
                e.apply(this);
            }
        }
        outAFparFparType(node);
    }

    public void inALrbkt(ALrbkt node)
    {
        defaultIn(node);
    }

    public void outALrbkt(ALrbkt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALrbkt(ALrbkt node)
    {
        inALrbkt(node);
        if(node.getLBkt() != null)
        {
            node.getLBkt().apply(this);
        }
        if(node.getRBkt() != null)
        {
            node.getRBkt().apply(this);
        }
        outALrbkt(node);
    }

    public void inAFuncDefLocalDef(AFuncDefLocalDef node)
    {
        defaultIn(node);
    }

    public void outAFuncDefLocalDef(AFuncDefLocalDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncDefLocalDef(AFuncDefLocalDef node)
    {
        inAFuncDefLocalDef(node);
        if(node.getFuncDef() != null)
        {
            node.getFuncDef().apply(this);
        }
        outAFuncDefLocalDef(node);
    }

    public void inAFuncDeclLocalDef(AFuncDeclLocalDef node)
    {
        defaultIn(node);
    }

    public void outAFuncDeclLocalDef(AFuncDeclLocalDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncDeclLocalDef(AFuncDeclLocalDef node)
    {
        inAFuncDeclLocalDef(node);
        if(node.getFuncDecl() != null)
        {
            node.getFuncDecl().apply(this);
        }
        outAFuncDeclLocalDef(node);
    }

    public void inAVarDefLocalDef(AVarDefLocalDef node)
    {
        defaultIn(node);
    }

    public void outAVarDefLocalDef(AVarDefLocalDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAVarDefLocalDef(AVarDefLocalDef node)
    {
        inAVarDefLocalDef(node);
        if(node.getVarDef() != null)
        {
            node.getVarDef().apply(this);
        }
        outAVarDefLocalDef(node);
    }

    public void inAVarVarDef(AVarVarDef node)
    {
        defaultIn(node);
    }

    public void outAVarVarDef(AVarVarDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAVarVarDef(AVarVarDef node)
    {
        inAVarVarDef(node);
        if(node.getVariable() != null)
        {
            node.getVariable().apply(this);
        }
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        {
            List<PCommaIdentifier> copy = new ArrayList<PCommaIdentifier>(node.getCommaIdentifier());
            for(PCommaIdentifier e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getColon() != null)
        {
            node.getColon().apply(this);
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outAVarVarDef(node);
    }

    public void inAFuncDecl(AFuncDecl node)
    {
        defaultIn(node);
    }

    public void outAFuncDecl(AFuncDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFuncDecl(AFuncDecl node)
    {
        inAFuncDecl(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outAFuncDecl(node);
    }
}