public class SkiResortNotFoundException extends RuntimeException {
    public SkiResortNotFoundException(Integer id) {
        super("SkiResort with ID " + id + " not found.");
    }
}
