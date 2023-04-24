package tecsor.andrei.dissertation.provider.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider/api/v1")
public class ProviderController {
    @GetMapping("/{pid}")
    ResponseEntity<Boolean> isClientAvailable(@PathVariable("pid") String pid) {
        if (pid.equals("3ac8bb5f170b7f34e5e6d74c3f0a33db186b31c7a68e5706a4f54c11693934f6")) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
