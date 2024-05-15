package com.eaproc.tutorials.librarymanagement.web.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DiskSpaceHealthIndicator implements HealthIndicator {

    private final File disk = new File("/");

    @Override
    public Health health() {
        long freeSpace = disk.getFreeSpace();
        long totalSpace = disk.getTotalSpace();

        // 10MB
        long threshold = 10485760;
        if (freeSpace < threshold) {
            return Health.down()
                    .withDetail("error", "Free disk space below threshold")
                    .withDetail("total", humanReadableByteCountBin(totalSpace))
                    .withDetail("free", humanReadableByteCountBin(freeSpace))
                    .withDetail("threshold", humanReadableByteCountBin(threshold))
                    .build();
        } else {
            return Health.up()
                    .withDetail("total", humanReadableByteCountBin(totalSpace))
                    .withDetail("free", humanReadableByteCountBin(freeSpace))
                    .withDetail("threshold", humanReadableByteCountBin(threshold))
                    .build();
        }
    }

    private String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        String[] units = {"KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};
        int i;
        for (i = 0; i < units.length && value >= 1024; i++) {
            value /= 1024;
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %s", value / 1024.0, units[i - 1]);
    }
}
