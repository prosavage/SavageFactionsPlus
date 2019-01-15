package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.ChatColor;

public class CmdSetStrikes extends FCommand {


   public CmdSetStrikes() {
      super();
      this.aliases.add("setstrikes");
      this.aliases.add("setstrike");


      this.errorOnToManyArgs = false;
      //this.optionalArgs

      this.permission = Permission.SETSTRIKES.node;

      this.disableOnLock = true;

      senderMustBePlayer = false;
      senderMustBeMember = false;
      senderMustBeModerator = false;
      senderMustBeColeader = false;
      senderMustBeAdmin = false;
   }


   @Override
   public void perform() {

      if (args.size() == 0) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAvailable actions are set, take, or give"));
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cex: /f setstrikes set <faction> <amount>"));
         return;
      }

      if (args.size() == 3) {


         Faction faction = Factions.getInstance().getByTag(args.get(1));
         boolean success = false;
         if (faction != null) {
            if (args.get(0).equalsIgnoreCase("set")) {
               faction.setStrikes(argAsInt(2));
               success = true;
            } else if (args.get(0).equalsIgnoreCase("give")) {
               faction.setStrikes(faction.getStrikes() + argAsInt(2));
               success = true;
            } else if (args.get(0).equalsIgnoreCase("take")) {
               faction.setStrikes(faction.getStrikes() - argAsInt(2));
               success = true;
            }
            if (success) {
               fme.msg(TL.COMMAND_SETSTRIKES_SUCCESS.toString()
                       .replace("{faction}", faction.getTag())
                       .replace("{strikes}", faction.getStrikes() + ""));
            }
         } else {
            fme.msg(TL.COMMAND_SETSTRIKES_FAILURE.toString().replace("{faction}", faction.getTag()));
         }
      }
   }


   @Override
   public TL getUsageTranslation() {
      return TL.COMMAND_SHOW_COMMANDDESCRIPTION;
   }


}
