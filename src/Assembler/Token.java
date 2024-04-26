package Assembler;

import java.util.Optional;

public class Token {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + startPosition;
		result = prime * result + lineNumber;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (startPosition != other.startPosition)
			return false;
		if (lineNumber != other.lineNumber)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public enum TokenType {
		MATH, ADD, SUBTRACT, MULTIPLY, AND, OR, NOT, XOR, COPY, HALT, BRANCH, JUMP, CALL, PUSH, LOAD, RETURN, STORE,
		PEEK, POP, INTERRUPT, EQUAL, UNEQUAL, GREATER, LESS, GREATEROREQUAL, LESSOREQUAL, REGISTER, VALUE, NEWLINE,
		CALLIF, RSHIFT, LSHIFT
	}

	private int startPosition;

	private int lineNumber;

	private TokenType type;

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

	public int getStartPosition() {
		return startPosition;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public TokenType getType() {
		return type;
	}

	// get the value of this token only works for registers and numbers
	public Optional<Integer> getValue() {
		return value;
	}

	@Override
	public String toString() {
		return type + value.map(c -> "(" + c + ")").orElse("") + "[" + startPosition + " " + lineNumber + "]";
	}

}
