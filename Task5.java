public class DocumentValidator {
    // Added the required logger
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DocumentValidator.class);

    public ValidationResult validate(Document doc) {
        try {
            if (doc == null) {
                throw new RuntimeException("Document is null");
            }
            String content = doc.extractContent();
            if (content.isEmpty()) {
                throw new RuntimeException("Empty content");
            }
            return runValidationRules(content);

        } catch (Exception e) {
            // FIX: use logger instead of printing full stack traces for expected validation errors
            log.warn("Validation failed: {}", e.getMessage());
            // FIX: return a failed validation object instead of null to prevent crashing the loop
            return new ValidationResult(false); 
        }
    }

    public void validateBatch(List<Document> docs) {
        for (Document doc : docs) {
            try {
                ValidationResult r = validate(doc);
                // FIX: safely check for null before calling isValid()
                if (r != null && r.isValid()) {                  
                    saveResult(r);
                }
            } catch (Exception e) {
                // FIX: log the error using slf4j instead of swallowing it silently
                log.error("Batch processing error", e);
            }
        }
    }
}
