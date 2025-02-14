package com.overridetech.funpay_monitor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

@AllArgsConstructor
@Getter
public enum Poe2Dictionary {
    DIVINE_ORB(Set.of("divine orb", "божественная сфера"));

    private final Set<String> alias;

    public static Poe2Dictionary getByAlias(String alias) {
        return Arrays.stream(Poe2Dictionary.values())
                .filter(v -> v.getAlias().contains(alias.toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}
