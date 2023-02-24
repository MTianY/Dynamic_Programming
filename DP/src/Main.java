import sun.nio.cs.ext.MacHebrew;

public class Main {
    public static void main(String[] args) {
        // 凑够 n 分钱
        int n = 0;
        // 1.零钱兑换, 递归方式
//        System.out.println("递归:" + coins1(n));
//        System.out.println("======================");
        // 2.优化 1: 记忆化搜索
//        System.out.println("记忆化搜索:" + coins2(n));
//        System.out.println("======================");
//         3.优化 2: 递推
//        System.out.println("递推:" + coins3(n));
//        System.out.println("======================");
        // 4.通用实现
//        System.out.println("通用实现:" + coins4(n, new int[] {1}));
        System.out.println("======================");

        // 最大连续子序列和
//        System.out.println(maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
//        System.out.println(maxSubArray2(new int[]{-2,1,-3,4,-1,2,1,-5,4}));

        // LIS 最长上升子序列
//        System.out.println(lenghtOfLIS(new int[]{0,1,0,3,2,3}));

        // LCS 最长公共子序列
        int[] lcsnums1 = new int[]{1,3,5,9,10};
        int[] lcsnums2 = new int[]{1,4,9,10};
        System.out.println(lcs(lcsnums1, lcsnums2));
        System.out.println(lcs2(lcsnums1, lcsnums2));
        System.out.println(lcs3(lcsnums1, lcsnums2));
        System.out.println(lcs4(lcsnums1, lcsnums2));
        System.out.println(lcs5(lcsnums1, lcsnums2));
        System.out.println(new Main().longestCommonSubsequence("abc","def"));
    }

    /**
     * 练习 1: 零钱兑换 (自顶向下调用)
     * 假设有 25 分, 20 分,5 分,1 分的硬币, 要找给客户 41 分的零钱, 如何办到硬币个数`最少`.
     */

    // 递归方式. 暂时固定死四个数据
    /**
     * @param n 找给客户 n 分的零钱
     * @return 最少硬币个数
     */
    static int coins1(int n) {
        // 递归基
        if (n < 1) return Integer.MAX_VALUE;
        if (n == 25 || n == 20 || n == 5 || n == 1) return 1;
        // 比较四个值中最小的
        int min1 = Math.min(coins1(n - 25), coins1(n - 20));
        int min2 = Math.min(coins1(n - 5), coins1(n - 1));
        // 取四个中最小的一个
        return Math.min(min1, min2) + 1;
    }

    /**
     * 递归会有重复调用问题
     *
     * 优化 1: 记忆化搜索 (自顶向下调用)
     */

    static int coins2(int n) {
        if (n < 1) return -1;
        int[] dp = new int[n + 1];
        int[] faces = {1, 5, 20, 25};
        for (int face : faces) {
            if (n < face) break;
            dp[face] = 1;
        }
        return coins2(n, dp);
    }

    static int coins2(int n, int[] dp) {
        // 递归基
        if (n < 1) return Integer.MAX_VALUE;    // 返回无穷大, min 方法取另一个值
        if (n == 25 || n == 20 || n == 5 || n == 1) return 1;
        // 之前没赋过值, 赋值
        if (dp[n] == 0) {
            int min1 = Math.min(coins2(n - 25, dp), coins2(n - 20, dp));
            int min2 = Math.min(coins2(n - 5, dp), coins2(n - 1, dp));
            dp[n] = Math.min(min1, min2) + 1;
        }
        // 赋过值,直接返回
        return dp[n];
    }

    /**
     * 优化 2: 递推 (自底向上, 也叫迭代)
     *
     * 不使用递归
     */

