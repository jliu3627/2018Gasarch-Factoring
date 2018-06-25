package test.primes;
import primes.PrimeTester;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class PrimalityTestTester {
    /**
     * Runs the Fermat Primality Test on a range of given numbers, with a range of given iterations, a certain number of times.
     * For each number to test, for each iteration to test, the test will be run "trials" times and the results are recorded.
     * @param numsToTest ArrayList of numbers to test the primality test on.
     * @param iterations ArrayList of iterations to run the primality test with for each number
     * @param trials number of times to run each number + iteration pair.
     * @return
     */
    public static int[][][] FermatPrimalityTestTester(ArrayList<Integer> numsToTest, ArrayList<Integer> iterations, int trials) {
        int[][][] results = new int[numsToTest.size()][iterations.size()][2];
        int currNum = 0, currIt; boolean result;
        for (Integer n : numsToTest) {
            currIt = 0;
            for (Integer i : iterations) {
                for (int j = 0; j < trials; j++) {
                    result = PrimeTester.fermatPrimalityTest(BigInteger.valueOf(n), i);
                    if (result)
                        results[currNum][currIt][0] += 1;
                    else
                        results[currNum][currIt][1] += 1;
                }
                currIt++;
            }
            currNum++;
        }
        return results;
    }

    public static void main(String[] args) {
        ArrayList<Integer> testSubjects = new ArrayList<Integer>(Arrays.asList(2,3,4,5,6,7,9,13,20,21,23,27,29));
        ArrayList<Integer> iterations = new ArrayList<Integer>(Arrays.asList(1,2,4,8,16,32));
        int[][][] FermatResults = FermatPrimalityTestTester(testSubjects, iterations, 1000);
        for (int i = 0; i < testSubjects.size(); i++) {
            System.out.println(testSubjects.get(i));
            for (int j = 0; j < iterations.size(); j++) {
                System.out.println("\t" + iterations.get(j) + ": " + Arrays.toString(FermatResults[i][j]));
            }
        }
    }
}
