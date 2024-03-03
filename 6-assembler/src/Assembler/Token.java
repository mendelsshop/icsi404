package Assembler;

import java.util.Optional;

public class Token {
    private int startPosition;

    public int getStartPosition() {
        return startPosition;
    }

    private int lineNumber;

    public int getLineNumber() {
        return lineNumber;
    }

    private TokenType type;

    public TokenType getType() {
        return type;
    }

    // we only have integers because the only things we stores values are registers
    // (1-32) or (0-31) (probably) and values (positive integers)
    private Optional<Integer> value = Optional.empty();

    public Token(int position, int line, TokenType type, Integer value) {
        startPosition = position;
        lineNumber = line;
        this.type = type;
        this.value = Optional.ofNullable(value);
    }

    public Token(int position, int line, TokenType type) {
        startPosition = position;
        lineNumber = line;
        this.type = type;
    }

    public enum TokenType {
        MATH, ADD, SUBTRACT, MULTIPLY, AND, OR, NOT, XOR, COPY, HALT, BRANCH, JUMP, CALL, PUSH, LOAD, RETURN, STORE,
        PEEK, POP, INTERRUPT, EQUAL, UNEQUAL, GREATER, LESS, GREATEROREQUAL, LESSOREQUAL, SHIFT, LEFT, RIGHT, REGISTER,
        VALUE, NEWLINE
    }

    @Override
    public String toString() {
        return type + value.map(c -> "(" + c + ")").orElse("") + "[" + startPosition + " " + lineNumber + "]";
    }
}
