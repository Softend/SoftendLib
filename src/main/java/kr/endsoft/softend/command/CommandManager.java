package kr.endsoft.softend.command;

import kr.endsoft.softend.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements TabExecutor {

    private static CommandManager instance;
    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }

        return instance;
    }

    private final Map<Command, SoftendCommand> executorMap = new HashMap<>();
    public void registerCommand(String name, SoftendCommand command) {
        PluginCommand bukkitCommand = command.getPlugin().getCommand(name);
        if (bukkitCommand == null) {
            return;
        }

        bukkitCommand.setExecutor(this);
        executorMap.put(bukkitCommand, command);
    }

    public SoftendCommand getExecutor(String name) {
        Command bukkitCommand = Main.getInstance().getServer().getPluginCommand(name);
        if (bukkitCommand == null) {
            return null;
        }
        return getExecutor(bukkitCommand);
    }

    public SoftendCommand getExecutor(Command command) {
        return executorMap.getOrDefault(command, null);
    }

    public boolean isRegistered(String name) {
        return isRegistered(Main.getInstance().getServer().getPluginCommand(name));
    }

    public boolean isRegistered(Command command) {
        return executorMap.containsKey(command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!isRegistered(command)) {
            return true;
        }

        getExecutor(command).run(commandSender, strings, s, command);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!isRegistered(command)) {
            return List.of();
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], getExecutor(command).tabList(commandSender, args, s, command), new ArrayList<>());
    }
}
