package test.cash.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import test.cash.model.Currency;
import test.cash.model.Transaction;
import test.cash.service.CurrencyServiceImpl;
import test.cash.service.TransactionService;

import java.util.List;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final CurrencyServiceImpl currencyService;

    public TransactionController(TransactionService transactionService, CurrencyServiceImpl currencyService) {
        this.transactionService = transactionService;
        this.currencyService = currencyService;
    }

    @GetMapping("/")
    public String viewTransfersList(Model model) {
        return findPaginated(1, "id", "desc", model);
    }

    @GetMapping("/add")
    public String addTransaction(Model model) {
        Transaction transaction = new Transaction();
        List<Currency> currencies = currencyService.getAllCurrencies();
        model.addAttribute("transaction", transaction);
        model.addAttribute("options", currencies);
        return "transaction/add";
    }

    @PostMapping("/save")
    public String saveTransaction(@ModelAttribute("transaction") Transaction transaction, RedirectAttributes redirectAttributes) {
        transactionService.saveTransaction(transaction);
        // redirect to page showing verification code
        redirectAttributes.addFlashAttribute("code", transaction.getCode());
        return "redirect:/code";
    }


    @GetMapping("/code")
    public Object showGeneratedCodeForWithdraw(ModelMap model) {
        if (model.getAttribute("code") == null)
            return "redirect:/";

        return new ModelAndView("transaction/code", model);
    }

    @GetMapping("/withdraw/{id}")
    public String withdraw(@PathVariable(value = "id") long id, Model model) {
        var transaction = transactionService.findById(id);

        if (!transaction.getStatus())
            throw new RuntimeException("Transaction has already withdrawn");

        transaction.setCode(null); // set property to null for view input
        model.addAttribute("transaction", transaction);
        return "transaction/withdraw";
    }

    @PostMapping("/withdraw/{id}")
    public String saveAfterWithdraw(@PathVariable(value = "id") long id, @ModelAttribute("transaction") Transaction transaction) {
        transactionService.saveTransactionAfterWithdraw(id, transaction.getCode());
        return "redirect:/";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 3;
        Page<Transaction> page = transactionService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Transaction> listTransactions = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listTransactions", listTransactions);
        return "transaction/index";
    }
}
