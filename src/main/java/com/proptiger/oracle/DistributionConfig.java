package com.proptiger.oracle;

import java.util.Arrays;
import java.util.List;

public class DistributionConfig {

    public static final DistributionConfig       PERCENTILE_25_50_25 = new DistributionConfig(new Integer[] {
            25,
            50,
            25                                                      });
    public static final DistributionConfig       PERCENTILE_20_60_20 = new DistributionConfig(new Integer[] {
            20,
            60,
            20                                                      });
    public static final DistributionConfig       PERCENTILE_35_50_15 = new DistributionConfig(new Integer[] {
            35,
            50,
            15                                                      });
    public static final DistributionConfig       PERCENTILE_30_40_30 = new DistributionConfig(new Integer[] {
            30,
            40,
            30                                                      });
    public static final DistributionConfig       PERCENTILE_25_40_35 = new DistributionConfig(new Integer[] {
            25,
            40,
            35                                                      });

    public static final DistributionConfig       PERCENTILE_35_35_30 = new DistributionConfig(new Integer[] {
            35,
            35,
            30                                                      });
    public static final DistributionConfig       PERCENTILE_30_30_40 = new DistributionConfig(new Integer[] {
            30,
            30,
            40                                                      });
    public static final DistributionConfig       PERCENTILE_25_30_45 = new DistributionConfig(new Integer[] {
            25,
            30,
            45                                                      });

    public static final List<DistributionConfig> KNOWN_CONFIGS       = Arrays.asList(new DistributionConfig[] {
            PERCENTILE_25_50_25,
            PERCENTILE_20_60_20,
            PERCENTILE_35_50_15,
            PERCENTILE_30_40_30,
            PERCENTILE_25_40_35,
            PERCENTILE_35_35_30,
            PERCENTILE_30_30_40,
            PERCENTILE_25_30_45                                     });

    private Integer[]                            configs;

    /**
     * Partition at 0th index represents highest scored partitions
     * 
     * @param configs
     */
    public DistributionConfig(Integer[] configs) {
        if (configs == null || configs.length == 0) {
            throw new IllegalArgumentException("Empty config");
        }
        int sum = 0;
        for (Integer config : configs) {
            sum += config;
        }
        if (sum != 100) {
            throw new IllegalArgumentException("Invalid config as sum is not 100 " + configs + " " + sum);
        }
        this.configs = configs;
    }

    public Integer[] getConfigs() {
        return configs;
    }

    /**
     * Split data based on config
     * 
     * @param list
     * @return
     */
    public List<?>[] split(List<?> list) {

        return null;
    }

    @Override
    public String toString() {
        return "DistributionConfig [configs=" + Arrays.toString(configs) + "]";
    }
}
