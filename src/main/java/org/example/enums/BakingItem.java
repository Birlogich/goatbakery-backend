package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter

public enum BakingItem {

    CAKE (1),
    MUFFIN (2),
    ECLAIR (3);

    private int id;

    public static boolean isValid(String value) {
        try {
            BakingItem.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
