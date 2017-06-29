package compiler;

import com.sun.org.apache.regexp.internal.RE;

import java.util.*;

public class Optimise
{
    private QuadList quadList;
    private int startLabel;
    private ArrayList<Integer> quadsWithLabel;
    private Graph graphCFG;

    public Optimise(QuadList quads, int startQuad, HashMap<String, Record> curScope, SymbolTable symTable)
    {
        quadList = quads;
        startLabel = startQuad;
        quadsWithLabel = new ArrayList<>();
        graphCFG = new Graph(curScope, symTable);
    }

    //create the CFG graph
    public void createCFG()
    {
        ArrayList<Quad> qList = quadList.getQuadList();
        Iterator<Quad> iterQuad;
        Quad currentQuad;

        eliminateIfJumps();

        //find all quads which have a jump to then
        for(iterQuad = qList.listIterator(startLabel); iterQuad.hasNext();) //iterate throught all quads
        {
            currentQuad = iterQuad.next();
            if(quadHasJmp(currentQuad))
            {
                quadsWithLabel.add(Integer.parseInt(currentQuad.getOpt3())); //add the labeled quad at the list
            }

        }

        createBlocks();

        graphCFG.findEdges();
    }

    public void run(int maxLoops)
    {
        boolean change = false;
        createCFG(); //create the control flow graph

        for(int i =0; i <maxLoops; i++)
        {
            change = false;
            if(graphCFG.optimizeBlocks(5)) //local constantFolding, constPropagation, algebra simplification
                change = true;
            if(graphCFG.deleteUnreachableCode())
                change = true;

            if(graphCFG.copyPropagationGlobal())
                change = true;
            if(graphCFG.calculateLiveness()) //global, eliminate deadcode
                change = true;

            if(change==false)
                break;
        }


    }

    //find the blocks and create the graph nodes
    public void createBlocks()
    {
        ArrayList<Quad> qList = quadList.getQuadList();
        Iterator<Quad> iterQuad;
        Quad currentQuad;

        boolean createNode = true;
        Node currentNode= null;// = new Node();
        Quad previousQuad = null;

        for(iterQuad = qList.listIterator(startLabel); iterQuad.hasNext();) //iterate throught all quads
        {
            currentQuad = iterQuad.next();
            if(createNode) //last command was a jump
            {
                if(previousQuad != null && (quadCmp(previousQuad) || previousQuad.getOperator().equals("call"))) //previous quad was a cmp, so if it doesnt jump, there must be an edge at the next quad
                {
                    currentNode.addNextNode(currentQuad.getLabel());
                }

                // currentNode = new Node(currentQuad.getLabel());
                currentNode = graphCFG.addNode(currentQuad.getLabel());
                createNode = false;
            }

            //jump to current quad, a new node has to be created
            if (quadsWithLabel.contains(currentQuad.getLabel())) //O(N) complexity kalytera me hashmap gia na exw mono ~O(1)
            {
                if(currentNode.getTotalQuads() !=0) //already new node from the jump
                {
                    currentNode.addNextNode(currentQuad.getLabel());//add an edge at the previous to the new one
                    currentNode = graphCFG.addNode(currentQuad.getLabel());
                }


            }

            //finish current node to here
            if(quadHasJmp(currentQuad) || currentQuad.getOperator().equals("ret") || currentQuad.getOperator().equals("call"))
            {
                //end of node
                currentNode.addQuad(currentQuad); //add the quad at the currentNode
                if(!(currentQuad.getOperator().equals("ret") || currentQuad.getOperator().equals("call"))) //return, call doesnt have and jump label at the same function
                {
                    currentNode.addNextNode(Integer.parseInt(currentQuad.getOpt3()));  //add the jump label at the neigbors
                }

                createNode = true; //current node has ended create a new Node
                previousQuad = currentQuad;
                continue;
            }


            currentNode.addQuad(currentQuad);
        }
    }

    //after every comparison two quads are generated, relop, x, y, label and jump,-,-,label
    //eliminate the jump quad by reversing the relop operator
    public void eliminateIfJumps()
    {
        ArrayList<Quad> qList = quadList.getQuadList();
        Iterator<Quad> iterQuad;
        Quad currentQuad ,nextQuad;
        String newCmpOper;

        for(iterQuad = qList.listIterator(startLabel); iterQuad.hasNext();) //iterate throught all quads
        {
            currentQuad = iterQuad.next();
            if(quadCmp(currentQuad)) //relop quad
            {
                newCmpOper = reverseCmp(currentQuad.getOperator());
                nextQuad = iterQuad.next(); //jump quad

                currentQuad.setOperator(newCmpOper); //reversed compare
                currentQuad.setOpt3(nextQuad.getOpt3()); //the label is the jump label


                nextQuad.setNop(); //remove jump, by setting it to nop
            }

        }

    }

    //returns the opposite comparison operator
    public String reverseCmp(String oper)
    {
        if(oper.equals(">"))
            return "<=";
        else if (oper.equals(">="))
            return "<";
        else if (oper.equals("<"))
            return ">=";
        else if (oper.equals("<="))
            return ">";
        else if (oper.equals("="))
            return "#";
        else // not equal #
            return "=";
    }

    //returns is the quad has a comparison operator
    public boolean quadCmp(Quad currentQuad)
    {
        return (currentQuad.getOperator().equals("=") || currentQuad.getOperator().equals("#")
                || currentQuad.getOperator().equals("<") || currentQuad.getOperator().equals("<=")
                || currentQuad.getOperator().equals(">") || currentQuad.getOperator().equals(">="));
    }

    //returns is the quad jumps to a label. So if its a comparison quad or a jump.
    public boolean quadHasJmp(Quad currentQuad)
    {
        return (quadCmp(currentQuad) || currentQuad.getOperator().equals("jump"));
    }
}
