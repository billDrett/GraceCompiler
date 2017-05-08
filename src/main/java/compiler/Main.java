
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
                Start tree;
                try
                {
                        if (args.length != 1)
                        {
                                System.out.println("Most have the file name as argument");
                                return;
                        }


                        InputStream inputStream = new FileInputStream(args[0]);

                        // Create a Parser instance.
                        Parser p =
                                new Parser(
                                        new Lexer(
                                                new PushbackReader(
                                                        new InputStreamReader(inputStream), 1024)));

                        // Create a Parser instance.
                 /*       Parser p =
                                new Parser(
                                        new Lexer(
                                                new PushbackReader(
                                                        new InputStreamReader(System.in), 1024)));
                        */

                        // Parse the input.

                        tree = p.parse();

                        tree.apply(new Print());

                }
                catch(Exception e)
                {
                        e.printStackTrace();
                }
/*
                SymbolTable sm = new SymbolTable();
                sm.enter();
                System.out.println(sm.insert(new Record("id1", "Int", "Variable")));
              //  sm.enter();
                System.out.println(sm.insert(new Record("id1", "Char", "Variable")));

                Record rec = sm.lookup("id1");
                System.out.println(rec.getType());
*/

        }

}
