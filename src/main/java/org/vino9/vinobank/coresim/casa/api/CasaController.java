package org.vino9.vinobank.coresim.casa.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
