package nl.scoutcraft.pillowfight.data;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import nl.scoutcraft.eagle.api.gui.inventory.hotbar.HotbarButton;
import nl.scoutcraft.eagle.api.utils.ItemBuilder;
import nl.scoutcraft.eagle.api.utils.TextUtils;
import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.lang.Locale;
import nl.scoutcraft.pillowfight.utils.BungeeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

import java.util.UUID;
import java.util.stream.Collectors;

public class Item {

    public static final HotbarButton VOTES;
    public static final HotbarButton RULEBOOK;
    public static final HotbarButton JOIN;
    public static final HotbarButton SPECTATE;
    public static final HotbarButton GLOBE;

    private static final HotbarButton PILLOW;

    public static void givePillow(Player player, Material color) {
        PILLOW.getItem().type(color);
        PILLOW.apply(player);
    }

    public static void giveArmor(Player player, Color color) {
        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).armorColor(color).hideDye(true).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).armorColor(color).hideDye(true).build());
        player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).armorColor(color).hideDye(true).build());
        player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).armorColor(color).hideDye(true).build());
    }

    private Item() {}

    static {
        PlayerProfile globeProfile = Bukkit.createProfile(UUID.randomUUID(), "");
        globeProfile.getProperties().add(new ProfileProperty("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThkYWExZTNlZDk0ZmYzZTMzZTFkNGM2ZTQzZjAyNGM0N2Q3OGE1N2JhNGQzOGU3NWU3YzkyNjQxMDYifX19"));

        VOTES = HotbarButton.builder().setSlot(0).setItem(new ItemBuilder(Material.NETHER_STAR).name(Locale.BUTTON_VOTES_NAME).lore(Locale.BUTTON_VOTES_LORE)).setAction(PillowFight.getInstance().getGameManager().getVotingMenu()::open).build(true);
        RULEBOOK = HotbarButton.builder()
                .setSlot(2)
                .setItem(new ItemBuilder(Material.WRITTEN_BOOK)
                        .bookTitle(Locale.BUTTON_RULEBOOK_TITLE.get((java.util.Locale) null, true))
                        .bookAuthor(Locale.BUTTON_RULEBOOK_AUTHOR.get((java.util.Locale) null, true))
                        .bookGeneration(BookMeta.Generation.TATTERED)
                        .bookPages(PillowFight.getInstance().getConfig().getStringList("RULEBOOK").stream().map(TextUtils::colorize).collect(Collectors.toList()))
                        .name(Locale.BUTTON_RULEBOOK_NAME))
                .build(true);
        JOIN = HotbarButton.builder()
                .setSlot(6)
                .setItem(new ItemBuilder(Material.WHITE_WOOL).name(Locale.BUTTON_JOIN_NAME))
                .setAction(p -> {
                    PillowFight.getInstance().getGameManager().join(p);
                    p.setCooldown(Material.ARMOR_STAND, 60);
                })
                .setCooldown(60)
                .build(true);
        SPECTATE = HotbarButton.builder()
                .setSlot(6)
                .setItem(new ItemBuilder(Material.ARMOR_STAND).name(Locale.BUTTON_SPECTATE_NAME))
                .setAction(p -> {
                    PillowFight.getInstance().getGameManager().quit(p);
                    PillowFight.getInstance().getGameManager().spectate(p);
                    p.setCooldown(Material.WHITE_WOOL, 60);
                })
                .setCooldown(60)
                .build(true);
        GLOBE = HotbarButton.builder().setSlot(8).setItem(new ItemBuilder(Material.PLAYER_HEAD).skull(globeProfile).name(Locale.BUTTON_GLOBE_NAME).lore(Locale.BUTTON_GLOBE_LORE)).setAction(BungeeUtil::sendToLobby).build(true);
        PILLOW = HotbarButton.builder().setSlot(0).setItem(new ItemBuilder(Material.WHITE_WOOL).name(Locale.BUTTON_PILLOW_NAME)).build(true);
    }
}
