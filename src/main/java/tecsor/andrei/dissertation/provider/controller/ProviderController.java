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

    private final ProviderService providerService;
    private final UserStatisticsRepository repository;

    public ProviderController(ProviderService providerService, UserStatisticsRepository repository) {
        this.providerService = providerService;
        this.repository = repository;
    }

    @GetMapping("/{pid}")
    public Boolean isClientAvailable(@PathVariable("pid") String pid) throws IOException {
        UserStatistics byId = repository.findById(pid);
        if (byId != null) {
            providerService.afterIsClientAvailable(byId);
            return true;
        }
        return false;
    }
}
