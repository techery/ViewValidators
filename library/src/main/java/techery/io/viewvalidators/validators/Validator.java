package techery.io.viewvalidators.validators;

import org.immutables.value.Value;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import techery.io.viewvalidators.results.ImmutableValidationResult;
import techery.io.viewvalidators.results.ValidationResult;

public abstract class Validator<T> {
    public abstract Func1<T, ValidationResult> predicate();
    public abstract Observable<T> source();
    protected abstract Observable<ValidationResult> create();

    private Observable<ValidationResult> saved;

    public Observable<ValidationResult> result() {
        if (saved == null) saved = create();
        return saved;
    }

    @Value.Derived
    public Validator<T> withDelay(long timeout, TimeUnit unit) {
        saved = result().throttleWithTimeout(timeout, unit);
        return this;
    }

    @Value.Derived
    public Validator<T> withError(final String message) {
        saved = result().map(new Func1<ValidationResult, ValidationResult>() {
            @Override
            public ValidationResult call(ValidationResult result) {
                ValidationResult mappedResult = result;
                if (result.status() == ValidationResult.ValidationStatus.ERROR) {
                    mappedResult = ImmutableValidationResult.copyOf(result)
                            .withError(new IllegalArgumentException(message));
                }
                return mappedResult;
            }
        });
        return this;
    }
}
