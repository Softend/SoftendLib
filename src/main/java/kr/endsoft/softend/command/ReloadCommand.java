package kr.endsoft.softend.command;

import kr.endsoft.softend.Main;
import kr.endsoft.softend.file.config.MessageLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ReloadCommand extends SoftendCommand {
    public ReloadCommand() {
        super("softend", Main.getInstance(), "§6/softend reload [<플러그인이름>/all] - §b해당 플러그인의 messages.yml 을 리로드합니다.");
    }

    @Override
    public void run(CommandSender sender, String[] args, String label, Command command) {
        if (!sender.hasPermission("softend.admin")) {
            return;
        }

        if (args.length != 2 || !args[0].equalsIgnoreCase("reload")) {
            printUsage(sender);
            return;
        }

        if (args[1].equalsIgnoreCase("all")) {
            MessageLoader.getPlugins().forEach(plugin -> {
                MessageLoader.getInstance(plugin).loadContent();
                plugin.reloadConfig();
            });
            sender.sendMessage("§amessages.yml 및 config.yml을 성공적으로 다시 불러왔습니다.");
            return;
        }

        Plugin plugin = Main.getInstance().getServer().getPluginManager().getPlugin(args[1]);
        if (plugin == null) {
            sender.sendMessage("§c존재하지 않는 플러그인입니다.");
            return;
        }

        if (!MessageLoader.getPlugins().contains(plugin)) {
            sender.sendMessage("§c해당 플러그인은 ECS 미지원 플러그인입니다.");
            return;
        }

        plugin.reloadConfig();
        if  (!MessageLoader.getInstance(plugin).loadContent()) {
            sender.sendMessage("§cmessages.yml 및 config.yml을 다시 불러오는데 실패했습니다.");
            return;
        }

        sender.sendMessage("§amessages.yml 및 config.yml을 성공적으로 다시 불러왔습니다.");
    }

    @Override
    public List<String> tabList(CommandSender sender, String[] args, String label, Command command) {
        if (!sender.hasPermission("softend.admin")) {
            return List.of();
        }

        if (args.length == 1) {
            return List.of("reload");
        }

        if (args[0].equalsIgnoreCase("reload") && args.length == 2) {
            return MessageLoader.getPlugins().stream().map(Plugin::getName).toList();
        }
        return List.of();
    }
}
