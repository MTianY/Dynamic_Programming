import sun.nio.cs.ext.MacHebrew;

public class Main {
    public static void main(String[] args) {
        // 凑够 n 分钱
        int n = 19;
        // 1.零钱兑换, 递归方式
        System.out.println("递归:" + coins1(n));
        System.out.println("======================");
        // 2.优化 1: 记忆化搜索
        System.out.println("记忆化搜索:" + coins2(n));
        System.out.println("======================");
        // 3.优化 2: 递推
        System.out.println("递推:" + coins3(n));
        System.out.println("======================");
        // 4.通用实现
        System.out.println("通用实现:" + coins4(n, new int[] {1, 5, 20, 25}));
        System.out.println("======================");
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
        int []dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            System.out.println("i = " + i);
            int min = Integer.MAX_VALUE;
            for (int face : faces) {
                System.out.println("face = " + face);
                if (i < face) {
                    System.out.println("(i = " + i + ") < (face = " + face + ") , continue! 跳出循环");
                    continue;
                }
                System.out.println("dp[" + i + " - " + face + "] = " + dp[i - face] + "; min =" + min);
                min = Math.min(dp[i - face], min);
                System.out.println("min = " + min);
            }
            dp[i] = min + 1;
            System.out.println("dp[i]:   dp[" + i + "] =" + dp[i]);
        }
        System.out.println("dp[n]:   dp[" + n + "] = " + dp[n]);
        return dp[n];
    }

}