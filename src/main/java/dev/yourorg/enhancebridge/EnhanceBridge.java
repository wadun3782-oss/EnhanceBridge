package dev.yourorg.enhancebridge;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.MMOItem;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnhanceBridge extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("✅ EnhanceBridge loaded! Skript → MMOItems 연결 활성화");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("enhancebridge")) return false;

        if (args.length < 4 || !args[0].equalsIgnoreCase("enhance")) {
            sender.sendMessage("§c사용법: /enhancebridge enhance <player> <stat> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§c플레이어를 찾을 수 없습니다.");
            return true;
        }

        String statKey = args[2].toUpperCase();
        double amount;
        try {
            amount = Double.parseDouble(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c숫자 형식이 잘못되었습니다.");
            return true;
        }

        ItemStack item = target.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            sender.sendMessage("§c플레이어가 손에 아이템을 들고 있지 않습니다.");
            return true;
        }

        MMOItem mmoItem = MMOItems.getMMOItem(item);
        if (mmoItem == null) {
            sender.sendMessage("§c이 아이템은 MMOItems 아이템이 아닙니다!");
            return true;
        }

        if (!(MMOItems.getStats().get(statKey) instanceof DoubleStat)) {
            sender.sendMessage("§c스탯 키가 잘못되었습니다: " + statKey);
            return true;
        }

        DoubleStat stat = (DoubleStat) MMOItems.getStats().get(statKey);
        double current = mmoItem.hasData(stat) ? mmoItem.getData(stat).toDouble() : 0.0;
        double newValue = current + amount;

        mmoItem.setData(stat, newValue);
        ItemStack newItem = mmoItem.newBuilder().build();

        target.getInventory().setItemInMainHand(newItem);
        sender.sendMessage("§a" + target.getName() + " 의 아이템 " + statKey + " 스탯을 +" + amount + " 강화했습니다!");
        target.sendMessage("§6[강화] §a" + statKey + " +" + amount + " 적용됨!");

        return true;
    }
}
