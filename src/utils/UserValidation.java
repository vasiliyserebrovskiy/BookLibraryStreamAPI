package utils;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (18.03.2025)
 */
public class UserValidation {

    public static void validateEmail(String email) throws EmailValidateException {

        // throw - ключевое слово, используется для явного выброса исключения
        if (email == null) throw new EmailValidateException("email should be not null");

        // 1. Собака
        int indexAt = email.indexOf('@');
        int lastAt = email.lastIndexOf('@');
        if (indexAt == -1 || indexAt != lastAt) throw new EmailValidateException("@ error");

        //2. Точка после собаки
        int dotIndexAfterAt = email.indexOf('.', (indexAt + 1));
        if (indexAt + 1 == dotIndexAfterAt) throw new EmailValidateException(". after @");

        //3. После точки два символа
        int lastDotIndex = email.lastIndexOf('.');
        if (lastDotIndex >= email.length() - 2) throw new EmailValidateException("last . error");

        //4. Алфавит
        for (char ch : email.toCharArray()) {
            // Проверка символа на правильность
            boolean isPass = Character.isAlphabetic(ch) || Character.isDigit(ch)
                    || ch == '-' || ch == '_' || ch == '.' || ch == '@';

            if (!isPass) throw new EmailValidateException("Illegal symbol");
        }
        //5. до собаки должен быть хотя бы один символ
        if (indexAt == 0) throw new EmailValidateException("no character before @");

        //6. Первый символ не число
        if (!Character.isAlphabetic(email.charAt(0))) throw new EmailValidateException("first symbol should be letter");

    }

    public static void validatePassword(String password) throws PasswordValidateException {
        if (password == null) throw new PasswordValidateException("Password is null");
        if (password.length() < 8) throw new PasswordValidateException("Error length");

        boolean isDigit = false,
                isLowerChar = false,
                isUpperChar = false,
                isSpecialSymbol = false;

        String specialSymbols = "!#%$@&*()[],.-";

        for (char ch : password.toCharArray()) {
            if (Character.isDigit(ch)) isDigit = true;
            if (Character.isLowerCase(ch)) isLowerChar = true;
            if (Character.isUpperCase(ch)) isUpperChar = true;
            if (specialSymbols.indexOf(ch) >= 0) isSpecialSymbol = true;
        }

        if (!isDigit) throw new PasswordValidateException("No digits");
        if (!isLowerChar) throw new PasswordValidateException("No lowercase symbols");
        if (!isUpperChar) throw new PasswordValidateException("No uppercase symbols");
        if (!isSpecialSymbol) throw new PasswordValidateException("No special symbols");
    }


}
