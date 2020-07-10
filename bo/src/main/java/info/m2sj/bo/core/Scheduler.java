package info.m2sj.bo.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {
    private final ParkInfoDataLoader parkInfoDataLoader;

    public Scheduler(ParkInfoDataLoader parkInfoDataLoader) {
        this.parkInfoDataLoader = parkInfoDataLoader;
    }

    @Scheduled(fixedDelayString = "${api.refresh_data_interval}")
    public void batch() {
        log.info("[ refresh cache data... ]");
        parkInfoDataLoader.evictAllCacheValues();
        parkInfoDataLoader.getParkInfoData();
    }

}
