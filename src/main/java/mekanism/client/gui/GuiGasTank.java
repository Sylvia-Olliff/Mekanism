package mekanism.client.gui;

import java.util.ArrayList;
import java.util.List;
import mekanism.api.chemical.gas.GasStack;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.button.GuiGasMode;
import mekanism.client.gui.element.tab.GuiRedstoneControlTab;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiTransporterConfigTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tier.ChemicalTankTier;
import mekanism.common.tile.TileEntityGasTank;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

//TODO - V10: Rename to GuiChemicalTank
public class GuiGasTank extends GuiMekanismTile<TileEntityGasTank, MekanismTileContainer<TileEntityGasTank>> {

    public GuiGasTank(MekanismTileContainer<TileEntityGasTank> container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    public void init() {
        super.init();
        addButton(new GuiChemicalBar<>(this, GuiChemicalBar.getProvider(tile.gasTank, tile.getGasTanks(null)), 42, 16, 116, 10, true));
        addButton(new GuiInnerScreen(this, 42, 37, 118, 28, () -> {
            List<ITextComponent> list = new ArrayList<>();
            ITextComponent component;
            GasStack gasStack = tile.gasTank.getStack();
            if (gasStack.isEmpty()) {
                component = MekanismLang.GAS.translate(MekanismLang.NONE);
            } else {
                component = MekanismLang.GAS.translate(gasStack);
            }
            list.add(component);
            if (!tile.gasTank.isEmpty() && tile.tier == ChemicalTankTier.CREATIVE) {
                component = MekanismLang.INFINITE.translate();
            } else {
                component = MekanismLang.GENERIC_FRACTION.translate(formatInt(tile.gasTank.getStored()),
                      tile.tier == ChemicalTankTier.CREATIVE ? MekanismLang.INFINITE : formatInt(tile.tier.getStorage()));
            }
            list.add(component);
            return list;
        }));
        addButton(new GuiRedstoneControlTab(this, tile));
        addButton(new GuiSecurityTab<>(this, tile));
        addButton(new GuiSideConfigurationTab(this, tile));
        addButton(new GuiTransporterConfigTab(this, tile));
        addButton(new GuiGasMode(this, getGuiLeft() + 159, getGuiTop() + 72, true, () -> tile.dumping, tile.getPos(), 0));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        renderTitleText();
        drawString(MekanismLang.INVENTORY.translate(), 8, getYSize() - 96 + 2, titleTextColor());
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}