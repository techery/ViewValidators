package techery.io.library.subscribers;

import android.view.View;

import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Action2;
import techery.io.library.results.ValidationResult;

public class ValidatorSubscriber extends Subscriber<ValidationResult> {
    public final Action1<View> onSuccess;
    public final Action2<View, Exception> onError;

    public ValidatorSubscriber(Action1<View> onSuccess) {
        this(onSuccess, null);
    }

    public ValidatorSubscriber(Action1<View> onSuccess, Action2<View, Exception> onError) {
        this.onSuccess = onSuccess;
        this.onError = onError;
    }


    @Override public void onCompleted() { }

    @Override public void onError(Throwable e) {

    }

    @Override public void onNext(ValidationResult result) {
        if (result == null) onError(new NullPointerException("Can't validate, view is null"));
        switch (result.status()) {
            case SUCCESS:
                if (onSuccess != null) onSuccess.call(result.view());
                break;
            case ERROR:
                if (onError != null) onError.call(result.view(), result.error());
                break;
        }

    }
}
