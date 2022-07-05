package test.cash.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.cash.model.Currency;
import test.cash.model.CurrencyRate;
import test.cash.repository.CurrencyRateRepository;
import test.cash.repository.CurrencyRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CurrencyServiceImpl {

    private final CurrencyRepository currencyRepository;
    private final CurrencyRateRepository currencyRateRepository;

    private final String BASE_CURRENCY_NAME = "KGS";

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyRateRepository currencyRateRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyRateRepository = currencyRateRepository;
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public double getConvertedTransferAmount(double transferAmount, Currency transferCurrency) {
        if (!transferCurrency.getName().equals(BASE_CURRENCY_NAME)) {
            transferAmount *= getCurrencyRate(transferCurrency);
        }
        return transferAmount;
    }

    private double getCurrencyRate(Currency currency) {
        long baseCurrencyId = currencyRepository.findByName(BASE_CURRENCY_NAME).getId();
        CurrencyRate currencyRate = currencyRateRepository.findByFromCurrencyAndToCurrency(currency.getId(), baseCurrencyId);
        return currencyRate.getRate();
    }

}
