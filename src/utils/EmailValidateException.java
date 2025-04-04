package utils;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (04.04.2025)
 */
// extends RuntimeException - для НЕ-проверяемых исключений
// extends Exception - для проверяемых исключений
public class EmailValidateException extends Exception{
    /*
    getMessage() - возвращает строку, с коротким описанием исключения
    getCause() - возвращает исключение, которое вызвало это исключение (родительское исключение, то которое породило текущее исключение)
    toString() - строковое представление исключения
    printStackTrace() - выводит трассировку исключения
     */
    // переопределяем конструктор с сообщением
    public EmailValidateException(String message) {
        super(message);
    }

    // могу переопределить метод getMessage()


    @Override
    public String getMessage() {
        return "Email validate exception | " + super.getMessage();
    }
}
