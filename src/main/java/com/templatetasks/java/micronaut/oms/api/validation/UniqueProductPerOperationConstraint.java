package com.templatetasks.java.micronaut.oms.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 15.05.2023 16:54
 */
@Documented
@Constraint(validatedBy = OrderRequestOperationsValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface UniqueProductPerOperationConstraint {

    String message() default "{com.templatetasks.java.micronaut.oms.api.validation.UniqueProductPerOperationConstraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
