package coms6998.fall2016.data.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public final class ModelValidator {

	public static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public static <T> Set<ConstraintViolation<T>> validate(T model) {
		return validator.validate(model);
	}

	public static <T> Set<ConstraintViolation<T>> validateForCreate(T model) {
		Set<ConstraintViolation<T>> createViolations = validator.validate(model, CreateValidation.class);
		Set<ConstraintViolation<T>> violations = validator.validate(model);
		violations.addAll(createViolations);
		return violations;
	}

	public static <T> Set<ConstraintViolation<T>> validateForUpdate(T model) {
		Set<ConstraintViolation<T>> updateValidations = validator.validate(model, UpdateValidation.class);
		validator.validate(model, UpdateValidation.class);
		Set<ConstraintViolation<T>> violations = validator.validate(model);
		violations.addAll(updateValidations);
		return violations;
	}

}
