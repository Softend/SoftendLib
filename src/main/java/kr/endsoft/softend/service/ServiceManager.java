package kr.endsoft.softend.service;

import kr.endsoft.softend.Main;
import kr.endsoft.softend.exception.service.RedundantServiceException;
import kr.endsoft.softend.exception.service.ServiceException;
import kr.endsoft.softend.service.event.ServiceUnregisterEvent;
import kr.endsoft.softend.service.event.ServiceRegisterEvent;
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

        services.add(instance);
        instance.setRegistered(true);
        Main.getInstance().getLogger().info(String.format("서비스 %s(이)가 등록됐습니다.", instance.getServiceId()));

        Main.getInstance().getServer().getScheduler()
                .runTask(Main.getInstance(), () -> Main.getInstance().getServer().getPluginManager().callEvent(new ServiceRegisterEvent(instance)));
    }

    public ServiceInstance getService(String serviceId) throws ServiceException {
        ServiceInstance instance = services.stream()
                .filter(service -> service.getServiceId().equalsIgnoreCase(serviceId))
                .findFirst().orElse(null);
        if (instance == null) {
            throw new ServiceException(ServiceException.UNKNOWN_SERVICE);
        }

        return instance;
    }

    public void unregister(String serviceId) throws ServiceException {
        ServiceInstance instance = services.stream()
                .filter(service -> service.getServiceId().equalsIgnoreCase(serviceId))
                .findFirst().orElse(null);
        if (instance == null) {
            throw new ServiceException(ServiceException.UNKNOWN_SERVICE);
        }

        instance.setRegistered(false);
        services.remove(instance);

        Main.getInstance().getLogger().info(String.format("서비스 %s(이)가 등록 해제됐습니다.", serviceId));

        Main.getInstance().getServer().getScheduler()
                .runTask(Main.getInstance(), () -> Main.getInstance().getServer().getPluginManager().callEvent(new ServiceUnregisterEvent(instance)));
    }

    public void unregisterAll(Plugin plugin) {
        List.copyOf(services).stream().filter(serviceInstance -> serviceInstance.getPlugin().equals(plugin)).forEach(serviceInstance -> unregister(serviceInstance.getServiceId()));
    }

}
