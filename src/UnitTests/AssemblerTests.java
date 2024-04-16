package UnitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import Assembler.Instruction;
import Assembler.Lexer;
import Assembler.Parser;
import Assembler.Token;

public class AssemblerTests {
    private static void assemblerTester(String input, Token[] tokens, Instruction[] instructions, String[] output)
            throws Exception {
        var lexer = new Lexer(input);
        var lexed = lexer.lex();
        assertArrayEquals(tokens, lexed.toArray());
        var parser = new Parser(lexed);
        var parsed = parser.parse().get();
        assertArrayEquals(instructions, parsed.toArray());
        var out = parsed.stream().map(Instruction::toBitPattern).toArray();
        assertArrayEquals(output, out);
    }

    @Test
    public void absTest1() throws Exception {
        assemblerTester(

                """
                        copy R1 100
                        copy R2 31
                        math rshift R1 R2 R3
                        math xor R1 R3 R4
                        math sub R4 R3 R5

                        """,
                new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
                        new Token(12, 1, Token.TokenType.VALUE, 100), new Token(12, 1, Token.TokenType.NEWLINE),
                        new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
                        new Token(11, 2, Token.TokenType.VALUE, 31), new Token(11, 2, Token.TokenType.NEWLINE),
                        new Token(1, 3, Token.TokenType.MATH), new Token(6, 3, Token.TokenType.RSHIFT),
                        new Token(13, 3, Token.TokenType.REGISTER, 1), new Token(16, 3, Token.TokenType.REGISTER, 2),
                        new Token(19, 3, Token.TokenType.REGISTER, 3), new Token(21, 3, Token.TokenType.NEWLINE),
                        new Token(1, 4, Token.TokenType.MATH), new Token(6, 4, Token.TokenType.XOR),
                        new Token(10, 4, Token.TokenType.REGISTER, 1), new Token(13, 4, Token.TokenType.REGISTER, 3),
                        new Token(16, 4, Token.TokenType.REGISTER, 4), new Token(18, 4, Token.TokenType.NEWLINE),
                        new Token(1, 5, Token.TokenType.MATH), new Token(6, 5, Token.TokenType.SUBTRACT),
                        new Token(10, 5, Token.TokenType.REGISTER, 4), new Token(13, 5, Token.TokenType.REGISTER, 3),
                        new Token(16, 5, Token.TokenType.REGISTER, 5), new Token(18, 5, Token.TokenType.NEWLINE), },
                new Instruction[] {
                        Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(100)
                                .operation(Instruction.Operation.NOP)
                                .registers(new Instruction.RegisterFormat.DestOnly(
                                        new Instruction.RegisterFormat.Register(1))),
                        Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(31)
                                .operation(Instruction.Operation.NOP)
                                .registers(new Instruction.RegisterFormat.DestOnly(
                                        new Instruction.RegisterFormat.Register(2))),
                        Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
                                .operation(Instruction.Operation.RIGHT_SHIFT)
                                .registers(new Instruction.RegisterFormat.ThreeR(
                                        new Instruction.RegisterFormat.Register(1),
                                        new Instruction.RegisterFormat.Register(2),
                                        new Instruction.RegisterFormat.Register(3))),
                        Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
                                .operation(Instruction.Operation.XOR)
                                .registers(new Instruction.RegisterFormat.ThreeR(
                                        new Instruction.RegisterFormat.Register(1),
                                        new Instruction.RegisterFormat.Register(3),
                                        new Instruction.RegisterFormat.Register(4))),
                        Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
                                .operation(Instruction.Operation.SUB)
                                .registers(new Instruction.RegisterFormat.ThreeR(
                                        new Instruction.RegisterFormat.Register(4),
                                        new Instruction.RegisterFormat.Register(3),
                                        new Instruction.RegisterFormat.Register(5))), },
                new String[] { "00000000000110010001100000100001", "00000000000001111101100001000001",
                        "00000000000010001011010001100010", "00000000000010001110100010000010",
                        "00000000001000001111110010100010", }

        );
    }
}
