package Assembler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

public class Assembler {
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
				// parse and output to file
				var parser = new Parser(lexed);
				Files.writeString(myOutPath, "", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				for (var inst : parser.parse().get()) {
					Files.writeString(myOutPath, inst.toBitPattern() + "\n", StandardOpenOption.APPEND);
				}
			} catch (AssemblerException e) {
				e.DisplayError(content, myPath.toString());
			}
		} catch (IOException e) {
			System.err.println("IO Error while reading input file or writing output file: " + e.getMessage());

		}
	}
}
