package in.tech_camp.protospace_b.factory;

import com.github.javafaker.Faker;

import in.tech_camp.protospace_b.form.UserForm;

public class UserFormFactory {

  private static final Faker faker = new Faker();

  public static UserForm createUser() {
    UserForm userForm = new UserForm();

    String generatedUsername = faker.name().username();

    userForm.setName(generatedUsername);
    userForm.setProfile(faker.animal().toString());
    userForm.setAffiliationId(1);
    userForm.setPositionId(1);
    userForm.setEmail(faker.internet().emailAddress());
    userForm.setPassword(faker.internet().password(6, 12));
    userForm.setPasswordConfirmation(userForm.getPassword());

    return userForm;
  }
}