package mekanism.client.gui.machine;

import java.util.Arrays;
import java.util.Collections;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiHeatInfo;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.progress.GuiFlame;
import mekanism.client.gui.element.progress.IProgressInfoHandler;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.machine.TileEntityFuelwoodHeater;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiFuelwoodHeater extends GuiMekanismTile<TileEntityFuelwoodHeater, MekanismTileContainer<TileEntityFuelwoodHeater>> {

    public GuiFuelwoodHeater(MekanismTileContainer<TileEntityFuelwoodHeater> container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    public void init() {
        super.init();
        addButton(new GuiInnerScreen(this, 48, 23, 80, 28, () -> Arrays.asList(
            MekanismLang.TEMPERATURE.translate(MekanismUtils.getTemperatureDisplay(tile.getTotalTemperature(), TemperatureUnit.KELVIN, true)),
            MekanismLang.FUEL.translate(tile.burnTime)
        )).defaultFormat());
        addButton(new GuiFlame(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tile.burnTime / (double) tile.maxBurnTime;
            }

            @Override
            public boolean isActive() {
                return tile.burnTime > 0;
            }
        }, this, 144, 31));
        addButton(new GuiSecurityTab<>(this, tile));
        addButton(new GuiHeatInfo(() -> {
            ITextComponent environment = MekanismUtils.getTemperatureDisplay(tile.lastEnvironmentLoss, TemperatureUnit.KELVIN, false);
            return Collections.singletonList(MekanismLang.DISSIPATED_RATE.translate(environment));
        }, this));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        renderTitleText();
        drawString(MekanismLang.INVENTORY.translate(), 8, (getYSize() - 94) + 2, titleTextColor());
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}