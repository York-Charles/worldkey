package com.worldkey.service.impl;

import com.worldkey.entity.SystemConfig;
import com.worldkey.mapper.SystemConfigMapper;
import com.worldkey.service.SystemConfigService;
import com.worldkey.util.FileUploadUtilAsync;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * @author HP
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {
    @Resource
    private SystemConfigMapper sMapper;
    @Override
    @Cacheable(cacheNames = "SystemConfig",key = "'SystemConfigFind'")
    public SystemConfig find() {
        return this.sMapper.find();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @CacheEvict(key = "'SystemConfigFind'",cacheNames = "SystemConfig")
    public Integer update(SystemConfig sc, MultipartFile file, String host) throws IOException {
        if (file.getSize() != 0) {
            String imgSrc = new FileUploadUtilAsync().getFileName(host, file);
            sc.setDefaultHeadimg(imgSrc);
        }
        return this.sMapper.update(sc);
    }



}
