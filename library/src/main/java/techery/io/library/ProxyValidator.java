package techery.io.library;

import android.support.annotation.Nullable;

import org.immutables.value.Value;

import rx.Observable;
import rx.functions.Func1;

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
