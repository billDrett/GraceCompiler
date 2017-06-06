package compiler;

import java.util.Iterator;


class RecordLValue
{
    protected Record record;
    protected Iterator<Integer> currentDimension;
    protected String addressIndex; //keeps the tmpVar identifier of the address index, its used for the array lvalue

    public RecordLValue(Record rec)
    {
        record = rec;
        addressIndex = null;
        if(record instanceof RecordArray)
        {
            RecordArray recArray = (RecordArray) record;
            currentDimension = recArray.getDimensions().iterator();
            currentDimension.next();
        }
        else
        {
            currentDimension = null;
        }
    }

    public RecordLValue() //string literal
    {
        record = null;
        addressIndex = null;
        currentDimension = null;
    }

    public Record getRecord()
    {
        if(record instanceof RecordArray)
        {
            return ((RecordArray) record);
        }
        return record;
    }

    public int getDimension()
    {
        return currentDimension.next();
    }

    public boolean emptyDimension()
    {
        if(record == null) //string literal
        {
            return addressIndex != null;
        }

        return !currentDimension.hasNext();
    }

    public String getAddressIndex()
    {
        return addressIndex;
    }

    public void setAddressIndex(String newAddressIndex)
    {
        addressIndex = newAddressIndex;
    }

}