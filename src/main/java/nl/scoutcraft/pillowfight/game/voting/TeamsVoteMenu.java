package nl.scoutcraft.pillowfight.game.voting;

import net.md_5.bungee.api.ChatColor;
import nl.scoutcraft.eagle.api.gui.inventory.base.AbstractInventoryMenu;
import nl.scoutcraft.eagle.api.gui.inventory.base.Button;
import nl.scoutcraft.eagle.api.locale.Placeholder;
import nl.scoutcraft.eagle.api.utils.ItemBuilder;
import nl.scoutcraft.pillowfight.game.TeamsMode;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Material;

import java.util.*;
import java.util.stream.IntStream;

public class TeamsVoteMenu extends AbstractInventoryMenu {

    private final VotingMenu votingMenu;
    private final TeamsMode[] modes;
    private final Map<UUID, TeamsMode> votes;

    public TeamsVoteMenu(VotingMenu votingMenu) {
        super.setTitle(ChatColor.BLUE + "" + ChatColor.BOLD + "Teams");

        this.votingMenu = votingMenu;
        this.modes = TeamsMode.values();
        this.votes = new HashMap<>();
    }

    @Override
    protected List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.spacer(IntStream.range(0, 27).toArray()).build());

        for (int i = 0; i < this.modes.length; i++) {
            TeamsMode mode = this.modes[i];
            int votes = this.getVotes(mode);
            buttons.add(Button.builder()
                    .setItem(new ItemBuilder(mode.getMaterial(), Math.max(1, votes))
                            .name(ChatColor.RED + mode.getName())
                            .lore(ChatColor.GOLD + "Votes: " + votes))
                    .setIndices(i + 11)
                    .setActions(p -> {
                        if (mode.equals(this.votes.get(p.getUniqueId()))) return;

                        this.votes.put(p.getUniqueId(), mode);
                        this.update();
                        Locale.VOTE_TEAMS.send(p, new Placeholder("%teams%", mode.getName()));
                    })
                    .build());
        }

        buttons.add(Button.builder().setIndices(15).setItem(new ItemBuilder(Material.BOOK).name(ChatColor.RED + "" + ChatColor.BOLD + "Go Back")).setActions(this.votingMenu::open).build());

        return buttons;
    }

    public void removeVote(UUID uuid) {
        this.votes.remove(uuid);
        this.update();
    }

    public TeamsMode chooseMode() {
        Random r = new Random();
        return Arrays.stream(this.modes).reduce((a1, a2) -> {
            int a1Votes = this.getVotes(a1);
            int a2Votes = this.getVotes(a2);
            return a1Votes > a2Votes ? a1 : a2Votes > a1Votes ? a2 : r.nextBoolean() ? a1 : a2;
        }).orElse(this.modes[0]);
    }

    private int getVotes(TeamsMode mode) {
        return (int) this.votes.values().stream().filter(mode::equals).count();
    }
}
