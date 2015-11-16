package techery.io.validotor;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.RxActivity;

import techery.io.library.composers.InputValidatorComposer;
import techery.io.library.results.ValidationResult;
import techery.io.library.subscribers.SimpleEnabledValidationSubscriber;
import techery.io.library.subscribers.SimpleTextValidationSubscriber;
import techery.io.library.validations.Validations;
import techery.io.library.validators.FormValidator;
import techery.io.library.validators.WidgetValidators;


public class LoginActivity extends RxActivity{

    private static final int TIME_DELAY = 888;

    private AutoCompleteTextView emailView;
    private EditText passwordView;

    private FormValidator formValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        Button signInButton = (Button) findViewById(R.id.email_sign_in_button);

        formValidator = new FormValidator();
        setEmailValidator();
        setPasswordValidator();
        formValidator.allCombined(signInButton)
                .startWith(ValidationResult.of(ValidationResult.ValidationStatus.ERROR, signInButton))
                .subscribe(new SimpleEnabledValidationSubscriber());

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Sign in!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEmailValidator(){
        formValidator
                .add(WidgetValidators.emailValidator(RxTextView.textChangeEvents(emailView).skip(1))
                        .withError("Incorrect email")
                        .result())
                .compose(new InputValidatorComposer<ValidationResult>())
                .compose(this.<ValidationResult>bindToLifecycle())
                .subscribe(new SimpleTextValidationSubscriber());
    }

    private void setPasswordValidator() {
        int minLength = 10;
        formValidator
                .add(WidgetValidators.lengthValidator(RxTextView.textChangeEvents(passwordView), Validations.GTELengthValidation.of(minLength))
                        .withError("Incorrect password")
                        .result())
                .compose(new InputValidatorComposer<ValidationResult>(TIME_DELAY))
                .compose(this.<ValidationResult>bindToLifecycle())
                .subscribe(new SimpleTextValidationSubscriber());
    }



}

