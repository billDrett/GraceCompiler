package compiler;

import com.sun.org.apache.regexp.internal.RE;

import java.util.*;

class FunctionOffset
{
    private int paramOffset;
    private int localOffset;

    public FunctionOffset()
    {
        paramOffset = 16;
        localOffset = 0;
    }

    public int getParamOffset()
    {
        return paramOffset;
    }
    public int getLocalOffset()
    {
        return localOffset;
    }
    public void setParamOffset(int param_offset)
    {
        paramOffset = param_offset;
    }
    public void setLocalOffset(int local_offset)
    {
        localOffset = local_offset;
    }

}

public class SymbolTable {
    private Deque<HashMap<String, Record>> scopes;
    private Deque<FunctionOffset> offsets;

    public SymbolTable()
    {
        scopes = new ArrayDeque<>();
        offsets = new ArrayDeque<>();
        this.enter(); //starting scope
    }

    //create a new scope
    public void enter()
    {
        scopes.addLast(new HashMap<String, Record>());
        offsets.addLast(new FunctionOffset());
    }

    public int getLocalOffset()
    {
        return offsets.getLast().getLocalOffset();
    }
    //insert an Record in the current scope
    public boolean insert(Record newRec)
    {
        HashMap<String, Record> currentScope = scopes.getLast();
        if(currentScope.containsKey(newRec.getName()))
        {
            return false;
        }

        if(!(newRec instanceof RecordFunction))
        {
            setOffsetAR(newRec);
        }

        newRec.setDepth(scopes.size());
        currentScope.put(newRec.getName(), newRec);

        return true;
    }

    public void setOffsetAR(Record newRec)
    {
        int arraySerialize;
        if(newRec instanceof RecordParam)
        {
            newRec.setOffset(offsets.getLast().getParamOffset());
            offsets.getLast().setParamOffset(offsets.getLast().getParamOffset()+4);         //paramOffset +=4;
        }
        else if(newRec instanceof RecordParamArray)
        {
            RecordArray recArray = (RecordArray) newRec;
            arraySerialize = recArray.getSerializedSize();
            newRec.setOffset(offsets.getLast().getParamOffset());
            offsets.getLast().setParamOffset(offsets.getLast().getParamOffset()+4);    //because its a reference of the array that was given as an argument, so dont calculate
        }
        else if (newRec instanceof RecordArray)
        {
            RecordArray recArray = (RecordArray) newRec;
            arraySerialize = recArray.getSerializedSize();

            if(newRec.getType().equals("int"))
            {
                offsets.getLast().setLocalOffset(offsets.getLast().getLocalOffset()-4*arraySerialize);   //   localOffset -=4*arraySerialize;
            }
            else
            {
                if(arraySerialize%4 ==0)
                {
                    offsets.getLast().setLocalOffset(offsets.getLast().getLocalOffset()-arraySerialize);   //   localOffset -=arraySerialize;
                }
                else
                {
                    offsets.getLast().setLocalOffset(offsets.getLast().getLocalOffset()-(arraySerialize/4)*4-4);   //   localOffset -=(arraySerialize/4)*4-4;
                }
            }

            newRec.setOffset(offsets.getLast().getLocalOffset());
        }
        else //Record
        {
            offsets.getLast().setLocalOffset(offsets.getLast().getLocalOffset()-4);   //   localOffset -=4;
            newRec.setOffset(offsets.getLast().getLocalOffset());
        }
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
        offsets.pollLast();
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

    public HashMap<String, Record> getCurrentScope()
    {
        return scopes.getLast();
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
                System.out.println("Record "+rec.getName()+" "+rec.getType()+" "+rec.getDepth()+" "+rec.getOffset());
 /*               if(rec instanceof RecordArray) // || rec instanceof RecordParamArray)
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
*/
            }

            depth--;

        }
    }

}
