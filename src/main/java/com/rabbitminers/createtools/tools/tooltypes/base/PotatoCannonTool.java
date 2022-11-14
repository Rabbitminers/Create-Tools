package com.rabbitminers.createtools.tools.tooltypes.base;

import com.rabbitminers.createtools.tools.ToolBase;
import com.simibubi.create.AllEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class PotatoCannonTool extends ToolBase {
    public PotatoCannonTool(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return false;
    }

    public static final Predicate<ItemStack> ARROW_ONLY = (p_43017_) -> {
        return p_43017_.is(ItemTags.ARROWS);
    };
    public static final Predicate<ItemStack> ARROW_OR_FIREWORK = ARROW_ONLY.or((p_43015_) -> {
        return p_43015_.is(Items.FIREWORK_ROCKET);
    });


    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.POWER_ARROWS)
            return true;
        if (enchantment == Enchantments.PUNCH_ARROWS)
            return true;
        if (enchantment == Enchantments.FLAMING_ARROWS)
            return true;
        if (enchantment == Enchantments.MOB_LOOTING)
            return true;
        if (enchantment == AllEnchantments.POTATO_RECOVERY.get())
            return true;
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
