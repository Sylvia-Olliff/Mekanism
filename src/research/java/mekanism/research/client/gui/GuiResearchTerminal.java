package mekanism.research.client.gui;

import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.scroll.GuiTextScrollList;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.research.common.ResearchLang;
import mekanism.research.common.tile.TileEntityResearchTerminal;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiResearchTerminal extends GuiMekanismTile<TileEntityResearchTerminal, MekanismTileContainer<TileEntityResearchTerminal>> {

    private GuiTextScrollList unlockedResearch;

    public GuiResearchTerminal(MekanismTileContainer<TileEntityResearchTerminal> container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        ySize += 42;
        dynamicSlots = true;
    }

    @Override
    public void init() {
        super.init();
        addButton(unlockedResearch = new GuiTextScrollList(this, 13, 13, 138, 42));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        renderTitleText(4);
        drawString(ResearchLang.RESEARCH.translate(tile.getPlayerResearch(minecraft.player)), 13, 58, titleTextColor());
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
