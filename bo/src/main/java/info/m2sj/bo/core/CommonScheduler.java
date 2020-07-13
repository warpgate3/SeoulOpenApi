package info.m2sj.bo.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 스케줄링 처리 클래스
 */
@Component
@Slf4j
public class CommonScheduler {
    private final ParkInfoDataLoader parkInfoDataLoader;

    public CommonScheduler(ParkInfoDataLoader parkInfoDataLoader) {
        this.parkInfoDataLoader = parkInfoDataLoader;
    }

    /**
     * Ehcache 에 load 돼있는 공공주차자 정보 리스트를 제거하고
     * Open Api를 호출해서 cache 재 적재 처리한다.
     */
    @Scheduled(fixedDelayString = "${api.refresh_data_interval}")
    public void batch() {
        log.info("[ refresh cache data... ]");
        //clear cache
        parkInfoDataLoader.evictAllCacheValues();
        //cache reload
        parkInfoDataLoader.getParkInfoData();
    }

}
