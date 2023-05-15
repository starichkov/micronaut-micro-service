package com.templatetasks.java.micronaut.oms.api.validation;

import com.templatetasks.java.micronaut.oms.api.OrderRequestItems;
import com.templatetasks.java.micronaut.oms.api.OrderRequestItemsOperation;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;

import java.util.Map;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 15.05.2023 16:53
 */
public class OrderRequestOperationsValidator implements ConstraintValidator<UniqueProductPerOperationConstraint, Map<OrderRequestItemsOperation, OrderRequestItems>> {

    @Override
    public void initialize(UniqueProductPerOperationConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Map<OrderRequestItemsOperation, OrderRequestItems> value,
                           AnnotationValue<UniqueProductPerOperationConstraint> annotationMetadata,
                           ConstraintValidatorContext context
    ) {
        // TODO implement validation
        return true;
    }
}
