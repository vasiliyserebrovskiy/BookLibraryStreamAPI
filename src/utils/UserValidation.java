package utils;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (18.03.2025)
 */
public class UserValidation {

    public static boolean isEmailValid(String email) {
        if (email == null) return false;

        // 1. Собака
        int indexAt = email.indexOf('@');
        int lastAt = email.lastIndexOf('@');
        if (indexAt == -1 || indexAt != lastAt) return false;

        //2. Точка после собаки
        int dotIndexAfterAt = email.indexOf('.', (indexAt + 1));
        if (indexAt + 1 == dotIndexAfterAt) return false;

        //3. После точки два символа
        int lastDotIndex = email.lastIndexOf('.');
        if (lastDotIndex >= email.length() - 2) return false;

        //4. Алфавит
        for (char ch : email.toCharArray()) {
            // Проверка символа на правильность
            boolean isPass = Character.isAlphabetic(ch) || Character.isDigit(ch)
                    || ch == '-' || ch == '_' || ch == '.' || ch == '@';

            if (!isPass) return false;
        }
        //5. до собаки должен быть хотя бы один символ
        if (indexAt == 0) return false;

        //6. Первый символ не число
        if (!Character.isAlphabetic(email.charAt(0))) return false;

        return true;
    }

    public static boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) return false;

        // boolean isLength = password.length() >= 8;
        boolean isDigit = false;
        boolean isLowerChar = false;
        boolean isUpperChar = false;
        boolean isSpecialSymbol = false;
        String specialSymbols = "!#%$@&*()[],.-";

        // альтернативный вариант объявления переменных
        boolean[] result = new boolean[4]; // false,false,false,false по умолчанию при создании

        for (char ch : password.toCharArray()) {
            if (Character.isDigit(ch)) isDigit = true;
            if (Character.isLowerCase(ch)) isLowerChar = true;
            if (Character.isUpperCase(ch)) isUpperChar = true;
            if (specialSymbols.indexOf(ch) >= 0) isSpecialSymbol = true;
        }

        return isDigit && isLowerChar && isUpperChar && isSpecialSymbol;
    }



}
