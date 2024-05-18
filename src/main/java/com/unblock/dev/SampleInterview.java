package com.unblock.dev;

import java.util.Arrays;

public class SampleInterview {
    /**
     * given an arr of int
     * size K
     * find max in cont sub arr
     * 1 2 3
     * K - 3
     *
     *
     * Input: arr[] = {1, 2, 3, 1, 4, 5, 2, 3, 6}, K = 3
     * Output: 3 3 4 5 5 5 6
     * Explanation: Maximum of 1, 2, 3 is 3
     *                        Maximum of 2, 3, 1 is 3
     *                        Maximum of 3, 1, 4 is 4
     *                        Maximum of 1, 4, 5 is 5
     *                        Maximum of 4, 5, 2 is 5
     *                        Maximum of 5, 2, 3 is 5
     *                        Maximum of 2, 3, 6 is 6
     *
     *    [3,3,3,3] K=3
     *    [3]
     *  calArr [n-k]
     *  int max = 3
     *  calArr 3
     */

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 4, 5, 2, 3, 6};
        int n = arr.length;
        int k = 3;
        int calArr[] = new int[n - k + 1];
        int id = 0;

        int max = arr[0];
        int st = 0, end = st + k;
        while (st < end) {
            max = Math.max(max, arr[st++]);
        }
        calArr[id++] = max;

        for (int i=end; i < n; i++) {
            // 1 2 3 4 -> 3 2
            if (max == arr[st++]) {
                int j = st;
                while (j < i && arr[j] == max) {
                    j++;
                }
                int secMax = max;
                if (j != i) {
                    secMax = arr[st+1];

                    while (j < i && arr[j] < max && arr[j] > secMax) {
                        secMax = arr[j];
                    }
                }
                max = secMax;
            } else {
                max = Math.max(max, arr[i]);
            }
            calArr[id++] = max;
        }

        Arrays.stream(calArr).forEach( val -> System.out.println(val));

    }
}
