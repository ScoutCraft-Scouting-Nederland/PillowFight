package nl.scoutcraft.pillowfight.game.voting;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import nl.scoutcraft.eagle.api.gui.inventory.base.AbstractInventoryMenu;
import nl.scoutcraft.eagle.api.gui.inventory.base.Button;
import nl.scoutcraft.eagle.api.utils.ItemBuilder;
import nl.scoutcraft.pillowfight.game.TeamsMode;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class VotingMenu extends AbstractInventoryMenu {

    private final ArenaVoteMenu arena;
    private final TeamsVoteMenu mode;
    private final GamemodeVoteMenu powerup;

    public VotingMenu(List<String> arenaNames) {
        super.setTitle(ChatColor.BLUE + "" + ChatColor.BOLD + "Voting");

        this.arena = new ArenaVoteMenu(this, arenaNames);
        this.mode = new TeamsVoteMenu(this);
        this.powerup = new GamemodeVoteMenu(this);
    }

    @Override
    protected List<Button> getButtons() {
        return Lists.newArrayList(
                Button.spacer(IntStream.range(0, 27).toArray()).build(),
                Button.builder().setIndices(11).setItem(new ItemBuilder(Material.MAP).name(Locale.ITEM_ARENA_NAME)).setActions(this.arena::open).build(),
                Button.builder().setIndices(12).setItem(new ItemBuilder(Material.ENDER_EYE).name(Locale.ITEM_GAMEMODE_NAME)).setActions(this.powerup::open).build(),
                Button.builder().setIndices(13).setItem(new ItemBuilder(Material.CROSSBOW).name(Locale.ITEM_TEAMS_NAME)).setActions(this.mode::open).build(),
                Button.builder().setIndices(15).setItem(new ItemBuilder(Material.BARRIER).name(ChatColor.RED + "" + ChatColor.BOLD + "Close Menu")).setActions(Player::closeInventory).build()
        );
    }

    public void closeAll() {
        super.close();
        this.arena.close();
        this.mode.close();
        this.powerup.close();
    }

    public void removeVotes(UUID uuid) {
        this.arena.removeVote(uuid);
        this.mode.removeVote(uuid);
        this.powerup.removeVote(uuid);
    }

    public String chooseArena() {
        return this.arena.chooseArena();
    }

    public TeamsMode chooseMode() {
        return this.mode.chooseMode();
    }

    public boolean choosePowerups() {
        return this.powerup.choosePowerups();
    }
}
