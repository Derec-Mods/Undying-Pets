package io.github.derexxd.undyingPets.listeners;

import io.github.derexxd.undyingPets.UndyingPets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class TotemListener implements Listener {

    private final NamespacedKey key;

    public TotemListener(UndyingPets plugin) {
        this.key = new NamespacedKey(plugin, "hasTotem");
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand == null || hand.getType() != Material.TOTEM_OF_UNDYING) {
            return;
        }

        if (!(clicked instanceof Tameable pet)) {
            return;
        }

        if (pet.getOwner() == null || !pet.getOwner().equals(player)) {
            return;
        }

        // use pdc so it cant be overwritten
        pet.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        // consume one totem
        int amount = hand.getAmount();
        if (amount <= 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            hand.setAmount(amount - 1);
            player.getInventory().setItemInMainHand(hand);
        }

        player.sendActionBar("Equipped Totem onto pet");
        event.setCancelled(true);
    }
}
