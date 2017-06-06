package compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Condition {
    private ArrayList<Integer> trueList;
    private ArrayList<Integer> falseList;

    public Condition()
    {
        trueList = new ArrayList<>();
        falseList = new ArrayList<>();
    }

    public ArrayList<Integer> getTrueList()
    {
        return trueList;
    }

    public ArrayList<Integer> getFalseList()
    {
        return falseList;
    }

    public void addLabelTrue(int label)
    {
        trueList.add(label);
    }

    public void addLabelFalse(int label)
    {
        falseList.add(label);
    }

    public void mergeLists(ArrayList<Integer> newList, boolean list)
    {
        if(list) //trueList
        {
            trueList.addAll(newList);
        }
        else
        {
            falseList.addAll(newList);
        }
    }

    public void swapLists()
    {
        ArrayList<Integer> tmpList = trueList;
        trueList = falseList;
        falseList = tmpList;
    }

}
