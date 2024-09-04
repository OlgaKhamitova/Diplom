package utils;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlHelper {
    private static final String user = "app";
    private static final String password = "pass";
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SqlHelper() {
    }

    private static Connection getConnect() throws SQLException {
        return DriverManager.getConnection(System.getProperty("db.url"), user, password);
    }

    @SneakyThrows
    public static String getPaymentId() {
        var orderPaymentIdQuery = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        var connect = getConnect();
        return QUERY_RUNNER.query(connect, orderPaymentIdQuery, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getPaymentMethod(String paymentId) {
        var debitMethodQuery = String.format("SELECT id FROM payment_entity WHERE transaction_id ='%s'", paymentId);
        var creditMethodQuery = String.format("SELECT id FROM credit_request_entity WHERE bank_id ='%s'", paymentId);
        var connect = getConnect();
        String debitMethodQueryResult = QUERY_RUNNER.query(connect, debitMethodQuery, new ScalarHandler<>());
        String creditMethodQueryResult = QUERY_RUNNER.query(connect, creditMethodQuery, new ScalarHandler<>());
        if (debitMethodQueryResult != null) {
            return "Debit card";
        }
        if (creditMethodQueryResult != null) {
            return "Credit card";
        }
        return "NONE";
    }


    @SneakyThrows
    public static String getPaymentStatus(boolean isCredit, String paymentId) {
        var debitStatusQuery = String.format("SELECT status FROM payment_entity WHERE transaction_id = '%s'", paymentId);
        var creditStatusQuery = String.format("SELECT status FROM credit_request_entity WHERE bank_id = '%s'", paymentId);
        var connect = getConnect();
        if (isCredit) {
            return QUERY_RUNNER.query(connect, creditStatusQuery, new ScalarHandler<>());
        }
        return QUERY_RUNNER.query(connect, debitStatusQuery, new ScalarHandler<>());
    }

    @SneakyThrows
    public static void cleanDataBase() {
        var connect = getConnect();
        QUERY_RUNNER.execute(connect, "DELETE FROM credit_request_entity", new ScalarHandler<>());
        QUERY_RUNNER.execute(connect, "DELETE FROM order_entity", new ScalarHandler<>());
        QUERY_RUNNER.execute(connect, "DELETE FROM payment_entity", new ScalarHandler<>());
    }

}
