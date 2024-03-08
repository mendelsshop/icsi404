package Assembler;

// import java.util.Optional;
import java.util.function.Function;

public sealed interface Result<T, E> permits Result.Ok, Result.Err {

    public class GetException extends RuntimeException {
        public GetException(Object error) {
            super("called Result::get() on an `Err` value: " + error);
        }
    }

    public record Ok<T, E>(T ok) implements Result<T, E> {
        public static <T, E> Result<T, E> ok(T ok) {
            return new Ok<>(ok);
        }

        @Override
        public <U> Result<U, E> map(Function<T, U> mapper) {
            return new Ok<>(mapper.apply(ok));
        }

        @Override
        public T get() {
            return ok;
        }

        @Override
        public <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper) {
            return mapper.apply(ok);
        }

        @Override
        public Result<T, E> filterOrErr(Function<T, Boolean> filter, E orElse) {
            return filter.apply(ok) ? this : Err.err(orElse);
        }

        @Override
        public T or(T r1) {
            return ok;
        }

        public T getValue() {
            return ok;
        }
    }

    public record Err<T, E>(E error) implements Result<T, E> {
        public static <T, E> Result<T, E> err(E error) {
            return new Err<>(error);
        }

        public E getValue() {
            return error;
        }

        @Override
        public <U> Result<U, E> map(Function<T, U> mapper) {
            return err(error);
        }

        @Override
        public T get() {
            throw new GetException(error);
        }

        @Override
        public <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper) {
            return err(error);
        }

        @Override
        public Result<T, E> filterOrErr(Function<T, Boolean> filter, E orElse) {
            return this;
        }

        @Override
        public T or(T r1) {
            return r1;
        }
    }

    public <U> Result<U, E> map(Function<T, U> mapper);

    public T get();

    // public Result<T, E> fromOptional(Optional<T> value, E orElse);

    public <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper);

    public Result<T, E> filterOrErr(Function<T, Boolean> filter, E orElse);

    public T or(T r1);

}
