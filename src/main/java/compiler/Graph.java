package compiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Graph
{
    private HashMap<Integer, Node> nodes;
    private Node endNode, startNode;
    private HashMap<String, Record> currentScope;
    private SymbolTable symTable;

    private HashMap<String, Boolean> varLiveness;
    private HashMap<String, String> copyPropVars;

    public Graph(HashMap<String, Record> curScope, SymbolTable symbolTable)
    {
        nodes = new HashMap<>();
        currentScope= curScope;
        startNode = null;
        symTable = symbolTable;

        copyPropVars = new HashMap<>();
        varLiveness = new HashMap<>();
        for(Record rec : currentScope.values()) //we create all the variables
        {
            if(rec instanceof RecordFunction)
            {
                continue;
            }

            copyPropVars.put(rec.getName(), "#");
            varLiveness.put(rec.getName(), false);

        }
    }

    public Node addNode(int startLbl)
    {
        Node node = new Node(startLbl, symTable);

        if(startNode == null) //keep the first node of the hashmap
        {
            startNode = node;
        }


        nodes.put(node.getStartLabel(), node);
        node.setOutBlock(copyPropVars);
        node.setInBlock(copyPropVars);
        node.setInBlockLiv(varLiveness);
        node.setOutBlockLiv(varLiveness);


        endNode= node;

        return node;
    }

    public Node getNode(int i)
    {
        return nodes.get(i);
    }

    //find the edges of the nodes
    public void findEdges()
    {
        int nextNode;
        Node edgeNode;

        for(Node currentNode :nodes.values()) //for all nodes
        {
            while ((nextNode = currentNode.popNextNode()) != -1) //find all the neighbor nodes
            {
                edgeNode = nodes.get(nextNode); //get the Node class
                currentNode.addEdgeTo(edgeNode); //add an edge to the founded node
                edgeNode.addEdgeFrom(currentNode); //add an edge From at the founded node from the current
            }
        }
    }

    //print the graph
    public void print()
    {
        for(Node currentNode :nodes.values())
        {
            currentNode.printQuads();
            currentNode.printEdgeNode();
        }

    }

    //make block level optimizations
    public boolean optimizeBlocks(int maxLoops)
    {
        boolean change = false;
        for(Node currentNode :nodes.values())
        {
            if(currentNode.optimizeBlock(maxLoops))
            {
                change =true;
            }
        }

        return change;
    }

    public boolean copyPropagationGlobal()
    {
        Node currentNode, nextNode;

        //initialise all nodes
        for(Node node: nodes.values())
        {
            node.initializeGlobalOptimization();
        }


        boolean change = false;

        do
        {
            change = false;
            for(Node node: nodes.values())
            {
                if(node.variableValues())
                {
                    change = true;
                }
            }

        }while (change);

        change = false;
        //now we can propagate
        for(Node node: nodes.values())
        {
            if(node.copyPropagateValues())
            {
                change = true;
            }
        }

        return change;
    }

    public boolean calculateLiveness()
    {
        HashMap<String, Boolean> variables = new HashMap<>();
        boolean change;
        int i;

        //initialise all nodes
        for(Node node: nodes.values())
        {
            node.initializeGlobalLiveness();
        }

        //find the values
        i=0;
        do
        {
            change = false;
            for(Node node: nodes.values())
            {
                if(node.calculateLiveness())
                {
                    change = true;
                }
            }

            i++;
        }while (change);

        //now we can propagate
        change = false;
        for(Node node: nodes.values())
        {
            if(node.eliminateDeadCode())
            {
                change = true;
            }
        }

        return change;
    }

    public boolean deleteUnreachableCode()
    {
        Node currentNode;
        Iterator<Map.Entry<Integer, Node>> nodeIter;
        Map.Entry<Integer, Node> entry;
        boolean change = false;

        for(nodeIter = nodes.entrySet().iterator(); nodeIter.hasNext();)
        {
            entry = nodeIter.next();
            currentNode = entry.getValue();

            if(currentNode.getTotalEdgesFrom()==0 && currentNode.getStartLabel() != startNode.getStartLabel() && currentNode.getStartLabel() != endNode.getStartLabel()) //if there arent any edges to it and its not the first node delete it
            {
                currentNode.deleteNode();
                nodeIter.remove(); //delete the node from the hashTable
                change = true;
            }
        }

        return change;
    }



}