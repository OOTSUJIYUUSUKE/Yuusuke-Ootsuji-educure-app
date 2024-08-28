package jp.co.example.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotEmptyMultipartFilesValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyMultipartFiles {
    String message() default "少なくとも1つの画像を選択してください";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
