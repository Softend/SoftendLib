package kr.endsoft.softend.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

public abstract class ServiceInstance {

    public abstract Plugin getPlugin();

    public abstract String getServiceId();

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private boolean registered = false;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceInstance instance)) {
            return false;
        }

        return instance.getServiceId().equalsIgnoreCase(this.getServiceId());
    }
}
