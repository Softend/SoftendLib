package kr.ypshop.softend.service.event;

import kr.ypshop.softend.service.ServiceInstance;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class ServiceRegisterEvent extends Event {

    @ApiStatus.Internal
    public ServiceRegisterEvent(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    @Getter
    private final ServiceInstance serviceInstance;
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
