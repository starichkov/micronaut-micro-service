package com.templatetasks.java.micronaut.oms.api;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 15.05.2023 16:14
 */
public enum OrderRequestItemsOperation {

    /**
     * Mark item as to be added to the order
     */
    ADD,

    /**
     * Mark existing item as to be modified in the order
     */
    MODIFY,

    /**
     * Remove existing item from the order
     */
    REMOVE

    ;
}
