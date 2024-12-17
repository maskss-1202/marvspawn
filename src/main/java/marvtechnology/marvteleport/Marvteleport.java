package marvtechnology.marvteleport;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Marvteleport extends JavaPlugin implements CommandExecutor, TabCompleter, Listener {

    private FileConfiguration messages;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        loadMessages();


        getServer().getPluginManager().registerEvents(this, this);


        getCommand("marvteleport").setExecutor(this);
        getCommand("marvteleport").setTabCompleter(this);

        getLogger().info("Marvteleport プラグインが有効になりました！");
    }

    @Override
    public void onDisable() {
        getLogger().info("Marvteleport プラグインが無効になりました！");
    }

    private void loadMessages() {
        File messageFile = new File(getDataFolder(), "message.yml");
        if (!messageFile.exists()) {
            saveResource("message.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messageFile);
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        int threshold = getConfig().getInt("teleport-threshold", 10);

        if (loc.getY() <= threshold) {
            Location teleportLocation = getCustomTeleportLocation();

            if (teleportLocation.getWorld() != null) {

                player.teleport(teleportLocation);


                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setVelocity(player.getVelocity().zero());
                    }
                }.runTaskLater(this, 2L); // 2L = 0.1秒（1tick = 1/20秒）

                player.sendMessage(getMessage("teleport-message"));
            } else {
                player.sendMessage("§cエラー: テレポート地点が無効です。");
            }
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location spawnLocation = getCustomTeleportLocation();

        if (spawnLocation.getWorld() != null) {
            player.teleport(spawnLocation);
            player.sendMessage(getMessage("join-teleport-message"));
        } else {
            getLogger().warning("カスタムスポーン地点のワールドが見つかりません！");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみ実行可能です。");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§a使用可能なサブコマンド: setspawn, getspawn, reload");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "setspawn":
                Location loc = player.getLocation();
                getConfig().set("teleport-location.world", loc.getWorld().getName());
                getConfig().set("teleport-location.x", loc.getX());
                getConfig().set("teleport-location.y", loc.getY());
                getConfig().set("teleport-location.z", loc.getZ());
                getConfig().set("teleport-location.yaw", loc.getYaw());
                getConfig().set("teleport-location.pitch", loc.getPitch());
                saveConfig();
                player.sendMessage("§aカスタム座標が設定されました！");
                break;

            case "getspawn":
                Location customLocation = getCustomTeleportLocation();
                player.sendMessage("§aカスタム座標: " + customLocation);
                break;

            case "reload":
                reloadConfig();
                loadMessages();
                player.sendMessage(getMessage("reload-message"));
                break;

            default:
                player.sendMessage("§c無効なサブコマンドです。");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("setspawn");
            completions.add("getspawn");
            completions.add("reload");
        }
        return completions;
    }

    private Location getCustomTeleportLocation() {
        String worldName = getConfig().getString("teleport-location.world");
        double x = getConfig().getDouble("teleport-location.x");
        double y = getConfig().getDouble("teleport-location.y");
        double z = getConfig().getDouble("teleport-location.z");
        float yaw = (float) getConfig().getDouble("teleport-location.yaw");
        float pitch = (float) getConfig().getDouble("teleport-location.pitch");
        return new Location(getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    private String getMessage(String path) {
        return messages.getString(path, "§cメッセージが見つかりません！");
    }
}
