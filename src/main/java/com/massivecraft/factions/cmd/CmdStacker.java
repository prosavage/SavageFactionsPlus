package com.massivecraft.factions.cmd;

import com.massivecraft.factions.PointRaiding;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.PointStacker;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.entity.Player;

public class CmdStacker extends FCommand {

    public CmdStacker() {
        super();
        this.aliases.add("stacker");
        this.aliases.add("stackers");

        this.optionalArgs.put("Player", "Name");

        permission = Permission.STACKER.node;

        this.senderMustBePlayer = true;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
    }

    @Override
    public void perform() {
        if (args.size() == 1) {
            if (!sender.hasPermission(Permission.STACKER_ADMIN.node)) {
                msg(TL.GENERIC_NOPERMISSION);
                return;
            }
            Player player = argAsPlayer(0);
            player.getInventory().addItem(PointStacker.buildItemStack());
            return;
        }
        if (!fme.hasMoney(PointRaiding.pointStackerCost)) {
            msg(TL.COMMAND_STACKER_NOTENOUGHMONEY);
            return;
        }
	    fme.takeMoney(PointRaiding.pointStackerCost);
	    fme.getPlayer().getInventory().addItem(PointStacker.buildItemStack());
    }

    public TL getUsageTranslation() {
        return TL.COMMAND_STACKER_DESCRIPTION;
    }


}
