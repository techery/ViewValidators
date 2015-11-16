package techery.io.library.validators;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.FuncN;
import techery.io.library.validations.Validations.LengthValidation;
import techery.io.library.predicates.ValidatorPredicate;
import techery.io.library.results.ValidationResult;

import static techery.io.library.results.ValidationResult.ValidationStatus.ERROR;
import static techery.io.library.results.ValidationResult.ValidationStatus.SUCCESS;

/** Validation factories of {@link Validator} class */
public class WidgetValidators {
    private WidgetValidators() { }

    public static TextValidator textValidator(Observable<TextViewTextChangeEvent> textSignal, Func1<TextViewTextChangeEvent, ValidationResult> predicate) {
        return ImmutableTextValidator
                .builder().source(textSignal)
                .predicate(predicate)
                .build();
    }

    public static TextValidator stringValidator(Observable<TextViewTextChangeEvent> textSignal, final String toCheck) {
        return textValidator(textSignal, new Func1<TextViewTextChangeEvent, ValidationResult>() {
            @Override
            public ValidationResult call(TextViewTextChangeEvent textEvent) {
                boolean isValid = textEvent.text().toString().equals(toCheck);
                return ValidatorPredicate.resultFor(textEvent.view(), isValid);
            }
        });
    }

    public static TextValidator emailValidator(Observable<TextViewTextChangeEvent> textSignal) {
        return textValidator(textSignal, new Func1<TextViewTextChangeEvent, ValidationResult>() {
            @Override
            public ValidationResult call(TextViewTextChangeEvent textEvent) {
                boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(textEvent.text()).matches();
                return ValidatorPredicate.resultFor(textEvent.view(), isEmailValid);
            }
        });
    }

    public static TextValidator phoneValidator(Observable<TextViewTextChangeEvent> textSignal) {
        return textValidator(textSignal, new Func1<TextViewTextChangeEvent, ValidationResult>() {
            @Override
            public ValidationResult call(TextViewTextChangeEvent textEvent) {
                boolean isEmailValid = Patterns.PHONE.matcher(textEvent.text()).matches();
                return ValidatorPredicate.resultFor(textEvent.view(), isEmailValid);
            }
        });
    }

    public static TextValidator lengthValidator(Observable<TextViewTextChangeEvent> textSignal, final LengthValidation validation) {
        return textValidator(textSignal, new Func1<TextViewTextChangeEvent, ValidationResult>() {
            @Override
            public ValidationResult call(TextViewTextChangeEvent textEvent) {
                boolean isValid = validation.call(textEvent.text().length());
                return ValidatorPredicate.resultFor(textEvent.view(), isValid);
            }
        });
    }

    public static ProxyValidator equalityValidator(Observable<TextViewTextChangeEvent> textSignal1, Observable<TextViewTextChangeEvent> textSignal2) {
        return ImmutableProxyValidator.builder().source(
                Observable.combineLatest(textSignal1, textSignal2, new Func2<TextViewTextChangeEvent, TextViewTextChangeEvent, ValidationResult>() {
                    @Override
                    public ValidationResult call(TextViewTextChangeEvent textEvent1, TextViewTextChangeEvent textEvent2) {
                        boolean textEquals = textEvent1.text().toString().equals(textEvent2.text().toString());
                        return ValidationResult.of(textEquals ? SUCCESS : ERROR, textEvent2.view());
                    }
                })
        ).build();
    }

    public static TextValidator nonEmptyValidator(Observable<TextViewTextChangeEvent> text) {
        return textValidator(text, new Func1<TextViewTextChangeEvent, ValidationResult>() {
            @Override
            public ValidationResult call(TextViewTextChangeEvent textEvent) {
                boolean isEmpty = textEvent.text().toString().trim().isEmpty();
                return ValidatorPredicate.resultFor(textEvent.view(), !isEmpty);
            }
        });
    }

    public static TextValidator emptyValidator(Observable<TextViewTextChangeEvent> text) {
        return textValidator(text, new Func1<TextViewTextChangeEvent, ValidationResult>() {
            @Override
            public ValidationResult call(TextViewTextChangeEvent textEvent) {
                boolean isEmpty = textEvent.text().toString().trim().isEmpty();
                return ValidatorPredicate.resultFor(textEvent.view(), isEmpty);
            }
        });
    }

    public static TextValidator emptyOr(final TextValidator source) {
        return ImmutableTextValidator.builder().from(source).predicate(new Func1<TextViewTextChangeEvent, ValidationResult>() {
            @Override
            public ValidationResult call(TextViewTextChangeEvent TextViewTextChangeEvent) {
                if (TextUtils.isEmpty(TextViewTextChangeEvent.text()))
                    return ValidationResult.of(SUCCESS, TextViewTextChangeEvent.view());
                else return source.predicate().call(TextViewTextChangeEvent);
            }
        }).build();
    }

    public static Validator<ValidationResult> allValid(final View view, final List<Observable<ValidationResult>> validators) {
        return ImmutableProxyValidator.builder().source(
                Observable.combineLatest(validators, new FuncN<ValidationResult>() {
                    @Override
                    public ValidationResult call(Object... results) {
                        for (Object result : results) {
                            if (((ValidationResult) result).status() == ERROR) {
                                return ValidatorPredicate.resultFor(view, false);
                            }
                        }
                        return ValidatorPredicate.resultFor(view, true);
                    }
                })
        ).build();
    }

    public static Validator<ValidationResult> firstWrong(List<Observable<ValidationResult>> validators) {
        return ImmutableProxyValidator.builder().source(
                Observable.combineLatest(validators, new FuncN<ValidationResult>() {
                    @Override
                    public ValidationResult call(Object... results) {
                        for (ValidationResult result : ((ValidationResult[]) results)) {
                            if (result.status() == ERROR) {
                                return result;
                            }
                        }
                        return ValidatorPredicate.resultFor(null, true);
                    }
                })
        ).build();
    }

    public static Validator<ValidationResult> oneValid(final View view, List<Observable<ValidationResult>> validators) {
        return ImmutableProxyValidator.builder().source(
                Observable.combineLatest(validators, new FuncN<ValidationResult>() {
                    @Override
                    public ValidationResult call(Object... results) {
                        for (Object result : results) {
                            if (((ValidationResult) result).status() == SUCCESS) {
                                return ValidatorPredicate.resultFor(view, true);
                            }
                        }
                        return ValidatorPredicate.resultFor(view, false);
                    }
                })
        ).build();
    }

    public static Validator<ValidationResult> allInvalid(final View view, List<Observable<ValidationResult>> validators) {
        return ImmutableProxyValidator.builder().source(
                Observable.combineLatest(validators, new FuncN<ValidationResult>() {
                    @Override
                    public ValidationResult call(Object... results) {
                        for (Object result : results) {
                            if (((ValidationResult) result).status() == SUCCESS) {
                                return ValidatorPredicate.resultFor(view, true);
                            }
                        }
                        return ValidatorPredicate.resultFor(view, false);
                    }
                })
        ).build();
    }

}
