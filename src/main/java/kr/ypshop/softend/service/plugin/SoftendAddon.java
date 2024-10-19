package kr.ypshop.softend.service.plugin;

import kr.ypshop.softend.service.ServiceInstance;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class SoftendAddon extends JavaPlugin {

    /**
     * 플러그인이 활성화 될 때 자동으로 등록 될 서비스 객체를 반환합니다.
     * @return 서비스 객체 리스트
     */
    public abstract List<ServiceInstance> getServices();

}
