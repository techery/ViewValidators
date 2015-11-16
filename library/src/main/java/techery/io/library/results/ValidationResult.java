package techery.io.library.results;

import android.support.annotation.Nullable;
import android.view.View;

import org.immutables.value.Value;

@Value.Immutable
public abstract class ValidationResult {
    public abstract View view();
    public abstract ValidationStatus status();
    public abstract @Nullable Exception error();

    public static ValidationResult of(ValidationStatus status, View view) {
        return ImmutableValidationResult.builder().status(status).view(view).build();
    }

    public enum ValidationStatus {
        SUCCESS, ERROR
    }
}
