package com.worldkey.service;

import java.util.List;
import java.util.Map;

import com.worldkey.entity.History;

public interface HistoryService {
	List<History> selectHistory(Long id);

}
