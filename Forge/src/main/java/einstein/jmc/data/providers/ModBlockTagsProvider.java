package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public static final TagKey<Block> F_CAKES = BlockTags.create(new ResourceLocation("forge", "cakes"));
    public static final TagKey<Block> F_CANDLE_CAKES = BlockTags.create(new ResourceLocation("forge", "candle_cakes"));

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CAKE_OVEN.get(), ModBlocks.OBSIDIAN_CAKE.get());

        Map<Supplier<BaseCakeBlock>, CakeBuilder> sortedCakes = Util.createValueSortedMap(CakeBuilder.BUILDER_BY_CAKE, Comparator.comparing(CakeBuilder::getCakeName));

        sortedCakes.forEach((cake, cakeBuilder) -> {
            tag(ModBlockTags.CAKES).add(cake.get());
            Map<Block, Supplier<BaseCandleCakeBlock>> sortedCandleCakes = Util.createKeySortedMap(cakeBuilder.getCandleCakeByCandle(), Comparator.comparing(o -> o.getName().toString()));
            sortedCandleCakes.forEach((candle, candleCake) -> {
                tag(ModBlockTags.CANDLE_CAKES).add(candleCake.get());
            });
        });

        tag(ModBlockTags.CAKES).add(ModBlocks.CUPCAKE.get());
        tag(ModBlockTags.C_CAKES).addTag(ModBlockTags.CAKES);
        tag(ModBlockTags.C_CANDLE_CAKES).addTag(ModBlockTags.CANDLE_CAKES);
        tag(F_CAKES).addTag(ModBlockTags.CAKES);
        tag(F_CANDLE_CAKES).addTag(ModBlockTags.CANDLE_CAKES);
        tag(ModBlockTags.CAKE_SPATULA_USABLE).add(Blocks.CAKE).addTag(F_CAKES).addTag(F_CANDLE_CAKES);
        Util.getVanillaCandleCakes().forEach(cake -> tag(ModBlockTags.CAKE_SPATULA_USABLE).add(cake));
    }
}
