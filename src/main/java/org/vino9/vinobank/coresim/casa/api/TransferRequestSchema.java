package org.vino9.vinobank.coresim.casa.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class TransferRequestSchema {

    @JsonProperty("ref_id")
    private String refId;

    @JsonProperty("trx_id")
    private String trxId;

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
    private BigDecimal amount;

    @NotBlank
    @JsonProperty("memo")
    private String memo;

}
