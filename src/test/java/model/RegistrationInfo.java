package model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationInfo {
    String cardNumber;
    String month;
    String year;
    String owner;
    String code;
}
