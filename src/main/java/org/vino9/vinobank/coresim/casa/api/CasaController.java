package org.vino9.vinobank.coresim.casa.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.vino9.vinobank.coresim.casa.service.CasaService;

@RestController
@RequestMapping("/api/casa")
public class CasaController {

    private final CasaService service;

    public CasaController(CasaService service) {
        this.service = service;
    }

    @GetMapping("accounts/{account_num}")
    public AccountSchema getAccountDetail(@PathVariable("account_num") String accountNum) {
        return service.getAccountDetail(accountNum);
    }

    @PostMapping("transfers")
    @ResponseStatus(HttpStatus.CREATED)
    public TransferSchema transfer(@RequestBody @Valid TransferSchema transferSchema) {
        return service.transfer(transferSchema);
    }
}
