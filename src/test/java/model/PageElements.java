package model;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PageElements {
    public static SelenideElement cardNumber = $$("[class=input__inner]").findBy(text("Номер карты"));
    public static SelenideElement cardNumberInput = cardNumber.$(" input");
    public static SelenideElement cardNumberErrorField = cardNumber.$("[class=input__sub]");
    public static SelenideElement year = $$("[class=input__inner]").findBy(text("Год"));
    public static SelenideElement yearInput = year.$(" input");
    public static SelenideElement yearErrorField = cardNumber.$("[class=input__sub]");
    public static SelenideElement month = $$("[class=input__inner]").findBy(text("Месяц"));
    public static SelenideElement monthInput = month.$(" input");
    public static SelenideElement monthErrorField = cardNumber.$("[class=input__sub]");
    public static SelenideElement cardOwner = $$("[class=input__inner]").findBy(text("Владелец"));
    public static SelenideElement cardOwnerInput = cardOwner.$(" input");
    public static SelenideElement cardOwnerErrorField = cardNumber.$("[class=input__sub]");
    public static SelenideElement cvv2 = $$("[class=input__inner]").findBy(text("CVC/CVV"));
    public static SelenideElement cvv2Input = cvv2.$(" input");
    public static SelenideElement cvv2ErrorField = cardNumber.$("[class=input__sub]");
    public static SelenideElement proceedButton = $(withText("Продолжить"));
    public static SelenideElement bankOperationApproval = $(withText("Операция одобрена Банком."));
    public static SelenideElement bankOperationReject = $(withText("Банк отказал в проведении операции."));
    public static SelenideElement necessaryField = $(withText("Поле обязательно для заполнения"));
    public static SelenideElement expiredField = $(withText("Истёк срок действия карты"));
    public static String wrongFormatError = "Неверный формат";
}
