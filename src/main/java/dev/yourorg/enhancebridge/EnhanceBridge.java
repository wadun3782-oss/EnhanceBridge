package dev.yourorg.enhancebridge;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.stats.StatType;

public final class EnhanceBridge extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("EnhanceBridge 활성화 완료 ✅");
        getCommand("enhancebridge").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("EnhanceBridge 비활성화 ❌");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용할 수 있는 명령어입니다.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 3) {
            player.sendMessage("§c사용법: /enhancebridge enhance <스탯> <값>");
            return true;
        }

        if (args[0].equalsIgnoreCase("enhance")) {
            try {
                StatType stat = StatType.valueOf(args[1].toUpperCase());
                double amount = Double.parseDouble(args[2]);
                PlayerData data = PlayerData.get(player);

                double current = data.getStats().getStat(stat);
                data.getStats().setStat(stat, current + amount);

                player.sendMessage("§a" + stat.name() + " 스탯이 " + amount + "만큼 증가했습니다!");
            } catch (Exception e) {
                player.sendMessage("§c잘못된 스탯 이름이거나 값입니다.");
            }
        }
        return true;
    }
}

