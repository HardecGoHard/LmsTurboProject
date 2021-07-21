package com.Turbo.Lms.annotations;

import com.Turbo.Lms.validator.TitleCaseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = TitleCaseValidator.class)
public @interface TitleCase {
    Language language() default Language.ANY;
    String message() default "Заголовок не прошёл валидацию";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum Language{
        ANY,
        RU,
        EN
    }
}
