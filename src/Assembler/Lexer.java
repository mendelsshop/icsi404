package Assembler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;

import Assembler.AssemblerException.ExceptionType;
import Assembler.Token.TokenType;

public class Lexer {
    protected StringHandler source;
    // position and line number are zero-based
    protected int position = 1;
    protected int currentLine = 1;

    // keyword list
    private HashMap<String, Token.TokenType> keywords = new HashMap<String, Token.TokenType>() {
        {
            put("math", TokenType.MATH);
            put("add", TokenType.ADD);
            put("sub", TokenType.SUBTRACT);
            put("mul", TokenType.MULTIPLY);
            put("and", TokenType.AND);
            put("or", TokenType.OR);
            put("not", TokenType.NOT);
            put("xor", TokenType.XOR);
            put("copy", TokenType.COPY);
            put("halt", TokenType.HALT);
            put("branch", TokenType.BRANCH);
            put("jump", TokenType.JUMP);
            put("call", TokenType.CALL);
            put("callif", TokenType.CALLIF);
            put("push", TokenType.PUSH);
            put("load", TokenType.LOAD);
            put("return", TokenType.RETURN);
            put("store", TokenType.STORE);
            put("peek", TokenType.PEEK);
            put("pop", TokenType.POP);
            put("interrupt", TokenType.INTERRUPT);
            put("eq", TokenType.EQUAL);
            put("ne", TokenType.UNEQUAL);
            put("gt", TokenType.GREATER);
            put("lt", TokenType.LESS);
            put("ge", TokenType.GREATEROREQUAL);
            put("le", TokenType.LESSOREQUAL);
            put("rshift", TokenType.RSHIFT);
            put("lshift", TokenType.LSHIFT);
        }
    };

    public Lexer(String input) {
        source = new StringHandler(input);
    }

    public LinkedList<Token> lex() throws AssemblerException {
        var tokens = new LinkedList<Token>();
        while (!source.IsDone()) {
            var token = lexCharacter(source.Peek());
            tokens.add(token);
            var foundWhiteSpace = absorb(' ');
            var line = currentLine;
            var newline = absorb('\n');
            // if we hit end of the line add newline
            if (newline) {
                tokens.add(new Token(position, line, TokenType.NEWLINE));
                position = 1;
                // otherwise if we dont have a whitespace after the current token
                // that means there is not proper spacing between tokens
            } else if (!foundWhiteSpace) {
                throw new AssemblerException(currentLine, position,
                        "expected white space between items in a statement\nor newline between statements",
                        AssemblerException.ExceptionType.LexicalError);
            }

        }
        return tokens;
    }

    // used to absorb (multiple) white space and comments
    // returns true if found the given whitespace type - which is needed to know if
    // there is random invalid characters at the end of a line
    private boolean absorb(char needle) {
        Function<Character, Boolean> checkWhiteSpace = ((peeked) -> peeked == needle || peeked == '\r'
                || (needle == '\n' && peeked == ';'));
        var whiteSpaceFound = false;
        // while we have valid whitespace
        while (!source.IsDone()
                && checkWhiteSpace
                        .apply(source.Peek())) {

            char getChar = source.GetChar();
            if (getChar == '\n') {
                currentLine++;
                // we use ; for comments
            } else if (getChar == ';') {
                // absorb comments
                while (!source.IsDone() && !(source.GetChar() == '\n')) {
                }
            } else {
                position++;
            }
            whiteSpaceFound = true;

        }
        return whiteSpaceFound;
    }

    // Manages state switching of the Lexer
    // but also manages the output after of a state ie the token it may produce
    private Token lexCharacter(Character current) throws AssemblerException {

        if (isLowerCase(current)) {
            return (ProcessWord());
        } else if (isDigit(current)) {
            return (processInteger(false));
        } else if (current == '-') {
            source.Swallow(1);
            return (processInteger(true));
        } else if (current == 'R') {
            return ProccesRegisterBetterErrors();
        } else {
            throw new AssemblerException(currentLine, position, "Character `" + current + "` not recognized",
                    AssemblerException.ExceptionType.LexicalError);
        }

    }

    private Character GetChar() {
        position++;
        return source.GetChar();
    }

