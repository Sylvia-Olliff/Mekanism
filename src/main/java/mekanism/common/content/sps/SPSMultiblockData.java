package mekanism.common.content.sps;

import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.Action;
import mekanism.api.NBTConstants;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.inventory.AutomationType;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.chemical.multiblock.MultiblockGasTank;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.lib.multiblock.IValveHandler;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.registries.MekanismGases;
import mekanism.common.tile.multiblock.TileEntitySPSCasing;
import mekanism.common.tile.multiblock.TileEntitySPSPort;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class SPSMultiblockData extends MultiblockData implements IValveHandler {

    private static final long MAX_OUTPUT_GAS = 1_000;

    @ContainerSync
    public MultiblockGasTank<SPSMultiblockData> inputTank;
    @ContainerSync
    public MultiblockGasTank<SPSMultiblockData> outputTank;

    public SyncableCoilData coilData = new SyncableCoilData();

    @ContainerSync
    public double progress;
    @ContainerSync
    public int inputProcessed = 0;

    public FloatingLong receivedEnergy = FloatingLong.ZERO;
    @ContainerSync
    public FloatingLong lastReceivedEnergy = FloatingLong.ZERO;
    @ContainerSync
    public double lastProcessed;

    public SPSMultiblockData(TileEntitySPSCasing tile) {
        super(tile);

        gasTanks.add(inputTank = MultiblockGasTank.create(this, tile, this::getMaxInputGas,
            (stack, automationType) -> automationType != AutomationType.EXTERNAL && isFormed(), (stack, automationType) -> isFormed(),
            gas -> gas == MekanismGases.POLONIUM.get(), ChemicalAttributeValidator.ALWAYS_ALLOW, null));
        gasTanks.add(outputTank = MultiblockGasTank.create(this, tile, () -> MAX_OUTPUT_GAS,
            (stack, automationType) -> isFormed(), (stack, automationType) -> automationType != AutomationType.EXTERNAL && isFormed(),
            gas -> gas == MekanismGases.ANTIMATTER.get(), ChemicalAttributeValidator.ALWAYS_ALLOW, null));
    }

    private long getMaxInputGas() {
        return MekanismConfig.general.spsInputPerAntimatter.get() * 2;
    }

    @Override
    public boolean tick(World world) {
        boolean needsPacket = super.tick(world);
        double processed = 0;
        final int inputPerAntimatter = MekanismConfig.general.spsInputPerAntimatter.get();
        if (canOperate() && !receivedEnergy.isZero()) {
            long inputNeeded = (inputPerAntimatter - inputProcessed) + inputPerAntimatter * (outputTank.getNeeded() - 1);
            double processable = receivedEnergy.doubleValue() / MekanismConfig.general.spsEnergyPerInput.get().doubleValue();
            if (processable + progress >= inputNeeded) {
                processed = inputNeeded;
                process(inputNeeded);
                progress = 0;
            } else {
                processed = processable;
                progress += processable;
                process((int) progress);
                progress %= 1;
            }
        }

        if (!receivedEnergy.equals(lastReceivedEnergy) || processed != lastProcessed) {
            needsPacket = true;
        }
        lastReceivedEnergy = receivedEnergy;
        receivedEnergy = FloatingLong.ZERO;
        lastProcessed = processed;

        needsPacket |= coilData.tick();
        return needsPacket;
    }

    @Override
    public void readUpdateTag(CompoundNBT tag) {
        super.readUpdateTag(tag);
        coilData.read(tag);
        lastReceivedEnergy = FloatingLong.parseFloatingLong(tag.getString(NBTConstants.ENERGY_USAGE));
    }

    @Override
    public void writeUpdateTag(CompoundNBT tag) {
        super.writeUpdateTag(tag);
        coilData.write(tag);
        tag.putString(NBTConstants.ENERGY_USAGE, lastReceivedEnergy.toString());
    }

    private void process(long operations) {
        if (operations == 0)
            return;
        final int inputPerAntimatter = MekanismConfig.general.spsInputPerAntimatter.get();
        inputProcessed += operations;
        inputTank.shrinkStack(operations, Action.EXECUTE);
        if (inputProcessed >= inputPerAntimatter) {
            GasStack toAdd = MekanismGases.ANTIMATTER.getStack(inputProcessed / inputPerAntimatter);
            outputTank.insert(toAdd, Action.EXECUTE, AutomationType.INTERNAL);
            inputProcessed %= inputPerAntimatter;
        }
    }

    public boolean canSupplyCoilEnergy(TileEntitySPSPort tile) {
        return canOperate() && coilData.coilMap.containsKey(tile.getPos());
    }

    public void addCoil(BlockPos portPos, Direction side) {
        coilData.coilMap.put(portPos, new CoilData(portPos, side));
    }

    public void supplyCoilEnergy(TileEntitySPSPort tile, FloatingLong energy) {
        receivedEnergy = receivedEnergy.plusEqual(energy);
        coilData.coilMap.get(tile.getPos()).receiveEnergy(energy);
    }

    public boolean canOperate() {
        return !inputTank.isEmpty() && outputTank.getNeeded() > 0;
    }

    public static int getCoilLevel(FloatingLong energy) {
        if (energy.isZero())
            return 0;
        return 1 + Math.max(0, (int) ((Math.log10(energy.doubleValue()) - 3) * 1.8));
    }

    public double getProcessRate() {
        return Math.round((lastProcessed / MekanismConfig.general.spsInputPerAntimatter.get()) * 1000) / 1000D;
    }

    public float getScaledProgress() {
        return (float) ((inputProcessed + progress) / MekanismConfig.general.spsInputPerAntimatter.get());
    }

    public boolean handlesSound(TileEntitySPSCasing tile) {
        return tile.getPos().equals(getMinPos().add(3, 0, 0)) || tile.getPos().equals(getMaxPos().add(3, 0, 7));
    }

    public static class SyncableCoilData {

        public Map<BlockPos, CoilData> coilMap = new Object2ObjectOpenHashMap<>();
        public int prevHash;

        private boolean tick() {
            coilMap.values().forEach(data -> {
                data.prevLevel = data.laserLevel;
                data.laserLevel = 0;
            });

            int newHash = coilMap.hashCode();
            boolean ret = newHash != prevHash;
            prevHash = newHash;
            return ret;
        }

        public void write(CompoundNBT tags) {
            ListNBT list = new ListNBT();
            for (CoilData data : coilMap.values()) {
                CompoundNBT tag = new CompoundNBT();
                tag.put(NBTConstants.POSITION, NBTUtil.writeBlockPos(data.coilPos));
                tag.putInt(NBTConstants.SIDE, data.side.ordinal());
                tag.putInt(NBTConstants.LEVEL, data.prevLevel);
                list.add(tag);
            }
            tags.put(NBTConstants.COILS, list);
        }

        public void read(CompoundNBT tags) {
            coilMap.clear();
            ListNBT list = tags.getList(NBTConstants.COILS, NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT tag = list.getCompound(i);
                BlockPos pos = NBTUtil.readBlockPos(tag.getCompound(NBTConstants.POSITION));
                Direction side = Direction.byIndex(tag.getInt(NBTConstants.SIDE));
                CoilData data = new CoilData(pos, side);
                data.prevLevel = tag.getInt(NBTConstants.LEVEL);
                coilMap.put(data.coilPos, data);
            }
        }
    }

    public static class CoilData {

        public final BlockPos coilPos;
        public final Direction side;
        // prev level is synced, as laserLevel is reset to 0 each tick
        public int prevLevel;
        private int laserLevel;

        private CoilData(BlockPos pos, Direction side) {
            this.coilPos = pos;
            this.side = side;
        }

        private void receiveEnergy(FloatingLong energy) {
            laserLevel += getCoilLevel(energy);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + coilPos.hashCode();
            result = prime * result + prevLevel;
            return result;
        }
    }
}
