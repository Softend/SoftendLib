[![tag](https://jitpack.io/v/Softend/SoftendLib.svg)](https://jitpack.io/#Softend/SoftendLib)

# SoftendLib
SoftendLib은 [이앤디소프트](https://github.com/Softend)에서 개발하는<br>
모든 플러그인에 사용되는 라이브러리입니다.

해당 라이브러리는 이앤디소프트 뿐만 아니라<br>
모든 마인크래프트 개발자분들께 개방되어있습니다.

## API Repository

### Gradle
```groovy
    repositories {
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        implementation 'com.github.Softend:SoftendLib:VERSION_HERE'
    }
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Softend</groupId>
        <artifactId>SoftendLib</artifactId>
        <version>Tag</version>
    </dependency>
</dependencies>
```

## 기능 설명
### [Metrics](https://bstats.org/getting-started)
[bstats](https://bstats.org)에서 제공하는 클래스를 탑재하였습니다.<br>
[자세한 설명 보기](https://bstats.org/getting-started)
### ItemBuilder
마인크래프트 ItemStack을 쉽게 다루기 위해 만들어진 클래스입니다.<br>

#### displayName(Component)
아이템의 이름을 변경합니다.

#### addLore()
아이템에 아이템 설명을 추가합니다.<br>
**기존 설명을 초기화하지 않습니다.**

#### setStringLore(List) / setLore(List)
아이템의 아이템 설명을 설정합니다.<br>
**기존 설명은 초기화합니다.**
```java
class MyEventListener implements Listener {
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = new ItemBuilder(Material.STONE)
                .displayName(Component.text("Hello, Stone!")) // 아이템의 displayName 을 설정합니다. ( String 도 가능 )
                .addLore("아이템 설명") // 아이템에 아이템 설명을 추가합니다
                .build();
        
        player.getInventory().addItem(itemStack);
    }
    
}
```

### PaginateHolder
페이지를 넘길 수 있는 인벤토리를 생성하도록 도와줍니다.
```java
class MyPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        Player player = Bukkit.getPlayer("sh7811");
        PaginateHolder holder = new PaginateHolder(YOUR_PAGINATE_CONFIG,0);
        
        player.openInventory(holder.getInventory());
    }
}
```

### ServiceManager
SoftendLib 을 통해 Singleton Instance 를 쉽게 보낼 수 있게 해줍니다.<br>

#### API 객체를 얻는 방법
ServiceManager 클래스의 getService(String) 메서드를 통해 객체를 얻을 수 있습니다.
```java
class ClientPlugin extends JavaPlugin {
    private SignletonInstance signletonInstance;
    
    @Override
    public void onEnable() {
        ServiceManager manager = ServiceManager.getInstance();
        
        try {
            signletonInstance = manager.getService("SINGLETON_SERVICE_ID");
        } catch (ServiceException e) {
            getLogger().info("서비스를 찾을 수 없습니다.");
            return;
        }
        
        signletonInstance.method("HI");
    }
}
```

#### API 객체를 수동으로 등록하는 방법
ServiceInstance 를 상속하는 객체를 ServiceManager 클래스의 register(ServiceInstance, Plugin) 메서드를 통해 등록할 수 있습니다.
```java
class APIPlugin extends JavaPlugin {
    private SignletonInstance signletonInstance;
    
    @Override
    public void onEnable() {
        ServiceManager manager = ServiceManager.getInstnace();
        manager.register(signletonInstance, this);
        
        getLogger().info("성공적으로 API를 등록하였습니다.");
    }
}
```

#### API 객체를 자동으로 등록하는 방법
Main 클래스를 SoftendAddon 을 상속하게 만든 후<br>
getServices() 메서드의 반환 값에 등록 할 객체를 추가합니다.

```java
class APIPlugin extends SoftendAddon {
    private SingletonInstance singletonInstance;
    
    @Override
    public List<ServiceInstance> getServices() {
        return List.of(singletonInstance);
    }
}
```


