package com.worldkey.service;

public interface CaptService {
    String getCapt(String token);
    String setCapt(String phone, Integer timeout);
    String getTelNumAndCodeMD5(String temToken);
}
