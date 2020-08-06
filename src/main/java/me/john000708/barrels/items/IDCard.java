package me.john000708.barrels.items;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.john000708.barrels.block.Barrel;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;

public class IDCard extends SimpleSlimefunItem<ItemUseHandler> {

	public IDCard(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	public ItemUseHandler getItemHandler() {
		return e -> {
			if (e.getClickedBlock().isPresent()) {
				Player p = e.getPlayer();
				Block clickedBlock = e.getClickedBlock().get();
				ItemStack item = e.getItem();
				
	            ItemMeta meta = item.getItemMeta();
	            if (!meta.hasLore()) return;
	            
	            List<String> lore = item.getItemMeta().getLore();
	            if (lore.size() != 2) return;

	            if (lore.get(0).equals("")) {
	                lore.set(0, ChatColor.translateAlternateColorCodes('&', "&0" + p.getUniqueId().toString()));
	                lore.set(1, ChatColor.translateAlternateColorCodes('&', "&f綁定至: " + p.getName()));
	                meta.setLore(lore);
	                item.setItemMeta(meta);
	                p.sendMessage(ChatColor.GREEN + "ID卡綁定.");
	            } 
	            else if (e.getSlimefunBlock().isPresent()) {
	            	SlimefunItem barrel = e.getSlimefunBlock().get();
	            	if (barrel instanceof Barrel && BlockStorage.getLocationInfo(clickedBlock.getLocation(), "whitelist") != null && BlockStorage.getLocationInfo(clickedBlock.getLocation(), "owner").equals(p.getUniqueId().toString())) {
		                String whitelistedPlayers = BlockStorage.getLocationInfo(clickedBlock.getLocation(), "whitelist");
		                
		                if (!whitelistedPlayers.contains(ChatColor.stripColor(lore.get(0)))) {
		                    BlockStorage.addBlockInfo(clickedBlock, "whitelist", whitelistedPlayers + ChatColor.stripColor(lore.get(0)) + ";");
		                    
		                    ItemUtils.consumeItem(item, false);
		                    p.sendMessage(ChatColor.GREEN + "玩家成功加入白名單!");
		                } 
		                else {
		                    p.sendMessage(ChatColor.RED + "此玩家已被加入白名單.");
		                }
		            }
	            }
			}
		};
	}

}
