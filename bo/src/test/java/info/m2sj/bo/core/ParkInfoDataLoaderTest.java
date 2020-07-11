package info.m2sj.bo.core;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest()
class ParkInfoDataLoaderTest {
    @Autowired
    private ParkInfoDataLoader parkInfoDataLoader;

    @Test
    public void getParkInfoData_test() {
        parkInfoDataLoader.getParkInfoData();

        List<Map<String, String>> parkInfoData = parkInfoDataLoader.getParkInfoData();
        assertThat(parkInfoData.size(),greaterThan(0));
    }
}