import model.PageElements;
import org.junit.jupiter.api.*;
import utils.PageElementUtils;
import utils.RegistrationDataGenerator;
import model.RegistrationInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;


public class DebitCardFormTest {
    private static final int notificationTimeout = 15;

    @BeforeEach
    void SetUp() {
        open("http://localhost:8080");
        $(withText("Купить")).click();
    }

    @AfterEach
    void tearDown(){
        closeWebDriver();
    }
    @Test
    @DisplayName("Payment by debit card using cyrillic alphabet and numbers")

    public void testRequestForm() {
        boolean isActive = true;
        String locale = "ru";
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, locale);

        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationApproval.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by debit card using latin alphabet and numbers")
    public void testRequestFormEn() {
        boolean isActive = true;
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);

        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationApproval.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by debit card using numbers")  // БАГ!!!!!
    public void testRequestFormNum() {
        boolean isActive = true;
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);

        registrationInfo.setOwner("1234567890");
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationReject.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by debit card using cyrillic alphabet in card number")
    public void testRequestFormCyr() {
        boolean isActive = true;
        String locale = "ru";
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, locale);

        registrationInfo.setCardNumber("Один два три");
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.cardNumberErrorField.shouldHave(text(PageElements.wrongFormatError));
    }

    @Test
    @DisplayName("Payment by debit card using latin alphabet in card number")
    public void testRequestFormEnCardNumber() {
        boolean isActive = true;
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);

        registrationInfo.setCardNumber("One two three");
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.cardNumberErrorField.shouldHave(text(PageElements.wrongFormatError));
    }

    @Test
    @DisplayName("Payment by debit card using special characters") //БАГ!!!!!!!!!!!
    public void testRequestFormSpecialCharacters() {
        boolean isActive = true;
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);

        PageElementUtils.fillPageElements(registrationInfo);
        registrationInfo.setOwner("...,,,,,№№№№");
        PageElements.bankOperationReject.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by debit card send empty form")
    public void testSendEmptyForm() {
        PageElements.proceedButton.click();
        PageElements.cardNumberErrorField.shouldHave(text(PageElements.wrongFormatError));
        PageElements.yearErrorField.shouldHave(text(PageElements.wrongFormatError));
        PageElements.monthErrorField.shouldHave(text(PageElements.wrongFormatError));
        PageElements.cardOwnerErrorField.shouldHave(text(PageElements.wrongFormatError));
        PageElements.cvv2ErrorField.shouldHave(text(PageElements.wrongFormatError));
        PageElements.necessaryField.shouldBe(visible);

    }

    @Test
    @DisplayName("Payment by debit card with declined card") // БАГ!!!!!!!!!!
    public void testDeclinedCard() {
        boolean isActive = false;
        String locale = "ru";
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, locale);

        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationReject.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by debit card with expired card")
    public void testExpiredCard() {
        boolean isActive = true;
        boolean isExpired = true;
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, isExpired);

        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.expiredField.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }
}