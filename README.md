# ViewValidators
ViewValidators is an useful library based on rxJava for performance validation functional in several steps!
During validation ViewValidations can control the state (enable/disable) of the view by `SimpleEnabledValidationSubscriber`
or check the correctness of the data with time delay (`InputValidatorComposer`)

# Available functional:
1  String Validator <br />
2  Email Validator <br />
3  Phone Validator <br />
4  Length Validator <br />
5  Equality Validator <br />
6  Non Empty Validator <br />
7  Empty Validator <br />
8  Empty Or Validator <br />
9  All Valid Validator <br />
10 First Wrong Validator <br />
11 All Invalid Validator <br />

# Sample
``` groovy
formValidator = new FormValidator();
formValidator
     .add(WidgetValidators.emailValidator(RxTextView.textChangeEvents(emailView).skip(1))
                .withError("Incorrect email")
                .result())
     .compose(new InputValidatorComposer<ValidationResult>())
     .compose(this.<ValidationResult>bindToLifecycle())
     .subscribe(new SimpleTextValidationSubscriber());
     
formValidator
     .add(WidgetValidators.lengthValidator(RxTextView.textChangeEvents(passwordView), Validations.GTELengthValidation.of(minLength))
                .withError("Incorrect password")
                .result())
     .compose(new InputValidatorComposer<ValidationResult>(TIME_DELAY_IN_MILLISECONDS))
     .compose(this.<ValidationResult>bindToLifecycle())
     .subscribe(new SimpleTextValidationSubscriber());
     
formValidator.allCombined(signInButton)
             .startWith(ValidationResult.of(ValidationResult.ValidationStatus.ERROR, signInButton))
             .subscribe(new SimpleEnabledValidationSubscriber());
```
             
# Installation
``` groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.1.3'
        classpath 'com.github.dcendents:android-maven-plugin:1.2'
    }
}
...
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'
...
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.techery:ViewValidators:0.1.1'
    ...
}
```