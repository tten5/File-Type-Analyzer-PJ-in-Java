package analyzer;

import java.util.ArrayList;
import java.util.List;

public class KMPMethod implements FindPatternMethod {

    private int[] prefixFunction(String str) {
        int[] prefixArr = new int[str.length()];

        // index i for the original string str and the prefixArr, prefixArr[0] = 0 by convention
        for (int i = 1; i < str.length(); i++) {

            // val is the value in the prefixArr at the index i - 1
            int val = prefixArr[i - 1];

            /* if there is a matching streak (val > 0)
            and at i this matching streak will be broken (str.charAt(i) != str.charAt(val))
            then the val of this prefixArr[i] will be smaller
            to be more specific,
            this new val will equal the val at prefix[somewhere that the streak will not be broken at i]
            */
            while (val > 0 && str.charAt(i) != str.charAt(val)) {
                val = prefixArr[val - 1];
            }

            // increase the val if the matching streak continue even at i
            if (str.charAt(i) == str.charAt(val)) {
                val++;
            }

            // now save val into prefixArr[i]
            prefixArr[i] = val;
        }

        return prefixArr;
    }

    private List<Integer> KMPSearch(String text, String pattern) {

        int[] prefixArr = prefixFunction(pattern);
        ArrayList<Integer> occurrences = new ArrayList<>();

        // j is the index of pattern and the prefixArr
        int j = 0;

        // i is the index of original text
        for (int i = 0; i < text.length(); i++) {

            // j has not move back to the original 0 start point yet
            // if j has move back to the original 0, skip this part
            // and the char at i break the matching streak with the pattern (at j+1 position)
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {

                /* move the j back to the index where at that index a small head part from prefixArr[0] to prefixArr[that index]
                has been matched with the text already
                so we don't need to compare that head part anymore
                */

                j = prefixArr[j - 1];
            }


            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }

            if (j == pattern.length()) {
                occurrences.add(i - j + 1);
                j = prefixArr[j - 1];
            }
        }

        return occurrences;
    }

    @Override
    public boolean find(String originalText, String pattern) {

        var occurrences = this.KMPSearch(originalText, pattern);
        return !occurrences.isEmpty();
    }
}
