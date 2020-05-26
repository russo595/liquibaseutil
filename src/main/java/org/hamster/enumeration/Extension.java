package org.hamster.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Extension {
    XML(".xml"),
    TXT(".txt");

    private String extension;
}
