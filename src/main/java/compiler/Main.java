
package compiler;

import compiler.lexer.Lexer;
import compiler.node.*;
import java.io.PushbackReader;
import java.io.InputStreamReader;
import compiler.parser.*;


public class Main {

        public static void main(String args[])
        {
                Start tree;
                try
                {
                        System.out.println("Type an expression:");


                        // Create a Parser instance.
                        Parser p =
                                new Parser(
                                        new Lexer(
                                                new PushbackReader(
                                                        new InputStreamReader(System.in), 1024)));


                        // Parse the input.

                        tree = p.parse();

                        System.out.println("START");
                        System.out.println(tree.toString());
                        System.out.println("END");

                        tree.apply(new Print());

                }
                catch(Exception e)
                {
                        e.printStackTrace();
                }


        }

}
