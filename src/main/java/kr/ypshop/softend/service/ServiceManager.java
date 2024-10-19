package kr.ypshop.softend.service;

import kr.ypshop.softend.Main;
import kr.ypshop.softend.exception.UnknownServiceException;
import kr.ypshop.softend.exception.RedundantServiceException;
import kr.ypshop.softend.service.event.ServiceRegisterEvent;
import kr.ypshop.softend.service.event.ServiceUnregisterEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

    private static ServiceManager instance;
    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }

        return instance;
    }

    private final List<ServiceInstance> services = new ArrayList<>();

    public void register(ServiceInstance instance, Plugin plugin) {
        if (services.contains(instance)) {
            throw new RedundantServiceException(instance.getServiceId());
        }
        if (!instance.getPlugin().equals(plugin)) {
            throw new IllegalArgumentException("서비스 객체의 플러그인 정보와 인자로 들어온 플러그인이 일치하지 않습니다!");
        }

        instance.setRegistered(true);
        Main.getInstance().getLogger().info(String.format("서비스 %s(이)가 등록됐습니다.", instance.getServiceId()));

        Main.getInstance().getServer().getScheduler()
                .runTask(Main.getInstance(), () -> Main.getInstance().getServer().getPluginManager().callEvent(new ServiceRegisterEvent(instance)));
    }

    public ServiceInstance getService(String serviceId) throws UnknownServiceException {
        ServiceInstance instance = services.stream()
                .filter(service -> service.getServiceId().equalsIgnoreCase(serviceId))
                .findFirst().orElse(null);
        if (instance == null) {
            throw new UnknownServiceException();
        }

        return instance;
    }

    public void unregister(String serviceId) {
        ServiceInstance instance = services.stream()
                .filter(service -> service.getServiceId().equalsIgnoreCase(serviceId))
                .findFirst().orElse(null);
        if (instance == null) {
            throw new UnknownServiceException();
        }

        instance.setRegistered(false);
        services.remove(instance);

        Main.getInstance().getLogger().info(String.format("서비스 %s(이)가 등록 해제됐습니다.", serviceId));

        Main.getInstance().getServer().getScheduler()
                .runTask(Main.getInstance(), () -> Main.getInstance().getServer().getPluginManager().callEvent(new ServiceUnregisterEvent(instance)));
    }

    public void unregisterAll(Plugin plugin) {
        services.stream().filter(serviceInstance -> serviceInstance.getPlugin().equals(plugin)).forEach(serviceInstance -> unregister(serviceInstance.getServiceId()));
    }

}
