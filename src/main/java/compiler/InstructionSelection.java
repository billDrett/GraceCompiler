package compiler;

import compiler.node.PVarIdentifier;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class InstructionSelection
{
    private SymbolTable symbolTable;
    private QuadList quadList;
    private int startLabel;
    private ArrayList<String> stringLiterals;
    private PrintWriter writer;

    InstructionSelection(SymbolTable symTable, QuadList qList)
    {
        symbolTable = symTable;
        quadList = qList;
        startLabel = 0;
        try{
            writer = new PrintWriter("assembly.s");
            writer.println(".intel_syntax noprefix");
            writer.println(".text");
            writer.println(".global main");
        } catch (IOException e) {
            // do something
        }

        stringLiterals = new ArrayList<>();
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
            writer.print(labelTag+currentQuad.getLabel()+": ");
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
                writer.println("mov ecx, "+size);
                writer.println("imul ecx");
                loadAddr("ecx", currentQuad.getOpt1(), rec);
                writer.println("add eax, ecx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("+"))
            {
                load("eax", currentQuad.getOpt1());
                load("edx", currentQuad.getOpt2());
                writer.println("add eax, edx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("-"))
            {
                load("eax", currentQuad.getOpt1());
                load("edx", currentQuad.getOpt2());
                writer.println("sub eax, edx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("*"))
            {
                load("eax", currentQuad.getOpt1());
                load("ecx", currentQuad.getOpt2());
                writer.println("imul eax, ecx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("/"))
            {
                load("eax", currentQuad.getOpt1());
                writer.println("cdq");
                load("ebx", currentQuad.getOpt2());
                writer.println("idiv ebx");
                store("eax", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("%"))
            {
                load("eax", currentQuad.getOpt1());
                writer.println("cdq");
                load("ebx", currentQuad.getOpt2());
                writer.println("idiv ebx");
                store("edx", currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("unit"))
            {
                if(symbolTable.getCurrentDepth()==2) //main function, must have main label
                {
                    writer.println("main: ");
                }
                writer.println(currentQuad.getOpt1()+": ");
                writer.println("push ebp");
                writer.println("mov ebp, esp");
                writer.println("sub esp, "+ Math.abs(symbolTable.getLocalOffset()));
            }
            else if(currentQuad.getOperator().equals("endu"))
            {
                //writer.println("add esp, "+ Math.abs(symbolTable.getLocalOffset()));

                writer.println("mov esp, ebp");
                writer.println("pop ebp");
                writer.println("ret");

                if(symbolTable.getCurrentDepth()==2) //main function, add at the end the library calls and .data part
                {
                    int counter =1;
                    addLibraryCalls();
                    //libraryCalls has .data part at the end
                    //so add all the string literals at the end
                    for(String i : stringLiterals)
                    {
                        writer.println("\tl"+counter+": .asciz \""+i+"\"");
                        counter++;
                    }
                    writer.close();
                }

            }
            else if (currentQuad.getOperator().equals("=") || currentQuad.getOperator().equals("#")
                  || currentQuad.getOperator().equals("<") || currentQuad.getOperator().equals("<=")
                  || currentQuad.getOperator().equals(">") || currentQuad.getOperator().equals(">="))
            {
                load("eax", currentQuad.getOpt1());
                load("edx", currentQuad.getOpt2());
                writer.println("cmp eax, edx");
                if(currentQuad.getOperator().equals("="))
                {
                    writer.println("je "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals("#"))
                {
                    writer.println("jne "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals("<"))
                {
                    writer.println("jl "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals("<="))
                {
                    writer.println("jle "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals(">"))
                {
                    writer.println("jg "+labelTag+currentQuad.getOpt3());
                }
                else if(currentQuad.getOperator().equals(">="))
                {
                    writer.println("jge "+labelTag+currentQuad.getOpt3());
                }
            }
            else if(currentQuad.getOperator().equals("jump"))
            {
                writer.println("jmp "+labelTag+currentQuad.getOpt3());
            }
            else if(currentQuad.getOperator().equals("par"))
            {
                if (currentQuad.getOpt2().equals("V"))
                {
                    load("eax", currentQuad.getOpt1());
                    writer.println("push eax");
                }
                else
                {
                    Record rec;
                    rec = symbolTable.lookup(currentQuad.getOpt1());

                    loadAddr("esi", currentQuad.getOpt1(), rec);
                    writer.println("push esi");
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
                    writer.println("sub esp, 4");
                }

                updateAL(recFunct);
                writer.println("call "+currentQuad.getOpt3()); //push return address ?
                writer.println("add esp, "+(recFunct.getFparameters().size()*4+8));

            }
            else if(currentQuad.getOperator().equals("ret"))
            {

                writer.println("jmp "+labelTag+(qList.get(qList.size()-1)).getLabel());
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
        writer.println("mov esi, dword ptr [ebp+8]");

        varScope = rec.getDepth();
        currentScope = symbolTable.getCurrentDepth()-1;
        while (currentScope > varScope)
        {
            writer.println("mov esi, dword ptr [esi+8]");
            currentScope--;
        }
    }

    public void updateAL(Record recFunct)
    {
        int callerScope = symbolTable.getCurrentDepth()-1;
        int calleeScope = recFunct.getDepth();

        if(callerScope < calleeScope)
        {
            writer.println("push ebp");
        }
        else if(callerScope == calleeScope)
        {
            writer.println("push dword ptr [ebp+8]");
        }
        else
        {
            writer.println("mov esi, dword ptr [ebp+8]");
            callerScope--;

            while (callerScope > calleeScope)
            {
                writer.println("mov esi, dword ptr [esi+8]");
                callerScope--;
            }

            writer.println("push dword ptr [esi+8]");
        }
    }

    public void load(String register, String operator)
    {
        Record rec;
        String derefOper;
        String size;

        if(isNumber(operator))
        {
            writer.println("mov "+register+", "+operator);
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

            //writer.println("movzx "+register+", "+asciValue);
            writer.println("mov "+register+", "+asciValue);//ASCI(Operator) missing!!!
        }
        else if(isString(operator))
        {
            String stringLiteral = operator.substring(1, operator.length()-1); //ignore the quotes
            stringLiterals.add(stringLiteral);

            writer.println("mov "+register+", OFFSET FLAT: l"+stringLiterals.size()); //string literal will have as label l id
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
                writer.println("movzx "+register+", "+size+" ptr [edi]");
            }
            else
            {
                writer.println("mov "+register+", "+size+" ptr [edi]");
            }

        }
        else    //variables
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
                    writer.println("mov esi, dword ptr [ebp"+rec.getOffset()+"]");
                    writer.println(mov+" "+register+", "+size+" ptr [esi]");
                }
                else
                {
                    writer.println(mov+" "+register+", "+size+" ptr [ebp"+rec.getOffset()+"]"); //mov R, size ptr [bp + offset]
                }

            }
            else //non local
            {
                ref = hasRef(rec);
                if(ref)
                {
                    getAR(rec);
                    writer.println("mov esi, dword ptr [esi"+rec.getOffset()+"]");
                    writer.println(mov+" "+register+", "+size+" ptr [esi]");
                }
                else
                {
                    getAR(rec);
                    writer.println(mov+" "+register+", "+size+" ptr [esi"+rec.getOffset()+"]");
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
            //writer.println("lea "+register+", byte ptr [ebp"+rec.getOffset()+"]");//lea R, byte ptr a
            writer.println("mov "+register+", dword ptr [ebp"+rec.getOffset()+"]");//lea R, byte ptr a
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
                    writer.println("mov "+register+", dword ptr [ebp"+rec.getOffset()+"]");
                }
                else
                {
                    writer.println("lea "+register+", "+size+" ptr [ebp"+rec.getOffset()+"]");
                }

            }
            else //non local
            {
                ref = hasRef(rec);
                if(ref)
                {
                    getAR(rec);
                    writer.println("mov "+register+", "+size+" ptr [esi"+rec.getOffset()+"]");
                }
                else
                {
                    getAR(rec);
                    writer.println("lea "+register+", "+size+" ptr [esi"+rec.getOffset()+"]");
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

            writer.println("mov "+size+" ptr [edi], "+register);
        }
        else //variables
        {
            boolean ref;

            if(operator.equals("$$"))
            {
                writer.println("mov esi, dword ptr [ebp+12]");
                writer.println("mov dword ptr [esi], "+register);
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
                    writer.println("mov esi, dword ptr [ebp"+rec.getOffset()+"]");
                    writer.println("mov "+size+" ptr [esi], "+register);
                }
                else
                {
                    writer.println("mov "+size+" ptr [ebp"+rec.getOffset()+"], "+register); //mov R, size ptr [bp + offset]
                }
            }
            else //non local
            {
                ref = hasRef(rec);
                if(ref)
                {
                    getAR(rec);
                    writer.println("mov esi, dword ptr [esi"+rec.getOffset()+"]");
                    writer.println("mov "+size+" ptr [esi],"+register);
                }
                else
                {
                    getAR(rec);
                    writer.println("mov "+size+" ptr [esi"+rec.getOffset()+"], "+register);
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

    public boolean isString(String operator)
    {
        if(operator.charAt(0) == '\"') return true;
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
        try
        {
            FileReader fileReader = new FileReader("libraryFunctions.s");
            BufferedReader br = new BufferedReader(fileReader);
            String line;

            while ((line = br.readLine()) != null) {
                writer.println(line);
            }


        }
        catch(Exception e)
        {

        }


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
