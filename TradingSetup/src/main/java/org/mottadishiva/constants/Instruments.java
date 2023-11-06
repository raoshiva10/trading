package org.mottadishiva.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Instruments {
    RELIANCE("NSE:RELIANCE");

    private final String instrument;
}
