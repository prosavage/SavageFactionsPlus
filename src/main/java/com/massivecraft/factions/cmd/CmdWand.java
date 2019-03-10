package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.Placeholder;
import com.massivecraft.factions.zcore.util.TL;
import com.massivecraft.factions.zcore.wands.impl.CondenseWand;
import com.massivecraft.factions.zcore.wands.impl.LightningWand;
import com.massivecraft.factions.zcore.wands.impl.SandWand;
import com.massivecraft.factions.zcore.wands.impl.SellWand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdWand extends FCommand {

   public CmdWand() {
      super();

      this.aliases.add("wand");

      this.requiredArgs.add("player");
      this.requiredArgs.add("lightning/sand/sell/craft");

      this.optionalArgs.put("Tier", " 1,2,3");

      this.permission = Permission.GIVEWAND.node;
      this.disableOnLock = true;

      senderMustBePlayer = false;
      senderMustBeMember = false;
      senderMustBeModerator = false;
      senderMustBeColeader = false;
      senderMustBeAdmin = false;
   }


   @Override
   public void perform() {
      Player player = argAsPlayer(0);
      String rodType = args.get(1);
      if (!rodType.equalsIgnoreCase("lightning")
              && !rodType.equalsIgnoreCase("sand")
              && !rodType.equalsIgnoreCase("sell")
              && !rodType.equalsIgnoreCase("craft")) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "The tooltype needs to be lightning/sand/sell/craft"));
         return;
      }
      int tier = 1;
      if (rodType.equalsIgnoreCase("sell") && args.get(2) == null) {
         fme.msg(TL.COMMAND_WAND_SELLWANDTIER_REQUIRED.toString().replace("{player}", player.getName()));
      } else if (rodType.equalsIgnoreCase("sell") && args.get(2) != null) {
         tier = argAsInt(2);
      }
      ItemStack wand = new ItemStack(Material.STICK);
      switch (rodType.toLowerCase()) {
         case "lightning":
	         wand = LightningWand.buildItem(Conf.lightningWandUses);
            break;
         case "sand":
            wand = SandWand.buildItem(Conf.sandWandUses);
            break;
         case "craft":
         case "condense":
            wand = CondenseWand.buildItem(Conf.craftWandUses);
            break;
         case "sell":
            wand = SellWand.buildItem(Conf.sellWandUses, tier);
      }
      FPlayer target = FPlayers.getInstance().getByPlayer(player);
      Placeholder itemTypePlaceholder = new Placeholder("{type}", rodType);
      Placeholder playerPlaceholder = new Placeholder("{player}", player.getName());
      if (player.getInventory().firstEmpty() == -1) {
         player.getWorld().dropItem(player.getLocation(), wand);
         target.msg(TL.COMMAND_WAND_NOSPACE);
      } else {
         player.getInventory().addItem(wand);
      }
      target.msg(TL.COMMAND_WAND_GOTTEN.toString()
              .replace(itemTypePlaceholder.getKey(), itemTypePlaceholder.getValue())
              .replace(playerPlaceholder.getKey(), playerPlaceholder.getValue()));
      fme.msg(TL.COMMAND_WAND_SUCCESS.toString()
              .replace(itemTypePlaceholder.getKey(), itemTypePlaceholder.getValue())
              .replace(playerPlaceholder.getKey(), playerPlaceholder.getValue()));


   }

   @Override
   public TL getUsageTranslation() {
      return TL.COMMAND_WAND_DESCRIPTION;
   }


}
