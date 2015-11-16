package techery.io.library;

import android.view.View;

import rx.functions.Action1;
import rx.functions.Action2;

public class SimpleEnabledValidationSubscriber extends ValidatorSubscriber {
    public SimpleEnabledValidationSubscriber() {
        super(new Action1<View>() {
            @Override
            public void call(View view) {
                view.setEnabled(true);
            }
        }, new Action2<View, Exception>() {
            @Override
            public void call(View view, Exception e) {
                view.setEnabled(false);
            }
        });
    }

    public SimpleEnabledValidationSubscriber(final View viewToControl) {
        super(new Action1<View>() {
            @Override
            public void call(View view) {
                if (viewToControl != null) view = viewToControl;
                view.setEnabled(true);
            }
        }, new Action2<View, Exception>() {
            @Override
            public void call(View view, Exception e) {
                if (viewToControl != null) view = viewToControl;
                view.setEnabled(false);
            }
        });
    }
}
