package techery.io.library.subscribers;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

import rx.functions.Action1;
import rx.functions.Action2;
import techery.io.library.R;

public class ClickProxyValidationSubscriber extends ValidatorSubscriber {

    public ClickProxyValidationSubscriber(View.OnClickListener listener) {
        this(listener, null);
    }

    public ClickProxyValidationSubscriber(final View.OnClickListener listener, final View viewToControl) {
        super(new Action1<View>() {
            @Override
            public void call(View view) {
                if (viewToControl != null) view = viewToControl;
                view.setTag(R.id.validation_key_passed, new Object());
                attachListenerIfNeeded(view, listener);
            }
        }, new Action2<View, Exception>() {
            @Override
            public void call(View view, Exception e) {
                if (viewToControl != null) view = viewToControl;
                view.setTag(R.id.validation_key_passed, null);
                attachListenerIfNeeded(view, listener);
            }
        });
    }

    private static void attachListenerIfNeeded(View view, View.OnClickListener listener) {
        if (view.getTag(R.id.validation_key_click_set) == null) {
            view.setTag(R.id.validation_key_click_set, new Object());
            view.setOnClickListener(new ProxyListener(getOnClickListener(view), listener));
        }
    }

    public static class ProxyListener implements View.OnClickListener {

        View.OnClickListener okListener;
        View.OnClickListener failListener;

        public ProxyListener(View.OnClickListener okListener, View.OnClickListener failListener) {
            this.okListener = okListener;
            this.failListener = failListener;
        }

        @Override public void onClick(View v) {
            if (v.getTag(R.id.validation_key_passed) == null) {
                failListener.onClick(v);
            } else {
                okListener.onClick(v);
            }
        }

    }

    /**
     * Returns the current View.OnClickListener for the given View
     *
     * @param view the View whose click listener to retrieve
     * @return the View.OnClickListener attached to the view; null if it could not be retrieved
     */
    private static View.OnClickListener getOnClickListener(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        String lInfoStr = "android.view.View$ListenerInfo";

        try {
            Field listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo");
            Object listenerInfo = null;

            if (listenerField != null) {
                listenerField.setAccessible(true);
                listenerInfo = listenerField.get(view);
            }

            Field clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnClickListener");

            if (clickListenerField != null && listenerInfo != null) {
                retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);
            }
        } catch (NoSuchFieldException ex) {
            Log.e("Reflection", "No Such Field.");
        } catch (IllegalAccessException ex) {
            Log.e("Reflection", "Illegal Access.");
        } catch (ClassNotFoundException ex) {
            Log.e("Reflection", "Class Not Found.");
        }

        return retrievedListener;
    }
}
