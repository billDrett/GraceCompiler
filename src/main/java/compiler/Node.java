package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Node
{
    private ArrayList<Quad> blockQuads;
    private ArrayList<Node> edgesFrom;
    private ArrayList<Node> edgesTo; //it has at most two Nodes
    private HashMap<Integer, Integer> nextNodes;
    private int startLabel;

    private HashMap<String, String> inBlock; //variable name as a key, and value the constant
    private HashMap<String, String> outBlock; //variable name as a key, and value the constant

    private HashMap<String, Boolean> inBlockLiv; //variable name as a key, and value for liveness
    private HashMap<String, Boolean> outBlockLiv; //variable name as a key, and value for liveness

    private SymbolTable symTable;

    private boolean firstPass;

    public Node(int startLbl, SymbolTable symbolTable)
    {
        blockQuads = new ArrayList<>();
        edgesFrom = new ArrayList<>();
        edgesTo = new ArrayList<>();
        nextNodes = new HashMap<>(); //forbit multiple edge at the same node, case while(1)do if(...) then;

        startLabel = startLbl;

        symTable = symbolTable;
    }

    public int getStartLabel()
    {
        return startLabel;
    }

    public int getTotalQuads()
    {
        return blockQuads.size();
    }

    public int getTotalEdgesFrom()
    {
        return edgesFrom.size();
    }

    public int getTotalEdgesTo()
    {
        return edgesTo.size();
    }

    public ArrayList<Node> getEdgesFrom()
    {
        return edgesFrom;
    }

    public ArrayList<Node> getEdgesTo()
    {
        return edgesTo;
    }

    public HashMap<String, String> getOutBlock()
    {
        return outBlock;
    }

    public HashMap<String, Boolean> getInBlockLiv()
    {
        return inBlockLiv;
    }

    public void setInBlock(HashMap<String,String> vars)
    {
        inBlock = new HashMap<>(vars);
    }

    public void setOutBlock(HashMap<String,String> vars)
    {
        outBlock = new HashMap<>(vars);
    }

    public void setInBlockLiv(HashMap<String, Boolean> vars)
    {
        inBlockLiv = new HashMap<>(vars);
    }

    public void setOutBlockLiv(HashMap<String, Boolean> vars)
    {
        outBlockLiv = new HashMap<>(vars);
    }

    public void addQuad(Quad quad)
    {
        blockQuads.add(quad);
    }

    public void addEdgeFrom(Node edge)
    {
        edgesFrom.add(edge);
    }

    public void addEdgeTo(Node edge)
    {
        edgesTo.add(edge);
    }

    //add the label of the edge To node
    public void addNextNode(int nodeLabel)
    {
        nextNodes.put(nodeLabel, nodeLabel); //we cant have two edge at the same node
    }

    //remove one edgeTo label
    public int popNextNode()
    {
        int node;

        if(nextNodes.size() == 0) return -1;

        node=(int)nextNodes.values().toArray()[0];
        nextNodes.remove(node);

        return node;
    }

    //deletes and edge From Node with the label nodeLabel
    public void deleteEdgeFrom(int nodeLabel)
    {
        Node edgeNode;
        for(Iterator<Node> iterEdge = edgesFrom.iterator(); iterEdge.hasNext();)
        {
            edgeNode = iterEdge.next();
            if(edgeNode.startLabel == nodeLabel) //edge node Found
            {
                iterEdge.remove();
                break;
            }
        }
    }

    //deletes and edge To Node, it has at most two edge To nodes
    //if equal is set it deletes the node with the label nodeLabel
    //if equal is false it deletes the node with different label than nodeLabel, its uses for the next node at the flow because we dont know it at the blocks
    public void deleteEdgeTo(int nodeLabel, boolean equal)
    {
        Node edgeNode;
        for(Iterator<Node> iterEdge = edgesTo.iterator(); iterEdge.hasNext();)
        {
            edgeNode = iterEdge.next();

            if(equal) //check for equal
            {
                if(edgeNode.startLabel == nodeLabel) //found
                {
                    iterEdge.remove();
                    edgeNode.deleteEdgeFrom(this.startLabel); //remove the edge from the current node at the neighbor node
                    break;
                }
            }
            else //check for not equal
            {
                if(edgeNode.startLabel != nodeLabel) //found
                {
                    iterEdge.remove();
                    edgeNode.deleteEdgeFrom(this.startLabel); //remove the edge from the current node at the neighbor node
                    break;
                }
            }
        }
    }

    public void deleteNode()
    {
        Node edgeNode;

        //delete the edges to the node
        for(Iterator<Node> iterEdge = edgesTo.iterator(); iterEdge.hasNext();)
        {
            edgeNode = iterEdge.next();
            iterEdge.remove();
            edgeNode.deleteEdgeFrom(this.startLabel);
        }
        for(Quad quad: blockQuads)
        {
            quad.setNop(); //remove it by seting it to nop
        }
    }

    public boolean optimizeBlock(int maxLoops)
    {
        HashMap<String, String> variables = new HashMap<>();
        boolean change =false;
        boolean changeMade;
        int i = 0;

        //loops while optimizations are made for maxLoops number of loops
        do
        {
            changeMade = false;
            for(Quad currentQuad : blockQuads) //for all quads
            {
                //copyPropagation(currentQuad, variables)
                if(constantPropagation(currentQuad, variables)) //apply copy/constant Propagation at block level
                {
                    changeMade = true;
                    change = true;
                }

                if(constantFolding(currentQuad)) //apply constant folding
                {
                    changeMade = true;
                    change = true;
                }

                if(algebraSimplification(currentQuad)) //apply some simple algebra simplification
                {
                    changeMade = true;
                    change = true;
                }

            }

            i++;
        }while (changeMade && i < maxLoops);

        return change;
    }

    public boolean algebraSimplification(Quad currentQuad)
    {
        boolean changeMade = false;
        int number;
        if(isNumber(currentQuad.getOpt1())) //first option is number
        {
            number = Integer.parseInt(currentQuad.getOpt1());
            if(currentQuad.getOperator().equals("+") && number == 0) //$1 = x + 0
            {
                //make it an assignment, it might be deleted later with the dead code elimination optimization
                currentQuad.setOperator(":=");
                currentQuad.setOpt1(currentQuad.getOpt2());
                currentQuad.setOpt2("-");
                changeMade = true;
            }
            else if(currentQuad.getOperator().equals("*") && number == 0) //result is 0, $1 = x*0
            {
                assignQuad(0, currentQuad);
                changeMade = true;
            }
            else if(currentQuad.getOperator().equals("*") && number == 1)//$1 = x*1
            {
                //make it an assignment, it might be deleted later with the dead code elimination optimization
                currentQuad.setOperator(":=");
                currentQuad.setOpt1(currentQuad.getOpt2());
                currentQuad.setOpt2("-");
                changeMade = true;
            }
        }
        else if(isNumber(currentQuad.getOpt2())) //second option is contant, same operation as above
        {
            number = Integer.parseInt(currentQuad.getOpt2());
            if((currentQuad.getOperator().equals("+") || currentQuad.getOperator().equals("-")) && number == 0) //$1 = x + 0, or -
            {
                //make it an assignment, it might be deleted later with the dead code elimination optimization
                currentQuad.setOperator(":=");
                currentQuad.setOpt2("-");
                changeMade = true;
            }
            else if(currentQuad.getOperator().equals("*") && number == 0) //result is 0
            {
                assignQuad(0, currentQuad);
                changeMade = true;
            }
            else if(currentQuad.getOperator().equals("*") && number == 1)
            {
                //make it an assignment, it might be deleted later with the dead code elimination optimization
                currentQuad.setOperator(":=");
                currentQuad.setOpt2("-");
                changeMade = true;
            }
        }

        return changeMade;
    }

    public boolean constantFoldingCompare(Quad currentQuad, int number1, int number2)
    {
        if(currentQuad.getOperator().equals("="))
        {
            if(number1 == number2)
            {
                setJumpQuad(currentQuad);
            }
            else //delete command
            {
                setIgnoreComparison(currentQuad);
            }
        }
        else if(currentQuad.getOperator().equals("#"))
        {
            if(number1 != number2)
            {
                setJumpQuad(currentQuad);
            }
            else //delete command
            {
                setIgnoreComparison(currentQuad);
            }
        }
        else if(currentQuad.getOperator().equals("<"))
        {
            if(number1 < number2)
            {
                setJumpQuad(currentQuad);
            }
            else //delete command
            {
                setIgnoreComparison(currentQuad);
            }
        }
        else if(currentQuad.getOperator().equals("<="))
        {
            if(number1 <= number2)
            {
                setJumpQuad(currentQuad);
            }
            else //delete command
            {
                setIgnoreComparison(currentQuad);
            }
        }
        else if(currentQuad.getOperator().equals(">"))
        {
            if(number1 > number2)
            {
                setJumpQuad(currentQuad);
            }
            else //delete command
            {
                setIgnoreComparison(currentQuad);
            }
        }
        else if(currentQuad.getOperator().equals(">="))
        {
            if(number1 >= number2)
            {
                setJumpQuad(currentQuad);
            }
            else //delete command
            {
                setIgnoreComparison(currentQuad);
            }
        }
        return false;
    }

    public boolean constantFolding(Quad currentQuad)
    {
        int number1, number2, result;
        boolean changeMade = false;
        String value1, value2;

        //if both are char get the value of those without the quotes
        if(isChar(currentQuad.getOpt1()) && isChar(currentQuad.getOpt2()))
        {
            value1 = currentQuad.getOpt1().substring(1, currentQuad.getOpt1().length()-1);
            value2 = currentQuad.getOpt2().substring(1, currentQuad.getOpt2().length()-1);

            //get the value of the first char
            if(value1.length()==1)
            {
                number1 = (int)value1.charAt(0);
            }
            else
            {
                number1 = covertEscapedChar(value1);
            }

            //get the value of the second char
            if(value2.length()==1)
            {
                number2 = (int)value2.charAt(0);
            }
            else
            {
                number2 = covertEscapedChar(value2);
            }

            constantFoldingCompare(currentQuad, number1, number2);
        }
        else if(isNumber(currentQuad.getOpt1()) && isNumber(currentQuad.getOpt2())) //both are numbers
        {
            number1 = Integer.parseInt(currentQuad.getOpt1());
            number2 = Integer.parseInt(currentQuad.getOpt2());

            constantFoldingCompare(currentQuad, number1, number2);

            //check for arithmetic
            if(currentQuad.getOperator().equals("+"))
            {
                result = number1+number2;
                assignQuad(result, currentQuad);
                changeMade = true;
            }
            else if(currentQuad.getOperator().equals("-"))
            {
                result = number1+number2;
                assignQuad(result, currentQuad);
                changeMade = true;
            }
            else if(currentQuad.getOperator().equals("*"))
            {
                result = number1*number2;
                assignQuad(result, currentQuad);
                changeMade = true;
            }
            else if(currentQuad.getOperator().equals("/"))
            {
                result = number1/number2;
                assignQuad(result, currentQuad);
                changeMade = true;
            }
            else if(currentQuad.getOperator().equals("%"))// %
            {
                result = number1%number2;
                assignQuad(result, currentQuad);
                changeMade = true;
            }
        }

        return changeMade;
    }

    public boolean constantPropagation(Quad currentQuad, HashMap<String, String> variables)
    {
        String value;
        boolean changeMade = false;

        if(!(currentQuad.getOperator().equals("unit") || currentQuad.getOperator().equals("endu") || currentQuad.getOperator().equals("call")
                || currentQuad.getOperator().equals("jump") || currentQuad.getOperator().equals("ret")))
        {
            value = variables.get(currentQuad.getOpt1());
            if(value != null) //variable might be from other function scope
            {
                if(value.equals("*") || value.equals("#")) return false; //mporei na einai # ????
                currentQuad.setOpt1(value);
                changeMade = true;
            }

            if(currentQuad.getOperator().equals("par")) return changeMade;

            value =variables.get(currentQuad.getOpt2());
            if(value != null)
            {
                if(value.equals("*")|| value.equals("#")) return false;
                currentQuad.setOpt2(value);
                changeMade = true;
            }
        }

        if(currentQuad.getOperator().equals(":=")) //new constant
        {
            //prepei na einai stathera
            //prosorina dn exw contant propagate gia string literal
            if(isNumber(currentQuad.getOpt1()) || isChar(currentQuad.getOpt1()) /*|| currentQuad.getOpt1().charAt(0)=='\"'*/)
            {
                variables.put(currentQuad.getOpt3(), currentQuad.getOpt1());//both could be deref operators is it a problem?
            }
            else
            {
                variables.put(currentQuad.getOpt3(), "*");//both could be deref operators is it a problem?
            }

        }

        return changeMade;
    }

    public boolean copyPropagation(Quad currentQuad, HashMap<String, String> variables)
    {
        String value;
        boolean changeMade = false;

        if(currentQuad.getOperator().equals(":=")) //new constant
        {
            variables.put(currentQuad.getOpt3(), currentQuad.getOpt1());//both could be deref operators is it a problem?
        }

        if(!(currentQuad.getOperator().equals("unit") || currentQuad.getOperator().equals("endu") || currentQuad.getOperator().equals("call")
                || currentQuad.getOperator().equals("jump") || currentQuad.getOperator().equals("ret")))
        {
            value = variables.get(currentQuad.getOpt1());

            if(value != null) //variable might be from other function scope
            {
                if(value.equals("*")) return false; //mporei na einai # ????
                currentQuad.setOpt1(value);
                changeMade = true;
            }

            value =variables.get(currentQuad.getOpt2());
            if(value != null)
            {
                if(value.equals("*")) return false;
                currentQuad.setOpt2(value);
                changeMade = true;
            }
        }

        return changeMade;
    }

    public void setJumpQuad(Quad currentQuad)
    {
        currentQuad.setOperator("jump");
        currentQuad.setOpt1("-");
        currentQuad.setOpt2("-");
        int jumpLabel = Integer.parseInt(currentQuad.getOpt3()); //delete the edge to the node which hasnt the jumpLabel
        this.deleteEdgeTo(jumpLabel, false);
    }

    public void setIgnoreComparison(Quad currentQuad)
    {
        int jumpLabel = Integer.parseInt(currentQuad.getOpt3()); //delete the edge to the node which has the jumpLabel
        this.deleteEdgeTo(jumpLabel, true);

        currentQuad.setNop(); //remove the quad  by making it nop
    }

    public void initializeGlobalLiveness()
    {
        firstPass = true;
    }

    public void initializeGlobalOptimization()
    {
        firstPass = true;
    }

    public boolean copyPropagateValues()
    {
        boolean changeMade = false;

        outBlock = new HashMap<>(inBlock);
        for(Quad currentQuad : blockQuads)
        {
            if(constantPropagation(currentQuad, outBlock))
            {
                changeMade = true;
            }
        }

        return changeMade;
    }

    //return if it has ref true
    public boolean isReferenceRecord(String name)
    {
        Record record;
        RecordParam parameter;

        record = symTable.lookup(name);
        if(record instanceof RecordParam)
        {
            parameter = (RecordParam)record;
            return parameter.getReference();
        }

        return false;
    }

    public boolean eliminateDeadCode()
    {
        Quad currentQuad;
        inBlockLiv = new HashMap<>(outBlockLiv);
        boolean change = false;

        for(int i = blockQuads.size()-1; i >=0; i--)
        {
            currentQuad = blockQuads.get(i);
            if(currentQuad.getOperator().equals("call")) //we dont know if call changes our variables, so the safe way is to thing that everything was changed
            {
                for(Map.Entry<String, Boolean> entry : inBlockLiv.entrySet())
                {
                    entry.setValue(true);
                }
            }
            else if(!(currentQuad.getOperator().equals("unit") || currentQuad.getOperator().equals("endu")))
            {
                boolean varValue;
                String optionName;

                if(currentQuad.getOperator().equals(":=")) //assign quad
                {
                    Object tmpObject;
                    optionName = currentQuad.getOpt3();
                    if(!(currentQuad.getOpt3().equals("$$") || isReferenceRecord(optionName)))
                    {
                        tmpObject = inBlockLiv.get(optionName); //if the varible is from other scope it might not be at the hashtable
                        //also the function call wont make it true, so for safety we dont delete it

                        if(tmpObject != null && (boolean)tmpObject ==false && optionName.charAt(0)!='[')
                        {
                            change = true;
                            currentQuad.setNop();
                            continue;//dont update the variable used in the function
                        }
                        inBlockLiv.put(optionName, false);
                    }
                }

                optionName = currentQuad.getOpt1();

                //its a variable
                if(isVariable(optionName))
                {
                    if(optionName.charAt(0)=='[')
                    {
                        optionName = optionName.substring(1, optionName.length()-1);
                    }
                    inBlockLiv.put(optionName, true);
                }


                optionName = currentQuad.getOpt2();

                //par has only the first as an argument, we dont want to take the mode and think it as an argument
                if(currentQuad.getOperator().equals("par")) continue;

                if(isVariable(optionName))
                {
                    if(optionName.charAt(0)=='[')
                    {
                        optionName = optionName.substring(1, optionName.length()-1);
                    }

                    inBlockLiv.put(optionName, true);
                }
            }

        }

        return change;
    }

    public boolean calculateLiveness()
    {
        boolean currentVarValue, inVarValue;
        boolean change = false;
        HashMap<String, Boolean> inputBlock;
        Quad currentQuad;

        //start from the output
        for(String key : outBlockLiv.keySet())
        {
            currentVarValue = outBlockLiv.get(key);
            for(Node prevNodes: edgesTo)
            {
                if(currentVarValue == true) break; //cant become something else

                inputBlock = prevNodes.getInBlockLiv();
                inVarValue = inputBlock.get(key);

                if(inVarValue == true)
                {
                    currentVarValue = true;
                    change = true;
                }
            }
            outBlockLiv.put(key, currentVarValue);
        }


        if(firstPass==false && change==false) //continue only if there was a change at the input and its not the first pass
        {
            return change; //false
        }

        inBlockLiv = new HashMap<>(outBlockLiv); //mallon tha to kanw argo...

        //do it for all the quads in reverse order
        for(int i = blockQuads.size()-1; i >=0; i--)
        {
            currentQuad = blockQuads.get(i);

            if(currentQuad.getOperator().equals("call")) //we dont know if call changes our variables, so the safe way is to think that everything was changed
            {
                for(Map.Entry<String, Boolean> entry : inBlockLiv.entrySet())
                {
                    entry.setValue(true);
                }
            }
            else if(!(currentQuad.getOperator().equals("unit") || currentQuad.getOperator().equals("endu")))
            {
                String optionName = currentQuad.getOpt1();

                //its a variable
                if(isVariable(optionName))
                {
                    if(optionName.charAt(0)=='[')
                    {
                        optionName = optionName.substring(1, optionName.length()-1);
                    }

                    inBlockLiv.put(optionName, true);
                }

                optionName = currentQuad.getOpt2();

                //par has only the first as an argument, we dont want to take the mode and think it as an argument
                if(currentQuad.getOperator().equals("par")) continue;

                if(isVariable(optionName))
                {
                    if(optionName.charAt(0)=='[')
                    {
                        optionName = optionName.substring(1, optionName.length()-1);
                    }

                    inBlockLiv.put(optionName, true);
                }


                if(currentQuad.getOperator().equals(":="))
                {
                    optionName = currentQuad.getOpt3();
                    if(currentQuad.getOpt3().equals("$$") || isReferenceRecord(optionName))
                    {
                        continue;
                    }

                    if(optionName.charAt(0)=='[')
                    {
                        optionName = optionName.substring(1, optionName.length()-1);
                    }
                    inBlockLiv.put(optionName, false);
                }
            }

        }

        if(firstPass == true)
        {
            firstPass = false;
            return true;
        }
        firstPass = false;

        return change;
    }

    public boolean variableValues()
    {
        boolean change = false;
        HashMap<String, String> inputBlock;
        String inVarValue;
        String currentVarValue;
        //calculate in

        for(String key : inBlock.keySet())
        {
            currentVarValue = inBlock.get(key);
            for(Node prevNodes: edgesFrom)
            {
                if(currentVarValue.equals("*")) break; //cant become something else

                inputBlock = prevNodes.getOutBlock();
                inVarValue = inputBlock.get(key);

                if(!inVarValue.equals("#")) //we ignore hash
                {
                    if(currentVarValue.equals("#")) //get whatever value is inVarValue
                    {
                        currentVarValue = inVarValue;
                        change = true;
                        continue;
                    }

                    if(!inVarValue.equals(currentVarValue)) //they are not equal, either we have a * or both constants
                    {
                        currentVarValue = "*";
                        change = true;
                    }
                }
            }

            inBlock.put(key, currentVarValue);


        }

        if(firstPass==false && change==false) //continue only if there was a change at the input and its not the first pass
        {
            return change; //false
        }


        outBlock = new HashMap<>(inBlock); //mallon tha to kanw argo...

        for(Quad currentQuad : blockQuads)
        {
            if(currentQuad.getOperator().equals(":="))
            {
                if(isNumber(currentQuad.getOpt1()) || isChar(currentQuad.getOpt1()) /*|| currentQuad.getOpt1().charAt(0)=='\"'*/)
                {
                    outBlock.put(currentQuad.getOpt3(), currentQuad.getOpt1());//both could be deref operators is it a problem?
                }
                else
                {
                    outBlock.put(currentQuad.getOpt3(), "*");//both could be deref operators is it a problem?
                }
            }
            else if(currentQuad.getOperator().equals("call")) //we dont know if call changes or variables, so the same way is to think that everythink was changed
            {
                for(Map.Entry<String, String> entry : outBlock.entrySet())
                {
                    entry.setValue("*");
                }
            }
        }

        return change;
    }

    //change quad to assignment
    private void assignQuad(int result, Quad currentQuad)
    {
        currentQuad.setOperator(":=");
        currentQuad.setOpt1(Integer.toString(result));
        currentQuad.setOpt2("-");
    }

    //check if operator is number
    public boolean isNumber(String operator)
    {
        int numb;
        try {
            numb = Integer.parseInt(operator);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isChar(String operator)
    {
        if(operator.charAt(0) == '\'') return true;
        return false;
    }

    public boolean isVariable(String operator)
    {
        return (('A'<=operator.charAt(0) && operator.charAt(0) <= 'z')|| operator.charAt(0)=='$');

    }

    public int covertEscapedChar(String escapedChar)
    {
        if(escapedChar.equals("\\n"))
        {
            return '\n';
        }
        else if(escapedChar.equals("\\t"))
        {
            return '\t';
        }
        else if(escapedChar.equals("\\r"))
        {
            return '\r';
        }
        else if(escapedChar.equals("\\0"))
        {
            return '\0';
        }
        else if(escapedChar.equals("\\\\"))
        {
            return '\\';
        }
        else if(escapedChar.equals("\\'"))
        {
            return '\'';
        }
        else if(escapedChar.equals("\\\""))
        {
            return '\"';
        }
        else
        {
            return hexToDecimal(escapedChar);
        }

    }

    //format of hex is '\xnn'
    public int hexToDecimal(String hex)
    {
        int number;
        number = (hex.charAt(2)-'0')*16 + (hex.charAt(3)-'0');
        return number;
    }

    public void printQuads()
    {
        System.out.println("NODE "+startLabel);
        for(Quad quad :blockQuads)
        {
            System.out.println(quad.getLabel()+": "+quad.getOperator()+",\t" +quad.getOpt1()+",\t"+ quad.getOpt2()+",\t" + quad.getOpt3());
        }
    }

    public void printEdgeNode()
    {
        for(Node node: edgesTo)
        {
            System.out.println("EdgeTo "+node.getStartLabel());
        }
        for(Node node: edgesFrom)
        {
            System.out.println("EdgeFrom "+node.getStartLabel());
        }
    }
}
