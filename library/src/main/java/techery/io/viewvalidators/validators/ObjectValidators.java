package techery.io.viewvalidators.validators;


import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import techery.io.viewvalidators.results.ValidationResult;


public class ObjectValidators {

    public static <U> ListValidator listValidator(Observable<List<U>> source, Func1<List, ValidationResult> predicate) {
        return ListValidator.build(source).predicate(predicate).build();
    }
}
