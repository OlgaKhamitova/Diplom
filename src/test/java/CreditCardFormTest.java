import constants.TestConstants;
import model.PageElements;
import org.junit.jupiter.api.*;
import utils.PageElementUtils;
import utils.RegistrationDataGenerator;
import model.RegistrationInfo;
import utils.SqlHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CreditCardFormTest {
    private static final int notificationTimeout = 15;
    private static final boolean isCredit = true;

    @BeforeEach
    void SetUp() {
        open(TestConstants.APPLICATION_HOST);
        $(withText("Купить в кредит")).click();
    }

    @AfterEach
    void tearDown(){
        closeWebDriver();
    }
    @Test
    @DisplayName("Payment by credit card using cyrillic alphabet and numbers")

    public void testRequestForm() {
        boolean isActive = true;

        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, TestConstants.RUS_LOCALE);
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationApproval.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by credit card using latin alphabet and numbers") // credit_request_entity+order_entity
    public void testRequestFormEn() {
        boolean isActive = true;

        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationApproval.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by credit card using numbers")  // БАГ!!!!!
    public void testRequestFormNum() {
        boolean isActive = true;

        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);
        registrationInfo.setOwner("1234567890");
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationReject.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by credit card using cyrillic alphabet in card number")
    public void testRequestFormCyr() {
        boolean isActive = true;

        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, TestConstants.RUS_LOCALE);
        registrationInfo.setCardNumber("Один два три");
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.cardNumberErrorField.shouldHave(text(PageElements.wrongFormatError));
    }

    @Test
    @DisplayName("Payment by credit card using latin alphabet in card number")
    public void testRequestFormEnCardNumber() {
        boolean isActive = true;

        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);
        registrationInfo.setCardNumber("One two three");
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.cardNumberErrorField.shouldHave(text(PageElements.wrongFormatError));
    }

    @Test
    @DisplayName("Payment by credit card using special characters") //БАГ!!!!!!!!!!!
    public void testRequestFormSpecialCharacters() {
        boolean isActive = true;

        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);
        PageElementUtils.fillPageElements(registrationInfo);
        registrationInfo.setOwner("...,,,,,№№№№");
        PageElements.bankOperationReject.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by credit card send empty form")
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
    @DisplayName("Payment by credit card with declined card") // БАГ!!!!!!!!!!
    public void testDeclinedCard() {
        boolean isActive = false;

        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, TestConstants.RUS_LOCALE);
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationReject.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }

    @Test
    @DisplayName("Payment by credit card with expired card")
    public void testExpiredCard() {
        boolean isActive = true;
        boolean isExpired = true;

        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, isExpired);
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.expiredField.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
    }
    @Test
    @DisplayName("Successfully order in order_entity")
    public void testRequestFormOrderEntity() {
        boolean isActive = true;

        SqlHelper.cleanDataBase();
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, TestConstants.RUS_LOCALE);
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationApproval.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
        String actual = SqlHelper.getPaymentId();

        Assertions.assertNotEquals(null, actual);
    }

    @Test
    @DisplayName("Unsuccessful order_entity")
    public void testUsingExpiredPeriodCard() {
        boolean isActive = true;
        boolean isExpired = true;

        SqlHelper.cleanDataBase();
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive, isExpired);
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.expiredField.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
        String actual = SqlHelper.getPaymentId();

        Assertions.assertNull(actual);

    }

    @Test
    @DisplayName("Payment method definition")
    public void testPaymentMethodDefinition() {
        boolean isActive = true;

        SqlHelper.cleanDataBase();
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationApproval.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
        String paymentId = SqlHelper.getPaymentId();
        String actual = SqlHelper.getPaymentMethod(paymentId);
        String expected = TestConstants.CREDIT_CARD_PAYMENT_METHOD;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Approved status definition")
    public void testApprovedStatusDefinition(){
        boolean isActive = true;

        SqlHelper.cleanDataBase();
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);
        PageElementUtils.fillPageElements(registrationInfo);
        PageElements.bankOperationApproval.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
        String paymentId = SqlHelper.getPaymentId();
        String actual = SqlHelper.getPaymentStatus(isCredit,paymentId);

        Assertions.assertEquals(TestConstants.APPROVED_STATUS, actual);
    }
    @Test
    @DisplayName("Declined status definition")
    public void testDeclinedStatusDefinition(){
        boolean isActive = false;

        SqlHelper.cleanDataBase();
        RegistrationInfo registrationInfo = RegistrationDataGenerator.getRegistrationInfo(isActive);
        PageElementUtils.fillPageElements(registrationInfo);
        registrationInfo.setCardNumber(registrationInfo.getCardNumber());
        PageElements.bankOperationApproval.shouldBe(visible, Duration.ofSeconds(notificationTimeout));
        String paymentId = SqlHelper.getPaymentId();
        String actual = SqlHelper.getPaymentStatus(isCredit,paymentId);

        Assertions.assertEquals(TestConstants.DECLINED_STATUS, actual);
    }
}

