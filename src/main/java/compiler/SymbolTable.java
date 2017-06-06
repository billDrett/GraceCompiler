package compiler;

import com.sun.org.apache.regexp.internal.RE;

import java.util.*;

public class SymbolTable {
    private Deque<HashMap<String, Record>> scopes;

    public SymbolTable()
    {
        scopes = new ArrayDeque<>();
        this.enter(); //starting scope
    }

    //create a new scope
    public void enter()
    {
        scopes.addLast(new HashMap<String, Record>());
    }

    //insert an Record in the current scope
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

        return rec;
    }

    public void exit()
    {
        scopes.pollLast();
    }

    public int getCurrentDepth()
    {
        return scopes.size();
    }

    public void checkDefinedFunct(Error error)
    {
        Record record;
        RecordFunction recFunct;

        HashMap<String, Record> currentScope = scopes.getLast();

        for(Map.Entry<String, Record> entry : currentScope.entrySet())
        {
            record = entry.getValue();

            if(record instanceof RecordFunction)
            {
                recFunct = (RecordFunction) record;
                if(!recFunct.getDefined())
                {
                    error.functNotDefined(recFunct);
                }
            }
        }
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
                System.out.println("Record "+rec.getName()+" "+rec.getType()+" "+rec.getDepth());
                if(rec instanceof RecordArray) // || rec instanceof RecordParamArray)
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
                else if(rec instanceof RecordFunction)
                {
                    RecordFunction recFunct = (RecordFunction) rec;
                    System.out.println("Function noParam"+recFunct.getNoParameters());

                    LinkedList<Record> parameters = ((RecordFunction) rec).getFparameters();
                    for(Record rec3:parameters)
                    {
                        System.out.println("Function RecordParam "+rec3.getName()+" "+rec3.getType()+" "+rec3.getDepth());

                    }

                }

            }

            depth--;

        }
    }

}
