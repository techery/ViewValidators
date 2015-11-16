package techery.io.library.validators;

import org.immutables.value.Value;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import techery.io.library.results.ValidationResult;


@Value.Immutable
public abstract class ListValidator extends Validator<List> {

    public static <U> ImmutableListValidator.Builder build(Observable<List<U>> source) {
        return ImmutableListValidator.builder().source(source.cast(List.class));
    }

    @Value.Derived
    @Override protected Observable<ValidationResult> create() {
        return source().flatMap(new Func1<List, Observable<? extends ValidationResult>>() {
            @Override
            public Observable<? extends ValidationResult> call(final List list) {
                return Observable.create(new Observable.OnSubscribe<ValidationResult>() {
                    @Override
                    public void call(Subscriber<? super ValidationResult> subscriber) {
                        subscriber.onNext(ListValidator.this.predicate().call(list));
                    }
                });
            }
        });
    }

    @Override public ListValidator withDelay(long timeout, TimeUnit unit) {
        return (ListValidator) super.withDelay(timeout, unit);
    }

    @Override public ListValidator withError(String message) {
        return (ListValidator) super.withError(message);
    }
}
