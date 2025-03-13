package com.tencent.wxcloudrun.utils;

import lombok.Data;

@Data
public
class IpLocation {
    private String ip;
    private String country;
    private String province;
    private String filteredProvince;
    private String city;
}