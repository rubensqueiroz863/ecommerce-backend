@RestController
public class HealthController {

  @GetMapping("/")
  public String health() {
    return "Backend rodando.";
  }
}