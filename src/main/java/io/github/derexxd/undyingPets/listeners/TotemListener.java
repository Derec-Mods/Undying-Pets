package io.github.derexxd.undyingPets.listeners;

import io.github.derexxd.undyingPets.UndyingPets;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
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
        EquipmentSlot slot = event.getHand();
        if (slot != EquipmentSlot.HAND && slot != EquipmentSlot.OFF_HAND) return;

        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();

        ItemStack item = player.getInventory().getItem(slot);
        if (item == null || item.getType() != Material.TOTEM_OF_UNDYING) return;

        if (!(clicked instanceof Tameable pet)) return;

        if (pet.getOwner() == null || !pet.getOwner().equals(player)) return;

        if (pet.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
            player.sendActionBar(Component.text("Pet already has a Totem equipped"));
            event.setCancelled(true);
            return;
        }

        pet.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        int amount = item.getAmount();
        if (amount <= 1) {
            player.getInventory().setItem(slot, null);
        } else {
            item.setAmount(amount - 1);
            player.getInventory().setItem(slot, item);
        }

        player.sendActionBar(Component.text("Equipped Totem onto pet"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Tameable pet)) return;

        if (!pet.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) return;

        pet.getPersistentDataContainer().remove(key);
        event.setCancelled(false);
    }
}

