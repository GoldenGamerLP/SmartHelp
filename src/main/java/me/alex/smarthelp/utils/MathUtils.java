package me.alex.smarthelp.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MathUtils {

    public MathUtils() {
    }

    public HashMap<Integer, String> getBestResult(String x, @NotNull List<String> objects) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        objects.forEach(y -> {
            Bukkit.getServer().getLogger().info(y);
            int[][] dp = new int[x.length() + 1][y.length() + 1];

            for (int i = 0; i <= x.length(); i++) {
                for (int j = 0; j <= y.length(); j++) {
                    if (i == 0) {
                        dp[i][j] = j;
                    } else if (j == 0) {
                        dp[i][j] = i;
                    } else {
                        dp[i][j] = min(dp[i - 1][j - 1]
                                        + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                                dp[i - 1][j] + 1,
                                dp[i][j - 1] + 1);
                    }
                }
            }
            if (dp[x.length()][y.length()] < 30) {
                hashMap.put(dp[x.length()][y.length()] + 1, y);
                //Bukkit.getLogger().info(""+ y + dp[x.length()][y.length()]);
            }
        });

        return hashMap;
    }

    private int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
}
