package com.example.vidupcoremodule.events;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public abstract class GenericEvent implements ResolvableTypeProvider {

    /**
     * Concrete classes should implement this method for correct generic types identification
     * @return
     */
    public abstract ResolvableType getResolvableType();
}
