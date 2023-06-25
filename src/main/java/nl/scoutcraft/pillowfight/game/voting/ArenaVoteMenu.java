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

public class ArenaVoteMenu extends AbstractInventoryMenu {

    private final VotingMenu votingMenu;
    private final List<String> arenaNames;
    private final Map<UUID, String> votes;

    public ArenaVoteMenu(VotingMenu votingMenu, List<String> arenaNames) {
        super.setTitle(ChatColor.BLUE + "" + ChatColor.BOLD + "Arena");

        this.votingMenu = votingMenu;
        this.votes = new HashMap<>();
        this.arenaNames = arenaNames;
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.spacer(IntStream.range(0, 27).toArray()).build());

        for (int i = 0; i < this.arenaNames.size(); i++) {
            String name = this.arenaNames.get(i);
            int votes = this.getVotes(name);
            buttons.add(Button.builder()
                    .setItem(new ItemBuilder(Material.FILLED_MAP, Math.max(1, votes))
                            .name(ChatColor.RED + name)
                            .hideAttributes(true)
                            .lore(ChatColor.GOLD + "Votes: " + votes))
                    .setIndices(i + 11)
                    .setActions(p -> {
                        if (name.equals(this.votes.get(p.getUniqueId()))) return;

                        this.votes.put(p.getUniqueId(), name);
                        this.update();
                        Locale.VOTE_ARENA.send(p, new Placeholder("%arena%", name));
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

    public String chooseArena() {
        Collections.shuffle(this.arenaNames);

        Random r = new Random();
        return this.arenaNames.stream().reduce((a1, a2) -> {
            int a1Votes = this.getVotes(a1);
            int a2Votes = this.getVotes(a2);
            return a1Votes > a2Votes ? a1 : a2Votes > a1Votes ? a2 : r.nextBoolean() ? a1 : a2;
        }).orElse(this.arenaNames.get(0));
    }

    private int getVotes(String name) {
        return (int) this.votes.values().stream().filter(name::equals).count();
    }
}
