package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

import com.github.javafaker.Faker;
import model.RegistrationInfo;

public class RegistrationDataGenerator {
    private static final int monthToAdd = 1;
    private static final int yearsToAdd = 0;
    private static final int yearsToReduce = 1;
    private static final String activeCard = "4444444444444441";
    private static final String deflectedCard = "4444444444444442";
    private static final String defaultLocale = "en";
    private static final String wrongCode = "000";

    public static RegistrationInfo getRegistrationInfo(boolean isActive) {
        return getRegistrationInfo(isActive, defaultLocale);
    }

    public static RegistrationInfo getRegistrationInfo(boolean isActive, String locale) {
        boolean isExpired = false;
        String month = generateRegistrationMonth();
        String year = generateRegistrationYear(isExpired);
        String cardNumber = generateCardNumber(isActive);
        String owner = generateOwner(locale);
        String code = generateCode();
        return new RegistrationInfo(cardNumber, month, year, owner, code);
    }

    public static RegistrationInfo getRegistrationInfo(boolean isActive, boolean isExpired) {
        String month = generateRegistrationMonth();
        String year = generateRegistrationYear(isExpired);
        String cardNumber = generateCardNumber(isActive);
        String owner = generateOwner(defaultLocale);
        String code = generateCode();
        return new RegistrationInfo(cardNumber, month, year, owner, code);
    }

    public static RegistrationInfo getRegistrationInfo(boolean isActive, String locale, boolean isExpired) {
        String month = generateRegistrationMonth();
        String year = generateRegistrationYear(isExpired);
        String cardNumber = generateCardNumber(isActive);
        String owner = generateWrongOwner(locale);
        String code = generateCode();
        return new RegistrationInfo(cardNumber, month, year, owner, code);
    }

    private static String generateRegistrationMonth() {
        LocalDate date = LocalDate.now().plusMonths(monthToAdd);
        return date.format(DateTimeFormatter.ofPattern("MM"));
    }

    private static String generateRegistrationYear(boolean isExpired) {
        LocalDate expiredDate = LocalDate.now().minusYears(yearsToReduce);
        LocalDate date = LocalDate.now().plusYears(yearsToAdd);
        if (isExpired) {
            return expiredDate.format(DateTimeFormatter.ofPattern("yy"));
        }
        return date.format(DateTimeFormatter.ofPattern("yy"));
    }

    private static String generateCardNumber(boolean isActive) {
        if (isActive) {
            return activeCard;
        }
        return deflectedCard;
    }

    private static String generateOwner(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().fullName();
    }

    private static String generateCode() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        char[] chars = "0123456789".toCharArray();
        for (int i = 0; i < 3; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        return sb.toString();
    }

    private static String generateWrongOwner(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.letterify("?");
    }
}
