package nl.scoutcraft.pillowfight.game.voting;

import net.md_5.bungee.api.ChatColor;
import nl.scoutcraft.eagle.api.gui.inventory.base.AbstractInventoryMenu;
import nl.scoutcraft.eagle.api.gui.inventory.base.Button;
import nl.scoutcraft.eagle.api.locale.Placeholder;
import nl.scoutcraft.eagle.api.utils.ItemBuilder;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Material;

import java.util.*;
import java.util.stream.IntStream;

public class GamemodeVoteMenu extends AbstractInventoryMenu {

    private final VotingMenu votingMenu;
    private final Map<UUID, Boolean> votes;

    public GamemodeVoteMenu(VotingMenu votingMenu) {
        super.setTitle(ChatColor.BLUE + "" + ChatColor.BOLD + "Gamemode");

        this.votingMenu = votingMenu;
        this.votes = new HashMap<>();
    }

    @Override
    protected List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.spacer(IntStream.range(0, 27).toArray()).build());

        int votes = this.getVotes(false);
        buttons.add(Button.builder()
                .setItem(new ItemBuilder(Material.FIREWORK_STAR, Math.max(1, votes)).name(ChatColor.RED + "Classic").lore(ChatColor.GOLD + "Votes: " + votes))
                .setActions(p -> {
                    Boolean current = this.votes.get(p.getUniqueId());
                    if (current != null && !current) return;

                    this.votes.put(p.getUniqueId(), false);
                    this.update();
                    Locale.VOTE_MODE.send(p, new Placeholder("%mode%", "Classic"));
                })
                .setIndices(11)
                .build());

        votes = this.getVotes(true);
        buttons.add(Button.builder()
                .setItem(new ItemBuilder(Material.ENDER_EYE, Math.max(1, votes)).name(ChatColor.RED + "Powerups").lore(ChatColor.GOLD + "Votes: " + votes))
                .setActions(p -> {
                    Boolean current = this.votes.get(p.getUniqueId());
                    if (current != null && current) return;

                    this.votes.put(p.getUniqueId(), true);
                    this.update();
                    Locale.VOTE_MODE.send(p, new Placeholder("%mode%", "Powerups"));
                })
                .setIndices(12)
                .build());

        buttons.add(Button.builder().setIndices(15).setItem(new ItemBuilder(Material.BOOK).name(ChatColor.RED + "" + ChatColor.BOLD + "Go Back")).setActions(this.votingMenu::open).build());

        return buttons;
    }

    public void removeVote(UUID uuid) {
        this.votes.remove(uuid);
        this.update();
    }

    public boolean choosePowerups() {
        return this.getVotes(true) >= this.getVotes(false);
    }

    private int getVotes(boolean value) {
        return (int) this.votes.values().stream().filter(b -> b == value).count();
    }
}
