package org.xingchang.brapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xingchang.brapi.entity.ItemType;
import org.xingchang.brapi.entity.Location;
import org.xingchang.brapi.entity.SystemConfig;
import org.xingchang.brapi.repository.ItemTypeRepository;
import org.xingchang.brapi.repository.LocationRepository;
import org.xingchang.brapi.repository.SystemConfigRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置Service
 */
@Service
public class SettingsService {
    
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    
    @Autowired
    private LocationRepository locationRepository;
    
    // ========== 基础设置 ==========
    
    public Map<String, String> getBasicSettings() {
        Map<String, String> settings = new HashMap<>();
        
        // 获取基础配置，如果不存在则使用默认值
        settings.put("siteName", getConfigStringValue("site_name", "校园失物追寻平台"));
        settings.put("logo", getConfigStringValue("logo", ""));
        settings.put("phone", getConfigStringValue("phone", ""));
        settings.put("email", getConfigStringValue("email", ""));
        
        return settings;
    }
    
    private String getConfigStringValue(String key, String defaultValue) {
        return systemConfigRepository.findByConfigKey(key)
                .map(SystemConfig::getConfigValue)
                .orElse(defaultValue);
    }
    
    @Transactional
    public void updateBasicSettings(Map<String, String> settings) {
        // 映射前端字段到配置键
        Map<String, String> keyMapping = new HashMap<>();
        keyMapping.put("siteName", "site_name");
        keyMapping.put("logo", "logo");
        keyMapping.put("phone", "phone");
        keyMapping.put("email", "email");
        
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            String configKey = keyMapping.getOrDefault(entry.getKey(), entry.getKey());
            saveConfig(configKey, entry.getValue());
        }
    }
    
    // ========== 物品类型管理 ==========
    
    public List<ItemType> getItemTypes() {
        return itemTypeRepository.findAll();
    }
    
    @Transactional
    public ItemType addItemType(ItemType itemType) {
        itemType.setCreateTime(LocalDateTime.now());
        itemType.setUpdateTime(LocalDateTime.now());
        return itemTypeRepository.save(itemType);
    }
    
    @Transactional
    public ItemType updateItemType(ItemType itemType) {
        ItemType existingType = itemTypeRepository.findById(itemType.getId())
                .orElseThrow(() -> new RuntimeException("物品类型不存在"));
        
        existingType.setName(itemType.getName());
        existingType.setEnabled(itemType.getEnabled());
        existingType.setSort(itemType.getSort());
        existingType.setUpdateTime(LocalDateTime.now());
        
        return itemTypeRepository.save(existingType);
    }
    
    @Transactional
    public void deleteItemType(Long id) {
        if (!itemTypeRepository.existsById(id)) {
            throw new RuntimeException("物品类型不存在");
        }
        itemTypeRepository.deleteById(id);
    }
    
    // ========== 地点管理 ==========
    
    public List<Location> getLocations() {
        return locationRepository.findAll();
    }
    
    @Transactional
    public Location addLocation(Location location) {
        location.setCreateTime(LocalDateTime.now());
        location.setUpdateTime(LocalDateTime.now());
        return locationRepository.save(location);
    }
    
    @Transactional
    public Location updateLocation(Location location) {
        Location existingLocation = locationRepository.findById(location.getId())
                .orElseThrow(() -> new RuntimeException("地点不存在"));
        
        existingLocation.setName(location.getName());
        existingLocation.setEnabled(location.getEnabled());
        existingLocation.setSort(location.getSort());
        existingLocation.setUpdateTime(LocalDateTime.now());
        
        return locationRepository.save(existingLocation);
    }
    
    @Transactional
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new RuntimeException("地点不存在");
        }
        locationRepository.deleteById(id);
    }
    
    // ========== 匹配算法配置 ==========
    
    public Map<String, Object> getMatchAlgorithmConfig() {
        Map<String, Object> config = new HashMap<>();
        
        // 匹配算法权重
        config.put("imageWeight", getConfigValue("image_weight", "0.4"));
        config.put("locationWeight", getConfigValue("location_weight", "0.3"));
        config.put("textWeight", getConfigValue("text_weight", "0.3"));
        config.put("matchThreshold", getConfigValue("match_threshold", "0.6"));
        
        // 其他系统配置
        config.put("minReward", getConfigValue("min_reward", "0"));
        config.put("maxReward", getConfigValue("max_reward", "1000"));
        config.put("maxImages", getConfigValue("max_images", "9"));
        config.put("maxImageSize", getConfigValue("max_image_size", "5"));
        config.put("autoCloseDay", getConfigValue("auto_close_day", "30"));
        
        return config;
    }
    
    @Transactional
    public void updateMatchAlgorithmConfig(Map<String, Object> config) {
        // 匹配算法权重
        if (config.containsKey("imageWeight")) {
            saveConfig("image_weight", config.get("imageWeight").toString());
        }
        if (config.containsKey("locationWeight")) {
            saveConfig("location_weight", config.get("locationWeight").toString());
        }
        if (config.containsKey("textWeight")) {
            saveConfig("text_weight", config.get("textWeight").toString());
        }
        if (config.containsKey("matchThreshold")) {
            saveConfig("match_threshold", config.get("matchThreshold").toString());
        }
        
        // 其他系统配置
        if (config.containsKey("minReward")) {
            saveConfig("min_reward", config.get("minReward").toString());
        }
        if (config.containsKey("maxReward")) {
            saveConfig("max_reward", config.get("maxReward").toString());
        }
        if (config.containsKey("maxImages")) {
            saveConfig("max_images", config.get("maxImages").toString());
        }
        if (config.containsKey("maxImageSize")) {
            saveConfig("max_image_size", config.get("maxImageSize").toString());
        }
        if (config.containsKey("autoCloseDay")) {
            saveConfig("auto_close_day", config.get("autoCloseDay").toString());
        }
    }
    
    private Object getConfigValue(String key, String defaultValue) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key).orElse(null);
        if (config == null) {
            return parseConfigValue(defaultValue);
        }
        return parseConfigValue(config.getConfigValue());
    }
    
    private Object parseConfigValue(String value) {
        try {
            // 尝试解析为整数
            if (!value.contains(".")) {
                return Integer.parseInt(value);
            }
            // 解析为浮点数
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // 返回字符串
            return value;
        }
    }
    
    private void saveConfig(String key, String value) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElse(new SystemConfig());
        config.setConfigKey(key);
        config.setConfigValue(value);
        config.setUpdateTime(LocalDateTime.now());
        systemConfigRepository.save(config);
    }
}
