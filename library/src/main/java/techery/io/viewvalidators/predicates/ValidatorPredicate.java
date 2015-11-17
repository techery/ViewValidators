package techery.io.viewvalidators.predicates;

import android.view.View;

import java.lang.ref.WeakReference;

import rx.functions.Func1;
import techery.io.viewvalidators.results.ValidationResult;

import static techery.io.viewvalidators.results.ValidationResult.ValidationStatus.ERROR;
import static techery.io.viewvalidators.results.ValidationResult.ValidationStatus.SUCCESS;

public class ValidatorPredicate implements Func1<Boolean, ValidationResult> {

    public static ValidationResult resultFor(View view, boolean isValid) {
        return new ValidatorPredicate(view).call(isValid);
    }

    private WeakReference<View> view;

    public ValidatorPredicate(View view) {
        this.view = new WeakReference<>(view);
    }

    @Override public ValidationResult call(Boolean isValid) {
        View view = this.view.get();
        ValidationResult result;
        if (isValid) {
            result = ValidationResult.of(SUCCESS, view);
        } else {
            result = ValidationResult.of(ERROR, view);
        }
        return result;
    }

}
