package com.rabbitminers.createtools.tools.tooltypes.base;

import com.rabbitminers.createtools.render.tools.render.DrillToolRender;
import com.rabbitminers.createtools.tools.ToolBase;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class DrillTool extends ToolBase {
    private final TagKey<Block> blocks;
    protected final float speed;


    public DrillTool(Properties properties, TagKey<Block> blocks, float speed) {
        super(properties);
        this.blocks = blocks;
        this.speed = speed;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(this.blocks) ? this.speed : 1.0F;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new DrillToolRender()));
    }
}
