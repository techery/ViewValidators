package techery.io.library.composers;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class InputValidatorComposer<T> extends DelayedComposer<T> {

    public InputValidatorComposer() {
        this(1000L);
    }

    public InputValidatorComposer(long timeout) {
        super(timeout);
    }

    @Override public Observable<T> call(Observable<T> observable) {
        return super.call(observable).distinctUntilChanged().observeOn(AndroidSchedulers.mainThread());
    }
}
