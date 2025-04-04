package utils;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (04.04.2025)
 */
public class PasswordValidateException extends Exception{
    public PasswordValidateException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Password validate exception | " + super.getMessage();
    }
}
