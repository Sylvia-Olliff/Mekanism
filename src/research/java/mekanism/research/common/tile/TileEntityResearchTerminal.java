package mekanism.research.common.tile;

import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.resolver.basic.BasicCapabilityResolver;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.capabilities.ResearchCapabilityProvider;
import mekanism.research.common.capabilities.ResearchViewCapabilityProvider;
import mekanism.research.common.capabilities.interfaces.IResearchView;
import mekanism.research.common.registries.ResearchBlocks;
import net.minecraft.entity.player.PlayerEntity;

import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TileEntityResearchTerminal extends TileEntityMekanism implements IResearchView {

    public TreeMap<UUID, Long> playerResearchCache = new TreeMap<>();

    public TileEntityResearchTerminal() { this(ResearchBlocks.RESEARCH_TERMINAL); }

    public TileEntityResearchTerminal(IBlockProvider blockProvider) {
        super(blockProvider);
        addCapabilityResolver(BasicCapabilityResolver.constant(ResearchViewCapabilityProvider.RESEARCH_VIEW_CAPABILITY, this));
    }


    @Override
    public long getPlayerResearch(PlayerEntity player) {
        if (player != null) {
            AtomicLong points = new AtomicLong(0L);
            player.getCapability(ResearchCapabilityProvider.RESEARCH_PLAYER_CAPABILITY).ifPresent(c -> {
                points.set(c.getResearchPoints());
            });
            return points.get();
        }
        return 0L;
    }
}
