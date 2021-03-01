package analyzer;

public class NaiveMethod implements FindPatternMethod {

    @Override
    public boolean find(String originalText, String pattern) {
        return originalText.contains(pattern);
    }

}
