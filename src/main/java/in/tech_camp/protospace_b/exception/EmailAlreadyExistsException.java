package in.tech_camp.protospace_b.exception;

/**
 * Emailが重複していた場合の例外
 */
public class EmailAlreadyExistsException extends Exception {

  public EmailAlreadyExistsException(String string) {
    super(string);
  }

}
