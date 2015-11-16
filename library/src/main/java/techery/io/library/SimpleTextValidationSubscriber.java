package techery.io.library;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import rx.functions.Action1;
import rx.functions.Action2;

public class SimpleTextValidationSubscriber extends ValidatorSubscriber {

    public SimpleTextValidationSubscriber() {
        this(null);
    }

    public SimpleTextValidationSubscriber(String error) {
        super(new SuccessAction(), new FailAction(error));
    }

    public SimpleTextValidationSubscriber(@StringRes int error) {
        super(new SuccessAction(), new FailAction(error));
    }

    private static class SuccessAction implements Action1<View> {
        @Override public void call(View view) {
            ((TextView) view).setError(null);
        }
    }

    private static class FailAction implements Action2<View, Exception> {
        private String error;
        private int errorRes;

        private FailAction(String error) {this.error = error;}

        private FailAction(@StringRes int errorRes) {this.errorRes = errorRes;}

        @Override public void call(View view, Exception e) {
            String message;
            if (error != null) message = error;
            else if (errorRes != 0) message = view.getResources().getString(errorRes);
            else message = e.getLocalizedMessage();
            ((TextView) view).setError(message);
            //
            view.requestFocus();
        }
    }

}
