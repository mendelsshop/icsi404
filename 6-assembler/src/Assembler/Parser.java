package Assembler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import Assembler.Instruction.*;
import Assembler.Instruction.RegisterFormat.*;
import Assembler.Result.*;
import Assembler.Token.TokenType;

public class Parser {
        /* * TODO: better error messages * TODO: stuff besides math */ public class ParseError {
                @Override

                public String toString() {
                        return "ParseError [error=" + error + " in token " +  lastToken+"]";
                }

                private String error = "";
                private Optional<Token> lastToken;
                public ParseError(String error) {
                this();
                        this.error = error;
                }

                public ParseError() {
                                                lastToken  = currentToken;
                }
        }

        private TokenHandler tokens;
        private final HashMap<TokenType, Supplier<Result<Instruction, ParseError>>> parsers = new HashMap<>() {
                {
                        /*
                        * TODO: should we have to differnt jump instruction b/c their both essentially NoR
                        * TODO: for 3r push an branch they are the same as their repsective 2rs, becuase they dont use their rd (make more instructin?)
                         * */
                        put(TokenType.MATH, parseInstruction(InstructionType.MATH, Optional.of(Parser::isMathOperator),
                                        Optional.empty(), Optional.empty(), Optional.of(false), Optional.of(false)));
                        put(TokenType.BRANCH,
                                        parseInstruction(InstructionType.BRANCH, Optional.of(Parser::isBooleanOperator),
                                                        Optional.empty(), Optional.empty(), Optional.of(true),
                                                        Optional.of(true)));
                        put(TokenType.CALLIF,
                                        parseInstruction(InstructionType.CALL, Optional.of(Parser::isBooleanOperator),
                                                        Optional.empty(), Optional.empty(), Optional.of(true),
                                                        Optional.of(true)));
                        put(TokenType.JUMP, parseInstruction(InstructionType.BRANCH, Optional.empty(),
                                        Optional.of(true), Optional.of(true), Optional.empty(), Optional.empty()));
                        put(TokenType.CALL, parseInstruction(InstructionType.CALL, Optional.empty(),
                                        Optional.of(true), Optional.of(true), Optional.empty(), Optional.empty()));
                        put(TokenType.PUSH, parseInstruction(InstructionType.PUSH, Optional.of(Parser::isMathOperator),
                                        Optional.empty(), Optional.of(true), Optional.of(false), Optional.of(false)));
                        put(TokenType.LOAD, parseInstruction(InstructionType.LOAD, Optional.empty(), Optional.empty(),
                                        Optional.of(true), Optional.of(true), Optional.of(false)));
                        put(TokenType.STORE, parseInstruction(InstructionType.STORE, Optional.empty(), Optional.empty(),
                                        Optional.of(true), Optional.of(true), Optional.of(false)));
                        put(TokenType.PEEK, parseInstruction(InstructionType.POP, Optional.empty(), Optional.empty(),
                                        Optional.empty(), Optional.of(true), Optional.of(false)));
                        put(TokenType.COPY, () -> parseRegister().map(
                                        r -> Instruction.buildWithType(InstructionType.MATH).registers(new DestOnly(r)))
                                        .flatMap(instruction -> parseNumber().map(n -> instruction.immediate(n))));
                        put(TokenType.POP, () -> parseRegister().map(r -> Instruction.buildWithType(InstructionType.POP)
                                        .registers(new DestOnly(r))));
                        put(TokenType.HALT, () -> Ok.ok(Instruction.buildWithType(InstructionType.MATH)));
                        put(TokenType.RETURN, () -> Ok.ok(Instruction.buildWithType(InstructionType.LOAD)));
                        put(TokenType.INTERRUPT, () -> parseNumber()
                                        .map(n -> Instruction.buildWithType(InstructionType.POP).immediate(n)));
                }
        };
		private Optional<Token> currentToken = Optional.empty() ;

        public Parser(LinkedList<Token> tokens) {
                this.tokens = new TokenHandler(tokens);
        }

        private static boolean isMathOperator(Token op) {
                return switch (op.getType()) {
                        case AND, OR, XOR, NOT, ADD, SUBTRACT, MULTIPLY, LSHIFT, RSHIFT -> true;
                        default -> false;
                };
        }

        public static boolean isBooleanOperator(Token op) {
                return switch (op.getType()) {
                        case EQUAL, UNEQUAL, GREATER, GREATEROREQUAL, LESS, LESSOREQUAL -> true;
                        default -> false;
                };
        }

