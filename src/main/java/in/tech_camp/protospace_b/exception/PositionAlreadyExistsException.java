package in.tech_camp.protospace_b.exception;

/**
 * 役職が重複していた場合の例外
 */
public class PositionAlreadyExistsException extends Exception {

  public PositionAlreadyExistsException(String string) {
    super(string);
  }

}
