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
        startLabel = 0;
    }

    public void production()
    {
        ArrayList<Quad> qList = quadList.getQuadList();
        Iterator<Quad> iterQuad;
        Quad currentQuad;
        String labelTag = "Label_";

        for(iterQuad = qList.listIterator(startLabel); iterQuad.hasNext(); )
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
                System.out.println(currentQuad.getOpt1()+": ");
                System.out.println("push ebp");
                System.out.println("mov ebp, esp");
                System.out.println("sub esp, "+ Math.abs(symbolTable.getLocalOffset()));
            }
            else if(currentQuad.getOperator().equals("endu"))
            {
                //System.out.println("add esp, "+ Math.abs(symbolTable.getLocalOffset()));

                System.out.println("mov esp, ebp");
                System.out.println("pop ebp");
                System.out.println("ret");
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

                    loadAddr("esi", currentQuad.getOpt1(), rec);
                    System.out.println("push esi");
                }
            }
            else if(currentQuad.getOperator().equals("call"))
            {
                Record rec;
                RecordFunction recFunct;
                rec = symbolTable.lookup(unfixName(currentQuad.getOpt3()));
                recFunct = (RecordFunction) rec;
                if(recFunct.getType().equals("nothing"))
                {
                    System.out.println("sub esp, 4");
                }

                updateAL(recFunct);
                System.out.println("call "+currentQuad.getOpt3()); //push return address ?
                System.out.println("add esp, "+(recFunct.getFparameters().size()*4+8));

            }
            else if(currentQuad.getOperator().equals("ret"))
            {

                System.out.println("jmp "+labelTag+(qList.get(qList.size()-1)).getLabel());
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
        System.out.println("mov esi, dword ptr [ebp+8]");

        varScope = rec.getDepth();
        currentScope = symbolTable.getCurrentDepth()-1;
        while (currentScope > varScope)
        {
            System.out.println("mov esi, dword ptr [esi+8]");
            currentScope--;
        }
    }

    public void updateAL(Record recFunct)
    {
        int callerScope = symbolTable.getCurrentDepth()-1;
        int calleeScope = recFunct.getDepth();

        if(callerScope < calleeScope)
        {
            System.out.println("push ebp");
        }
        else if(callerScope == calleeScope)
        {
            System.out.println("push dword ptr [ebp+8]");
        }
        else
        {
            System.out.println("mov esi, dword ptr [ebp+8]");
            callerScope--;

            while (callerScope > calleeScope)
            {
                System.out.println("mov esi, dword ptr [esi+8]");
                callerScope--;
            }

            System.out.println("push dword ptr [esi+8]");
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
            int asciValue;
            String character = operator.substring(1, operator.length()-1);

            if(character.length()==1)
            {
                asciValue = (int)character.charAt(0);
            }
            else
            {
                asciValue = covertEscapedChar(character);
            }

            //System.out.println("movzx "+register+", "+asciValue);
            System.out.println("mov "+register+", "+asciValue);//ASCI(Operator) missing!!!
        }
        else if(!(derefOper=isDereference(operator)).equals(""))
        {
            rec = symbolTable.lookup(derefOper);
            load("edi", derefOper);
            size = operatorSize(rec);

            if(rec.getType().equals("pointerchar"))
            {
                //me movzx
                size = "byte";
                System.out.println("movzx "+register+", "+size+" ptr [edi]");
            }
            else
            {
                System.out.println("mov "+register+", "+size+" ptr [edi]");
            }


        }
        else //variables
        {
            boolean ref;
            String mov;
            rec = symbolTable.lookup(operator);
            size = operatorSize(rec);

            if(size.equals("byte")) mov = "movzx";
            else mov = "mov";

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
                    System.out.println(mov+" "+register+", "+size+" ptr [esi]");
                }
                else
                {
                    System.out.println(mov+" "+register+", "+size+" ptr [ebp"+rec.getOffset()+"]"); //mov R, size ptr [bp + offset]
                }

            }
            else //non local
            {
                ref = hasRef(rec);
                if(ref)
                {
                    getAR(rec);
                    System.out.println("mov esi, dword ptr [esi"+rec.getOffset()+"]");
                    System.out.println(mov+" "+register+", "+size+" ptr [esi]");
                }
                else
                {
                    getAR(rec);
                    System.out.println(mov+" "+register+", "+size+" ptr [esi"+rec.getOffset()+"]");
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
                    System.out.println("mov "+register+", dword ptr [ebp"+rec.getOffset()+"]");
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

            if(size.equals("byte")) register = "al";
            if(rec.getType().equals("pointerchar"))
            {
                register = "al";
                size ="byte";
            }

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
            if(size.equals("byte")) register = "al";

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

    //public void addDataSegment()
    public void addLibraryCalls()
    {
        String puti= "puti_grace:\n" +
                "\tpush ebp\n" +
                "\tmov ebp, esp\n" +
                "\t\t\n" +
                "\tmov eax, dword ptr[ebp+16]\n" +
                "\tpush eax\n" +
                "\tmov eax, OFFSET FLAT:in\n" +
                "\tpush eax\n" +
                "\n" +
                "\tcall printf\n" +
                "\tadd esp, 4\n" +
                "\n" +
                "\tmov esp, ebp\n" +
                "   \tpop ebp\n" +
                "    \tret\n";

        String putc = "putc_grace:\n" +
                "\tpush ebp\n" +
                "\tmov ebp, esp\n" +
                "\t\t\n" +
                "\tmov eax, byte ptr[ebp+16]\n" +
                "\tpush eax\n" +
                "\tmov eax, OFFSET FLAT:char\n" +
                "\tpush eax\n" +
                "\n" +
                "\tcall printf\n" +
                "\tadd esp, 4\n" +
                "\n" +
                "\tmov esp, ebp\n" +
                "   \tpop ebp\n" +
                "    \tret";

        String geti = "geti_grace:\n" +
                "\tpush ebp\n" +
                "\tmov ebp, esp\n" +
                "\n" +
                " # This is the address of the local variable that we're passing to scanf\n" +
                "    mov eax, dword ptr[ebp+12]\n" +
                "    push eax\n" +
                "\n" +
                "    # Pass the format string literal to scanf\n" +
                "    mov eax, OFFSET FLAT:scanf_fmt\n" +
                "    push eax\n" +
                "    call scanf\n" +
                "    add esp, 8\n" +
                "\n" +
                "\tmov esp, ebp\n" +
                "   \tpop ebp\n" +
                "    ret";

        String dataPart = ".data\n" +
                "    integer: .asciz  \"%d\"\n" +
                "    char: .asciz \"%c\"\n"+
                "scanf_fmt: .asciz  \"%d\"";
    }

    //format of hex is '\xnn'
    public int hexToDecimal(String hex)
    {
        int number;
        number = (hex.charAt(2)-'0')*16 + (hex.charAt(3)-'0');
        return number;
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

}
