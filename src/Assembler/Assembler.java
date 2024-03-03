package Assembler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Assembler {
    public static void main(String[] args) {
        // Handleing case when no awk file is specified
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
                System.out.println(lexer.lex());
            } catch (AssemblerException e) {
                e.DisplayError(content, myPath.toString());
            }
        } catch (IOException e) {
            System.err.println("Error while reading awk file: " + myPath.toString() + ": " + e.getMessage());
        }
    }
}
