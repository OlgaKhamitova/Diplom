package utils;

import model.PageElements;
import model.RegistrationInfo;

public class PageElementUtils {
    public static void fillPageElements(RegistrationInfo registrationInfo) {
        PageElements.cardNumberInput.setValue(registrationInfo.getCardNumber());
        PageElements.monthInput.setValue(registrationInfo.getMonth());
        PageElements.yearInput.setValue(registrationInfo.getYear());
        PageElements.cardOwnerInput.setValue(registrationInfo.getOwner());
        PageElements.cvv2Input.setValue(registrationInfo.getCode());
        PageElements.proceedButton.click();
    }
}
