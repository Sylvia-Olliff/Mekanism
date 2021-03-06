package mekanism.client.gui.filter;

import mekanism.common.inventory.container.tile.EmptyTileContainer;
import mekanism.common.network.PacketGuiButtonPress.ClickedTileButton;
import mekanism.common.tile.TileEntityLogisticalSorter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiTFilterSelect extends GuiFilterSelect<TileEntityLogisticalSorter, EmptyTileContainer<TileEntityLogisticalSorter>> {

    public GuiTFilterSelect(EmptyTileContainer<TileEntityLogisticalSorter> container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
    }

    @Override
    protected Runnable onItemStackButton() {
        return () -> sendPacketToServer(ClickedTileButton.LS_FILTER_ITEMSTACK, -1);
    }

    @Override
    protected Runnable onTagButton() {
        return () -> sendPacketToServer(ClickedTileButton.LS_FILTER_TAG, -1);
    }

    @Override
    protected Runnable onMaterialButton() {
        return () -> sendPacketToServer(ClickedTileButton.LS_FILTER_MATERIAL, -1);
    }

    @Override
    protected Runnable onModIDButton() {
        return () -> sendPacketToServer(ClickedTileButton.LS_FILTER_MOD_ID, -1);
    }

    @Override
    protected Runnable onBackButton() {
        return () -> sendPacketToServer(ClickedTileButton.BACK_BUTTON);
    }
}