    static int coins3(int n) {
        if (n < 1) return -1;
        int[] dp = new int[n + 1];
        int[] faces = new int[n + 1];   // 凑够 n 分钱, 最后一个面值
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            if (i >= 1 && dp[i - 1] < min)  {
                min = Math.min(dp[i - 1], min);
                // 凑够 n 分钱, 最后面值
                faces[i] = 1;
            }
            if (i >= 5 && dp[i - 5] < min)  {
                min = Math.min(dp[i - 5], min);
                // 凑够 n 分钱, 最后面值
                faces[i] = 5;
            }
            if (i >= 20 && dp[i - 20] < min) {
                min = Math.min(dp[i - 20], min);
                // 凑够 n 分钱, 最后面值
                faces[i] = 20;
            }
            if (i >= 25 && dp[i - 25] < min) {
                min = Math.min(dp[i - 25], min);
                // 凑够 n 分钱, 最后面值
                faces[i] = 25;
            }
            dp[i] = min + 1;

            // 打印凑够 n 分钱,需要的具体面值
            print(faces, i);

        }
        // 打印凑够 n 分钱,需要的具体面值
//        print(faces, n);
        return dp[n];
    }

    static void print(int[] faces, int n) {
        System.out.print("凑够 [" + n + "] 分 =");
        while (n > 0) {
            System.out.print(faces[n] + " ");
            n -= faces[n];
        }
        System.out.println();
    }

    /**
     * 通用实现方案
     */

    static int coins4(int n, int[] faces) {
        if (n < 1 || faces == null || faces.length == 0) return -1;
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            System.out.println("i = " + i);
            int min = Integer.MAX_VALUE;
            for (int face : faces) {
                System.out.println("face = " + face);
                if (i < face) {
                    System.out.println("(i = " + i + ") < (face = " + face + ") , continue! 跳出循环");
                    continue;
                }
                int v = dp[i - face];
                if (v < 0 || v >= min) continue;
                min = v;
                System.out.println("min = " + min);
            }
            if (min == Integer.MAX_VALUE) {
                dp[i] = -1;
            } else {
                dp[i] = min + 1;
            }
            System.out.println("dp[i]:   dp[" + i + "] =" + dp[i]);
        }
        System.out.println("dp[n]:   dp[" + n + "] = " + dp[n]);
        return dp[n];
    }

    /**
     * 练习 2 - 最大连续子序列的和.
     *
     * 给定一个长度为 n 的整数序列, 求它的最大连续子序列的和.
     * 如 -2, 1, -3, 4, -1, 2, 1, -5, 4 的最大连续子序列和是 4 + (-1) + 2 + 1 = 6
     */

    /**
     * 状态定义:
     * 假设 dp(i) 是以 nums[i] 结尾的最大连续子序列和 (nums 是整个序列)
     * -2 结尾最大和 dp(0) = -2
     * 1 结尾则 dp(1) = 1
     * ...
     *
     * 状态转移方程
     * 所以求 dp(i), 参考前一个的和, dp(i-1),如果 dp(i-1)<0,那么就不需要加上 dp(i-1)的和
     * 即:
     * if (dp(i-1) <= 0) {
     *      // 不要 i-1 的和, 那么 dp[i]就和 i 位置元素一样
     *     dp[i] = nums[i];
     * } else {
     *      // 前 i-1 的和加上 i 位置元素的和
     *     dp[i] = dp[i-1] + nums[i];
     * }
     *
     *
     * 初始状态:
     * dp(0) = nums[0];
     *
     * 推导出最终解:
     * 最大连续子序列和是所有 dp(i)中最大值 max{dp(i)}, i∈[0,nums.length);
      */

    static int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        // 以 nums[i] 结尾的最大连续子序列和
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        int max = dp[0];
        System.out.println("dp[0] = " + dp[0]);
        for (int i = 1; i < nums.length; i++) {
            if (dp[i - 1] <= 0) {
                dp[i] = nums[i];
            } else {
                dp[i] = dp[i - 1] + nums[i];
            }
            max = Math.max(dp[i], max);
            System.out.println("dp[" + i + "] = " + dp[i]);
        }
        return max;
    }

    // 优化, 去掉数组
    // {-2,1,-3,4,-1,2,1,-5,4}
    static int maxSubArray2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int dp = nums[0];
        int max = dp;
        for (int i = 1; i < nums.length; i++) {
            System.out.print("i = " + i + "; dp = " + dp);
            if (dp <= 0) {
                dp = nums[i];
            } else {
                dp = dp + nums[i];
            }
            System.out.print(";  dp = " + dp);
            System.out.println();
            max = Math.max(dp, max);
        }
        return max;
    }

    // 最长上升子序列 LIS
    // [0,1,0,3,2,3]
    static int lenghtOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] dp = new int[nums.length];
        int max = dp[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            System.out.println("i = " + i);
            // 初始值为 1
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                System.out.println("j = " + j);
                // 后面的比前面的小, 直接略过
                if (nums[i] <= nums[j]) continue;
                // nums[i] 比 nums[j] 的值大, 则加到 dp[j] 的后面, 加 1
                dp[i] = Math.max(dp[i], dp[j] + 1);
                System.out.println("dp[i]; dp[" + i + "] = " + dp[i]);
            }
            max = Math.max(dp[i], max);
            System.out.println("max = " + max);
        }
        return max;
    }

    /**
     * 练习 4: 最长公共子序列, LCS
     */

    /**
     * 递归方式:
     * 空间复杂度: O(k), k = min{n, m}. n,m是 2 个序列的长度.
     * 时间复杂度: O(2^n). 当 n = m 时
      */

    static int lcs(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) return 0;
        if (nums2 == null || nums2.length == 0) return 0;
        return lcs(nums1, nums1.length, nums2, nums2.length);
    }

    static int lcs(int[] nums1, int i, int[] nums2, int j) {
        if (i == 0 || j == 0) return 0;
        if (nums1[i - 1] == nums2[j - 1]) {
            return lcs(nums1, i - 1, nums2, j - 1) + 1;
        }
        return Math.max(lcs(nums1, i - 1, nums2, j), lcs(nums1, i, nums2, j - 1));
    }

    /**
     * 非递归实现
     */
    
    static int lcs2(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) return 0;
        if (nums2 == null || nums2.length == 0) return 0;
        int[][] dp = new int[nums1.length + 1][nums2.length + 1];
        for (int i = 1; i <= nums1.length ; i++) {
            for (int j = 1; j <= nums2.length; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[nums1.length][nums2.length];
    }

    /**
     * 滚动数组
     */

    static int lcs3(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) return 0;
        if (nums2 == null || nums2.length == 0) return 0;
        int[][] dp = new int[2][nums2.length + 1];
        for (int i = 1; i <= nums1.length; i++) {
            int row = i & 1;
            int prevRow = (i - 1) & 1;
            for (int j = 1; j <= nums2.length; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[row][j] = dp[prevRow][j - 1] + 1;
                } else {
                    dp[row][j] = Math.max(dp[prevRow][j], dp[row][j - 1]);
                }
            }
        }
        return dp[nums1.length & 1][nums2.length];
    }

    /**
     * 一维数组
     * 后面算出来的值要覆盖之前的值.
     * 如果 i 和 j 位置元素不相等, 假设此时一位数组元素存储的是, 前 j 个是后面新算出来覆盖后的元素, j 开始还是之前的元素.
     * 所有 i 和 j 位置元素不相等, 求 j 位置元素则是,dp(j)和dp(j-1)较大者
     * 如果 i 和 j 位置元素相等, 那么取左上角元素加 1, 但是左上角之前的元素已经被覆盖掉了, 取遍历 cur 记录. cur 默认为 0.
     * 因为最开始左上角元素都是 0.遍历 j 时,存 cur = dp[j]
     */

    static int lcs4(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) return 0;
        if (nums2 == null || nums2.length == 0) return 0;
        int[] dp = new int[nums2.length + 1];
        for (int i = 1; i <= nums1.length; i++) {
            int cur = 0;
            for (int j = 1; j <= nums2.length; j++) {
                int leftTop = cur;
                cur = dp[j];
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[j] = leftTop + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
            }
        }
        return dp[nums2.length];
    }

    static int lcs5(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) return 0;
        if (nums2 == null || nums2.length == 0) return 0;
        int[] rowsNums = nums1, colsNums = nums2;
        // 取长度最短的作为列, 优化一维数组长度
        if (nums1.length < nums2.length) {
            colsNums = nums1;
            rowsNums = nums2;
        }
        int[] dp = new int[colsNums.length + 1];
        for (int i = 1; i <= rowsNums.length; i++) {
            int cur = 0;
            for (int j = 1; j <= colsNums.length; j++) {
                int leftTop = cur;
                cur = dp[j];
                if (rowsNums[i - 1] == colsNums[j - 1]) {
                    dp[j] = leftTop + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
            }
        }
        return dp[colsNums.length];
    }

    public int longestCommonSubsequence(String text1, String text2) {
        if (text1 == null || text2 == null) return 0;
        char[] chars1 = text1.toCharArray();
        if (chars1.length == 0) return 0;
        char[] chars2 = text2.toCharArray();
        if (chars2.length == 0) return 0;

        char[] rowChars = chars1, colsChars = chars2;
        if (chars1.length < chars2.length) {
            colsChars = chars1;
            rowChars = chars2;
        }
        int[] dp = new int[colsChars.length + 1];
        for (int i = 1; i <= rowChars.length; i++) {
            int cur = 0;
            for (int j = 1; j <= colsChars.length; j++) {
                int leftTop = cur;
                cur = dp[j];
                if (rowChars[i - 1] == colsChars[j - 1]) {
                    dp[j] = leftTop + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
            }
        }
        return dp[colsChars.length];
    }

    public int longestCommonSubsequence2(String text1, String text2) {
        if (text1 == null || text2 == null) return 0;
        char[] chars1 = text1.toCharArray();
        if (chars1.length == 0) return 0;
        char[] chars2 = text2.toCharArray();
        if (chars2.length == 0) return 0;

        int[] dp = new int[chars2.length + 1];

        for (int i = 1; i <= chars1.length; i++) {
            int cur = 0;
            for (int j = 1; j <= chars2.length; j++) {
                int leftTop = cur;
                cur = dp[j];
                if (chars1[i - 1] == chars2[j - 1]) {
                    dp[j] = leftTop + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
            }
        }
        return dp[chars2.length];
    }


}