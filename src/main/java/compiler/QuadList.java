package compiler;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by bill on 17/05/17.
 */
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
}


public class QuadList
{
    private ArrayList<Quad> qList;
    private LinkedList<TmpVar> tmpVars;

    public QuadList()
    {
        qList = new ArrayList<>();
        tmpVars = new LinkedList<>();
    }

    public int NextQuad()
    {
        return qList.size();
    }

    public void GenQuad(String oper, String opt1,String opt2, String opt3)
    {
        Quad quad = new Quad(qList.size(), oper, opt1, opt2, opt3);
        qList.add(quad);

        System.out.println(quad.getLabel()+": "+quad.getOperator()+",\t" +quad.getOpt1()+",\t"+ quad.getOpt2()+",\t" + quad.getOpt3());
    }

    public String NewTemp(String type)
    {
        String id = String.format("$%d",tmpVars.size()+1);
        TmpVar tmpVar = new TmpVar(type, id);
        tmpVars.addLast(tmpVar);
        return id;
    }

    //public void BackPatch(LinkedList<Integer> labels, )

}
