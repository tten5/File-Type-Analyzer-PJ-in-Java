package analyzer;

public class FindPatternMachine {

    private FindPatternMethod findPatternMethod;

    public void setMethod(FindPatternMethod findPatternMethod) {
        this.findPatternMethod = findPatternMethod;
    }

  public boolean find(String originalText, String pattern) {
        return this.findPatternMethod.find(originalText, pattern);
  }
}
