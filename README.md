# 动态规划 Dynamic Programming

简称 DP.

是求解最优问题的一种常见策略.

通常的使用套路(一步一步优化).

1. 暴力递归 (自顶向下, 出现了重叠子问题)
2. 记忆化搜索 (自顶向下)
3. 递推 (自底向上)

## 动态规划常规步骤

1.定义状态 (状态是原问题、子问题的解)

- 如定义 `dp(i)` 的含义

2.设置初始状态 (边界)

- 如设置 `dp(0)` 的值

3.确定状态转移方程

- 如确定 `dp(i)` 和 `dp(i-1)` 的关系

## 动态规划概念

- 将复杂的原问题拆解成若干个简单的子问题
- 每个子问题仅仅解决 1 次, 并保存它们的解
- 最后推到出原问题的解

可以用动态规划来解决的问题, 通常具备 2 个特点:

- 最优子结构(最优化原理): 通过求解子问题的最优解, 可以获得原问题的最优解
- 无后效性
    - 某阶段的状态一旦确定, 则此后过程的演变不再受此前各状态及决策的影响(未来与过去无关)
    - 在推到后面阶段的状态时,只关系前面阶段的具体状态值,不关系这个状态是怎么一步步推到出来的 

#### 无后效性示例:

从起点 (0,0) 走到终点 (4,4) 一共有多少种走法? 只能向右, 向下走.

| (0,0) |  |  |  |  |
| :-: | :-: | :-: | :-: | :-: |
|  |  | (i,j-1) |  |  |
|  | (i-1,j) | (i,j) |  |  |
|  |  |  |  |  |
|  |  |  |  | (4,4) |

假设 `dp(i,j)` 是从 (0,0) 走到 (i,j) 的走法.

- dp(i,0) = dp(0,j) = 1;
- dp(i,j) = dp(i,j-1) + dp(i-1,j);

无后效性:
- 推到 dp(i,j) 时只需要用到 dp(i,j-1). dp(i-1,j) 的值.
- 不需要关心 dp(i,j-1), dp(i-1,j)的值是怎么求出来的

## 练习 1. 找零钱. 零钱兑换

假设有 25 分, 20 分,5 分,1 分的硬币, 要找给客户 41 分的零钱, 如何办到硬币个数`最少`.

- 之前贪心算法求的 5 枚硬币, 不是最优解

**1. 定义状态** :

假设 dp(n) 是凑到 n 分需要的最少硬币个数:

dp(41) 就是凑到 41 分的最少硬币个数.

动态规划考虑到了所有可能的情况.

- 如果第 1 次选择 25 分硬币, 那么 dp(n) = dp(n - 25) + 1;
- 如果第 1 次选择 20 分硬币, 那么 dp(n) = dp(n - 20) + 1;
- 如果第 1 次选择 5 分硬币, 那么 dp(n) = dp(n - 5) + 1;
- 如果第 1 次选择 1 分硬币, 那么 dp(n) = dp(n - 1) + 1;

所以取其中`最小`的一个加 1, dp(n) = min{dp(n - 25),dp(n - 20),dp(n - 5),dp(n - 1)} + 1;

#### 方式 1, 递归调用.

```java
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
```


#### 方式 2. 优化递归调用, 记忆化搜索

```java
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
```

#### 方式 3: 递推, 迭代

```java
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
```

#### 方式 4: 通用实现方案

```java
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
```

### 练习 2: 求最大子序列的和.

给定一个长度为 n 的整数序列, 求它的最大连续子序列的和.
如 -2, 1, -3, 4, -1, 2, 1, -5, 4 的最大连续子序列和是 4 + (-1) + 2 + 1 = 6

1.状态定义:
假设 dp(i) 是以 nums[i] 结尾的最大连续子序列和 (nums 是整个序列),
-2 结尾最大和 dp(0) = -2.
1 结尾则 dp(1) = 1.
...

2.状态转移方程:
所以求 dp(i), 参考前一个的和, dp(i-1),如果 dp(i-1)<0,那么就不需要加上 dp(i-1)的和
即:
```java
if (dp(i-1) <= 0) {
    // 不要 i-1 的和, 那么 dp[i]就和 i 位置元素一样
    dp[i] = nums[i];
    } else {
    // 前 i-1 的和加上 i 位置元素的和
    dp[i] = dp[i-1] + nums[i];
}
```

3.初始状态:
dp(0) = nums[0]

4.最终推导出最终解:
最大连续子序列和是所有 dp(i)中最大值 `max{dp(i)}`, i∈[0,nums.length);

实现方案 1:

```java
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
```

实现方案 2: (基于 1 的优化)

```java
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
```


