
package compiler;

import compiler.lexer.Lexer;
import compiler.node.*;
import java.io.PushbackReader;
import java.io.InputStreamReader;

public class Main {

        public static void main(String args[]) {
                PushbackReader reader = new PushbackReader(new InputStreamReader(System.in));
                Lexer lexer = new Lexer(reader);

                for(;;) {
                        try {
                                Token t = lexer.next();

                        if (t instanceof EOF)
                                break;
                        System.out.println("token "+t.toString());
                        } catch (Exception e)
                        {
                                System.err.println(e.getMessage());
                        }
                }

                System.exit(0);
        }

}
