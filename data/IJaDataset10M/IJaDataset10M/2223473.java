package hu.akarnokd.reactiv4java;

/**
 * A class representing a value or nothing.
 * @author akarnokd
 * @param <T> the type of the contained object
 */
public abstract class Option<T> {

    /** @return query for the value. */
    public abstract T value();

    /**
	 * The helper class representing an option holding nothing.
	 * @author akarnokd
	 *
	 * @param <T> the type of the nothing - not really used but required by the types
	 */
    public static final class None<T> extends Option<T> {

        /** Single instance! */
        private None() {
        }

        @Override
        public T value() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "None";
        }
    }

    /**
	 * A helper class representing an option holding something of T.
	 * @author karnokd
	 *
	 * @param <T> the type of the contained stuff
	 */
    public static final class Some<T> extends Option<T> {

        /** The value that is hold by this option. */
        private final T value;

        /**
		 * Construct the some with a value.
		 * @param value the value.
		 */
        private Some(T value) {
            this.value = value;
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public String toString() {
            return "Some with " + value;
        }
    }

    /**
	 * Class representing an error option.
	 * Calling value on this will throw a RuntimeException which wraps
	 * the original exception.
	 * @author akarnokd, 2011.01.30.
	 * @param <T> the element type
	 */
    public static final class Error<T> extends Option<T> {

        /** The exception held. */
        private final Throwable ex;

        /**
		 * Constructor.
		 * @param ex the exception to hold
		 */
        private Error(Throwable ex) {
            this.ex = ex;
        }

        @Override
        public T value() {
            throw new RuntimeException(ex);
        }

        @Override
        public String toString() {
            return ex.toString();
        }

        /** @return the contained throwable value. */
        public Throwable error() {
            return ex;
        }
    }

    /** The single instance of the nothingness. */
    private static final None<Void> NONE = new None<Void>();

    /**
	 * Returns a none of T.
	 * @param <T> the type of the T
	 * @return the None of T
	 */
    @SuppressWarnings("unchecked")
    public static <T> None<T> none() {
        return (None<T>) NONE;
    }

    /**
	 * Create a new Some instance with the supplied value.
	 * @param <T> the value type
	 * @param value the initial value
	 * @return the some object
	 */
    public static <T> Some<T> some(T value) {
        return new Some<T>(value);
    }

    /**
	 * Create an error instance with the given Throwable.
	 * @param <T> the element type, irrelevant
	 * @param t the throwable
	 * @return the error instance
	 */
    public static <T> Error<T> error(Throwable t) {
        return new Error<T>(t);
    }

    /**
	 * Returns true if the option is of type Error.
	 * @param o the option
	 * @return true if the option is of type Error.
	 */
    public static boolean isError(Option<?> o) {
        return o instanceof Error<?>;
    }

    /**
	 * Returns true if the option is of type None.
	 * @param o the option
	 * @return true if the option is of type None.
	 */
    public static boolean isNone(Option<?> o) {
        return o == NONE;
    }

    /**
	 * Returns true if the option is of type Some.
	 * @param o the option
	 * @return true if the option is of type Some.
	 */
    public static boolean isSome(Option<?> o) {
        return o instanceof Some<?>;
    }
}
