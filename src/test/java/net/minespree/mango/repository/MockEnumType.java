package net.minespree.mango.repository;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.minespree.mango.repository.types.Name;

/**
 * @since 09/02/2018
 */
@AllArgsConstructor
public enum MockEnumType {
    @Name("all")
    ALL(1),
    @Name("none")
    NONE(2);

    private int value;


}
