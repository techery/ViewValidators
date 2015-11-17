package techery.io.viewvalidators.validators;

import android.support.annotation.Nullable;

import org.immutables.value.Value;

import rx.Observable;
import rx.functions.Func1;
import techery.io.viewvalidators.results.ValidationResult;
import techery.io.viewvalidators.validators.ImmutableProxyValidator;


@Value.Immutable
public abstract class ProxyValidator extends Validator<ValidationResult> {

    public static ProxyValidator of(Observable<ValidationResult> source) {
        return ImmutableProxyValidator.builder().source(source).build();
    }

    @Value.Derived
    @Override protected Observable<ValidationResult> create() {
        return source();
    }

    @Nullable
    @Override public abstract Func1<ValidationResult, ValidationResult> predicate();
}
