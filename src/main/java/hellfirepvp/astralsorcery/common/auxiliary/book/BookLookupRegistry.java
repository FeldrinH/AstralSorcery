package hellfirepvp.astralsorcery.common.auxiliary.book;

import hellfirepvp.astralsorcery.common.data.research.*;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.item.ItemComparator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BookLookupRegistry
 * Created by HellFirePvP
 * Date: 10.10.2019 / 17:51
 */
public class BookLookupRegistry {

    private static Map<ItemStack, BookLookupInfo> lookupMap = new HashMap<>();

    private BookLookupRegistry() {}

    @Nullable
    public static BookLookupInfo findPage(PlayerEntity player, LogicalSide side, ItemStack search) {
        for (ItemStack compare : lookupMap.keySet()) {
            if (ItemComparator.compare(compare, search, ItemComparator.Clause.Sets.ITEMSTACK_CRAFTING)) {
                BookLookupInfo info = lookupMap.get(compare);
                PlayerProgress prog = ResearchHelper.getProgress(player, side);
                if (info.canSee(prog)) {
                    return info;
                }
            }
        }
        return null;
    }

    public static void registerItemLookup(ItemStack stack, ResearchNode parentNode, int nodePage, ResearchProgression neededProgression) {
        lookupMap.put(stack, new BookLookupInfo(parentNode, nodePage, neededProgression));
    }

}