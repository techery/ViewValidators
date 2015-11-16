package techery.io.library.validators;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import org.immutables.value.Value;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import techery.io.library.results.ValidationResult;



@Value.Immutable
public abstract class TextValidator extends Validator<TextViewTextChangeEvent> {

    @Value.Derived
    @Override protected Observable<ValidationResult> create() {
        return source().flatMap(new Func1<TextViewTextChangeEvent, Observable<? extends ValidationResult>>() {
                                    @Override
                                    public Observable<? extends ValidationResult> call(final TextViewTextChangeEvent textEvent) {
                                        return Observable.create(new Observable.OnSubscribe<ValidationResult>() {
                                            @Override
                                            public void call(Subscriber<? super ValidationResult> subscriber) {
                                                subscriber.onNext(TextValidator.this.predicate().call(textEvent));
                                            }
                                        });
                                    }
                                }
        );
    }

    @Override public TextValidator withDelay(long timeout, TimeUnit unit) {
        return (TextValidator) super.withDelay(timeout, unit);
    }

    @Override public TextValidator withError(String message) {
        return (TextValidator) super.withError(message);
    }
}
