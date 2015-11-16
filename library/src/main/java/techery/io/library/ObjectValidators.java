package techery.io.library;


import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class ObjectValidators {

    public static <U> ListValidator listValidator(Observable<List<U>> source, Func1<List, ValidationResult> predicate) {
        return ListValidator.build(source).predicate(predicate).build();
    }
}
