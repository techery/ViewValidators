package techery.io.library.validations;

import org.immutables.value.Value;

import rx.functions.Func1;

public class Validations {
    private Validations() {
    }

    public interface Validation<T> extends Func1<T, Boolean> {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Length
    ///////////////////////////////////////////////////////////////////////////

    public interface LengthValidation extends Validation<Integer> {
        int limit();
    }

    @Value.Immutable
    public static abstract class ELengthValidation implements LengthValidation {
        @Value.Derived
        @Override public Boolean call(Integer value) {
            return value == limit();
        }

        public static ELengthValidation of(int limit) {
            return ImmutableELengthValidation.builder().limit(limit).build();
        }
    }

    @Value.Immutable
    public static abstract class GTLengthValidation implements LengthValidation {
        @Value.Derived
        @Override public Boolean call(Integer value) {
            return value > limit();
        }

        public static GTLengthValidation of(int limit) {
            return ImmutableGTLengthValidation.builder().limit(limit).build();
        }
    }

    @Value.Immutable
    public static abstract class GTELengthValidation implements LengthValidation {
        @Value.Derived
        @Override public Boolean call(Integer value) {
            return value >= limit();
        }

        public static GTELengthValidation of(int limit) {
            return ImmutableGTELengthValidation.builder().limit(limit).build();
        }
    }

    @Value.Immutable
    public static abstract class LTLengthValidation implements LengthValidation {
        @Value.Derived
        @Override public Boolean call(Integer value) {
            return value < limit();
        }

        public static LTLengthValidation of(int limit) {
            return ImmutableLTLengthValidation.builder().limit(limit).build();
        }
    }

    @Value.Immutable
    public static abstract class LTELengthValidation implements LengthValidation {
        @Value.Derived
        @Override public Boolean call(Integer value) {
            return value <= limit();
        }

        public static LTELengthValidation of(int limit) {
            return ImmutableLTELengthValidation.builder().limit(limit).build();
        }
    }
}
