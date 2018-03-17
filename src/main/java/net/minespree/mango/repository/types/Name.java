package net.minespree.mango.repository.types;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @since 20/10/2017
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String value();
}
