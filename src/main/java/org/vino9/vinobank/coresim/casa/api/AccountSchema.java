package org.vino9.vinobank.coresim.casa.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AccountSchema {

    @NotBlank
    @JsonProperty("account_num")
    private String accountNum;

    @NotBlank
    @JsonProperty("currency")
    private String currency;

    @NotNull
    @JsonProperty("balance")
    private Double balance;

    @NotNull
    @JsonProperty("avail_balance")
    private Double availBalance;

    @NotBlank
    @JsonProperty("status")
    private String status;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
