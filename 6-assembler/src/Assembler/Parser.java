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
    /*
     * TODO: better error messages
     */
    public class ParseError {
        @Override

        public String toString() {
            return "ParseError [error=" + error + " in token " + lastToken + "]";
        }

        private String error = "";
        private Optional<Token> lastToken;

        public ParseError(String error) {
            this();
            this.error = error;
        }

        public ParseError() {
            lastToken = currentToken;
        }
    }

    private TokenHandler tokens;
    private final HashMap<TokenType, Supplier<Result<Instruction, ParseError>>> parsers = new HashMap<>() {
        {
            /*
             * TODO: should we have to differnt jump instruction b/c their both
             * essentially NoR
             * TODO: for 3r push an branch they are the same as their repsective
             * 2rs, becuase they dont use their rd (make more instructin?)
             */
            put(TokenType.MATH, parseInstruction(InstructionType.MATH,
                    Optional.of(Parser::isMathOperator),
                    Optional.empty(), Optional.empty(),
                    Optional.of(false),
                    Optional.of(false)));
            put(TokenType.BRANCH,
                    parseInstruction(InstructionType.BRANCH,
                            Optional.of(Parser::isBooleanOperator),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.of(true),
                            Optional.of(true)));
            put(TokenType.CALLIF,
                    parseInstruction(InstructionType.CALL,
                            Optional.of(Parser::isBooleanOperator),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.of(true),
                            Optional.of(true)));
            put(TokenType.JUMP, parseInstruction(InstructionType.BRANCH,
                    Optional.empty(),
                    Optional.of(true), Optional.of(true),
                    Optional.empty(), Optional.empty()));
            put(TokenType.CALL, parseInstruction(InstructionType.CALL,
                    Optional.empty(),
                    Optional.of(true), Optional.of(true),
                    Optional.empty(), Optional.empty()));
            put(TokenType.PUSH, parseInstruction(InstructionType.PUSH,
                    Optional.of(Parser::isMathOperator),
                    Optional.empty(), Optional.of(true),
                    Optional.of(false),
                    Optional.of(false)));
            put(TokenType.LOAD, parseInstruction(InstructionType.LOAD,
                    Optional.empty(), Optional.empty(),
                    Optional.of(true), Optional.of(true),
                    Optional.of(false)));
            put(TokenType.STORE, parseInstruction(InstructionType.STORE,
                    Optional.empty(), Optional.empty(),
                    Optional.of(true), Optional.of(true),
                    Optional.of(false)));
            put(TokenType.PEEK, parseInstruction(InstructionType.POP,
                    Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.of(true),
                    Optional.of(false)));
            put(TokenType.COPY, () -> parseRegister().map(
                    r -> Instruction.buildWithType(InstructionType.MATH)
                            .registers(new DestOnly(r)))
                    .flatMap(instruction -> parseNumber()
                            .map(n -> instruction.immediate(n))));
            put(TokenType.POP, () -> parseRegister().map(r -> Instruction
                    .buildWithType(InstructionType.POP)
                    .registers(new DestOnly(r))));
            put(TokenType.HALT, () -> Ok.ok(Instruction
                    .buildWithType(InstructionType.MATH)));
            put(TokenType.RETURN, () -> Ok.ok(Instruction
                    .buildWithType(InstructionType.LOAD)));
            put(TokenType.INTERRUPT, () -> parseNumber()
                    .map(n -> Instruction.buildWithType(
                            InstructionType.POP)
                            .immediate(n)));
        }
    };
    private Optional<Token> currentToken = Optional.empty();

    public Parser(LinkedList<Token> tokens) {
        this.tokens = new TokenHandler(tokens);
    }

    // verifys that current token is a math operator
    private static boolean isMathOperator(Token op) {
        return switch (op.getType()) {
            case AND, OR, XOR, NOT, ADD, SUBTRACT, MULTIPLY, LSHIFT, RSHIFT -> true;
            default -> false;
        };
    }

    // verifys that current token is a boolean operator
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

    // this is what effectivly in most cases ends up doing all the grunt work for
    // parsing a single line (statement)
    // it takes in what instruction this should result in math, call, branch, ...
    // (type)
    // and a optional function that tells if and how to parse functions like add,
    // sub, mul, ... (operator)
    // it then takes in 4 optional booleans denoting whether a particular
    // instruction supports a particular register format
    // if the option is empty it means that register format is not supported at all,
    // if its present but is false that means that this instruction with this
    // register format should not have an immediate value otherwise it means that
    // the instruction with the register format must have an immediate value
    // example: if TwoR is Optional.of(false) then the instruction is allowed to
    // have two register, but not have an immediate value
    //
    // NOTE: there is a lot of currying (functions/lambdas) returning other
    // (functions/lambdas)
    // this is because this was written in a functionalish style
    //
    public Supplier<Result<Instruction, ParseError>> parseInstruction(InstructionType type,
            Optional<Function<Token, Boolean>> operator, Optional<Boolean> NoR,
            Optional<Boolean> DestOnly,
            Optional<Boolean> TwoR, Optional<Boolean> ThreeR) {
        // given a flag that says whether or not to have an immdiate value parse an
        // immediate when needed and add it to hte instruction
        Function<Boolean, Function<Instruction, Result<Instruction, ParseError>>> parseImmediate = immediateEnabled -> instruction -> immediateEnabled
                ? parseNumber().map(instruction::immediate)
                : Ok.ok(instruction);
        // given a set of registers and an instruction put the registers in the
        // instruction
        Function<RegisterFormat, Function<Instruction, Instruction>> setInstructionRegisters = registers -> instruction -> instruction
                .registers(registers);

        // given some whether an instruction has an immediate, some registers and an
        // instruction
        // parse the immediate and put it and the registers in the instruction
        // and verify if the instrcution format parsed is allowed for this instruction
        Function<Optional<Boolean>, Function<RegisterFormat, Function<Instruction, Result<Instruction, ParseError>>>> validateRegister = (
                hasImmediate) -> registers -> instruction -> hasImmediate
                        // parse and set immediate
                        .map(parseImmediate)
                        // if instruction not supported just error
                        .orElse(bad_instruction -> Err.err(new ParseError("invalid register format")))
                        .apply(instruction)
                        // set registers
                        .map(setInstructionRegisters.apply(
                                registers));
        // parse registers and immediate using the above functions
        Function<Instruction, Result<Instruction, ParseError>> parseRegisters = instruction -> 
        
        // validate registers parsed based on the format that the registers were parsed
        // to and the parameters to this java method
        ((Function<RegisterFormat, Result<Instruction, ParseError>>) registers ->
        validateRegister
                .apply(
                        registers instanceof NoR ? NoR
                                : registers instanceof DestOnly ? DestOnly
                                        : registers instanceof TwoR ? TwoR
                                                : ThreeR)
                .apply(registers).apply(instruction))
                // parse the regiisters, this may seem a bit counterintutive, but the casted function above where we match on the regsiter format is beind applied to the result of this
                .apply(parseRegisters());

        // if this instruction supports function like add, sub, ...
        return () -> operator.<Function<Instruction, Result<Instruction, ParseError>>>map(
                // parse the function
                operatorValidator -> instruction -> (
                // try to get the function token    
                resultToOptional(
                        () -> Remove(),
                        new ParseError("could not get function"))
                        // make sure its a valid token for this type of function
                        .filterOrErr(operatorValidator, new ParseError(
                                "expected function"))
                        .map(Parser::tokenToOperator)
                        // and add it to the instruction
                        .map(instruction::operation)))
                        // if mo function dont parse function
                .orElseGet(() -> instruction -> Ok.ok(instruction))
                // create the instruction, and give it the function parser
                .apply(Instruction.buildWithType(type))
                // and then use then add the register to the instruction
                .flatMap(parseRegisters);
    }

    // just overides token handles behaviour so that we can keep track of current
    // token for better errors
    private Optional<Token> Remove() {
        var token = tokens.Remove();
        if (token.isPresent()) {
            currentToken = token;
        }
        return token;
    }

    // tries to parse up to 3 register of depending on how many register it can find
    public RegisterFormat parseRegisters() {
        return parseRegister().<RegisterFormat>map(r1 -> parseRegister()
                .map(r2 -> parseRegister()
                        // if we are able to parse three register make 3R register format
                        .<RegisterFormat>map(r3 -> new ThreeR(
                                r1,
                                r2,
                                r3))
                        // if we are only able to parse two register make 2R register format
                        .orElse(new TwoR(r1, r2)))
                // if we are only able to parse one register make 1R register format
                .orElse(new DestOnly(r1)))
                // if there are no register to parse then in must be in 0R register format
                .orElse(() -> new NoR());
    }

    public Result<Register, ParseError> parseRegister() {
        return resultToOptional(() -> MatchAndRemove(TokenType.REGISTER),
                new ParseError("could not parse register"))
                .map(r -> new Register(r.getValue().get()));
    }

    public Result<Integer, ParseError> parseNumber() {
        return resultToOptional(() -> MatchAndRemove(TokenType.VALUE),
                new ParseError("could not parse number"))
                .map(r -> r.getValue().get());
    }

    // just overides token handles behaviour so that we can keep track of current
    // token for better errors
    private Optional<Token> MatchAndRemove(TokenType value) {
        var token = tokens.MatchAndRemove(value);
        if (token.isPresent()) {
            currentToken = token;
        }
        return token;
    }

    // takes something returns an optional token and an error
    // and turns into a result - which is like an option except instead of having
    // empty it can be ok or error (kinda like java exceptions but just using raw
    // types)
    private static Result<Token, ParseError> resultToOptional(Supplier<Optional<Token>> token,
            ParseError orElse) {
        return token.get().map(t -> Ok.<Token, ParseError>ok(t))
                .orElse(Err.<Token, ParseError>err(orElse));
    }

    public Result<Instruction, ParseError> parseLine() {
        // get start token determins what type of instruction this will be
        return resultToOptional(() -> Remove(),
                new ParseError("could not find new start token"))
                // get the parser for the given start token
                .map(token -> parsers.getOrDefault(token.getType(),
                        // or make a dummy parser that does nothing but error out immediatly
                        () -> Err.<Instruction, ParseError>err(
                                new ParseError(
                                        "could not find parser for that keyword "
                                                + token))))
                // run the parser
                .flatMap(Supplier::get);
    }

    public Result<LinkedList<Instruction>, ParseError> parse() {
        var result = new LinkedList<Instruction>();
        // parse lines while there are newline delimeted statements
        do {
            switch (parseLine()) {
                case Err<Instruction, ParseError> e -> {
                    return Err.err(e.getValue());
                }
                case Ok<Instruction, ParseError> o ->
                    result.add(o.getValue());
            }
        } while (MatchAndRemove(TokenType.NEWLINE).isPresent() && tokens.MoreTokens());
        // if we reach a point where there are more tokens but they are after a
        // statement then the assembly is incorrect
        if (tokens.MoreTokens()) {
            return Err.err(new ParseError("found extra tokens"));
        } else {
            return Ok.<LinkedList<Instruction>, ParseError>ok(result);
        }
    }
}
