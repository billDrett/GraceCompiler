package compiler;

import java.util.ArrayList;
import java.util.LinkedList;
/*
class TmpVar
{
    private String type;
    private String id;

    public TmpVar(String typ, String tmpId)
    {
        type = typ;
        id = tmpId;
    }

    String getId()
    {
        return id;
    }

    String getType()
    {
        return type;
    }
}*/


public class QuadList
{
    private SymbolTable symbolTable;
    private ArrayList<Quad> qList;
    private int tmpVarCounter = 1;

    public QuadList(SymbolTable symTable)
    {
        qList = new ArrayList<>();
        symbolTable = symTable;
    }

    public int NextQuad()
    {
        return qList.size();
    }

    public void GenQuad(String oper, String opt1,String opt2, String opt3)
    {
        Quad quad = new Quad(qList.size(), oper, opt1, opt2, opt3);
        qList.add(quad);
    }

    public String NewTemp(String type)
    {
        String id = String.format("$%d",tmpVarCounter);
        Record tmpVar = new Record(id, type, 0);
        symbolTable.insert(tmpVar);

        tmpVarCounter++;

        return id;
    }

    public Quad getQuad(int label)
    {
        return qList.get(label);
    }

    public ArrayList<Quad> getQuadList()
    {
        return qList;
    }
    //public void BackPatch(LinkedList<Integer> labels, )

    public void printAll()
    {
        for (Quad quad: qList)
        {
            System.out.println(quad.getLabel()+": "+quad.getOperator()+",\t" +quad.getOpt1()+",\t"+ quad.getOpt2()+",\t" + quad.getOpt3());
        }
    }

    public void clearIntermidateCode()
    {
        qList.clear();
    }

}
