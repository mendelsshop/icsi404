package Assembler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Assembler {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage Assembler [input] [output]");
            System.exit(1);
        }
        Path myPath = Paths.get(args[1]);
        String content;
        try {
            content = new String(Files.readAllBytes(myPath));
            
            try {
                Lexer lexer = new Lexer(content);
                LinkedList<Token> lexed = lexer.lex();
                System.out.println(lexed);
                var parser = new Parser(lexed);
                for (var inst: parser.parse().get()) {
                    System.out.println(inst);
                }
            } catch (AssemblerException e) {
                e.DisplayError(content, myPath.toString());
            }
        } catch (IOException e) {
            System.err.println("Error while reading assembly file: " + myPath.toString() + ": " + e.getMessage());
        }
    }
}
