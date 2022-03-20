package co.tala.bankaccountmanagement.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@NoArgsConstructor
public class ResourceResponse {
    private String message;
    private String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private int status;
    private Object body;

    public ResourceResponse(String message, int status, Object body) {
        this.message = message;
        this.status = status;
        this.body = body;
    }
}
