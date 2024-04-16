package Assembler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

import Assembler.Instruction.RegisterFormat;

public class AssemblerTest {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage Assembler [input] [output]");
            System.exit(1);
        }
        // get the paths
        Path myPath = Paths.get(args[1]);
        Path myOutPath = Paths.get(args[2]);
        String content;
        try {
            // read the assembly
            content = new String(Files.readAllBytes(myPath));

            try {
                // lex the assembly for tokens
                Lexer lexer = new Lexer(content);
                LinkedList<Token> lexed = lexer.lex();

                System.out.print("\"\"\"\n" + content + "\"\"\", new Token[] {");
                for (var token : lexed) {
                    System.out.print("new Token(" + token.getStartPosition() + ", " + token.getLineNumber()
                            + ", Token.TokenType." + token.getType()
                            + token.getValue().map(val -> ", " + val).orElse("") + "), ");

                }
                System.out.println("},");
                // parse and output to file
                var parser = new Parser(lexed);
                LinkedList<Instruction> parsed = parser.parse().get();

                System.out.print("new Instruction[] {");
                for (var inst : parsed) {
                    System.out.println("Instruction.buildWithType(Instruction.InstructionType." + inst.getType()
                            + ").immediate(" + inst.getImmediate() + ").operation(Instruction.Operation."
                            + inst.getOperation() + ").registers(new Instruction.RegisterFormat."
                            + switch (inst.getRegisters()) {
                                case RegisterFormat.NoR() -> "NoR()),";
                                case RegisterFormat.DestOnly(RegisterFormat.Register rd) ->
                                    "DestOnly(new Instruction.RegisterFormat.Register(" + rd.number() + " ))),";
                                case RegisterFormat.TwoR(RegisterFormat.Register rs1, RegisterFormat.Register rd) ->
                                    "TwoR(new Instruction.RegisterFormat.Register(" + rs1.number() + " ), new Instruction.RegisterFormat.Register(" + rd.number() + " ))),";
                                case RegisterFormat.ThreeR(RegisterFormat.Register rs1, RegisterFormat.Register rs2, RegisterFormat.Register rd) ->
                                    "ThreeR( new Instruction.RegisterFormat.Register(" + rs1.number() + " ), new Instruction.RegisterFormat.Register(" + rs2.number() + " ), new Instruction.RegisterFormat.Register(" + rd.number() + " ))),";

                            });

                }
                System.out.println("},");
                System.out.print("new String[] {");
                for (var inst : parsed) {
                    System.out.print("\"" +inst.toBitPattern() + "\", ");
                }
                System.out.println("}");
            } catch (AssemblerException e) {
                e.DisplayError(content, myPath.toString());
            }
        } catch (IOException e) {
            System.err.println("IO Error while reading input file or writing output file: " + e.getMessage());

        }
    }
}