    public Token ProccesRegisterBetterErrors() throws AssemblerException {
        var startPosition = position++;
        source.Swallow(1);

        // if we parse a register and the user does not specify the register number
        if (source.IsDone()) {
            throw new AssemblerException(currentLine, startPosition, "invalied register: register number not given",
                    AssemblerException.ExceptionType.LexicalError);
        }
        var firstDigit = GetChar();
        // absorb digit of register number
        if (!isDigit(firstDigit)) {
            throw new AssemblerException(currentLine, startPosition,
                    "invalied register: register number is not a number: `" + firstDigit + "`",
                    AssemblerException.ExceptionType.LexicalError);
        }
        // get possible second digit of the register number
        if (!source.IsDone() && isDigit(source.Peek())) {
            var secondDigit = GetChar();
            // if there is a second digit validate that the number is not to big ie < 32
            var registerNumber = (firstDigit - '0') * 10 + (secondDigit - '0');
            if (registerNumber > 31) {
                throw new AssemblerException(currentLine, startPosition,
                        "invalied register: register number to big: `" + registerNumber + "`",
                        AssemblerException.ExceptionType.LexicalError);
            }
            return new Token(startPosition, currentLine, TokenType.REGISTER, registerNumber);

            // if second digit is not a number whe have an error in the assembly
        } else if (!source.IsDone() && (notWhiteSpace(source.Peek()))) {
            throw new AssemblerException(currentLine, startPosition,
                    "invalied register: second digit of register not a digit: `" + source.Peek() + "`",
                    AssemblerException.ExceptionType.LexicalError);
        } else {
            return new Token(startPosition, currentLine, TokenType.REGISTER, firstDigit - '0');
        }

    }

    private Boolean notWhiteSpace(Character c) {
        return !(c == ' ' || c == '\r' || c == '\n');
    }

    public Token ProcessRegister() throws AssemblerException {
        var startPosition = position++;
        source.Swallow(1);

        return Optional
                .ofNullable(!source.IsDone() && isDigit(source.Peek()) ? GetChar() : null)
                // then we make sure that next character is digit (part of register number)
                .map(register_10_digit -> Optional.ofNullable(isDigit(source.Peek()) ? GetChar() : null)

                        // we use char - '0' to convert from ascci to integer
                        // if the second digit is present we mutliply the first digits value by 10 (for
                        // 10s place)
                        // and it to value of second digit
                        // TODO: and make sure fist digit is less then 4 and the second digit is less
                        // then 2 if the first digit is 3
                        .map(register_1_digit -> (register_10_digit - '0') * 10
                                + (register_1_digit - '0'))
                        // otherwise (just single digit) we get first digits value
                        .orElse(register_10_digit - '0'))
                // then we bounds check because with 2 digits we can go up to 99, but we only
                // have 32 registers
                .filter(n -> n <= 31)
                // if we find a correct register number we create registeer token
                .map(regsiter_number -> new Token(startPosition, currentLine, TokenType.REGISTER, regsiter_number))
                .orElseThrow(() -> new AssemblerException(currentLine, startPosition, "invalied register",
                        AssemblerException.ExceptionType.LexicalError));

    }

    protected Token ProcessWord() throws AssemblerException {

        int startPosition = position;
        String word = "";
        // read the word any lower case sequence of characters
        while (!source.IsDone() && (isLowerCase(source.Peek()))) {
            position++;
            word += source.GetChar();
        }
        // we duplicate the word, so that java does not complain about mutable variables
        // in lambdas
        var words = word;

        // if the word is not in the keyword error - otherwise get the token for the
        // word
        return Optional.ofNullable(keywords.get(word)).map(tt -> new Token(startPosition, currentLine, tt))
                .orElseThrow(() -> new AssemblerException(currentLine, startPosition,
                        "invalid instruction: `" + words + "`",
                        ExceptionType.LexicalError));
    }

    // proceces number
    // takes a boolean to know if the number should be negative or not
    private Token processInteger(boolean negative) throws AssemblerException {
        String number = "";
        int startPosition = position;
        // absorb the digits
        while (!source.IsDone() && isDigit(source.Peek())) {
            position++;
            number += source.GetChar();
        }
        // since a number is the last thing in a line if there any no white space
        // character the line is wrong
        if (!source.IsDone() && notWhiteSpace(source.Peek())) {

            throw new AssemblerException(currentLine, startPosition,
                    "invalid line: character found after number `" + source.Peek() + "`",
                    AssemblerException.ExceptionType.LexicalError);
        }
        // if the number is naegative its possible that there were no digits in which
        // case its an invalid number
        if (number.isBlank()) {

            throw new AssemblerException(currentLine, startPosition,
                    "invalied number: negative number has no digits",
                    AssemblerException.ExceptionType.LexicalError);
        }
        return new Token(position, currentLine, TokenType.VALUE, (negative ? -1 : 1) * Integer.parseInt(number));
    }

    private static boolean isLowerCase(char c) {
        return c >= 'a' && c <= 'z';
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
