package Assembler;

// import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

// TODO: better explantion of result
// Results can either be ok, or error
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
        public T orElse(T r1) {
            return ok;
        }

        public T getValue() {
            return ok;
        }

        // @Override
        // public Result<T, E> fromOptional(Optional<T> value, E orElse) {
        // throw new UnsupportedOperationException("Unimplemented method
        // 'fromOptional'");
        // }

        // @Override
        // public Result<T, E> fromOptional(Optional<T> value, Supplier<E> orElse) {
        // throw new UnsupportedOperationException("Unimplemented method
        // 'fromOptional'");
        // }

        @Override
        public T orElse(Supplier<T> r1) {
            return ok;
        }

        @Override
        public Result<T, E> or(Supplier<Result<T, E>> or) {
            return this;
        }

        @Override
        public Result<T, E> or(Result<T, E> or) {
            return this;
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
        public T orElse(T r1) {
            return r1;
        }

        // @Override
        // public Result<T, E> fromOptional(Optional<T> value, E orElse) {
        // throw new UnsupportedOperationException("Unimplemented method
        // 'fromOptional'");
        // }

        // @Override
        // public Result<T, E> fromOptional(Optional<T> value, Supplier<E> orElse) {
        // throw new UnsupportedOperationException("Unimplemented method
        // 'fromOptional'");
        // }

        @Override
        public T orElse(Supplier<T> r1) {
            return r1.get();
        }

        @Override
        public Result<T, E> or(Supplier<Result<T, E>> or) {
            return or.get();
        }

        @Override
        public Result<T, E> or(Result<T, E> or) {
            return or;
        }
    }

    public <U> Result<U, E> map(Function<T, U> mapper);

    public T get();

    // public Result<T, E> fromOptional(Optional<T> value, E orElse);

    // public Result<T, E> fromOptional(Optional<T> value, Supplier<E> orElse);

    public <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper);

    public Result<T, E> filterOrErr(Function<T, Boolean> filter, E orElse);

    public T orElse(T r1);

    public T orElse(Supplier<T> r1);

    public Result<T, E> or(Supplier<Result<T, E>> or);

    public Result<T, E> or(Result<T, E> or);

}
