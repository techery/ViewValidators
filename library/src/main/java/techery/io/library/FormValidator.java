package techery.io.library;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

public class FormValidator {

    private List<Observable<ValidationResult>> validators;
    private PublishSubject<Void> listNotifier;

    public FormValidator() {
        validators = new ArrayList<>();
        listNotifier = PublishSubject.create();
    }

    public Observable<ValidationResult> add(Observable<ValidationResult> validator) {
        validators.add(validator);
        listNotifier.onNext(null);
        return validator;
    }

    public Observable<ValidationResult> remove(Observable<ValidationResult> validator) {
        validators.remove(validator);
        listNotifier.onNext(null);
        return validator;
    }

    public void clear() {
        validators.clear();
        listNotifier.onNext(null);
    }

    public List<Observable<ValidationResult>> validators() {
        return new ArrayList<>(validators);
    }

    public Observable<ValidationResult> allCombined(final View view) {
        return listNotifier.startWith((Void) null)
                .switchMap(new Func1<Void, Observable<? extends ValidationResult>>() {
                               @Override
                               public Observable<? extends ValidationResult> call(Void aVoid) {
                                   return WidgetValidators.allValid(view, validators).result();
                               }
                           }
                );
    }
}
