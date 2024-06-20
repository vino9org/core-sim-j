package org.vino9.vinobank.coresim.casa.api;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class TransferSchema {

    @JsonProperty("ref_id")
    private String refId;

    @NotBlank
    @JsonProperty("trx_date")
    private String trxDate;

    @NotBlank
    @JsonProperty("debit_account_num")
    private String debitAccountNum;

    @NotBlank
    @JsonProperty("credit_account_num")
    private String creditAccountNum;

    @NotBlank
    @JsonProperty("currency")
    private String currency;

    @Positive
    @JsonProperty("amount")
    private Double amount;

    @NotBlank
    @JsonProperty("memo")
    private String memo;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}