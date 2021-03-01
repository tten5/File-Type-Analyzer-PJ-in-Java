package analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RabinKarpMethod implements FindPatternMethod {

    /* 1 */
    static long charToLong(char ch) {
        return ch;
    }

    private List<Integer> RabinKarp(String text, String pattern) {

        // check whether the length of the pattern is longer than that of original Text
        if (pattern.length() > text.length()) {
            return null;
        }

        /* 2 */
        int a = 53;
        long m = 1_000_000_000 + 9;

        /* 3 */
        long patternHash = 0;
        long currSubstringHash = 0;
        long pow = 1;

        // find the hash value for pattern and the first substring in the text
        for (int i = 0; i < pattern.length(); i++) {
            patternHash += charToLong(pattern.charAt(i)) * pow;
            patternHash %= m;

            currSubstringHash += charToLong(text.charAt(text.length() - pattern.length() + i)) * pow;
            currSubstringHash %= m;

            if (i != pattern.length() - 1) {
                pow = pow * a % m;
            }
        }

        /* 4 */
        // save the matched substring position to occurrences list
        ArrayList<Integer> occurrences = new ArrayList<>();

        for (int i = text.length(); i >= pattern.length(); i--) {
            if (patternHash == currSubstringHash) {
                boolean patternIsFound = true;

                for (int j = 0; j < pattern.length(); j++) {
                    if (text.charAt(i - pattern.length() + j) != pattern.charAt(j)) {
                        patternIsFound = false;
                        break;
                    }
                }

                if (patternIsFound) {
                    occurrences.add(i - pattern.length());
                }
            }

            if (i > pattern.length()) {
                /* 5 */
                currSubstringHash = (m + currSubstringHash - charToLong(text.charAt(i - 1)) * pow % m) % m * a % m;
                currSubstringHash = (currSubstringHash + charToLong(text.charAt(i - pattern.length() - 1))) % m;
            }
        }

        Collections.reverse(occurrences);
        return occurrences;

    }


    @Override
    public boolean find(String originalText, String pattern) {

        var occurrences = this.RabinKarp(originalText, pattern);
        assert occurrences != null;
        return !occurrences.isEmpty();
    }
}
