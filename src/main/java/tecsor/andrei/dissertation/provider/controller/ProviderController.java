package tecsor.andrei.dissertation.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tecsor.andrei.dissertation.provider.model.UserStatistics;
import tecsor.andrei.dissertation.provider.repository.UserStatisticsRepository;
import tecsor.andrei.dissertation.provider.service.ProviderService;

import java.io.IOException;

@RestController
@RequestMapping("/provider/api/v1")
public class ProviderController {
    public static final String ANSI_ORANGE = "\u001B[38;2;255;165;0m";


    private final ProviderService providerService;
    private final UserStatisticsRepository repository;

    public ProviderController(ProviderService providerService, UserStatisticsRepository repository) {
        this.providerService = providerService;
        this.repository = repository;
    }

    @GetMapping("/{pid}")
    public Boolean isClientAvailable(@PathVariable("pid") String hashedPid) throws IOException {
        System.out.println(ANSI_ORANGE + "Received request for " + hashedPid);
        UserStatistics byId = repository.findById(hashedPid);
        if (byId != null) {
            providerService.afterIsClientAvailable(byId);
            System.out.println("Returning true for " + hashedPid);
            System.out.println();
            return true;
        }
        System.out.println("Returning false for " + hashedPid);
        System.out.println();
        return false;
    }
}
