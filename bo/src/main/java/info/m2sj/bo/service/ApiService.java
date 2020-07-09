package info.m2sj.bo.service;

import info.m2sj.bo.dto.ApiParam;

import java.util.Map;

public interface ApiService {
    Map call(ApiParam param);
}
