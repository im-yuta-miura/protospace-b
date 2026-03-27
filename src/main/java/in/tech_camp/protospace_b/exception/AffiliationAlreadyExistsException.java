package in.tech_camp.protospace_b.exception;

/**
 * 所属が重複していた場合の例外
 */
public class AffiliationAlreadyExistsException extends Exception {

  public AffiliationAlreadyExistsException(String string) {
    super(string);
  }

}
