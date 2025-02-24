package org.example.utils;

import lombok.experimental.UtilityClass;
import org.mindrot.jbcrypt.BCrypt;

@UtilityClass
public class PasswordUtil {

    // Метод для хеширования пароля
    public static String hashPassword(String plainTextPassword) {
        // Генерируем хеш пароля с солью. 12 — это "work factor", указывающий на количество итераций.
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    // Метод для проверки введенного пароля с хешированным значением в базе данных
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        // Сравниваем хеш пароля, который ввел пользователь, с тем, что хранится в базе
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
