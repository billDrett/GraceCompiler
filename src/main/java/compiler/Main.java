
package compiler;

import compiler.lexer.Lexer;
import compiler.node.*;
import java.io.PushbackReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import compiler.parser.*;


public class Main {

        public static void main(String args[])
        {
                boolean optimize = false;
                int noLoops = 100;
                Start tree;
                try
                {
                        if (args.length <2 || args.length > 4)
                        {
                                System.out.println("Most have the input file and output file as argument, optional -optimize NoLoops");
                                return;
                           }


                        InputStream inputStream = new FileInputStream(args[0]);

                        // Create a Parser instance.
                        Parser p =
                                new Parser(
                                        new Lexer(
                                                new PushbackReader(
                                                        new InputStreamReader(inputStream), 1024)));

                        // Parse the input.

                        tree = p.parse();

                        if(args.length >=3) //we want to optimize
                        {
                                optimize = true;
                        }

                        if(args.length ==4) //read the number
                        {
                                try
                                {
                                        noLoops = Integer.parseInt(args[3]);
                                }catch(Exception e)
                                {
                                        return;
                                }
                        }

                        tree.apply(new Semantic(args[1], optimize, noLoops)); //do semantic analysis

                }
                catch(Exception e)
                {
                        e.printStackTrace();
                }

        }

}
