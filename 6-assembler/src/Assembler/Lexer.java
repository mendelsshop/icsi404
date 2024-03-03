package Assembler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

import Assembler.Token.TokenType;

public class Lexer {
    // protected so I don't have to duplicate this for FunctionalLexer
    protected StringHandler source;
    // position and line number are zero-based
    protected int position = 1;
    protected int currentLine = 1;
    private HashMap<String, Token.TokenType> keywords = new HashMap<String, Token.TokenType>() {
        {
            put("math", TokenType.MATH);
            put("add", TokenType.ADD);
            put("subtract", TokenType.SUBTRACT);
            put("multiply", TokenType.MULTIPLY);
            put("and", TokenType.AND);
            put("or", TokenType.OR);
            put("not", TokenType.NOT);
            put("xor", TokenType.XOR);
            put("copy", TokenType.COPY);
            put("halt", TokenType.HALT);
            put("branch", TokenType.BRANCH);
            put("jump", TokenType.JUMP);
            put("call", TokenType.CALL);
            put("push", TokenType.PUSH);
            put("load", TokenType.LOAD);
            put("return", TokenType.RETURN);
            put("store", TokenType.STORE);
            put("peek", TokenType.PEEK);
            put("pop", TokenType.POP);
            put("interrupt", TokenType.INTERRUPT);
            put("equal", TokenType.EQUAL);
            put("unequal", TokenType.UNEQUAL);
            put("greater", TokenType.GREATER);
            put("less", TokenType.LESS);
            put("greaterOrEqual", TokenType.GREATEROREQUAL);
            put("lessOrEqual", TokenType.LESSOREQUAL);
            put("shift", TokenType.SHIFT);
            put("left", TokenType.LEFT);
            put("right", TokenType.RIGHT);
        }
    };

    public Lexer(String input) {
        source = new StringHandler(input);
    }

    public LinkedList<Token> lex() throws AssemblerException {
        var tokens = new LinkedList<Token>();
        // TODO: make sure each none newline token is delimeted by space or newline
        while (!source.IsDone()) {
            var token = lexCharacter(source.Peek());
            if (token.isPresent()) {
                tokens.add(token.get());
            }
        }
        return tokens;
    }

    // Manages state switching of the Lexer
    // but also manages the output after of a state ie the token it may produce
    private Optional<Token> lexCharacter(Character current) throws AssemblerException {
        if (isLetter(current)) {
            return ProcessWord();
        } else if (isDigit(current)) {
            return processInteger();
        } else if (current == ' ') {
            source.Swallow(1);
            position++;
            return Optional.empty();
        } else if (current == '\r') {
            source.Swallow(1);
            return Optional.empty();
        } else if (current == '\n') {
            source.Swallow(1);
            var prev_position = position + 1;
            position = 1;
            return Optional.of(new Token(prev_position, currentLine++, TokenType.NEWLINE));
        } else {
            throw new AssemblerException(currentLine, position, "Character `" + current + "` not recognized",
                    AssemblerException.ExceptionType.LexicalError);
        }

    }

    protected Optional<Token> ProcessWord() {
        return
        // first we check if its register first char == "R"
        Optional.ofNullable((source.Peek() == 'R') ? source.GetChar() : null)
                .flatMap(unsused -> Optional
                        // then we make sure that next character is digit (part of register number)
                        .ofNullable(!source.IsDone() && isDigit(source.Peek()) ? source.GetChar() : null)
                        // then we make sure that next character is digit (part of register number)
                        .map(register_10_digit -> Optional.ofNullable(isDigit(source.Peek()) ? source.GetChar() : null)
                                // we use char - '0' to convert from ascci to integer
                                // if the second digit is present we mutliply the first digits value by 10 (for
                                // 10s place)
                                // and it to value of second digit
                                // TODO: and make sure fist digit is less then 4 and the second digit is less
                                // then 2 if the first digit is 3
                                .map(register_1_digit -> (register_10_digit - '0') * 10
                                        + (register_1_digit - '0'))
                                // otherwise (just single digit) we get first digits value
                                .orElse(register_10_digit - '0')))
                // if we find a correct register number we create registeer token
                .map(regsiter_number -> new Token(position, currentLine, TokenType.REGISTER, regsiter_number))
                // otherwise if register is invalid or its not register read word and find it if
                // possible
                .or(() -> {

                    int startPosition = position;
                    String word = "";
                    // if its not a register it can only be alphabetic
                    while (!source.IsDone() && (isLetter(source.Peek()))) {
                        position++;
                        word += source.GetChar();
                    }
                    return Optional.ofNullable(keywords.get(word)).map(tt -> new Token(startPosition, currentLine, tt));
                });
    }

    private Optional<Token> processInteger() {
        String number = "";
        while (!source.IsDone() && isDigit(source.Peek())) {
            position++;
            number += source.GetChar();
        }
        return number == "" ? Optional.empty()
                : Optional.of(new Token(position, currentLine, TokenType.VALUE, Integer.parseInt(number)));
    }

    private static boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
