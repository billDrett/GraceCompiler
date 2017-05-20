package compiler;

import com.sun.org.apache.regexp.internal.RE;

import java.util.*;

/**
 * Created by bill on 04/05/17.
 */
public class SymbolTable {
    private Deque<HashMap<String, Record>> scopes;

    public SymbolTable()
    {
        scopes = new ArrayDeque<>();
        this.enter(); //starting scope
    }

    public void enter()
    {
        scopes.addLast(new HashMap<String, Record>());
    }

    public boolean insert(Record newRec)
    {
        HashMap<String, Record> currentScope = scopes.getLast();
        if(currentScope.containsKey(newRec.getName()))
        {
            return false;
        }

        newRec.setDepth(scopes.size());
        currentScope.put(newRec.getName(), newRec);

        return true;
    }

    public Record lookup(String varName)
    {
        Record rec = null;
        int depth = scopes.size()-1;
        HashMap<String, Record> currentScope;

        for(Iterator< HashMap<String, Record>> iter = scopes.descendingIterator(); iter.hasNext();)
        {
            currentScope = iter.next();
            rec = currentScope.get(varName);

            if(rec != null)
            {
                break;
            }
            depth--;

        }

        System.out.println("the record depth "+depth+ " found "+ rec);
        return rec;
    }

    public void exit()
    {
        scopes.pollLast();
    }

    public void printALl()
    {
        Set set;
        HashMap<String, Record> currentScope;
        int depth = scopes.size()-1;

        for(Iterator< HashMap<String, Record>> iter1 = scopes.descendingIterator(); iter1.hasNext();)
        {
            currentScope = (HashMap)iter1.next();
            set = currentScope.entrySet();

            for(Record rec : currentScope.values())
            {
                System.out.println("Record "+rec.getName()+" "+rec.getType()+" "+rec.getDeftype()+" "+rec.getDepth());
                if(rec.getDeftype()=="Array" || rec.getDeftype() == "functParamArray")
                {
                    RecordArray rec2 = (RecordArray) rec;
                    System.out.print("Dimensions ");
                    LinkedList<Integer> dimList =rec2.getDimensions();

                    for(Integer dim: dimList)
                    {
                        System.out.print(dim+".");
                    }
                    System.out.println();
                }
                else if(rec.getDeftype()=="Function")
                {
                    RecordFunction recFunct = (RecordFunction) rec;
                    System.out.println("Function noParam"+recFunct.getNoParameters());

                    LinkedList<Record> parameters = ((RecordFunction) rec).getFparameters();
                    for(Record rec3:parameters)
                    {
                        System.out.println("Function RecordParam "+rec3.getName()+" "+rec3.getType()+" "+rec3.getDeftype()+" "+rec3.getDepth());

                    }

                }

            }

            depth--;

        }
    }

    public int getCurrentDepth()
    {
        return scopes.size();
    }

}
