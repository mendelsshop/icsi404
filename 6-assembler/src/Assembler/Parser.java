package Assembler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Supplier;
import Assembler.Instruction.InstructionType;
import Assembler.Instruction.Operation;
import Assembler.Instruction.RegisterFormat;
import Assembler.Instruction.RegisterFormat.DestOnly;
import Assembler.Instruction.RegisterFormat.Register;
import Assembler.Instruction.RegisterFormat.ThreeR;
import Assembler.Instruction.RegisterFormat.TwoR;
import Assembler.Result.Err;
import Assembler.Result.Ok;
import Assembler.Token.TokenType;

public class Parser {
    // TODO: better error messages
    //
    // TODO: stuff besides math
    public class ParseError {
        @Override
        public String toString() {
            return "ParseError [error=" + error + "]";
        }

        private String error = "";

        public ParseError(String error) {
            this.error = error;
        }

        public ParseError() {
        }
    }

    private TokenHandler tokens;
    private final HashMap<TokenType, Supplier<Result<Instruction, ParseError>>> parsers = new HashMap<>() {
        {
            put(TokenType.MATH, () -> parseMath());
        }
    };

    public Parser(LinkedList<Token> tokens) {
        this.tokens = new TokenHandler(tokens);
    }

    private static boolean isMathOperator(Token op) {
        return switch (op.getType()) {
            case AND, OR, XOR, NOT, ADD, SUBTRACT, MULTIPLY -> true;
            default -> false;
        };
    }

    public static Operation tokenToOperator(Token op) {
        return switch (op.getType()) {
            case AND -> Operation.AND;
            case OR -> Operation.OR;
            case XOR -> Operation.XOR;
            case NOT -> Operation.NOT;
            case LEFT -> Operation.LEFT_SHIFT;
            case RIGHT -> Operation.RIGHT_SHIFT;
            case ADD -> Operation.ADD;
            case SUBTRACT -> Operation.SUB;
            case MULTIPLY -> Operation.MUL;
            case EQUAL -> Operation.EQ;
            case UNEQUAL -> Operation.NE;
            case LESSOREQUAL -> Operation.LE;
            case GREATER -> Operation.GT;
            case GREATEROREQUAL -> Operation.GE;
            case LESS -> Operation.LT;
            default -> Operation.NOP;
        };
    }

    public Result<Instruction, ParseError> parseMath() {
        return resultToOptional(() -> tokens.Remove(), new ParseError("could not get math op"))
                .filterOrErr(Parser::isMathOperator, new ParseError("not a math op")).map(
                        Parser::tokenToOperator)
                .map(op -> Instruction.buildWithType(InstructionType.MATH).operation(op))
                .flatMap(instruction -> parseRegisters()
                        .filterOrErr(registers -> registers instanceof ThreeR || registers instanceof TwoR,
                                new ParseError("not twor or threer"))
                        .map(registers -> instruction.registers(registers)));
    }

    public Result<RegisterFormat, ParseError> parseRegisters() {
        return parseRegister().<RegisterFormat>map(r1 -> parseRegister()
                .map(r2 -> parseRegister().<RegisterFormat>map(r3 -> new ThreeR(r1, r2, r3)).or(new TwoR(r1, r2)))
                .or(new DestOnly(r1)));
    }

    public Result<Register, ParseError> parseRegister() {
        return resultToOptional(() -> tokens.MatchAndRemove(TokenType.REGISTER),
                new ParseError("could not parse register")).map(r -> new Register(r.getValue().get()));
    }

    private static Result<Token, ParseError> resultToOptional(Supplier<Optional<Token>> token, ParseError orElse) {
        return token.get().map(t -> Ok.<Token, ParseError>ok(t)).orElse(Err.<Token, ParseError>err(orElse));
    }

    public Result<Instruction, ParseError> parseLine() {
        return resultToOptional(() -> tokens.Remove(), new ParseError("could not find new start token"))
                .map(token -> parsers.get(token.getType()))
                .flatMap(parser -> parser == null
                        ? Err.<Instruction, ParseError>err(new ParseError("could not find parser for that keyword"))
                        : parser.get());
    }

    public Result<LinkedList<Instruction>, ParseError> parse() {
        var result = new LinkedList<Instruction>();
        do {
            switch (parseLine()) {
                case Err<Instruction, ParseError> e -> {
                    return Err.err(e.getValue());
                }
                case Ok<Instruction, ParseError> o -> result.add(o.getValue());
            }
            ;
        } while (tokens.MatchAndRemove(TokenType.NEWLINE).isPresent() && tokens.MoreTokens());
        if (tokens.MoreTokens()) {
            return Err.err(new ParseError("found extra tokens"));
        } else {
            return Ok.<LinkedList<Instruction>, ParseError>ok(result);
        }
    }
}
