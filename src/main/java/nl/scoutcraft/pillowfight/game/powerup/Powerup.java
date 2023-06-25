package nl.scoutcraft.pillowfight.game.powerup;

import nl.scoutcraft.eagle.api.Eagle;
import nl.scoutcraft.eagle.api.gui.inventory.hotbar.HotbarButton;
import nl.scoutcraft.eagle.api.locale.IMessage;
import nl.scoutcraft.eagle.api.utils.ItemBuilder;
import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.data.Keys;
import nl.scoutcraft.pillowfight.game.runnables.countdown.PowerupCountdown;
import nl.scoutcraft.pillowfight.game.wrapper.PillowPlayer;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Predicate;

public abstract class Powerup {

    private final Material type;
    private final IMessage<String> name;
    private final IMessage<String> lore;
    private final IMessage<String> countdown;
    private final IMessage<String> countdownEnd;
    private final byte id;
    private final boolean singleUse;
    private final int cooldown;
    private final int duration;
    private final HotbarButton button;

    protected Powerup(Material type, IMessage<String> name, IMessage<String> lore, IMessage<String> countdown, IMessage<String> countdownEnd, byte id, boolean singleUse, int cooldown, int duration) {
        this.type = type;
        this.name = name;
        this.lore = lore;
        this.countdown = countdown;
        this.countdownEnd = countdownEnd;
        this.id = id;
        this.singleUse = singleUse;
        this.cooldown = cooldown;
        this.duration = duration;
        this.button = create(this);

        Eagle.getInventoryMenuManager().register(this.button);
    }

    public boolean isActive(Player player) {
        Byte id = player.getPersistentDataContainer().get(Keys.POWERUP_ID, PersistentDataType.BYTE);
        return id != null && this.id == id;
    }

    /**
     * Will activate the powerup, if all conditions are met.
     *
     * @param player The {@link Player}.
     * @return Whether the powerup was successfully activated.
     */
    public boolean use(Player player) {
        return this.use(player, this.duration, false);
    }

    /**
     * Will activate the powerup, if all conditions are met.
     *
     * @param player The {@link Player}.
     * @param seconds The amount of seconds the powerup will last.
     * @param force Whether to force the powerup to activate.
     * @return Whether the powerup was successfully activated.
     */
    public boolean use(Player player, int seconds, boolean force) {
        if (!force && this.singleUse && isAnyActive(player)) {
            Locale.POWERUP_INVALID.send(player);
            return false;
        }

        if (this.singleUse) {
            this.start(player);
        } else {
            PillowFight.getInstance().getGameManager().addTask(player, new PowerupCountdown(PillowFight.getInstance(), this, player, this.countdown, this.countdownEnd, seconds).start());
        }

        if (!force)
            PillowPlayer.get(player).addPowerupUse();

        return true;
    }

    /**
     * Will activate the powerup, regardless of other parameters.
     * Should only be used by the {@link PowerupCountdown}.
     *
     * @param player The {@link Player}.
     */
    public abstract void start(Player player);

    /**
     * Will stop the powerup, regardless of other parameters.
     * Should only be used by the {@link PowerupCountdown}.
     *
     * @param player The {@link Player}.
     */
    public abstract void stop(Player player);

    public Material getType() {
        return this.type;
    }

    public IMessage<String> getName() {
        return this.name;
    }

    public byte getId() {
        return this.id;
    }

    public HotbarButton getButton() {
        return this.button;
    }

    public static boolean isAnyActive(Player player) {
        return player.getPersistentDataContainer().has(Keys.POWERUP_ID, PersistentDataType.BYTE);
    }

    private static HotbarButton create(final Powerup powerup) {
        return HotbarButton.builder()
                .setItem(new ItemBuilder(powerup.type)
                        .name(powerup.name)
                        .lore(powerup.lore)
                        .data(Keys.POWERUP_ID, PersistentDataType.BYTE, powerup.id))
                .setAction((Predicate<Player>) powerup::use)
                .setCooldown(powerup.cooldown)
                .setRemoveOnUse(true)
                .setMoveable(true)
                .build(true);
    }
}
