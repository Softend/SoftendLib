package kr.endsoft.softend.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class SoftendCommand {

    private final String commandName;
    private final JavaPlugin plugin;
    private final String usage;

    public abstract void run(CommandSender sender, String[] args, String label, Command command);
    public abstract List<String> tabList(CommandSender sender, String[] args, String label, Command command);

    protected void printUsage(CommandSender sender) {
        sender.sendMessage(usage);
    }

    protected Player isPlayer(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return null;
        }

        return player;
    }
}