        public static Operation tokenToOperator(Token op) {
                return switch (op.getType()) {
                        case AND -> Operation.AND;
                        case OR -> Operation.OR;
                        case XOR -> Operation.XOR;
                        case NOT -> Operation.NOT;
                        case LSHIFT -> Operation.LEFT_SHIFT;
                        case RSHIFT -> Operation.RIGHT_SHIFT;
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

        /*     */ public Supplier<Result<Instruction, ParseError>> parseInstruction(InstructionType type,
                        Optional<Function<Token, Boolean>> operator, Optional<Boolean> NoR, Optional<Boolean> DestOnly,
                        Optional<Boolean> TwoR, Optional<Boolean> ThreeR) {
                Function<Boolean, Function<Instruction, Result<Instruction, ParseError>>> validateImmediate = immediateEnabled->  instruction  ->  immediateEnabled ? parseNumber().map(number -> instruction.immediate(number)) :  Result.Ok.ok( instruction) ;
                Function<RegisterFormat, Function<Instruction, Instruction>> setInstructionRegisters = registers -> instruction -> instruction.registers(registers);
                Function<Optional<Boolean>, Function<RegisterFormat, Function<Instruction, Result<Instruction, ParseError>>>> validateRegister = (
                                allowed) -> registers -> instruction -> allowed
                                                .map(validateImmediate).orElse(x -> Ok.ok(x)).apply(instruction).map(setInstructionRegisters.apply(registers));
                Function<Instruction, Result<Instruction, ParseError>> parseRegisters = instruction ->  ((Function<RegisterFormat, Result<Instruction, ParseError>>) registers -> 
                                                validateRegister
                                                .apply(
                                                registers instanceof NoR ? NoR :
                                                registers instanceof DestOnly ? DestOnly
                                                                                : registers instanceof TwoR ? TwoR
                                                                                                : ThreeR)
                                                .apply(registers).apply(instruction)).apply(parseRegisters());
                return () -> operator.<Function<Instruction, Result<Instruction, ParseError>>>map(
                                operatorValidator -> instruction -> (resultToOptional(() -> Remove(),
                                                new ParseError("could not get math op"))
                                                .filterOrErr(operatorValidator, new ParseError("not a math op"))
                                                .map(Parser::tokenToOperator).map(op -> instruction.operation(op))))
                                .orElseGet(() -> instruction -> Ok.ok(instruction))
                                .apply(Instruction.buildWithType(type)).flatMap(parseRegisters);
        }

        private Optional<Token> Remove() {
                var token = tokens.Remove();
                                if (token.isPresent()) {
                                currentToken = token;
                                }
                                return token;
		}

		public RegisterFormat parseRegisters() {
                return parseRegister().<RegisterFormat>map(r1 -> parseRegister().map(r2 -> parseRegister()
                                .<RegisterFormat>map(r3 -> new ThreeR(r1, r2, r3)).orElse(new TwoR(r1, r2)))
                                .orElse(new DestOnly(r1))).orElse(() -> new NoR());
        }

        public Result<Register, ParseError> parseRegister() {
                return resultToOptional(() -> MatchAndRemove(TokenType.REGISTER),
                                new ParseError("could not parse register")).map(r -> new Register(r.getValue().get()));
        }


        public Result<Integer, ParseError> parseNumber() {
                return resultToOptional(() -> MatchAndRemove(TokenType.VALUE),
                                new ParseError("could not parse number")).map(r -> r.getValue().get());
        }

        private Optional<Token> MatchAndRemove(TokenType value) {
                var token = tokens.MatchAndRemove(value);
                                if (token.isPresent()) {
                                currentToken = token;
                                }
                                return token;
		}

		private static Result<Token, ParseError> resultToOptional(Supplier<Optional<Token>> token, ParseError orElse) {
                return token.get().map(t -> Ok.<Token, ParseError>ok(t)).orElse(Err.<Token, ParseError>err(orElse));
        }

        public Result<Instruction, ParseError> parseLine() {
                return resultToOptional(() -> Remove(), new ParseError("could not find new start token"))
                                .map(token -> parsers.getOrDefault(token.getType(), () -> Err.<Instruction, ParseError>err(new ParseError(
                                                                "could not find parser for that keyword " + token))
 ))
                                .flatMap(parser -> 
                                                 parser.get());
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
                } while (MatchAndRemove(TokenType.NEWLINE).isPresent() && tokens.MoreTokens());
                if (tokens.MoreTokens()) {
                        return Err.err(new ParseError("found extra tokens"));
                } else {
                        return Ok.<LinkedList<Instruction>, ParseError>ok(result);
                }
        }
}
