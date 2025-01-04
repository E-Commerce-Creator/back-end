package com.e_commerce_creator.common.annotations;

import com.e_commerce_creator.common.enums.annotations.ActionAdditionalDetails;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//todo: will use for auditing in future
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {
    String name() default "";

    ActionAdditionalDetails additionalDetails() default ActionAdditionalDetails.NONE;
}
