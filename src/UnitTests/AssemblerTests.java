package UnitTests;

import static org.junit.Assert.*;


import org.junit.Test;

import Assembler.Instruction;
import Assembler.Lexer;
import Assembler.Parser;
import Assembler.Token;

public class AssemblerTests {
    private static void assmblerTester(String input, Token[] tokens, Instruction[] instructions, String[] output)
            throws Exception {
        var lexer = new Lexer(input);
        var lexed = lexer.lex();
        assertEquals(tokens, lexed.toArray());
        var parser = new Parser(lexed);
        var parsed = parser.parse().get();
        assertEquals(instructions, parsed.toArray());
        var out = parsed.stream().map(Instruction::toBitPattern).toArray(); 
        assertEquals(output, out);
    }

}
