package com.worldkey.service;

import com.worldkey.entity.SystemConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author HP
 */
public interface SystemConfigService {
    SystemConfig find();
    Integer update(SystemConfig sc, MultipartFile file, String host) throws IOException;
}
