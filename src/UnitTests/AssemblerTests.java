package UnitTests;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import Assembler.AssemblerException;
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
	public void abs() throws Exception {
		assemblerTester("""
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
						"00000000001000001111110010100010", });
	}

	@Test
	public void and() throws Exception {
		assemblerTester("""
				copy R1 10
				copy R2 9
				copy R3 8
				copy R4 7
				copy R5 6
				copy R6 5
				copy R7 4
				copy R8 3
				copy R9 2
				copy R10 1

				math and R10 R10 R10
				math and R10 R9
				math and R10 R8
				math and R10 R7
				math and R10 R6
				math and R10 R5
				math and R10 R4
				math and R10 R3
				math and R10 R2
				math and R10 R1
				""", new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
				new Token(11, 1, Token.TokenType.VALUE, 10), new Token(11, 1, Token.TokenType.NEWLINE),
				new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
				new Token(10, 2, Token.TokenType.VALUE, 9), new Token(10, 2, Token.TokenType.NEWLINE),
				new Token(1, 3, Token.TokenType.COPY), new Token(6, 3, Token.TokenType.REGISTER, 3),
				new Token(10, 3, Token.TokenType.VALUE, 8), new Token(10, 3, Token.TokenType.NEWLINE),
				new Token(1, 4, Token.TokenType.COPY), new Token(6, 4, Token.TokenType.REGISTER, 4),
				new Token(10, 4, Token.TokenType.VALUE, 7), new Token(10, 4, Token.TokenType.NEWLINE),
				new Token(1, 5, Token.TokenType.COPY), new Token(6, 5, Token.TokenType.REGISTER, 5),
				new Token(10, 5, Token.TokenType.VALUE, 6), new Token(10, 5, Token.TokenType.NEWLINE),
				new Token(1, 6, Token.TokenType.COPY), new Token(6, 6, Token.TokenType.REGISTER, 6),
				new Token(10, 6, Token.TokenType.VALUE, 5), new Token(10, 6, Token.TokenType.NEWLINE),
				new Token(1, 7, Token.TokenType.COPY), new Token(6, 7, Token.TokenType.REGISTER, 7),
				new Token(10, 7, Token.TokenType.VALUE, 4), new Token(10, 7, Token.TokenType.NEWLINE),
				new Token(1, 8, Token.TokenType.COPY), new Token(6, 8, Token.TokenType.REGISTER, 8),
				new Token(10, 8, Token.TokenType.VALUE, 3), new Token(10, 8, Token.TokenType.NEWLINE),
				new Token(1, 9, Token.TokenType.COPY), new Token(6, 9, Token.TokenType.REGISTER, 9),
				new Token(10, 9, Token.TokenType.VALUE, 2), new Token(10, 9, Token.TokenType.NEWLINE),
				new Token(1, 10, Token.TokenType.COPY), new Token(6, 10, Token.TokenType.REGISTER, 10),
				new Token(11, 10, Token.TokenType.VALUE, 1), new Token(11, 10, Token.TokenType.NEWLINE),
				new Token(1, 12, Token.TokenType.MATH), new Token(6, 12, Token.TokenType.AND),
				new Token(10, 12, Token.TokenType.REGISTER, 10), new Token(14, 12, Token.TokenType.REGISTER, 10),
				new Token(18, 12, Token.TokenType.REGISTER, 10), new Token(21, 12, Token.TokenType.NEWLINE),
				new Token(1, 13, Token.TokenType.MATH), new Token(6, 13, Token.TokenType.AND),
				new Token(10, 13, Token.TokenType.REGISTER, 10), new Token(14, 13, Token.TokenType.REGISTER, 9),
				new Token(16, 13, Token.TokenType.NEWLINE), new Token(1, 14, Token.TokenType.MATH),
				new Token(6, 14, Token.TokenType.AND), new Token(10, 14, Token.TokenType.REGISTER, 10),
				new Token(14, 14, Token.TokenType.REGISTER, 8), new Token(16, 14, Token.TokenType.NEWLINE),
				new Token(1, 15, Token.TokenType.MATH), new Token(6, 15, Token.TokenType.AND),
				new Token(10, 15, Token.TokenType.REGISTER, 10), new Token(14, 15, Token.TokenType.REGISTER, 7),
				new Token(16, 15, Token.TokenType.NEWLINE), new Token(1, 16, Token.TokenType.MATH),
				new Token(6, 16, Token.TokenType.AND), new Token(10, 16, Token.TokenType.REGISTER, 10),
				new Token(14, 16, Token.TokenType.REGISTER, 6), new Token(16, 16, Token.TokenType.NEWLINE),
				new Token(1, 17, Token.TokenType.MATH), new Token(6, 17, Token.TokenType.AND),
				new Token(10, 17, Token.TokenType.REGISTER, 10), new Token(14, 17, Token.TokenType.REGISTER, 5),
				new Token(16, 17, Token.TokenType.NEWLINE), new Token(1, 18, Token.TokenType.MATH),
				new Token(6, 18, Token.TokenType.AND), new Token(10, 18, Token.TokenType.REGISTER, 10),
				new Token(14, 18, Token.TokenType.REGISTER, 4), new Token(16, 18, Token.TokenType.NEWLINE),
				new Token(1, 19, Token.TokenType.MATH), new Token(6, 19, Token.TokenType.AND),
				new Token(10, 19, Token.TokenType.REGISTER, 10), new Token(14, 19, Token.TokenType.REGISTER, 3),
				new Token(16, 19, Token.TokenType.NEWLINE), new Token(1, 20, Token.TokenType.MATH),
				new Token(6, 20, Token.TokenType.AND), new Token(10, 20, Token.TokenType.REGISTER, 10),
				new Token(14, 20, Token.TokenType.REGISTER, 2), new Token(16, 20, Token.TokenType.NEWLINE),
				new Token(1, 21, Token.TokenType.MATH), new Token(6, 21, Token.TokenType.AND),
				new Token(10, 21, Token.TokenType.REGISTER, 10), new Token(14, 21, Token.TokenType.REGISTER, 1),
				new Token(16, 21, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(10)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(9)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(8)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(7)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(6)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(5))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(6))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(4)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(7))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(8))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(9))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(10))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(10),
										new Instruction.RegisterFormat.Register(10),
										new Instruction.RegisterFormat.Register(10))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(10),
												new Instruction.RegisterFormat.Register(9))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(10),
												new Instruction.RegisterFormat.Register(8))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(10),
												new Instruction.RegisterFormat.Register(7))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(10),
												new Instruction.RegisterFormat.Register(6))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(10),
												new Instruction.RegisterFormat.Register(5))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(10),
												new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(10),
												new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(10),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(10),
												new Instruction.RegisterFormat.Register(1))), },
				new String[] { "00000000000000101001100000100001", "00000000000000100101100001000001",
						"00000000000000100001100001100001", "00000000000000011101100010000001",
						"00000000000000011001100010100001", "00000000000000010101100011000001",
						"00000000000000010001100011100001", "00000000000000001101100100000001",
						"00000000000000001001100100100001", "00000000000000000101100101000001",
						"00000000010100101010000101000010", "00000000000000101010000100100011",
						"00000000000000101010000100000011", "00000000000000101010000011100011",
						"00000000000000101010000011000011", "00000000000000101010000010100011",
						"00000000000000101010000010000011", "00000000000000101010000001100011",
						"00000000000000101010000001000011", "00000000000000101010000000100011", });
	}

	@Test
	public void average1() throws Exception {
		assemblerTester("""
				copy R1 5
				copy R2 7
				math add R1 R2 R3
				copy R4 1
				math rshift R3 R4 R3

				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 5), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, 7), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.MATH), new Token(6, 3, Token.TokenType.ADD),
						new Token(10, 3, Token.TokenType.REGISTER, 1), new Token(13, 3, Token.TokenType.REGISTER, 2),
						new Token(16, 3, Token.TokenType.REGISTER, 3), new Token(18, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.COPY), new Token(6, 4, Token.TokenType.REGISTER, 4),
						new Token(10, 4, Token.TokenType.VALUE, 1), new Token(10, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.MATH), new Token(6, 5, Token.TokenType.RSHIFT),
						new Token(13, 5, Token.TokenType.REGISTER, 3), new Token(16, 5, Token.TokenType.REGISTER, 4),
						new Token(19, 5, Token.TokenType.REGISTER, 3), new Token(21, 5, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(7)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.RIGHT_SHIFT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(3))), },
				new String[] { "00000000000000010101100000100001", "00000000000000011101100001000001",
						"00000000000010001011100001100010", "00000000000000000101100010000001",
						"00000000000110010011010001100010", });
	}

	@Test
	public void average2() throws Exception {
		assemblerTester("""
				copy R1 5
				copy R2 7
				math xor R1 R2 R3
				copy R4 1
				math rshift R3 R4 R3
				math and R1 R2 R4
				math add R3 R4 R3

				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 5), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, 7), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.MATH), new Token(6, 3, Token.TokenType.XOR),
						new Token(10, 3, Token.TokenType.REGISTER, 1), new Token(13, 3, Token.TokenType.REGISTER, 2),
						new Token(16, 3, Token.TokenType.REGISTER, 3), new Token(18, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.COPY), new Token(6, 4, Token.TokenType.REGISTER, 4),
						new Token(10, 4, Token.TokenType.VALUE, 1), new Token(10, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.MATH), new Token(6, 5, Token.TokenType.RSHIFT),
						new Token(13, 5, Token.TokenType.REGISTER, 3), new Token(16, 5, Token.TokenType.REGISTER, 4),
						new Token(19, 5, Token.TokenType.REGISTER, 3), new Token(21, 5, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.MATH), new Token(6, 6, Token.TokenType.AND),
						new Token(10, 6, Token.TokenType.REGISTER, 1), new Token(13, 6, Token.TokenType.REGISTER, 2),
						new Token(16, 6, Token.TokenType.REGISTER, 4), new Token(18, 6, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.MATH), new Token(6, 7, Token.TokenType.ADD),
						new Token(10, 7, Token.TokenType.REGISTER, 3), new Token(13, 7, Token.TokenType.REGISTER, 4),
						new Token(16, 7, Token.TokenType.REGISTER, 3), new Token(18, 7, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(7)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.XOR)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.RIGHT_SHIFT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.AND)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(3))), },
				new String[] { "00000000000000010101100000100001", "00000000000000011101100001000001",
						"00000000000010001010100001100010", "00000000000000000101100010000001",
						"00000000000110010011010001100010", "00000000000010001010000010000010",
						"00000000000110010011100001100010", });
	}

	@Test
	public void branch1() throws Exception {
		assemblerTester("""
				copy R1 -1
				copy R2 -1
				branch eq R1 R2 2
				copy R3 3
				math add R3 R1
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, -1), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, -1), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.BRANCH), new Token(8, 3, Token.TokenType.EQUAL),
						new Token(11, 3, Token.TokenType.REGISTER, 1), new Token(14, 3, Token.TokenType.REGISTER, 2),
						new Token(18, 3, Token.TokenType.VALUE, 2), new Token(18, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.COPY), new Token(6, 4, Token.TokenType.REGISTER, 3),
						new Token(10, 4, Token.TokenType.VALUE, 3), new Token(10, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.MATH), new Token(6, 5, Token.TokenType.ADD),
						new Token(10, 5, Token.TokenType.REGISTER, 3), new Token(13, 5, Token.TokenType.REGISTER, 1),
						new Token(15, 5, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(-1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(-1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(2)
								.operation(Instruction.Operation.EQ).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(3),
												new Instruction.RegisterFormat.Register(1))), },
				new String[] { "11111111111111111101100000100001", "11111111111111111101100001000001",
						"00000000000100000100000001000111", "00000000000000001101100001100001",
						"00000000000000001111100000100011", });
	}

	@Test
	public void branch2() throws Exception {
		assemblerTester("""
				copy R1 7
				copy R2 8
				branch gt R1 R2 R0 3
				copy R3 -1
				math mul R1 R3 R4
				halt
				copy R4 5
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 7), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, 8), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.BRANCH), new Token(8, 3, Token.TokenType.GREATER),
						new Token(11, 3, Token.TokenType.REGISTER, 1), new Token(14, 3, Token.TokenType.REGISTER, 2),
						new Token(17, 3, Token.TokenType.REGISTER, 0), new Token(21, 3, Token.TokenType.VALUE, 3),
						new Token(21, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.COPY),
						new Token(6, 4, Token.TokenType.REGISTER, 3), new Token(10, 4, Token.TokenType.VALUE, -1),
						new Token(10, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.MATH),
						new Token(6, 5, Token.TokenType.MULTIPLY), new Token(10, 5, Token.TokenType.REGISTER, 1),
						new Token(13, 5, Token.TokenType.REGISTER, 3), new Token(16, 5, Token.TokenType.REGISTER, 4),
						new Token(18, 5, Token.TokenType.NEWLINE), new Token(1, 6, Token.TokenType.HALT),
						new Token(5, 6, Token.TokenType.NEWLINE), new Token(1, 7, Token.TokenType.COPY),
						new Token(6, 7, Token.TokenType.REGISTER, 4), new Token(10, 7, Token.TokenType.VALUE, 5),
						new Token(10, 7, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(7)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(8)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(3)
								.operation(Instruction.Operation.GT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(0))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(-1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.MUL)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))), },
				new String[] { "00000000000000011101100000100001", "00000000000000100001100001000001",
						"00000011000010001001000000000110", "11111111111111111101100001100001",
						"00000000000010001101110010000010", "00000000000000000000000000000000",
						"00000000000000010101100010000001", });
	}

	@Test
	public void call1() throws Exception {
		assemblerTester("""
				copy R1 5
				call 3
				halt
				math mul R1 R1 R31
				return
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 5), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.CALL), new Token(7, 2, Token.TokenType.VALUE, 3),
						new Token(7, 2, Token.TokenType.NEWLINE), new Token(1, 3, Token.TokenType.HALT),
						new Token(5, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.MATH),
						new Token(6, 4, Token.TokenType.MULTIPLY), new Token(10, 4, Token.TokenType.REGISTER, 1),
						new Token(13, 4, Token.TokenType.REGISTER, 1), new Token(16, 4, Token.TokenType.REGISTER, 31),
						new Token(19, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.RETURN),
						new Token(7, 5, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(3)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.MUL)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(31))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000010101100000100001", "00000000000000000000000001101000",
						"00000000000000000000000000000000", "00000000000010000101111111100010",
						"00000000000000000000000000010000", });
	}

	@Test
	public void call2() throws Exception {
		assemblerTester("""
				copy R1 3
				copy R2 4
				call R1 1
				halt
				math sub R1 R2 R31
				return
				math add R1 R1
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 3), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, 4), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.CALL), new Token(6, 3, Token.TokenType.REGISTER, 1),
						new Token(10, 3, Token.TokenType.VALUE, 1), new Token(10, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.HALT), new Token(5, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.MATH), new Token(6, 5, Token.TokenType.SUBTRACT),
						new Token(10, 5, Token.TokenType.REGISTER, 1), new Token(13, 5, Token.TokenType.REGISTER, 2),
						new Token(16, 5, Token.TokenType.REGISTER, 31), new Token(19, 5, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.RETURN), new Token(7, 6, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.MATH), new Token(6, 7, Token.TokenType.ADD),
						new Token(10, 7, Token.TokenType.REGISTER, 1), new Token(13, 7, Token.TokenType.REGISTER, 1),
						new Token(15, 7, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(4)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(31))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(1))), },
				new String[] { "00000000000000001101100000100001", "00000000000000010001100001000001",
						"00000000000000000101100000101001", "00000000000000000000000000000000",
						"00000000000010001011111111100010", "00000000000000000000000000010000",
						"00000000000000000111100000100011", });
	}

	@Test
	public void call3() throws Exception {
		assemblerTester("""
				copy R1 5
				copy R2 6
				callif ne R1 R2 2
				math not R31 R0 R3
				halt
				copy R3 2
				math lshift R1 R3 R31
				return
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 5), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, 6), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.CALLIF), new Token(8, 3, Token.TokenType.UNEQUAL),
						new Token(11, 3, Token.TokenType.REGISTER, 1), new Token(14, 3, Token.TokenType.REGISTER, 2),
						new Token(18, 3, Token.TokenType.VALUE, 2), new Token(18, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.MATH), new Token(6, 4, Token.TokenType.NOT),
						new Token(10, 4, Token.TokenType.REGISTER, 31), new Token(14, 4, Token.TokenType.REGISTER, 0),
						new Token(17, 4, Token.TokenType.REGISTER, 3), new Token(19, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.HALT), new Token(5, 5, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.COPY), new Token(6, 6, Token.TokenType.REGISTER, 3),
						new Token(10, 6, Token.TokenType.VALUE, 2), new Token(10, 6, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.MATH), new Token(6, 7, Token.TokenType.LSHIFT),
						new Token(13, 7, Token.TokenType.REGISTER, 1), new Token(16, 7, Token.TokenType.REGISTER, 3),
						new Token(19, 7, Token.TokenType.REGISTER, 31), new Token(22, 7, Token.TokenType.NEWLINE),
						new Token(1, 8, Token.TokenType.RETURN), new Token(7, 8, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(6)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(2)
								.operation(Instruction.Operation.NE).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(31),
										new Instruction.RegisterFormat.Register(0),
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.LEFT_SHIFT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(31))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000010101100000100001", "00000000000000011001100001000001",
						"00000000000100000100010001001011", "00000000111110000010110001100010",
						"00000000000000000000000000000000", "00000000000000001001100001100001",
						"00000000000010001111001111100010", "00000000000000000000000000010000", });
	}

	@Test
	public void call4() throws Exception {
		assemblerTester("""
				copy R1 1
				copy R2 -3
				callif lt R2 R1 R1 4
				math or R31 R2
				halt
				math not R2 R0 R31
				return
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 1), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, -3), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.CALLIF), new Token(8, 3, Token.TokenType.LESS),
						new Token(11, 3, Token.TokenType.REGISTER, 2), new Token(14, 3, Token.TokenType.REGISTER, 1),
						new Token(17, 3, Token.TokenType.REGISTER, 1), new Token(21, 3, Token.TokenType.VALUE, 4),
						new Token(21, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.MATH),
						new Token(6, 4, Token.TokenType.OR), new Token(9, 4, Token.TokenType.REGISTER, 31),
						new Token(13, 4, Token.TokenType.REGISTER, 2), new Token(15, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.HALT), new Token(5, 5, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.MATH), new Token(6, 6, Token.TokenType.NOT),
						new Token(10, 6, Token.TokenType.REGISTER, 2), new Token(13, 6, Token.TokenType.REGISTER, 0),
						new Token(16, 6, Token.TokenType.REGISTER, 31), new Token(19, 6, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.RETURN), new Token(7, 7, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(-3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(4)
								.operation(Instruction.Operation.LT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.OR).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(31),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(0),
										new Instruction.RegisterFormat.Register(31))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000000101100000100001", "11111111111111110101100001000001",
						"00000100000100000100100000101010", "00000000000001111110010001000011",
						"00000000000000000000000000000000", "00000000000100000010111111100010",
						"00000000000000000000000000010000", });
	}

	@Test
	public void call5() throws Exception {
		assemblerTester("""
				copy R1 1
				copy R2 -27
				callif gt R1 R2 1
				halt
				push xor R1 R2 R0
				copy R3 5
				callif eq R1 R3 R1 2
				pop R4
				return
				copy R5 1
				copy R6 30
				push or R6 R3
				callif eq R5 R1 -8
				pop R7
				return
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 1), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(11, 2, Token.TokenType.VALUE, -27), new Token(11, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.CALLIF), new Token(8, 3, Token.TokenType.GREATER),
						new Token(11, 3, Token.TokenType.REGISTER, 1), new Token(14, 3, Token.TokenType.REGISTER, 2),
						new Token(18, 3, Token.TokenType.VALUE, 1), new Token(18, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.HALT), new Token(5, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.PUSH), new Token(6, 5, Token.TokenType.XOR),
						new Token(10, 5, Token.TokenType.REGISTER, 1), new Token(13, 5, Token.TokenType.REGISTER, 2),
						new Token(16, 5, Token.TokenType.REGISTER, 0), new Token(18, 5, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.COPY), new Token(6, 6, Token.TokenType.REGISTER, 3),
						new Token(10, 6, Token.TokenType.VALUE, 5), new Token(10, 6, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.CALLIF), new Token(8, 7, Token.TokenType.EQUAL),
						new Token(11, 7, Token.TokenType.REGISTER, 1), new Token(14, 7, Token.TokenType.REGISTER, 3),
						new Token(17, 7, Token.TokenType.REGISTER, 1), new Token(21, 7, Token.TokenType.VALUE, 2),
						new Token(21, 7, Token.TokenType.NEWLINE), new Token(1, 8, Token.TokenType.POP),
						new Token(5, 8, Token.TokenType.REGISTER, 4), new Token(7, 8, Token.TokenType.NEWLINE),
						new Token(1, 9, Token.TokenType.RETURN), new Token(7, 9, Token.TokenType.NEWLINE),
						new Token(1, 10, Token.TokenType.COPY), new Token(6, 10, Token.TokenType.REGISTER, 5),
						new Token(10, 10, Token.TokenType.VALUE, 1), new Token(10, 10, Token.TokenType.NEWLINE),
						new Token(1, 11, Token.TokenType.COPY), new Token(6, 11, Token.TokenType.REGISTER, 6),
						new Token(11, 11, Token.TokenType.VALUE, 30), new Token(11, 11, Token.TokenType.NEWLINE),
						new Token(1, 12, Token.TokenType.PUSH), new Token(6, 12, Token.TokenType.OR),
						new Token(9, 12, Token.TokenType.REGISTER, 6), new Token(12, 12, Token.TokenType.REGISTER, 3),
						new Token(14, 12, Token.TokenType.NEWLINE), new Token(1, 13, Token.TokenType.CALLIF),
						new Token(8, 13, Token.TokenType.EQUAL), new Token(11, 13, Token.TokenType.REGISTER, 5),
						new Token(14, 13, Token.TokenType.REGISTER, 1), new Token(18, 13, Token.TokenType.VALUE, -8),
						new Token(18, 13, Token.TokenType.NEWLINE), new Token(1, 14, Token.TokenType.POP),
						new Token(5, 14, Token.TokenType.REGISTER, 7), new Token(7, 14, Token.TokenType.NEWLINE),
						new Token(1, 15, Token.TokenType.RETURN), new Token(7, 15, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(-27)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(1)
								.operation(Instruction.Operation.GT).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.XOR)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(0))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(2)
								.operation(Instruction.Operation.EQ)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(5))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(30)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(6))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.OR).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(6),
												new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(-8)
								.operation(Instruction.Operation.EQ).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(5),
												new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(7))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000000101100000100001", "11111111111110010101100001000001",
						"00000000000010000101000001001011", "00000000000000000000000000000000",
						"00000000000010001010100000001110", "00000000000000010101100001100001",
						"00000010000010001100000000101010", "00000000000000000001100010011001",
						"00000000000000000000000000010000", "00000000000000000101100010100001",
						"00000000000001111001100011000001", "00000000000000011010010001101111",
						"11111111110000010100000000101011", "00000000000000000001100011111001",
						"00000000000000000000000000010000", });
	}

	@Test
	public void callandbranch() throws Exception {
		assemblerTester("""
				copy R1 45
				copy R2 67
				branch le R1 R2 R0 1
				halt
				copy R3 67
				callif ge R2 R2 R0 8
				math add R4 R1 R5
				halt
				copy R6 2
				push add R2 R6 R31
				callif gt R2 R2 R21 5
				pop R4
				return
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(11, 1, Token.TokenType.VALUE, 45), new Token(11, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(11, 2, Token.TokenType.VALUE, 67), new Token(11, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.BRANCH), new Token(8, 3, Token.TokenType.LESSOREQUAL),
						new Token(11, 3, Token.TokenType.REGISTER, 1), new Token(14, 3, Token.TokenType.REGISTER, 2),
						new Token(17, 3, Token.TokenType.REGISTER, 0), new Token(21, 3, Token.TokenType.VALUE, 1),
						new Token(21, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.HALT),
						new Token(5, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.COPY),
						new Token(6, 5, Token.TokenType.REGISTER, 3), new Token(11, 5, Token.TokenType.VALUE, 67),
						new Token(11, 5, Token.TokenType.NEWLINE), new Token(1, 6, Token.TokenType.CALLIF),
						new Token(8, 6, Token.TokenType.GREATEROREQUAL), new Token(11, 6, Token.TokenType.REGISTER, 2),
						new Token(14, 6, Token.TokenType.REGISTER, 2), new Token(17, 6, Token.TokenType.REGISTER, 0),
						new Token(21, 6, Token.TokenType.VALUE, 8), new Token(21, 6, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.MATH), new Token(6, 7, Token.TokenType.ADD),
						new Token(10, 7, Token.TokenType.REGISTER, 4), new Token(13, 7, Token.TokenType.REGISTER, 1),
						new Token(16, 7, Token.TokenType.REGISTER, 5), new Token(18, 7, Token.TokenType.NEWLINE),
						new Token(1, 8, Token.TokenType.HALT), new Token(5, 8, Token.TokenType.NEWLINE),
						new Token(1, 9, Token.TokenType.COPY), new Token(6, 9, Token.TokenType.REGISTER, 6),
						new Token(10, 9, Token.TokenType.VALUE, 2), new Token(10, 9, Token.TokenType.NEWLINE),
						new Token(1, 10, Token.TokenType.PUSH), new Token(6, 10, Token.TokenType.ADD),
						new Token(10, 10, Token.TokenType.REGISTER, 2), new Token(13, 10, Token.TokenType.REGISTER, 6),
						new Token(16, 10, Token.TokenType.REGISTER, 31), new Token(19, 10, Token.TokenType.NEWLINE),
						new Token(1, 11, Token.TokenType.CALLIF), new Token(8, 11, Token.TokenType.GREATER),
						new Token(11, 11, Token.TokenType.REGISTER, 2), new Token(14, 11, Token.TokenType.REGISTER, 2),
						new Token(17, 11, Token.TokenType.REGISTER, 21), new Token(22, 11, Token.TokenType.VALUE, 5),
						new Token(22, 11, Token.TokenType.NEWLINE), new Token(1, 12, Token.TokenType.POP),
						new Token(5, 12, Token.TokenType.REGISTER, 4), new Token(7, 12, Token.TokenType.NEWLINE),
						new Token(1, 13, Token.TokenType.RETURN), new Token(7, 13, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(45)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(67)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(1)
								.operation(Instruction.Operation.LE)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(0))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(67)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(8)
								.operation(Instruction.Operation.GE)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(0))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(5))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(6))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(6),
										new Instruction.RegisterFormat.Register(31))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(5)
								.operation(Instruction.Operation.GT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(21))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000010110101100000100001", "00000000000100001101100001000001",
						"00000001000010001001010000000110", "00000000000000000000000000000000",
						"00000000000100001101100001100001", "00001000000100001000110000001010",
						"00000000001000000111100010100010", "00000000000000000000000000000000",
						"00000000000000001001100011000001", "00000000000100011011101111101110",
						"00000101000100001001001010101010", "00000000000000000001100010011001",
						"00000000000000000000000000010000", });
	}

	@Test
	public void comments() throws Exception {
		assemblerTester("""
				copy R1 5 ;fff
				;;; eerer
				;gg
				math add R0 R0
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 5), new Token(11, 1, Token.TokenType.NEWLINE),
						new Token(1, 1, Token.TokenType.MATH), new Token(6, 1, Token.TokenType.ADD),
						new Token(10, 1, Token.TokenType.REGISTER, 0), new Token(13, 1, Token.TokenType.REGISTER, 0),
						new Token(15, 1, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(0),
												new Instruction.RegisterFormat.Register(0))), },
				new String[] { "00000000000000010101100000100001", "00000000000000000011100000000011", });
	}

	@Test
	public void factorial1() throws Exception {
		assemblerTester("""
				copy R1 8
				call 3
				halt
				copy R2 1
				branch lt R2 R1 2
				copy R3 1
				return
				push sub R1 0
				math sub R1 R2 R1
				call 4
				pop R1
				math mul R1 R3
				return
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 8), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.CALL), new Token(7, 2, Token.TokenType.VALUE, 3),
						new Token(7, 2, Token.TokenType.NEWLINE), new Token(1, 3, Token.TokenType.HALT),
						new Token(5, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.COPY),
						new Token(6, 4, Token.TokenType.REGISTER, 2), new Token(10, 4, Token.TokenType.VALUE, 1),
						new Token(10, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.BRANCH),
						new Token(8, 5, Token.TokenType.LESS), new Token(11, 5, Token.TokenType.REGISTER, 2),
						new Token(14, 5, Token.TokenType.REGISTER, 1), new Token(18, 5, Token.TokenType.VALUE, 2),
						new Token(18, 5, Token.TokenType.NEWLINE), new Token(1, 6, Token.TokenType.COPY),
						new Token(6, 6, Token.TokenType.REGISTER, 3), new Token(10, 6, Token.TokenType.VALUE, 1),
						new Token(10, 6, Token.TokenType.NEWLINE), new Token(1, 7, Token.TokenType.RETURN),
						new Token(7, 7, Token.TokenType.NEWLINE), new Token(1, 8, Token.TokenType.PUSH),
						new Token(6, 8, Token.TokenType.SUBTRACT), new Token(10, 8, Token.TokenType.REGISTER, 1),
						new Token(14, 8, Token.TokenType.VALUE, 0), new Token(14, 8, Token.TokenType.NEWLINE),
						new Token(1, 9, Token.TokenType.MATH), new Token(6, 9, Token.TokenType.SUBTRACT),
						new Token(10, 9, Token.TokenType.REGISTER, 1), new Token(13, 9, Token.TokenType.REGISTER, 2),
						new Token(16, 9, Token.TokenType.REGISTER, 1), new Token(18, 9, Token.TokenType.NEWLINE),
						new Token(1, 10, Token.TokenType.CALL), new Token(7, 10, Token.TokenType.VALUE, 4),
						new Token(7, 10, Token.TokenType.NEWLINE), new Token(1, 11, Token.TokenType.POP),
						new Token(5, 11, Token.TokenType.REGISTER, 1), new Token(7, 11, Token.TokenType.NEWLINE),
						new Token(1, 12, Token.TokenType.MATH), new Token(6, 12, Token.TokenType.MULTIPLY),
						new Token(10, 12, Token.TokenType.REGISTER, 1), new Token(13, 12, Token.TokenType.REGISTER, 3),
						new Token(15, 12, Token.TokenType.NEWLINE), new Token(1, 13, Token.TokenType.RETURN),
						new Token(7, 13, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(8)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(3)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(2)
								.operation(Instruction.Operation.LT).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(2),
												new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(4)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.MUL).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000100001100000100001", "00000000000000000000000001101000",
						"00000000000000000000000000000000", "00000000000000000101100001000001",
						"00000000000100001000100000100111", "00000000000000000101100001100001",
						"00000000000000000000000000010000", "00000000000000000011110000101101",
						"00000000000010001011110000100010", "00000000000000000000000010001000",
						"00000000000000000001100000111001", "00000000000000000101110001100011",
						"00000000000000000000000000010000", });
	}

	@Test
	public void factorial() throws Exception {
		assemblerTester("""
				copy R1 5
				call 6
				math add R3 R0 R5
				copy R1 7
				call 6
				halt
				copy R2 0
				copy R4 1
				branch ne R1 R2 2
				copy R3 1
				return
				math sub R1 R4 R1
				call 8
				math add R1 R4 R1
				math mul R1 R3
				return
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 5), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.CALL), new Token(7, 2, Token.TokenType.VALUE, 6),
						new Token(7, 2, Token.TokenType.NEWLINE), new Token(1, 3, Token.TokenType.MATH),
						new Token(6, 3, Token.TokenType.ADD), new Token(10, 3, Token.TokenType.REGISTER, 3),
						new Token(13, 3, Token.TokenType.REGISTER, 0), new Token(16, 3, Token.TokenType.REGISTER, 5),
						new Token(18, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.COPY),
						new Token(6, 4, Token.TokenType.REGISTER, 1), new Token(10, 4, Token.TokenType.VALUE, 7),
						new Token(10, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.CALL),
						new Token(7, 5, Token.TokenType.VALUE, 6), new Token(7, 5, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.HALT), new Token(5, 6, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.COPY), new Token(6, 7, Token.TokenType.REGISTER, 2),
						new Token(10, 7, Token.TokenType.VALUE, 0), new Token(10, 7, Token.TokenType.NEWLINE),
						new Token(1, 8, Token.TokenType.COPY), new Token(6, 8, Token.TokenType.REGISTER, 4),
						new Token(10, 8, Token.TokenType.VALUE, 1), new Token(10, 8, Token.TokenType.NEWLINE),
						new Token(1, 9, Token.TokenType.BRANCH), new Token(8, 9, Token.TokenType.UNEQUAL),
						new Token(11, 9, Token.TokenType.REGISTER, 1), new Token(14, 9, Token.TokenType.REGISTER, 2),
						new Token(18, 9, Token.TokenType.VALUE, 2), new Token(18, 9, Token.TokenType.NEWLINE),
						new Token(1, 10, Token.TokenType.COPY), new Token(6, 10, Token.TokenType.REGISTER, 3),
						new Token(10, 10, Token.TokenType.VALUE, 1), new Token(10, 10, Token.TokenType.NEWLINE),
						new Token(1, 11, Token.TokenType.RETURN), new Token(7, 11, Token.TokenType.NEWLINE),
						new Token(1, 12, Token.TokenType.MATH), new Token(6, 12, Token.TokenType.SUBTRACT),
						new Token(10, 12, Token.TokenType.REGISTER, 1), new Token(13, 12, Token.TokenType.REGISTER, 4),
						new Token(16, 12, Token.TokenType.REGISTER, 1), new Token(18, 12, Token.TokenType.NEWLINE),
						new Token(1, 13, Token.TokenType.CALL), new Token(7, 13, Token.TokenType.VALUE, 8),
						new Token(7, 13, Token.TokenType.NEWLINE), new Token(1, 14, Token.TokenType.MATH),
						new Token(6, 14, Token.TokenType.ADD), new Token(10, 14, Token.TokenType.REGISTER, 1),
						new Token(13, 14, Token.TokenType.REGISTER, 4), new Token(16, 14, Token.TokenType.REGISTER, 1),
						new Token(18, 14, Token.TokenType.NEWLINE), new Token(1, 15, Token.TokenType.MATH),
						new Token(6, 15, Token.TokenType.MULTIPLY), new Token(10, 15, Token.TokenType.REGISTER, 1),
						new Token(13, 15, Token.TokenType.REGISTER, 3), new Token(15, 15, Token.TokenType.NEWLINE),
						new Token(1, 16, Token.TokenType.RETURN), new Token(7, 16, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(6)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(0),
										new Instruction.RegisterFormat.Register(5))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(7)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(6)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(2)
								.operation(Instruction.Operation.NE).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(8)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.MUL).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000010101100000100001", "00000000000000000000000011001000",
						"00000000000110000011100010100010", "00000000000000011101100000100001",
						"00000000000000000000000011001000", "00000000000000000000000000000000",
						"00000000000000000001100001000001", "00000000000000000101100010000001",
						"00000000000100000100010001000111", "00000000000000000101100001100001",
						"00000000000000000000000000010000", "00000000000010010011110000100010",
						"00000000000000000000000100001000", "00000000000010010011100000100010",
						"00000000000000000101110001100011", "00000000000000000000000000010000", });
	}

	@Test
	public void fibannocci1() throws Exception {
		assemblerTester("""
				copy R1 10 ; set paramter to 10
				call 3 ; fib 4
				halt ; program done
				; fib start x = R1
				copy R2 2
				copy R4 1
				branch ge R1 R2 2 ; if x >= 2
				; x < 2
				math add R1 R3 ; result += x
				return
				; x > 1
				push add R1 0 ; save x
				math sub R1 R4 R1 ; x--
				call 5 ; call x (x - 1)
				math sub R1 R4 R1 ; x--
				call 5 ; call x (x - 2)
				pop R1 ; restore x
				return
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(11, 1, Token.TokenType.VALUE, 10), new Token(12, 1, Token.TokenType.NEWLINE),
						new Token(1, 1, Token.TokenType.CALL), new Token(7, 1, Token.TokenType.VALUE, 3),
						new Token(8, 1, Token.TokenType.NEWLINE), new Token(1, 1, Token.TokenType.HALT),
						new Token(6, 1, Token.TokenType.NEWLINE), new Token(1, 1, Token.TokenType.COPY),
						new Token(6, 1, Token.TokenType.REGISTER, 2), new Token(10, 1, Token.TokenType.VALUE, 2),
						new Token(10, 1, Token.TokenType.NEWLINE), new Token(1, 2, Token.TokenType.COPY),
						new Token(6, 2, Token.TokenType.REGISTER, 4), new Token(10, 2, Token.TokenType.VALUE, 1),
						new Token(10, 2, Token.TokenType.NEWLINE), new Token(1, 3, Token.TokenType.BRANCH),
						new Token(8, 3, Token.TokenType.GREATEROREQUAL), new Token(11, 3, Token.TokenType.REGISTER, 1),
						new Token(14, 3, Token.TokenType.REGISTER, 2), new Token(18, 3, Token.TokenType.VALUE, 2),
						new Token(19, 3, Token.TokenType.NEWLINE), new Token(1, 3, Token.TokenType.MATH),
						new Token(6, 3, Token.TokenType.ADD), new Token(10, 3, Token.TokenType.REGISTER, 1),
						new Token(13, 3, Token.TokenType.REGISTER, 3), new Token(16, 3, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.RETURN), new Token(7, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.PUSH), new Token(6, 4, Token.TokenType.ADD),
						new Token(10, 4, Token.TokenType.REGISTER, 1), new Token(14, 4, Token.TokenType.VALUE, 0),
						new Token(15, 4, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.MATH),
						new Token(6, 4, Token.TokenType.SUBTRACT), new Token(10, 4, Token.TokenType.REGISTER, 1),
						new Token(13, 4, Token.TokenType.REGISTER, 4), new Token(16, 4, Token.TokenType.REGISTER, 1),
						new Token(19, 4, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.CALL),
						new Token(7, 4, Token.TokenType.VALUE, 5), new Token(8, 4, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.MATH), new Token(6, 4, Token.TokenType.SUBTRACT),
						new Token(10, 4, Token.TokenType.REGISTER, 1), new Token(13, 4, Token.TokenType.REGISTER, 4),
						new Token(16, 4, Token.TokenType.REGISTER, 1), new Token(19, 4, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.CALL), new Token(7, 4, Token.TokenType.VALUE, 5),
						new Token(8, 4, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.POP),
						new Token(5, 4, Token.TokenType.REGISTER, 1), new Token(8, 4, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.RETURN), new Token(7, 4, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(10)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(3)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(2)
								.operation(Instruction.Operation.GE).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(5)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(5)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000101001100000100001", "00000000000000000000000001101000",
						"00000000000000000000000000000000", "00000000000000001001100001000001",
						"00000000000000000101100010000001", "00000000000100000100110001000111",
						"00000000000000000111100001100011", "00000000000000000000000000010000",
						"00000000000000000011100000101101", "00000000000010010011110000100010",
						"00000000000000000000000010101000", "00000000000010010011110000100010",
						"00000000000000000000000010101000", "00000000000000000001100000111001",
						"00000000000000000000000000010000", });
	}

	@Test
	public void fibannocci() throws Exception {
		assemblerTester("""
				copy R1 6
				call 3
				halt
				copy R2 1
				copy R3 2
				branch le R3 R1 2 ;if R2 <= R1
				math add R1 R4 ;r4 += r1
				return
				math sub R1 R2 R1 ; R1--
				call 5 ;fib n -1
				math sub R1 R2 R1 ;R1--
				call 5 ;fib n - 2
				math add R1 R3 R1 ;R1+=2
				return
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 6), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.CALL), new Token(7, 2, Token.TokenType.VALUE, 3),
						new Token(7, 2, Token.TokenType.NEWLINE), new Token(1, 3, Token.TokenType.HALT),
						new Token(5, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.COPY),
						new Token(6, 4, Token.TokenType.REGISTER, 2), new Token(10, 4, Token.TokenType.VALUE, 1),
						new Token(10, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.COPY),
						new Token(6, 5, Token.TokenType.REGISTER, 3), new Token(10, 5, Token.TokenType.VALUE, 2),
						new Token(10, 5, Token.TokenType.NEWLINE), new Token(1, 6, Token.TokenType.BRANCH),
						new Token(8, 6, Token.TokenType.LESSOREQUAL), new Token(11, 6, Token.TokenType.REGISTER, 3),
						new Token(14, 6, Token.TokenType.REGISTER, 1), new Token(18, 6, Token.TokenType.VALUE, 2),
						new Token(19, 6, Token.TokenType.NEWLINE), new Token(1, 6, Token.TokenType.MATH),
						new Token(6, 6, Token.TokenType.ADD), new Token(10, 6, Token.TokenType.REGISTER, 1),
						new Token(13, 6, Token.TokenType.REGISTER, 4), new Token(16, 6, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.RETURN), new Token(7, 6, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.MATH), new Token(6, 7, Token.TokenType.SUBTRACT),
						new Token(10, 7, Token.TokenType.REGISTER, 1), new Token(13, 7, Token.TokenType.REGISTER, 2),
						new Token(16, 7, Token.TokenType.REGISTER, 1), new Token(19, 7, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.CALL), new Token(7, 7, Token.TokenType.VALUE, 5),
						new Token(8, 7, Token.TokenType.NEWLINE), new Token(1, 7, Token.TokenType.MATH),
						new Token(6, 7, Token.TokenType.SUBTRACT), new Token(10, 7, Token.TokenType.REGISTER, 1),
						new Token(13, 7, Token.TokenType.REGISTER, 2), new Token(16, 7, Token.TokenType.REGISTER, 1),
						new Token(19, 7, Token.TokenType.NEWLINE), new Token(1, 7, Token.TokenType.CALL),
						new Token(7, 7, Token.TokenType.VALUE, 5), new Token(8, 7, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.MATH), new Token(6, 7, Token.TokenType.ADD),
						new Token(10, 7, Token.TokenType.REGISTER, 1), new Token(13, 7, Token.TokenType.REGISTER, 3),
						new Token(16, 7, Token.TokenType.REGISTER, 1), new Token(19, 7, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.RETURN), new Token(7, 7, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(6)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(3)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(2)
								.operation(Instruction.Operation.LE).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(3),
												new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(5)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.CALL).immediate(5)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000011001100000100001", "00000000000000000000000001101000",
						"00000000000000000000000000000000", "00000000000000000101100001000001",
						"00000000000000001001100001100001", "00000000000100001101010000100111",
						"00000000000000000111100010000011", "00000000000000000000000000010000",
						"00000000000010001011110000100010", "00000000000000000000000010101000",
						"00000000000010001011110000100010", "00000000000000000000000010101000",
						"00000000000010001111100000100010", "00000000000000000000000000010000", });
	}

	@Test
	public void jump1() throws Exception {
		assemblerTester("""
				jump 3
				copy R1 3
				copy R2 0
				copy R1 1
				""",
				new Token[] { new Token(1, 1, Token.TokenType.JUMP), new Token(7, 1, Token.TokenType.VALUE, 3),
						new Token(7, 1, Token.TokenType.NEWLINE), new Token(1, 2, Token.TokenType.COPY),
						new Token(6, 2, Token.TokenType.REGISTER, 1), new Token(10, 2, Token.TokenType.VALUE, 3),
						new Token(10, 2, Token.TokenType.NEWLINE), new Token(1, 3, Token.TokenType.COPY),
						new Token(6, 3, Token.TokenType.REGISTER, 2), new Token(10, 3, Token.TokenType.VALUE, 0),
						new Token(10, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.COPY),
						new Token(6, 4, Token.TokenType.REGISTER, 1), new Token(10, 4, Token.TokenType.VALUE, 1),
						new Token(10, 4, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(3)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))), },
				new String[] { "00000000000000000000000001100100", "00000000000000001101100000100001",
						"00000000000000000001100001000001", "00000000000000000101100000100001", });
	}

	@Test
	public void jump2() throws Exception {
		assemblerTester("""
				copy R1 5
				jump R0 3
				copy R2 5
				math add R2 R1
				jump R0 2
				copy R3 1
				math add R3 R1
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 5), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.JUMP), new Token(6, 2, Token.TokenType.REGISTER, 0),
						new Token(10, 2, Token.TokenType.VALUE, 3), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.COPY), new Token(6, 3, Token.TokenType.REGISTER, 2),
						new Token(10, 3, Token.TokenType.VALUE, 5), new Token(10, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.MATH), new Token(6, 4, Token.TokenType.ADD),
						new Token(10, 4, Token.TokenType.REGISTER, 2), new Token(13, 4, Token.TokenType.REGISTER, 1),
						new Token(15, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.JUMP),
						new Token(6, 5, Token.TokenType.REGISTER, 0), new Token(10, 5, Token.TokenType.VALUE, 2),
						new Token(10, 5, Token.TokenType.NEWLINE), new Token(1, 6, Token.TokenType.COPY),
						new Token(6, 6, Token.TokenType.REGISTER, 3), new Token(10, 6, Token.TokenType.VALUE, 1),
						new Token(10, 6, Token.TokenType.NEWLINE), new Token(1, 7, Token.TokenType.MATH),
						new Token(6, 7, Token.TokenType.ADD), new Token(10, 7, Token.TokenType.REGISTER, 3),
						new Token(13, 7, Token.TokenType.REGISTER, 1), new Token(15, 7, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(0))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(2),
												new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(0))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(3),
												new Instruction.RegisterFormat.Register(1))), },
				new String[] { "00000000000000010101100000100001", "00000000000000001101100000000101",
						"00000000000000010101100001000001", "00000000000000001011100000100011",
						"00000000000000001001100000000101", "00000000000000000101100001100001",
						"00000000000000001111100000100011", });
	}

	@Test
	public void load1() throws Exception {
		assemblerTester("""
				load R1 2
				halt
				""",
				new Token[] { new Token(1, 1, Token.TokenType.LOAD), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 2), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.HALT), new Token(5, 2, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000001001100000110001", "00000000000000000000000000000000", });
	}

	@Test
	public void load2() throws Exception {
		assemblerTester("""
				copy R1 3
				load R1 R2 1
				halt
				halt
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 3), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.LOAD), new Token(6, 2, Token.TokenType.REGISTER, 1),
						new Token(9, 2, Token.TokenType.REGISTER, 2), new Token(13, 2, Token.TokenType.VALUE, 1),
						new Token(13, 2, Token.TokenType.NEWLINE), new Token(1, 3, Token.TokenType.HALT),
						new Token(5, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.HALT),
						new Token(5, 4, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(1)
								.operation(Instruction.Operation.NOP).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000001101100000100001", "00000000000010000101100001010011",
						"00000000000000000000000000000000", "00000000000000000000000000000000", });
	}

	@Test
	public void load3() throws Exception {
		assemblerTester("""
				copy R1 2
				copy R7 3
				load R1 R7 R31
				halt
				halt
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 2), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 7),
						new Token(10, 2, Token.TokenType.VALUE, 3), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.LOAD), new Token(6, 3, Token.TokenType.REGISTER, 1),
						new Token(9, 3, Token.TokenType.REGISTER, 7), new Token(12, 3, Token.TokenType.REGISTER, 31),
						new Token(15, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.HALT),
						new Token(5, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.HALT),
						new Token(5, 5, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(7))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(7),
										new Instruction.RegisterFormat.Register(31))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.NoR()), },
				new String[] { "00000000000000001001100000100001", "00000000000000001101100011100001",
						"00000000000010011101101111110010", "00000000000000000000000000000000",
						"00000000000000000000000000000000", });
	}

	@Test
	public void max() throws Exception {
		assemblerTester("""
				copy R1 1
				copy R7 31
				math lshift R1 R7 R2
				math not R2 R3

				copy R4 1
				copy R6 31
				math lshift R4 R6 R5
				math sub R5 R4 R6

				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 1), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 7),
						new Token(11, 2, Token.TokenType.VALUE, 31), new Token(11, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.MATH), new Token(6, 3, Token.TokenType.LSHIFT),
						new Token(13, 3, Token.TokenType.REGISTER, 1), new Token(16, 3, Token.TokenType.REGISTER, 7),
						new Token(19, 3, Token.TokenType.REGISTER, 2), new Token(21, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.MATH), new Token(6, 4, Token.TokenType.NOT),
						new Token(10, 4, Token.TokenType.REGISTER, 2), new Token(13, 4, Token.TokenType.REGISTER, 3),
						new Token(15, 4, Token.TokenType.NEWLINE), new Token(1, 6, Token.TokenType.COPY),
						new Token(6, 6, Token.TokenType.REGISTER, 4), new Token(10, 6, Token.TokenType.VALUE, 1),
						new Token(10, 6, Token.TokenType.NEWLINE), new Token(1, 7, Token.TokenType.COPY),
						new Token(6, 7, Token.TokenType.REGISTER, 6), new Token(11, 7, Token.TokenType.VALUE, 31),
						new Token(11, 7, Token.TokenType.NEWLINE), new Token(1, 8, Token.TokenType.MATH),
						new Token(6, 8, Token.TokenType.LSHIFT), new Token(13, 8, Token.TokenType.REGISTER, 4),
						new Token(16, 8, Token.TokenType.REGISTER, 6), new Token(19, 8, Token.TokenType.REGISTER, 5),
						new Token(21, 8, Token.TokenType.NEWLINE), new Token(1, 9, Token.TokenType.MATH),
						new Token(6, 9, Token.TokenType.SUBTRACT), new Token(10, 9, Token.TokenType.REGISTER, 5),
						new Token(13, 9, Token.TokenType.REGISTER, 4), new Token(16, 9, Token.TokenType.REGISTER, 6),
						new Token(18, 9, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(31)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(7))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.LEFT_SHIFT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(7),
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOT).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(2),
												new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(31)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(6))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.LEFT_SHIFT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(6),
										new Instruction.RegisterFormat.Register(5))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(5),
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(6))), },
				new String[] { "00000000000000000101100000100001", "00000000000001111101100011100001",
						"00000000000010011111000001000010", "00000000000000001010110001100011",
						"00000000000000000101100010000001", "00000000000001111101100011000001",
						"00000000001000011011000010100010", "00000000001010010011110011000010", });
	}

	@Test
	public void not() throws Exception {
		assemblerTester("""
				copy R1 1
				copy R2 500

				math not R2 R2
				math add R1 R2

				copy R3 10

				math sub R0 R3 R3
				math not R3 R3 R3
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 1), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(12, 2, Token.TokenType.VALUE, 500), new Token(12, 2, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.MATH), new Token(6, 4, Token.TokenType.NOT),
						new Token(10, 4, Token.TokenType.REGISTER, 2), new Token(13, 4, Token.TokenType.REGISTER, 2),
						new Token(15, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.MATH),
						new Token(6, 5, Token.TokenType.ADD), new Token(10, 5, Token.TokenType.REGISTER, 1),
						new Token(13, 5, Token.TokenType.REGISTER, 2), new Token(15, 5, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.COPY), new Token(6, 7, Token.TokenType.REGISTER, 3),
						new Token(11, 7, Token.TokenType.VALUE, 10), new Token(11, 7, Token.TokenType.NEWLINE),
						new Token(1, 9, Token.TokenType.MATH), new Token(6, 9, Token.TokenType.SUBTRACT),
						new Token(10, 9, Token.TokenType.REGISTER, 0), new Token(13, 9, Token.TokenType.REGISTER, 3),
						new Token(16, 9, Token.TokenType.REGISTER, 3), new Token(18, 9, Token.TokenType.NEWLINE),
						new Token(1, 10, Token.TokenType.MATH), new Token(6, 10, Token.TokenType.NOT),
						new Token(10, 10, Token.TokenType.REGISTER, 3), new Token(13, 10, Token.TokenType.REGISTER, 3),
						new Token(16, 10, Token.TokenType.REGISTER, 3), new Token(18, 10, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(500)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOT).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(2),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(10)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(0),
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(3))), },
				new String[] { "00000000000000000101100000100001", "00000000011111010001100001000001",
						"00000000000000001010110001000011", "00000000000000000111100001000011",
						"00000000000000101001100001100001", "00000000000000001111110001100010",
						"00000000000110001110110001100010", });
	}

	@Test
	public void or() throws Exception {
		assemblerTester("""
				copy R1 10
				copy R2 1

				math add R1 R2 R3
				math or R3 R1 R4

				math add R2 R3 R1
				math or R1 R3
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(11, 1, Token.TokenType.VALUE, 10), new Token(11, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, 1), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.MATH), new Token(6, 4, Token.TokenType.ADD),
						new Token(10, 4, Token.TokenType.REGISTER, 1), new Token(13, 4, Token.TokenType.REGISTER, 2),
						new Token(16, 4, Token.TokenType.REGISTER, 3), new Token(18, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.MATH), new Token(6, 5, Token.TokenType.OR),
						new Token(9, 5, Token.TokenType.REGISTER, 3), new Token(12, 5, Token.TokenType.REGISTER, 1),
						new Token(15, 5, Token.TokenType.REGISTER, 4), new Token(17, 5, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.MATH), new Token(6, 7, Token.TokenType.ADD),
						new Token(10, 7, Token.TokenType.REGISTER, 2), new Token(13, 7, Token.TokenType.REGISTER, 3),
						new Token(16, 7, Token.TokenType.REGISTER, 1), new Token(18, 7, Token.TokenType.NEWLINE),
						new Token(1, 8, Token.TokenType.MATH), new Token(6, 8, Token.TokenType.OR),
						new Token(9, 8, Token.TokenType.REGISTER, 1), new Token(12, 8, Token.TokenType.REGISTER, 3),
						new Token(14, 8, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(10)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.OR)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.ADD)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.OR).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(3))), },
				new String[] { "00000000000000101001100000100001", "00000000000000000101100001000001",
						"00000000000010001011100001100010", "00000000000110000110010010000010",
						"00000000000100001111100000100010", "00000000000000000110010001100011", });
	}

	@Test
	public void pushpop1() throws Exception {
		assemblerTester("""
				copy R10 5
				push not R10 4
				push and R0 1
				copy R2 56
				copy R3 3
				push and R2 R3 R0
				copy R3 2
				peek R3 R0 R5
				pop R6
				pop R7
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 10),
						new Token(11, 1, Token.TokenType.VALUE, 5), new Token(11, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.PUSH), new Token(6, 2, Token.TokenType.NOT),
						new Token(10, 2, Token.TokenType.REGISTER, 10), new Token(15, 2, Token.TokenType.VALUE, 4),
						new Token(15, 2, Token.TokenType.NEWLINE), new Token(1, 3, Token.TokenType.PUSH),
						new Token(6, 3, Token.TokenType.AND), new Token(10, 3, Token.TokenType.REGISTER, 0),
						new Token(14, 3, Token.TokenType.VALUE, 1), new Token(14, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.COPY), new Token(6, 4, Token.TokenType.REGISTER, 2),
						new Token(11, 4, Token.TokenType.VALUE, 56), new Token(11, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.COPY), new Token(6, 5, Token.TokenType.REGISTER, 3),
						new Token(10, 5, Token.TokenType.VALUE, 3), new Token(10, 5, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.PUSH), new Token(6, 6, Token.TokenType.AND),
						new Token(10, 6, Token.TokenType.REGISTER, 2), new Token(13, 6, Token.TokenType.REGISTER, 3),
						new Token(16, 6, Token.TokenType.REGISTER, 0), new Token(18, 6, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.COPY), new Token(6, 7, Token.TokenType.REGISTER, 3),
						new Token(10, 7, Token.TokenType.VALUE, 2), new Token(10, 7, Token.TokenType.NEWLINE),
						new Token(1, 8, Token.TokenType.PEEK), new Token(6, 8, Token.TokenType.REGISTER, 3),
						new Token(9, 8, Token.TokenType.REGISTER, 0), new Token(12, 8, Token.TokenType.REGISTER, 5),
						new Token(14, 8, Token.TokenType.NEWLINE), new Token(1, 9, Token.TokenType.POP),
						new Token(5, 9, Token.TokenType.REGISTER, 6), new Token(7, 9, Token.TokenType.NEWLINE),
						new Token(1, 10, Token.TokenType.POP), new Token(5, 10, Token.TokenType.REGISTER, 7),
						new Token(7, 10, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(10))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(4)
								.operation(Instruction.Operation.NOT)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(10))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(1)
								.operation(Instruction.Operation.AND)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(0))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(56)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.AND)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(0))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(0),
										new Instruction.RegisterFormat.Register(5))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(6))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(7))), },
				new String[] { "00000000000000010101100101000001", "00000000000000010010110101001101",
						"00000000000000000110000000001101", "00000000000011100001100001000001",
						"00000000000000001101100001100001", "00000000000100001110000000001110",
						"00000000000000001001100001100001", "00000000000110000001100010111010",
						"00000000000000000001100011011001", "00000000000000000001100011111001", });
	}

	@Test
	public void pushpop() throws Exception {
		assemblerTester("""
				copy R1 10
				copy R2 15
				push sub R1 R2
				copy R3 5
				copy R4 16
				math sub R2 R4 R4
				push mul R3 R4
				push mul R3 R4
				push mul R3 R4
				push mul R3 R4
				pop R5
				copy R3 3
				peek R3 R6 0
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(11, 1, Token.TokenType.VALUE, 10), new Token(11, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(11, 2, Token.TokenType.VALUE, 15), new Token(11, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.PUSH), new Token(6, 3, Token.TokenType.SUBTRACT),
						new Token(10, 3, Token.TokenType.REGISTER, 1), new Token(13, 3, Token.TokenType.REGISTER, 2),
						new Token(15, 3, Token.TokenType.NEWLINE), new Token(1, 4, Token.TokenType.COPY),
						new Token(6, 4, Token.TokenType.REGISTER, 3), new Token(10, 4, Token.TokenType.VALUE, 5),
						new Token(10, 4, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.COPY),
						new Token(6, 5, Token.TokenType.REGISTER, 4), new Token(11, 5, Token.TokenType.VALUE, 16),
						new Token(11, 5, Token.TokenType.NEWLINE), new Token(1, 6, Token.TokenType.MATH),
						new Token(6, 6, Token.TokenType.SUBTRACT), new Token(10, 6, Token.TokenType.REGISTER, 2),
						new Token(13, 6, Token.TokenType.REGISTER, 4), new Token(16, 6, Token.TokenType.REGISTER, 4),
						new Token(18, 6, Token.TokenType.NEWLINE), new Token(1, 7, Token.TokenType.PUSH),
						new Token(6, 7, Token.TokenType.MULTIPLY), new Token(10, 7, Token.TokenType.REGISTER, 3),
						new Token(13, 7, Token.TokenType.REGISTER, 4), new Token(15, 7, Token.TokenType.NEWLINE),
						new Token(1, 8, Token.TokenType.PUSH), new Token(6, 8, Token.TokenType.MULTIPLY),
						new Token(10, 8, Token.TokenType.REGISTER, 3), new Token(13, 8, Token.TokenType.REGISTER, 4),
						new Token(15, 8, Token.TokenType.NEWLINE), new Token(1, 9, Token.TokenType.PUSH),
						new Token(6, 9, Token.TokenType.MULTIPLY), new Token(10, 9, Token.TokenType.REGISTER, 3),
						new Token(13, 9, Token.TokenType.REGISTER, 4), new Token(15, 9, Token.TokenType.NEWLINE),
						new Token(1, 10, Token.TokenType.PUSH), new Token(6, 10, Token.TokenType.MULTIPLY),
						new Token(10, 10, Token.TokenType.REGISTER, 3), new Token(13, 10, Token.TokenType.REGISTER, 4),
						new Token(15, 10, Token.TokenType.NEWLINE), new Token(1, 11, Token.TokenType.POP),
						new Token(5, 11, Token.TokenType.REGISTER, 5), new Token(7, 11, Token.TokenType.NEWLINE),
						new Token(1, 12, Token.TokenType.COPY), new Token(6, 12, Token.TokenType.REGISTER, 3),
						new Token(10, 12, Token.TokenType.VALUE, 3), new Token(10, 12, Token.TokenType.NEWLINE),
						new Token(1, 13, Token.TokenType.PEEK), new Token(6, 13, Token.TokenType.REGISTER, 3),
						new Token(9, 13, Token.TokenType.REGISTER, 6), new Token(13, 13, Token.TokenType.VALUE, 0),
						new Token(13, 13, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(10)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(15)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.SUB).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(16)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.SUB)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.MUL).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(3),
												new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.MUL).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(3),
												new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.MUL).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(3),
												new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.MUL).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(3),
												new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(5))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(3)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(3),
												new Instruction.RegisterFormat.Register(6))), },
				new String[] { "00000000000000101001100000100001", "00000000000000111101100001000001",
						"00000000000000000111110001001111", "00000000000000010101100001100001",
						"00000000000001000001100010000001", "00000000000100010011110010000010",
						"00000000000000001101110010001111", "00000000000000001101110010001111",
						"00000000000000001101110010001111", "00000000000000001101110010001111",
						"00000000000000000001100010111001", "00000000000000001101100001100001",
						"00000000000000001101100011011011", });
	}

	@Test
	public void shift() throws Exception {
		assemblerTester("""
				copy R1 10
				copy R2 5
				math lshift R2 R1 R3
				math rshift R1 R3
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(11, 1, Token.TokenType.VALUE, 10), new Token(11, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, 5), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.MATH), new Token(6, 3, Token.TokenType.LSHIFT),
						new Token(13, 3, Token.TokenType.REGISTER, 2), new Token(16, 3, Token.TokenType.REGISTER, 1),
						new Token(19, 3, Token.TokenType.REGISTER, 3), new Token(21, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.MATH), new Token(6, 4, Token.TokenType.RSHIFT),
						new Token(13, 4, Token.TokenType.REGISTER, 1), new Token(16, 4, Token.TokenType.REGISTER, 3),
						new Token(18, 4, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(10)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.LEFT_SHIFT)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.RIGHT_SHIFT).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(3))), },
				new String[] { "00000000000000101001100000100001", "00000000000000010101100001000001",
						"00000000000100000111000001100010", "00000000000000000111010001100011", });
	}

	@Test
	public void store1() throws Exception {
		assemblerTester("""
				copy R22 99

				store R22 256
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 22),
						new Token(12, 1, Token.TokenType.VALUE, 99), new Token(12, 1, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.STORE), new Token(7, 3, Token.TokenType.REGISTER, 22),
						new Token(14, 3, Token.TokenType.VALUE, 256), new Token(14, 3, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(99)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(22))),
						Instruction.buildWithType(Instruction.InstructionType.STORE).immediate(256)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(22))), },
				new String[] { "00000000000110001101101011000001", "00000000010000000001101011010101", });
	}

	@Test
	public void store2() throws Exception {
		assemblerTester("""
				copy R1 15
				copy R27 27
				store R27 R1 17
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(11, 1, Token.TokenType.VALUE, 15), new Token(11, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 27),
						new Token(12, 2, Token.TokenType.VALUE, 27), new Token(12, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.STORE), new Token(7, 3, Token.TokenType.REGISTER, 27),
						new Token(11, 3, Token.TokenType.REGISTER, 1), new Token(16, 3, Token.TokenType.VALUE, 17),
						new Token(16, 3, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(15)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(27)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(27))),
						Instruction.buildWithType(Instruction.InstructionType.STORE).immediate(17)
								.operation(Instruction.Operation.NOP).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(27),
												new Instruction.RegisterFormat.Register(1))), },
				new String[] { "00000000000000111101100000100001", "00000000000001101101101101100001",
						"00000000100011101101100000110111", });
	}

	@Test
	public void store3() throws Exception {
		assemblerTester("""
				copy R4 13
				copy R29 100
				copy R16 1122
				store R4 R16 R29
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 4),
						new Token(11, 1, Token.TokenType.VALUE, 13), new Token(11, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 29),
						new Token(13, 2, Token.TokenType.VALUE, 100), new Token(13, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.COPY), new Token(6, 3, Token.TokenType.REGISTER, 16),
						new Token(14, 3, Token.TokenType.VALUE, 1122), new Token(14, 3, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.STORE), new Token(7, 4, Token.TokenType.REGISTER, 4),
						new Token(10, 4, Token.TokenType.REGISTER, 16), new Token(14, 4, Token.TokenType.REGISTER, 29),
						new Token(17, 4, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(13)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(100)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(29))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(1122)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(16))),
						Instruction.buildWithType(Instruction.InstructionType.STORE).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(4),
										new Instruction.RegisterFormat.Register(16),
										new Instruction.RegisterFormat.Register(29))), },
				new String[] { "00000000000000110101100010000001", "00000000000110010001101110100001",
						"00000001000110001001101000000001", "00000000001001000001101110110110", });
	}

	@Test
	public void swap() throws Exception {
		assemblerTester("""
				copy R1 10
				copy R2 20

				math xor R1 R2 R1
				math xor R2 R1 R2
				math xor R1 R2 R1

				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(11, 1, Token.TokenType.VALUE, 10), new Token(11, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(11, 2, Token.TokenType.VALUE, 20), new Token(11, 2, Token.TokenType.NEWLINE),
						new Token(1, 4, Token.TokenType.MATH), new Token(6, 4, Token.TokenType.XOR),
						new Token(10, 4, Token.TokenType.REGISTER, 1), new Token(13, 4, Token.TokenType.REGISTER, 2),
						new Token(16, 4, Token.TokenType.REGISTER, 1), new Token(18, 4, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.MATH), new Token(6, 5, Token.TokenType.XOR),
						new Token(10, 5, Token.TokenType.REGISTER, 2), new Token(13, 5, Token.TokenType.REGISTER, 1),
						new Token(16, 5, Token.TokenType.REGISTER, 2), new Token(18, 5, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.MATH), new Token(6, 6, Token.TokenType.XOR),
						new Token(10, 6, Token.TokenType.REGISTER, 1), new Token(13, 6, Token.TokenType.REGISTER, 2),
						new Token(16, 6, Token.TokenType.REGISTER, 1), new Token(18, 6, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(10)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(20)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.XOR)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.XOR)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.XOR)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(1))), },
				new String[] { "00000000000000101001100000100001", "00000000000001010001100001000001",
						"00000000000010001010100000100010", "00000000000100000110100001000010",
						"00000000000010001010100000100010", });
	}

	@Test
	public void test2() throws Exception {
		assemblerTester("""
				copy R1 5
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 5), new Token(10, 1, Token.TokenType.NEWLINE), },
				new Instruction[] { Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(5)
						.operation(Instruction.Operation.NOP).registers(
								new Instruction.RegisterFormat.DestOnly(new Instruction.RegisterFormat.Register(1))), },
				new String[] { "00000000000000010101100000100001", });
	}

	@Test
	public void test() throws Exception {
		assemblerTester("""
				math add R4  R0  R2

				pop R1

				push add R1 R2

				load R1 R2 33
				interrupt 12

				peek R1 R2 33

				copy R1 56

				halt

				jump R1 35
				""",
				new Token[] { new Token(1, 1, Token.TokenType.MATH), new Token(6, 1, Token.TokenType.ADD),
						new Token(10, 1, Token.TokenType.REGISTER, 4), new Token(14, 1, Token.TokenType.REGISTER, 0),
						new Token(18, 1, Token.TokenType.REGISTER, 2), new Token(20, 1, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.POP), new Token(5, 3, Token.TokenType.REGISTER, 1),
						new Token(7, 3, Token.TokenType.NEWLINE), new Token(1, 5, Token.TokenType.PUSH),
						new Token(6, 5, Token.TokenType.ADD), new Token(10, 5, Token.TokenType.REGISTER, 1),
						new Token(13, 5, Token.TokenType.REGISTER, 2), new Token(15, 5, Token.TokenType.NEWLINE),
						new Token(1, 7, Token.TokenType.LOAD), new Token(6, 7, Token.TokenType.REGISTER, 1),
						new Token(9, 7, Token.TokenType.REGISTER, 2), new Token(14, 7, Token.TokenType.VALUE, 33),
						new Token(14, 7, Token.TokenType.NEWLINE), new Token(1, 8, Token.TokenType.INTERRUPT),
						new Token(13, 8, Token.TokenType.VALUE, 12), new Token(13, 8, Token.TokenType.NEWLINE),
						new Token(1, 10, Token.TokenType.PEEK), new Token(6, 10, Token.TokenType.REGISTER, 1),
						new Token(9, 10, Token.TokenType.REGISTER, 2), new Token(14, 10, Token.TokenType.VALUE, 33),
						new Token(14, 10, Token.TokenType.NEWLINE), new Token(1, 12, Token.TokenType.COPY),
						new Token(6, 12, Token.TokenType.REGISTER, 1), new Token(11, 12, Token.TokenType.VALUE, 56),
						new Token(11, 12, Token.TokenType.NEWLINE), new Token(1, 14, Token.TokenType.HALT),
						new Token(5, 14, Token.TokenType.NEWLINE), new Token(1, 16, Token.TokenType.JUMP),
						new Token(6, 16, Token.TokenType.REGISTER, 1), new Token(11, 16, Token.TokenType.VALUE, 35),
						new Token(11, 16, Token.TokenType.NEWLINE), },
				new Instruction[] { Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
						.operation(Instruction.Operation.ADD)
						.registers(new Instruction.RegisterFormat.ThreeR(new Instruction.RegisterFormat.Register(4),
								new Instruction.RegisterFormat.Register(0),
								new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(0)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.PUSH).immediate(0)
								.operation(Instruction.Operation.ADD).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.LOAD).immediate(33)
								.operation(Instruction.Operation.NOP).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(12)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.POP).immediate(33)
								.operation(Instruction.Operation.NOP).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(1),
												new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(56)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.NOP).registers(new Instruction.RegisterFormat.NoR()),
						Instruction.buildWithType(Instruction.InstructionType.BRANCH).immediate(35)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))), },
				new String[] { "00000000001000000011100001000010", "00000000000000000001100000111001",
						"00000000000000000111100001001111", "00000001000010000101100001010011",
						"00000000000000000000000110011000", "00000001000010000101100001011011",
						"00000000000011100001100000100001", "00000000000000000000000000000000",
						"00000000000010001101100000100101", });
	}

	@Test
	public void xor() throws Exception {
		assemblerTester("""
				copy R1 2
				copy R2 4
				copy R3 8

				math xor R1 R2 R4
				math xor R2 R3 R5

				copy R6 16

				math xor R3 R6
				""",
				new Token[] { new Token(1, 1, Token.TokenType.COPY), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(10, 1, Token.TokenType.VALUE, 2), new Token(10, 1, Token.TokenType.NEWLINE),
						new Token(1, 2, Token.TokenType.COPY), new Token(6, 2, Token.TokenType.REGISTER, 2),
						new Token(10, 2, Token.TokenType.VALUE, 4), new Token(10, 2, Token.TokenType.NEWLINE),
						new Token(1, 3, Token.TokenType.COPY), new Token(6, 3, Token.TokenType.REGISTER, 3),
						new Token(10, 3, Token.TokenType.VALUE, 8), new Token(10, 3, Token.TokenType.NEWLINE),
						new Token(1, 5, Token.TokenType.MATH), new Token(6, 5, Token.TokenType.XOR),
						new Token(10, 5, Token.TokenType.REGISTER, 1), new Token(13, 5, Token.TokenType.REGISTER, 2),
						new Token(16, 5, Token.TokenType.REGISTER, 4), new Token(18, 5, Token.TokenType.NEWLINE),
						new Token(1, 6, Token.TokenType.MATH), new Token(6, 6, Token.TokenType.XOR),
						new Token(10, 6, Token.TokenType.REGISTER, 2), new Token(13, 6, Token.TokenType.REGISTER, 3),
						new Token(16, 6, Token.TokenType.REGISTER, 5), new Token(18, 6, Token.TokenType.NEWLINE),
						new Token(1, 8, Token.TokenType.COPY), new Token(6, 8, Token.TokenType.REGISTER, 6),
						new Token(11, 8, Token.TokenType.VALUE, 16), new Token(11, 8, Token.TokenType.NEWLINE),
						new Token(1, 10, Token.TokenType.MATH), new Token(6, 10, Token.TokenType.XOR),
						new Token(10, 10, Token.TokenType.REGISTER, 3), new Token(13, 10, Token.TokenType.REGISTER, 6),
						new Token(15, 10, Token.TokenType.NEWLINE), },
				new Instruction[] {
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(2)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(1))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(4)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(2))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(8)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(3))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.XOR)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(1),
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(4))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.XOR)
								.registers(new Instruction.RegisterFormat.ThreeR(
										new Instruction.RegisterFormat.Register(2),
										new Instruction.RegisterFormat.Register(3),
										new Instruction.RegisterFormat.Register(5))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(16)
								.operation(Instruction.Operation.NOP)
								.registers(new Instruction.RegisterFormat.DestOnly(
										new Instruction.RegisterFormat.Register(6))),
						Instruction.buildWithType(Instruction.InstructionType.MATH).immediate(0)
								.operation(Instruction.Operation.XOR).registers(
										new Instruction.RegisterFormat.TwoR(new Instruction.RegisterFormat.Register(3),
												new Instruction.RegisterFormat.Register(6))), },
				new String[] { "00000000000000001001100000100001", "00000000000000010001100001000001",
						"00000000000000100001100001100001", "00000000000010001010100010000010",
						"00000000000100001110100010100010", "00000000000001000001100011000001",
						"00000000000000001110100011000011", });
	}

	@Test(expected = AssemblerException.class)
	public void invalidLex() throws Exception {
		assemblerTester("Foo", null, null, null);
	}

	@Test(expected = AssemblerException.class)
	public void invalidLex1() throws Exception {
		assemblerTester("math add R1 10.1", null, null, null);
	}

	@Test(expected = AssemblerException.class)
	public void invalidLex2() throws Exception {
		assemblerTester("pop r1", null, null, null);
	}

	@Test(expected = AssemblerException.class)
	public void invalidLex3() throws Exception {
		assemblerTester("math add R45", null, null, null);
	}

	@Test(expected = AssemblerException.class)
	public void invalidLex4() throws Exception {
		assemblerTester("push glt R1", null, null, null);
	}

	@Test(expected = Exception.class)
	public void missingimmediate() throws Exception {
		assemblerTester(
				"""
						copy R2
						""", new Token[] { new Token(1, 1, Token.TokenType.COPY),
						new Token(6, 1, Token.TokenType.REGISTER, 2), new Token(8, 1, Token.TokenType.NEWLINE), },
				null, null);
	}

	@Test(expected = Exception.class)
	public void missingop() throws Exception {
		assemblerTester("""
				math R1 R2 R3
				""",
				new Token[] { new Token(1, 1, Token.TokenType.MATH), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(9, 1, Token.TokenType.REGISTER, 2), new Token(12, 1, Token.TokenType.REGISTER, 3),
						new Token(14, 1, Token.TokenType.NEWLINE), },
				null, null);
	}

	@Test(expected = Exception.class)
	public void noimmediate() throws Exception {
		assemblerTester("""
				math add R1 5
				""",
				new Token[] { new Token(1, 1, Token.TokenType.MATH), new Token(6, 1, Token.TokenType.ADD),
						new Token(10, 1, Token.TokenType.REGISTER, 1), new Token(14, 1, Token.TokenType.VALUE, 5),
						new Token(14, 1, Token.TokenType.NEWLINE), },
				null, null);
	}

	@Test(expected = Exception.class)
	public void nonewline() throws Exception {
		assemblerTester("""
				math add R1 R2 copy R2
				""",
				new Token[] { new Token(1, 1, Token.TokenType.MATH), new Token(6, 1, Token.TokenType.ADD),
						new Token(10, 1, Token.TokenType.REGISTER, 1), new Token(13, 1, Token.TokenType.REGISTER, 2),
						new Token(16, 1, Token.TokenType.COPY), new Token(21, 1, Token.TokenType.REGISTER, 2),
						new Token(23, 1, Token.TokenType.NEWLINE), },
				null, null);
	}

	@Test(expected = Exception.class)
	public void noop() throws Exception {
		assemblerTester("""
				load eq R2
				""",
				new Token[] { new Token(1, 1, Token.TokenType.LOAD), new Token(6, 1, Token.TokenType.EQUAL),
						new Token(9, 1, Token.TokenType.REGISTER, 2), new Token(11, 1, Token.TokenType.NEWLINE), },
				null, null);
	}

	@Test(expected = Exception.class)
	public void opwrongposition() throws Exception {
		assemblerTester("""
				math R1 add R2 R3
				""",
				new Token[] { new Token(1, 1, Token.TokenType.MATH), new Token(6, 1, Token.TokenType.REGISTER, 1),
						new Token(9, 1, Token.TokenType.ADD), new Token(13, 1, Token.TokenType.REGISTER, 2),
						new Token(16, 1, Token.TokenType.REGISTER, 3), new Token(18, 1, Token.TokenType.NEWLINE), },
				null, null);
	}

	@Test(expected = Exception.class)
	public void unsoppotedformat1() throws Exception {
		assemblerTester("""
				load
				""", new Token[] { new Token(1, 1, Token.TokenType.LOAD), new Token(5, 1, Token.TokenType.NEWLINE), },
				null, null);
	}

	@Test(expected = Exception.class)
	public void unsoppotedformat() throws Exception {
		assemblerTester("""
				pop R1 R2 R2
				""",
				new Token[] { new Token(1, 1, Token.TokenType.POP), new Token(5, 1, Token.TokenType.REGISTER, 1),
						new Token(8, 1, Token.TokenType.REGISTER, 2), new Token(11, 1, Token.TokenType.REGISTER, 2),
						new Token(13, 1, Token.TokenType.NEWLINE), },
				null, null);
	}

}
