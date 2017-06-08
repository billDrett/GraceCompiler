package compiler;

import compiler.node.PVarIdentifier;

import java.util.ArrayList;
import java.util.Iterator;

public class InstructionSelection
{
    private SymbolTable symbolTable;
    private QuadList quadList;
    private int startLabel;

    InstructionSelection(SymbolTable symTable, QuadList qList)
    {
        symbolTable = symTable;
        quadList = qList;
        startLabel = 1;
    }

    public void production()
    {
        ArrayList<Quad> qList = quadList.getQuadList();
        Iterator<Quad> iterQuad;
        Quad currentQuad;
        String labelTag = "Label_";

        for(iterQuad = qList.listIterator(startLabel-1); iterQuad.hasNext(); )
        {
            currentQuad = iterQuad.next();
            System.out.print(labelTag+currentQuad.getLabel()+": ");
            //generate object code
            if(currentQuad.getOperator().equals(":="))
            {
                load("eax", currentQuad.getOpt1());
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("array"))
            {
                Record rec;
                int size;
                rec = symbolTable.lookup(currentQuad.getOpt1());

                if(rec.getType().equals("char"))
                {
                    size = 1;
                }
                else
                {
                    size = 4;
                }

                load("eax", currentQuad.getOpt2());
                System.out.println("mov ecx, "+size);
                System.out.println("imul ecx");
                loadAddr("ecx", currentQuad.getOpt1(), rec);
                System.out.println("add eax, ecx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("+"))
            {
                load("eax", currentQuad.getOpt1());
                load("edx", currentQuad.getOpt2());
                System.out.println("add eax, edx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("-"))
            {
                load("eax", currentQuad.getOpt1());
                load("edx", currentQuad.getOpt2());
                System.out.println("sub eax, edx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("*"))
            {
                load("eax", currentQuad.getOpt1());
                load("ecx", currentQuad.getOpt2());
                System.out.println("imul eax, ecx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("/"))
            {
                load("eax", currentQuad.getOpt1());
                System.out.println("cdq");
                load("ebx", currentQuad.getOpt2());
                System.out.println("idiv ebx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("%"))
            {
                load("eax", currentQuad.getOpt1());
                System.out.println("cdq");
                load("ebx", currentQuad.getOpt2());
                System.out.println("idiv ebx");
                store("edx", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("unit"))
            {
                System.out.println(currentQuad.getOpt1()+" proc near");
                System.out.println("push ebp");
                System.out.println("mov ebp, esp");
                System.out.println("sub esp, "+ Math.abs(symbolTable.getLocalOffset()));
            }
            else if(currentQuad.getOperator().equals("endu"))
            {
                System.out.println("mov esp, ebp");
                System.out.println("pop ebp");
                System.out.println("ret");
                System.out.println(currentQuad.getOpt1()+" endp");
            }
            else if (currentQuad.getOperator().equals("=") || currentQuad.getOperator().equals("#")
                  || currentQuad.getOperator().equals("<") || currentQuad.getOperator().equals("<=")
                  || currentQuad.getOperator().equals(">") || currentQuad.getOperator().equals(">="))
            {
                load("eax", currentQuad.getOpt1());
                load("edx", currentQuad.getOpt2());
                System.out.println("cmp eax, edx");
                if(currentQuad.getOperator().equals("="))
                {
                    System.out.println("je "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals("#"))
                {
                    System.out.println("jne "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals("<"))
                {
                    System.out.println("jl "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals("<="))
                {
                    System.out.println("jle "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals(">"))
                {
                    System.out.println("jg "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals(">="))
                {
                    System.out.println("jge "+labelTag+currentQuad.getOpt3());
                }
            }
            else if(currentQuad.getOperator().equals("jump"))
            {
                System.out.println("jmp "+labelTag+currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("par"))
            {
                if (currentQuad.getOpt2().equals("V"))
                {
                    load("eax", currentQuad.getOpt1());
                    System.out.println("push eax");
                }
                else
                {
                    Record rec;
                    rec = symbolTable.lookup(currentQuad.getOpt1());

                    loadAddr("eax", currentQuad.getOpt1(), rec);
                    System.out.println("push esi");
                }
            }
            else if(currentQuad.getOperator().equals("call"))
            {
                Record rec;
                RecordFunction recFunct;
                rec = symbolTable.lookup(unfixName(currentQuad.getOpt3()));
                recFunct = (RecordFunction) rec;

                System.out.println("sub esp, 4");
                updateAL(recFunct);
                System.out.println("call near ptr "+currentQuad.getOpt3());
                System.out.println("add esp, "+(recFunct.getFparameters().size()*4+8));

            }
            else if(currentQuad.getOperator().equals("ret"))
            {

                System.out.println("jmp "+labelTag+qList.get(qList.size()-1));
            }

        }
        startLabel = qList.size();
    }

    public String unfixName(String fixedName)
    {
        for(int i=fixedName.length()-1; i>=0; i--)
            if(fixedName.charAt(i) == '_')
                return fixedName.substring(0,i);

        return "";
    }

    public void getAR(Record rec)
    {
        int varScope, currentScope;
        System.out.println("mov esi, word ptr [ebp+8]");

        varScope = rec.getDepth();
        currentScope = symbolTable.getCurrentDepth()-1;
        while (currentScope > varScope)
        {
            System.out.println("mov esi, word ptr [esi+8]");
            currentScope--;
        }
    }

    public void updateAL(Record recFunct)
    {
        int callerScope = symbolTable.getCurrentDepth();
        int calleeScope = recFunct.getDepth();

        if(callerScope < calleeScope)
        {
            System.out.println("push ebp");
        }
        else if(callerScope == calleeScope)
        {
            System.out.println("push word ptr [ebp+8]");
        }
        else
        {
            System.out.println("mov esi, word ptr [ebp+8]");
            callerScope--;

            while (callerScope > calleeScope)
            {
                System.out.println("mov esi, word ptr [esi+8]");
                callerScope--;
            }

            System.out.println("push word ptr [esi+8]");
        }
    }


    public void load(String register, String operator)
    {
        Record rec;
        String derefOper;
        String size;

        if(isNumber(operator))
        {
            System.out.println("mov "+register+", "+operator);
        }
        else if(isChar(operator))
        {
            System.out.println("mov "+register+", "+operator.charAt(0));//ASCI(Operator) missing!!!
        }
        else if(!(derefOper=isDereference(operator)).equals(""))
        {
            rec = symbolTable.lookup(derefOper);
            load("edi", derefOper);
            size = operatorSize(rec);

            System.out.println("mov "+register+", "+size+" ptr [edi]");
        }
        else //variables
        {
            boolean ref;

            rec = symbolTable.lookup(operator);
            size = operatorSize(rec);

            /*if(rec instanceof RecordArray || rec.getType().equals("pointer") ||rec.getType().equals("pointerStr")) //address
            {
                loadAddr(register, operator, rec);
            }
            else*/ if(rec.getDepth() == symbolTable.getCurrentDepth()) //local scope
            {
                ref = hasRef(rec);
                if(ref) //has reference
                {
                    System.out.println("mov esi, dword ptr [ebp"+rec.getOffset()+"]");
                    System.out.println("mov "+size+" ptr [esi], "+register);
                }
                else
                {
                    System.out.println("mov "+register+", "+size+" ptr [ebp"+rec.getOffset()+"]"); //mov R, size ptr [bp + offset]
                }

            }
            else //non local
            {
                ref = hasRef(rec);
                if(ref)
                {
                    getAR(rec);
                    System.out.println("mov esi, dword ptr [esi"+rec.getOffset()+"]");
                    System.out.println("mov "+register+", "+size+" ptr [esi]");
                }
                else
                {
                    getAR(rec);
                    System.out.println("mov "+register+", "+size+" ptr [esi"+rec.getOffset()+"]");
                }
            }
        }
    }

    public void loadAddr(String register, String operator, Record rec)
    {
        String derefOper;
        String size;

        if(rec.getType().equals("pointerStr"))
        {
            System.out.println("lea "+register+", byte ptr "+rec.getName());//lea R, byte ptr a
        }
        else if(!(derefOper=isDereference(operator)).equals("")) //[x]
        {
            load(register, derefOper);
        }
        else //variables
        {
            boolean ref;

            size = operatorSize(rec);

            if(rec.getDepth() == symbolTable.getCurrentDepth()) //local scope
            {
                ref = hasRef(rec);
                if(ref) //has reference
                {
                    System.out.println("mov "+register+" dword ptr [ebp"+rec.getOffset()+"]");
                }
                else
                {
                    System.out.println("lea "+register+", "+size+" ptr [ebp"+rec.getOffset()+"]");
                }

            }
            else //non local
            {
                ref = hasRef(rec);
                if(ref)
                {
                    getAR(rec);
                    System.out.println("mov "+register+", "+size+" ptr [esi"+rec.getOffset()+"]");
                }
                else
                {
                    getAR(rec);
                    System.out.println("lea "+register+", "+size+" ptr [esi"+rec.getOffset()+"]");
                }
            }
        }
    }

    public void store(String register, String operator)
    {
        Record rec;
        String derefOper;
        String size;


        if(!(derefOper=isDereference(operator)).equals("")) //[x]
        {
            rec = symbolTable.lookup(derefOper);
            load("edi", derefOper);
            size = operatorSize(rec);

            System.out.println("mov "+size+" ptr [edi], "+register);
        }
        else //variables
        {
            boolean ref;

            if(operator.equals("$$"))
            {
                System.out.println("mov esi, dword ptr [ebp+12]");
                System.out.println("mov dword ptr [esi], "+register);
                return;
            }

            rec = symbolTable.lookup(operator);
            size = operatorSize(rec);

            if(rec.getDepth() == symbolTable.getCurrentDepth()) //local scope
            {
                ref = hasRef(rec);
                if(ref) //has reference
                {
                    System.out.println("mov esi, dword ptr [ebp"+rec.getOffset()+"]");
                    System.out.println("mov "+size+" ptr [esi], "+register);
                }
                else
                {
                    System.out.println("mov "+size+" ptr [ebp"+rec.getOffset()+"], "+register); //mov R, size ptr [bp + offset]
                }
            }
            else //non local
            {
                ref = hasRef(rec);
                if(ref)
                {
                    getAR(rec);
                    System.out.println("mov esi, dword ptr [esi"+rec.getOffset()+"]");
                    System.out.println("mov "+size+" ptr [esi],"+register);
                }
                else
                {
                    getAR(rec);
                    System.out.println("mov "+size+" ptr [esi"+rec.getOffset()+"], "+register);
                }
            }
        }
    }

    public String operatorSize(Record rec)
    {
        if(rec.getType().equals("char"))
        {
            return  "byte";
        }
        else
        {
            return "dword";
        }
    }

    public boolean hasRef(Record rec)
    {
        RecordParam recParam;
        RecordParamArray recordParamArray;

        if(rec instanceof RecordParam)
        {
            recParam = (RecordParam) rec;
            return recParam.getReference();
        }
        else if(rec instanceof RecordParamArray)
        {
            recordParamArray = (RecordParamArray) rec;
            return recordParamArray.getReference();
        }

        return false;
    }


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

    public String isDereference(String operator)
    {
        if(operator.charAt(0) == '[' && operator.charAt(operator.length()-1)==']')
        {
            return operator.substring(1,operator.length()-1);
        }

        return "";
    }
}
