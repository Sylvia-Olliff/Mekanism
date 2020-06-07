package mekanism.common.content.blocktype;

import static mekanism.common.util.VoxelShapeUtils.setShape;
import static net.minecraft.block.Block.makeCuboidShape;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.shapes.VoxelShape;

public final class BlockShapes {

    public static final VoxelShape[] ELECTROLYTIC_SEPARATOR = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] DIGITAL_MINER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] CHEMICAL_CRYSTALLIZER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] PRESSURIZED_REACTION_CHAMBER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] METALLURGIC_INFUSER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] CHEMICAL_WASHER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] CHEMICAL_OXIDIZER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] CHEMICAL_INFUSER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] CHEMICAL_DISSOLUTION_CHAMBER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] ROTARY_CONDENSENTRATOR = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] FLUIDIC_PLENISHER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] ELECTRIC_PUMP = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] SOLAR_NEUTRON_ACTIVATOR = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] CHARGEPAD = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] FLUID_TANK = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] LASER = new VoxelShape[EnumUtils.DIRECTIONS.length];
    public static final VoxelShape[] LASER_AMPLIFIER = new VoxelShape[EnumUtils.DIRECTIONS.length];
    public static final VoxelShape[] RESISTIVE_HEATER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] SEISMIC_VIBRATOR = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] PERSONAL_CHEST = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] QUANTUM_ENTANGLOPORTER = new VoxelShape[EnumUtils.DIRECTIONS.length];
    public static final VoxelShape[] LOGISTICAL_SORTER = new VoxelShape[EnumUtils.DIRECTIONS.length];
    public static final VoxelShape[] SECURITY_DESK = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] CHEMICAL_TANK = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] INDUSTRIAL_ALARM = new VoxelShape[EnumUtils.DIRECTIONS.length];
    public static final VoxelShape[] QIO_DASHBOARD = new VoxelShape[EnumUtils.DIRECTIONS.length];
    public static final VoxelShape[] RADIOACTIVE_WASTE_BARREL = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];

    static {
        setShape(VoxelShapeUtils.rotate(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 4, 16), // base
              makeCuboidShape(15, 3, 3, 16, 13, 13), // portToggle1
              makeCuboidShape(0, 4, 4, 1, 12, 12), // portToggle2a
              makeCuboidShape(4, 4, 0, 12, 12, 1), // portToggle3a
              makeCuboidShape(4, 4, 15, 12, 12, 16), // portToggle4a
              makeCuboidShape(1, 4, 7, 3, 11, 9), // portToggle2b
              makeCuboidShape(7, 4, 1, 8, 11, 3), // portToggle3b
              makeCuboidShape(7, 4, 13, 8, 11, 15), // portToggle4b
              makeCuboidShape(8, 4, 0, 16, 16, 16), // tank1
              makeCuboidShape(0, 4, 9, 7, 14, 16), // tank2
              makeCuboidShape(0, 4, 0, 7, 14, 7), // tank3
              makeCuboidShape(6.5, 10, 7.5, 9.5, 11, 8.5), // tube1
              makeCuboidShape(3, 12, 7.5, 7, 13, 8.5), // tube2
              makeCuboidShape(3, 12, 7.5, 4, 15, 8.5), // tube3
              makeCuboidShape(3, 15, 3, 4, 16, 13), // tube4
              makeCuboidShape(3, 14, 3, 4, 15, 4), // tube5
              makeCuboidShape(3, 14, 12, 4, 15, 13)// tube6
        ), Rotation.CLOCKWISE_90), ELECTROLYTIC_SEPARATOR);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(5, 9, -14, 6, 10, -13),
              makeCuboidShape(10, 9, -14, 11, 10, -13),
              makeCuboidShape(10, 9, -13, 11, 11, -9),
              makeCuboidShape(5, 9, -13, 6, 11, -9),
              makeCuboidShape(10, 20, -11, 12, 22, -9),
              makeCuboidShape(4, 20, -11, 6, 22, -9),
              makeCuboidShape(-8, 3, -9, 24, 32, 3),
              makeCuboidShape(-8, 3, 20, 24, 32, 32),
              makeCuboidShape(-8, 3, 4, 24, 8, 19),
              makeCuboidShape(24, 24, -8, 29, 29, -6),
              makeCuboidShape(24, 24, 0, 29, 29, 2),
              makeCuboidShape(24, 24, 21, 29, 29, 23),
              makeCuboidShape(24, 24, 29, 29, 29, 31),
              makeCuboidShape(-13, 24, -8, -8, 29, -6),
              makeCuboidShape(-13, 24, 0, -8, 29, 2),
              makeCuboidShape(-13, 24, 21, -8, 29, 23),
              makeCuboidShape(-13, 24, 29, -8, 29, 31),
              makeCuboidShape(24, 24, -6, 25, 29, 0),
              makeCuboidShape(24, 24, 23, 25, 29, 29),
              makeCuboidShape(-9, 24, -6, -8, 29, 0),
              makeCuboidShape(-9, 24, 23, -8, 29, 29),
              makeCuboidShape(26, 2, -7, 30, 30, 1),
              makeCuboidShape(26, 2, 22, 30, 30, 30),
              makeCuboidShape(-14, 2, -7, -10, 30, 1),
              makeCuboidShape(-14, 2, 22, -10, 30, 30),
              makeCuboidShape(24, 0, -8, 31, 2, 2),
              makeCuboidShape(24, 0, 21, 31, 2, 31),
              makeCuboidShape(-15, 0, 21, -8, 2, 31),
              makeCuboidShape(-15, 0, -8, -8, 2, 2),
              makeCuboidShape(-7, 4, 3, 23, 31, 20),
              makeCuboidShape(5, 2, -6, 11, 4, 5),
              makeCuboidShape(5, 1, 5, 11, 4, 11),
              makeCuboidShape(-15, 5, 5, -6, 11, 11),
              makeCuboidShape(22, 5, 5, 31, 11, 11),
              makeCuboidShape(4, 0, 4, 12, 1, 12),
              makeCuboidShape(-16, 4, 4, -15, 12, 12),
              makeCuboidShape(-9, 4, 4, -8, 12, 12),
              makeCuboidShape(31, 4, 4, 32, 12, 12),
              makeCuboidShape(24, 4, 4, 25, 12, 12),
              makeCuboidShape(-8, 27, 4, 24, 32, 19),
              makeCuboidShape(-8, 21, 4, 24, 26, 19),
              makeCuboidShape(-8, 15, 4, 24, 20, 19),
              makeCuboidShape(-8, 9, 4, 24, 14, 19),
              //Keyboard
              makeCuboidShape(3, 11, -10.5, 13, 12.5, -11.75),
              makeCuboidShape(3, 10, -11.75, 13, 11.5, -13),
              makeCuboidShape(3, 9.5, -13, 13, 11, -14.25),
              makeCuboidShape(3, 9, -14.25, 13, 10.5, -15.25),
              makeCuboidShape(4, 9.5, -12, 12, 10, -13),
              makeCuboidShape(4, 8.5, -13, 12, 9.5, -14.25),
              //Center monitor
              makeCuboidShape(2, 18, -10.5, 14, 24, -11.5),
              makeCuboidShape(1, 16, -11.5, 15, 26, -13.5),
              //Left monitor
              makeCuboidShape(17, 17.75, -10, 18.5, 24.25, -11.5),
              makeCuboidShape(18.5, 17.75, -10.5, 22, 24.25, -12),
              makeCuboidShape(22, 17.75, -11.5, 25.5, 24.25, -13),
              makeCuboidShape(25.5, 17.75, -12.5, 29, 24.25, -14),
              makeCuboidShape(15.5, 16, -11.5, 19.5, 26, -13.5),
              makeCuboidShape(18.5, 16, -12, 23, 26, -14),
              makeCuboidShape(22, 16, -13, 26.5, 26, -15),
              makeCuboidShape(25.5, 16, -14, 30, 26, -16),
              //Right Monitor
              makeCuboidShape(-3 + 2.5, 17.75, -10, -6.5 + 2.5, 24.25, -11.5),
              makeCuboidShape(-6.5 + 2.5, 17.75, -10.5, -10 + 2.5, 24.25, -12),
              makeCuboidShape(-10 + 2.5, 17.75, -11.5, -13.5 + 2.5, 24.25, -13),
              makeCuboidShape(-13.5 + 2.5, 17.75, -12.5, -15 + 2.5, 24.25, -14),
              makeCuboidShape(-6.5 + 2.5, 16, -11.5, -2 + 2.5, 26, -13.5),
              makeCuboidShape(-10 + 2.5, 16, -12, -5.5 + 2.5, 26, -14),
              makeCuboidShape(-13.5 + 2.5, 16, -13, -9 + 2.5, 26, -15),
              makeCuboidShape(-16.5 + 2.5, 16, -14, -12.5 + 2.5, 26, -16)
        ), DIGITAL_MINER);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 5, 16), // base
              makeCuboidShape(0, 11, 0, 16, 16, 16), // tank
              makeCuboidShape(3, 4.5, 3, 13, 5.5, 13), // tray
              makeCuboidShape(1, 7, 1, 15, 11, 15), // Shape1
              makeCuboidShape(0, 3, 3, 1, 13, 13), // portRight
              makeCuboidShape(15, 4, 4, 16, 12, 12), // portLeft
              makeCuboidShape(0, 5, 0, 16, 7, 2), // rimBack
              makeCuboidShape(0, 5, 2, 2, 7, 14), // rimRight
              makeCuboidShape(14, 5, 2, 16, 7, 14), // rimLeft
              makeCuboidShape(0, 5, 14, 16, 7, 16), // rimFront
              makeCuboidShape(14.5, 6, 14.5, 15.5, 11, 15.5), // support1
              makeCuboidShape(0.5, 6, 14.5, 1.5, 11, 15.5), // support2
              makeCuboidShape(14.5, 6, 0.5, 15.5, 11, 1.5), // support3
              makeCuboidShape(0.5, 6, 0.5, 1.5, 11, 1.5)// support4
        ), CHEMICAL_CRYSTALLIZER);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 4, 16), // base
              makeCuboidShape(1, 4, 1, 10, 15, 6), // front
              makeCuboidShape(0, 4, 6, 16, 16, 16), // body
              makeCuboidShape(13, 3.5, 0.5, 15, 15.5, 6.5), // frontDivider1
              makeCuboidShape(10, 3.5, 0.5, 12, 15.5, 6.5), // frontDivider2
              makeCuboidShape(12, 5, 1, 13, 6, 6), // bar1
              makeCuboidShape(12, 7, 1, 13, 8, 6), // bar2
              makeCuboidShape(12, 9, 1, 13, 10, 6), // bar3
              makeCuboidShape(12, 11, 1, 13, 12, 6), // bar4
              makeCuboidShape(12, 13, 1, 13, 14, 6)// bar5
        ), PRESSURIZED_REACTION_CHAMBER);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 4, 16), // base
              makeCuboidShape(0, 4, 15, 16, 16, 16), // back
              makeCuboidShape(0, 15, 8, 16, 16, 15), // top
              makeCuboidShape(1.5, 7, 1.5, 14.5, 8, 15.5), // divider
              makeCuboidShape(0, 4, 8, 1, 15, 15), // sideRight
              makeCuboidShape(15, 4, 8, 16, 15, 15), // sideLeft
              makeCuboidShape(13.5, 11, 1.5, 14.5, 12, 2.5), // bar1
              makeCuboidShape(1.5, 11, 1.5, 2.5, 12, 2.5), // bar2
              makeCuboidShape(11, 10.5, 5, 12, 15.5, 8), // connector1
              makeCuboidShape(4, 10.5, 5, 5, 15.5, 8), // connector2
              makeCuboidShape(10.5, 10.5, 13, 12.5, 11.5, 15), // tapBase1
              makeCuboidShape(3.5, 10.5, 13, 5.5, 11.5, 15), // tapBase2
              makeCuboidShape(10.5, 11.5, 4, 12.5, 12.5, 15), // tap1
              makeCuboidShape(3.5, 11.5, 4, 5.5, 12.5, 15), // tap2
              makeCuboidShape(1, 12, 1, 15, 15, 15), // plate1
              makeCuboidShape(1, 8, 1, 15, 11, 15), // plate2
              makeCuboidShape(1, 4, 1, 15, 7, 15)// plate3
        ), METALLURGIC_INFUSER);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 4, 16), // base
              makeCuboidShape(7, 3.5, 3, 9, 4.5, 5), // conduit
              makeCuboidShape(10.49, 2, 4.5, 11.49, 10, 8.5), // pipe2b
              makeCuboidShape(13, 5, 8, 15, 11, 10), // connectorLeft
              makeCuboidShape(1, 5, 8, 3, 11, 10), // connectorRight
              makeCuboidShape(3, 15, 3, 13, 16, 13), // portTop
              makeCuboidShape(0, 4, 4, 1, 12, 12), // portRight
              makeCuboidShape(15, 4, 4, 16, 12, 12), // portLeft
              makeCuboidShape(0, 4, 10, 16, 14, 16), // tankBack
              makeCuboidShape(9, 4, 0, 16, 14, 8), // tankLeft
              makeCuboidShape(0, 4, 0, 7, 14, 8), // tankRight
              makeCuboidShape(13, 13.5, 11, 14, 15.5, 12), // tubeLeft1
              makeCuboidShape(13, 14.5, 4, 14, 15.5, 12), // tubeLeft2
              makeCuboidShape(13, 12.5, 4, 14, 14.5, 5), // tubeLeft3
              makeCuboidShape(1, 13, 1.5, 2, 15, 2.5), // tubeRight1
              makeCuboidShape(1, 13, 3.5, 2, 15, 4.5), // tubeRight2
              makeCuboidShape(1, 13, 5.5, 2, 15, 6.5), // tubeRight3
              makeCuboidShape(4.5, 10, 4.5, 11.5, 15, 11.5), // pipe1
              makeCuboidShape(4.51, 2, 4.5, 10.51, 10, 8.5), // pipe2
              makeCuboidShape(7, 12, 1, 9, 13, 2), // bridge1
              makeCuboidShape(7, 10, 1, 9, 11, 2), // bridge2
              makeCuboidShape(7, 8, 1, 9, 9, 2), // bridge3
              makeCuboidShape(7, 6, 1, 9, 7, 2)// bridge4
        ), CHEMICAL_WASHER);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 4, 16), // base
              makeCuboidShape(8.5, 4, 1.5, 13.5, 5, 14.5), // stand
              makeCuboidShape(15, 3, 3, 16, 13, 13), // connector
              makeCuboidShape(8.5, 5.5, 6, 13.5, 15.5, 7), // bridge
              makeCuboidShape(0, 4, 0, 7, 16, 16), // tank
              makeCuboidShape(0, 4, 4, 1, 12, 12), // connectorToggle
              makeCuboidShape(8, 5, 1, 14, 16, 6), // tower1
              makeCuboidShape(8, 5, 7, 14, 16, 15), // tower2
              makeCuboidShape(7, 7, 9, 8, 10, 12), // pipe1
              makeCuboidShape(13, 5, 5, 15, 11, 11)// pipe2
        ), CHEMICAL_OXIDIZER);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 5, 16), // base
              makeCuboidShape(5, 12.5, 5.5, 11, 15.5, 8.5), // compressor
              makeCuboidShape(7, 5, 13, 9, 11, 15), // connector
              makeCuboidShape(7, 3, 13, 9, 11, 15), // connectorAngle
              makeCuboidShape(4, 4, 0, 12, 12, 1), // portFront
              makeCuboidShape(4, 4, 15, 12, 12, 16), // portBack
              makeCuboidShape(15, 4, 4, 16, 12, 12), // portLeft
              makeCuboidShape(0, 4, 4, 1, 12, 12), // portRight
              makeCuboidShape(14, 5, 5, 15, 11, 9), // pipe1
              makeCuboidShape(1, 5, 5, 2, 11, 9), // pipe2
              makeCuboidShape(8, 5, 6, 13, 11, 9), // pipeAngle1
              makeCuboidShape(3, 5, 6, 8, 11, 9), // pipeAngle2
              makeCuboidShape(9, 5, 9, 15, 16, 15), // tank1
              makeCuboidShape(1, 5, 9, 7, 16, 15), // tank2
              makeCuboidShape(2, 5, 1, 14, 12, 8), // tank3
              makeCuboidShape(6.67, 11.5, 1.8, 7.67, 12.5, 2.8), // exhaust1
              makeCuboidShape(5, 11.5, 1.8, 6, 12.5, 2.8), // exhaust2
              makeCuboidShape(10, 11.5, 1.8, 11, 12.5, 2.8), // exhaust3
              makeCuboidShape(8.33, 11.5, 1.8, 9.33, 12.5, 2.8), // exhaust4
              makeCuboidShape(12, 13.5, 7.5, 13, 14.5, 9.5), // tube1
              makeCuboidShape(11, 13.5, 6.5, 13, 14.5, 7.5), // tube2
              makeCuboidShape(9, 11.5, 4, 10, 13.5, 5), // tube3
              makeCuboidShape(9, 13.5, 4, 10, 14.5, 6), // tube4
              makeCuboidShape(6, 13.5, 4, 7, 14.5, 6), // tube5
              makeCuboidShape(6, 11.5, 4, 7, 13.5, 5), // tube6
              makeCuboidShape(3, 13.5, 6.5, 5, 14.5, 7.5), // tube7
              makeCuboidShape(3, 13.5, 7.5, 4, 14.5, 9.5), // tube8
              makeCuboidShape(7, 14, 10, 9, 15, 11), // tube9
              makeCuboidShape(7, 14, 13, 9, 15, 14)// tube10
        ), CHEMICAL_INFUSER);

        setShape(VoxelShapeUtils.rotate(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 7, 16), // base
              makeCuboidShape(1, 7, 0, 15, 15, 2), // back
              makeCuboidShape(1, 7, 2, 15, 12, 15), // glass
              makeCuboidShape(4, 13, 2, 12, 15, 12), // vents
              makeCuboidShape(0, 15, 0, 16, 16, 16), // top
              makeCuboidShape(0, 12, 1, 16, 13, 16), // top2
              makeCuboidShape(15, 7, 0, 16, 15, 1), // backEdge1
              makeCuboidShape(0, 7, 0, 1, 15, 1), // backEdge2
              makeCuboidShape(14, 13, 14, 15, 15, 15), // support1
              makeCuboidShape(1, 13, 14, 2, 15, 15), // support2
              makeCuboidShape(0, 3, 3, 1, 13, 13), // portToggle1
              makeCuboidShape(15, 4, 4, 16, 12, 12)// portToggle2
        ), Rotation.CLOCKWISE_180), CHEMICAL_DISSOLUTION_CHAMBER);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 5, 16), // base
              makeCuboidShape(0, 15, 0, 16, 16, 16), // top
              makeCuboidShape(0, 13, 0, 16, 14, 16), // middle
              makeCuboidShape(7.5, 11, 7.5, 8.5, 13, 8.5), // shaft
              makeCuboidShape(4, 14, 4, 12, 15, 12), // bridge
              makeCuboidShape(7, 5, 5, 9, 11, 11), // pipe
              makeCuboidShape(9, 5, 1, 15, 13, 15), // tankLeft
              makeCuboidShape(1, 5, 1, 7, 13, 15), // tankRight
              makeCuboidShape(15, 4, 4, 16, 12, 12), // portLeft
              makeCuboidShape(0, 3, 3, 1, 13, 13), // portRight
              makeCuboidShape(14, 14, 14, 15, 15, 15), // support1
              makeCuboidShape(14, 14, 1, 15, 15, 2), // support2
              makeCuboidShape(1, 14, 1, 2, 15, 2), // support3
              makeCuboidShape(1, 14, 14, 2, 15, 15), // support4
              makeCuboidShape(7, 11, 2, 9, 12, 3), // tube1
              makeCuboidShape(7, 9, 2, 9, 10, 3), // tube2
              makeCuboidShape(7, 7, 2, 9, 8, 3), // tube3
              makeCuboidShape(7, 5, 2, 9, 6, 3), // tube4
              makeCuboidShape(7, 7, 13, 9, 8, 14), // tube5
              makeCuboidShape(7, 9, 13, 9, 10, 14), // tube6
              makeCuboidShape(7, 11, 13, 9, 12, 14), // tube7
              makeCuboidShape(7, 5, 13, 9, 6, 14)// tube8
        ), ROTARY_CONDENSENTRATOR);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(3, 15, 3, 13, 16, 13), // portTop
              makeCuboidShape(4, 4, 15, 12, 12, 16), // portBack
              makeCuboidShape(3.5, 1, 3.5, 12.5, 13, 12.5), // tank
              makeCuboidShape(5.5, 5.5, 11, 10.5, 10.5, 15), // Connector
              makeCuboidShape(4.5, 4.5, 13, 11.5, 11.5, 14), // connectorRing
              makeCuboidShape(2.5, 13, 2.5, 13.5, 14, 13.5), // ringTank
              makeCuboidShape(4, 0, 4, 12, 1, 12), // ringBottom
              makeCuboidShape(4, 14, 4, 12, 15, 12), // ringTop
              makeCuboidShape(12, 6, 6, 13, 10, 10), // bearingLeft
              makeCuboidShape(3, 6, 6, 4, 10, 10), // bearingRight
              makeCuboidShape(10, 10, 12, 11, 11, 15), // rod1
              makeCuboidShape(5, 10, 12, 6, 11, 15), // rod2
              makeCuboidShape(10, 5, 12, 11, 6, 15), // rod3
              makeCuboidShape(5, 5, 12, 6, 6, 15)// rod4
        ), FLUIDIC_PLENISHER);

        setShape(VoxelShapeUtils.rotate(VoxelShapeUtils.combine(
              makeCuboidShape(4.5, 1, 4.5, 11.5, 13, 11.5), // pumpCasing
              makeCuboidShape(5, 0, 5, 11, 15, 11), // pumpBase
              makeCuboidShape(4, 13, 4, 12, 14, 12), // pumpRingTop
              makeCuboidShape(4, 15, 4, 12, 16, 12), // pumpPortTop
              makeCuboidShape(4, 4, 0, 12, 12, 1), // powerPort
              makeCuboidShape(5.5, 5.5, 1, 10.5, 10.5, 5), // powerConnector
              makeCuboidShape(10, 10, 1, 11, 11, 5), // powerConnectorFrame1
              makeCuboidShape(5, 10, 1, 6, 11, 5), // powerConnectorFrame2
              makeCuboidShape(10, 5, 1, 11, 6, 5), // powerConnectorFrame3
              makeCuboidShape(5, 5, 1, 6, 6, 5)// powerConnectorFrame4
        ), Rotation.CLOCKWISE_180), ELECTRIC_PUMP);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(6, 14, 14, 10, 29, 16),
              makeCuboidShape(4, 4, 0, 12, 12, 1),
              makeCuboidShape(2, 4, 2, 14, 5, 15),
              makeCuboidShape(5, 14, 12, 6, 15, 13),
              makeCuboidShape(5, 15, 10, 11, 16, 11),
              makeCuboidShape(5, 14, 10, 6, 15, 11),
              makeCuboidShape(5, 15, 12, 11, 16, 13),
              makeCuboidShape(10, 14, 12, 11, 15, 13),
              makeCuboidShape(10, 14, 10, 11, 15, 11),
              makeCuboidShape(7, 13.5, 7, 9, 14.5, 14),
              makeCuboidShape(0, 5, 0, 16, 14, 16),
              makeCuboidShape(0, 0, 0, 16, 4, 16),
              makeCuboidShape(0.5, 4, 0.5, 1.5, 5, 1.5),
              makeCuboidShape(0.5, 4, 14.5, 1.5, 5, 15.5),
              makeCuboidShape(0.5, 4, 2.5, 1.5, 5, 3.5),
              makeCuboidShape(0.5, 4, 4.5, 1.5, 5, 5.5),
              makeCuboidShape(0.5, 4, 6.5, 1.5, 5, 7.5),
              makeCuboidShape(0.5, 4, 8.5, 1.5, 5, 9.5),
              makeCuboidShape(0.5, 4, 10.5, 1.5, 5, 11.5),
              makeCuboidShape(0.5, 4, 12.5, 1.5, 5, 13.5),
              makeCuboidShape(14.5, 4, 14.5, 15.5, 5, 15.5),
              makeCuboidShape(14.5, 4, 12.5, 15.5, 5, 13.5),
              makeCuboidShape(14.5, 4, 10.5, 15.5, 5, 11.5),
              makeCuboidShape(14.5, 4, 8.5, 15.5, 5, 9.5),
              makeCuboidShape(14.5, 4, 6.5, 15.5, 5, 7.5),
              makeCuboidShape(14.5, 4, 4.5, 15.5, 5, 5.5),
              makeCuboidShape(14.5, 4, 2.5, 15.5, 5, 3.5),
              makeCuboidShape(14.5, 4, 0.5, 15.5, 5, 1.5),
              makeCuboidShape(5, 4, 1, 11, 5, 2),
              //Rough estimates of slanted things
              makeCuboidShape(6, 14, 1, 7, 14.75, 3),
              makeCuboidShape(9, 14, 1, 10, 14.75, 3),
              makeCuboidShape(5, 14, 3, 11, 15.25, 4.5),
              makeCuboidShape(5, 14, 4.5, 11, 15, 6),
              makeCuboidShape(5, 14, 6, 11, 14.875, 7.5),
              makeCuboidShape(5, 14, 7.5, 11, 14.75, 9),
              makeCuboidShape(6.5, 14, 4.5, 9.5, 16, 5.5),
              makeCuboidShape(6.5, 14, 5.5, 7.5, 16, 6.5),
              makeCuboidShape(8.5, 14, 5.5, 9.5, 16, 6.5),
              makeCuboidShape(6.5, 14, 6.5, 9.5, 16, 7.5),
              //Top center
              makeCuboidShape(7, 26, 10, 9, 26.5, 14),
              makeCuboidShape(7, 26.5, 5.75, 9, 29, 14),
              makeCuboidShape(7.5, 25.75, 6.625, 8.5, 26.5, 7.625),
              makeCuboidShape(5, 29.5, 0, 11, 30.5, 1),
              makeCuboidShape(5, 28.5, 1, 11, 30.5, 4.5),
              makeCuboidShape(5, 28, 4.5, 11, 30, 8),
              makeCuboidShape(5, 27.75, 8, 11, 29.5, 11.5),
              makeCuboidShape(5, 27.25, 11.5, 11, 29, 15),
              //Left Side panel
              makeCuboidShape(11, 30, 0, 12.25, 31, 4),
              makeCuboidShape(11, 29.5, 4, 12.25, 30.5, 8),
              makeCuboidShape(11, 29, 8, 12.25, 30, 12),
              makeCuboidShape(11, 28.5, 12, 12.25, 29.5, 16.1),
              makeCuboidShape(12.25, 30.5, 0, 14.75, 31.5, 4),
              makeCuboidShape(12.25, 30, 4, 14.75, 31, 8),
              makeCuboidShape(12.25, 29.5, 8, 14.75, 30.5, 12),
              makeCuboidShape(12.25, 29, 12, 14.75, 30, 16.1),
              makeCuboidShape(14.75, 31, 0.25, 16.5, 32.25, 4),
              makeCuboidShape(14.75, 30.5, 4, 16.5, 31.5, 8),
              makeCuboidShape(14.75, 30, 8, 16.5, 31, 12),
              makeCuboidShape(14.75, 29.5, 12, 16.5, 30.5, 16.1),
              //Right Side panel
              makeCuboidShape(3.75, 30, 0, 5, 31, 4),
              makeCuboidShape(3.75, 29.5, 4, 5, 30.5, 8),
              makeCuboidShape(3.75, 29, 8, 5, 30, 12),
              makeCuboidShape(3.75, 28.5, 12, 5, 29.5, 16.1),
              makeCuboidShape(1.25, 30.5, 0, 3.75, 31.5, 4),
              makeCuboidShape(1.25, 30, 4, 3.75, 31, 8),
              makeCuboidShape(1.25, 29.5, 8, 3.75, 30.5, 12),
              makeCuboidShape(1.25, 29, 12, 3.75, 30, 16.1),
              makeCuboidShape(-0.5, 31, 0.25, 1.25, 32.25, 4),
              makeCuboidShape(-0.5, 30.5, 4, 1.25, 31.5, 8),
              makeCuboidShape(-0.5, 30, 8, 1.25, 31, 12),
              makeCuboidShape(-0.5, 29.5, 12, 1.25, 30.5, 16.1)
        ), SOLAR_NEUTRON_ACTIVATOR);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(10, 1, 11.99, 12, 8, 13.99),//pillar1
              makeCuboidShape(4, 1, 11.99, 6, 8, 13.99),//pillar2
              makeCuboidShape(0, 0, 0, 16, 1, 16),//base
              makeCuboidShape(5, 5, 14, 11, 11, 15),//connector
              makeCuboidShape(5, 1, 13, 11, 11, 14),//stand
              makeCuboidShape(7, 4, 11, 9, 5, 13),//plug
              makeCuboidShape(4, 4, 15, 12, 12, 16),//port
              makeCuboidShape(5, 5, 15, 11, 11, 16)//port_ring
        ), CHARGEPAD);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(5, 2, 4, 11, 9, 12),//body
              makeCuboidShape(8.5, 8, 7.5, 9.49, 13.99, 8.5),//wire
              makeCuboidShape(6.5, 9, 6.5, 9.5, 11, 9.5),//shaft
              makeCuboidShape(7, 1, 7, 9, 16, 9),//center
              makeCuboidShape(4, 0, 4, 12, 1, 12),//port
              makeCuboidShape(5, 1, 5, 11, 2, 11),//connector
              makeCuboidShape(6, 13, 6, 10, 14, 10),//ring1
              makeCuboidShape(6, 11, 6, 10, 12, 10),//ring2
              makeCuboidShape(4.2, 3, 9, 5.2, 8, 10),//rod1
              makeCuboidShape(4.2, 3, 6, 5.2, 8, 7),//rod2
              makeCuboidShape(10.8, 3, 9, 11.8, 8, 10),//rod3
              makeCuboidShape(10.8, 3, 6, 11.8, 8, 7),//rod4
              makeCuboidShape(4, 2, 4, 5, 3, 12),//fin1
              makeCuboidShape(4, 4, 4, 5, 5, 12),//fin2
              makeCuboidShape(4, 6, 4, 5, 7, 12),//fin3
              makeCuboidShape(4, 8, 4, 5, 9, 12),//fin4
              makeCuboidShape(11, 8, 4, 12, 9, 12),//fin5
              makeCuboidShape(11, 6, 4, 12, 7, 12),//fin6
              makeCuboidShape(11, 4, 4, 12, 5, 12),//fin7
              makeCuboidShape(11, 2, 4, 12, 3, 12)//fin8
        ), LASER, true, true);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(1, 1, 1, 15, 15, 15),//Base
              makeCuboidShape(0, 3, 3, 1, 13, 13),//S1
              makeCuboidShape(3, 3, 15, 13, 13, 16),//S2
              makeCuboidShape(15, 3, 3, 16, 13, 13),//S3
              makeCuboidShape(3, 0, 3, 13, 1, 13),//S4
              makeCuboidShape(3, 3, 0, 13, 13, 1),//S5
              makeCuboidShape(3, 15, 3, 13, 16, 13)//S6
        ), LASER_AMPLIFIER, true);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(0, 0, 0, 16, 7, 16),//base
              makeCuboidShape(5, 6.5, 7.5, 11, 12.5, 8.5),//center
              makeCuboidShape(15, 4, 4, 16, 12, 12),//portRight
              makeCuboidShape(0, 4, 4, 1, 12, 12),//portLeft
              makeCuboidShape(13, 7, 0, 16, 16, 16),//wallRight
              makeCuboidShape(0, 7, 0, 3, 16, 16),//wallLeft
              makeCuboidShape(11, 13.5, 1.5, 12, 14.5, 14.5),//bar1
              makeCuboidShape(9, 13.5, 1.5, 10, 14.5, 14.5),//bar2
              makeCuboidShape(6, 13.5, 1.5, 7, 14.5, 14.5),//bar3
              makeCuboidShape(4, 13.5, 1.5, 5, 14.5, 14.5),//bar4
              makeCuboidShape(3, 6.5, 14.5, 13, 15.5, 15.5),//fin1
              makeCuboidShape(3, 6.5, 13, 13, 15.5, 14),//fin2
              makeCuboidShape(3, 6.5, 11.5, 13, 15.5, 12.5),//fin3
              makeCuboidShape(3, 6.5, 10, 13, 15.5, 11),//fin4
              makeCuboidShape(3, 6.5, 8.5, 13, 15.5, 9.5),//fin5
              makeCuboidShape(3, 6.5, 6.5, 13, 15.5, 7.5),//fin6
              makeCuboidShape(3, 6.5, 5, 13, 15.5, 6),//fin7
              makeCuboidShape(3, 6.5, 3.5, 13, 15.5, 4.5),//fin8
              makeCuboidShape(3, 6.5, 2, 13, 15.5, 3),//fin9
              makeCuboidShape(3, 6.5, 0.5, 13, 15.5, 1.5)//fin10
        ), RESISTIVE_HEATER);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(4, 0, 4, 12, 2, 12),
              makeCuboidShape(0, 0, 13, 16, 5, 16),
              makeCuboidShape(5, 25, 5, 11, 29, 15),
              makeCuboidShape(4, 4, 15, 12, 12, 16),
              makeCuboidShape(0.5, 5, 14.5, 1.5, 30, 15.5),
              makeCuboidShape(6.5, 18, 6.5, 9.5, 29, 9.5),
              makeCuboidShape(7, 3, 7, 9, 18, 9),
              makeCuboidShape(6, 1, 6, 10, 3, 10),
              makeCuboidShape(6, 15, 6, 10, 17, 10),
              makeCuboidShape(6.5, 15, 10, 9.5, 17, 14),
              makeCuboidShape(0, 30, 0, 16, 32, 16),
              makeCuboidShape(0.5, 5, 0.5, 1.5, 30, 1.5),
              makeCuboidShape(0, 0, 3, 3, 5, 13),
              makeCuboidShape(0, 0, 0, 16, 5, 3),
              makeCuboidShape(13, 0, 3, 16, 5, 13),
              makeCuboidShape(0.5, 17, 1.5, 1.5, 18, 14.5),
              makeCuboidShape(14.5, 5, 0.5, 15.5, 30, 1.5),
              makeCuboidShape(1.5, 17, 14.5, 14.5, 18, 15.5),
              makeCuboidShape(14.5, 5, 14.5, 15.5, 30, 15.5),
              makeCuboidShape(14.5, 17, 1.5, 15.5, 18, 14.5),
              makeCuboidShape(6, 5, 14, 10, 30, 16),
              makeCuboidShape(3, 29, 3, 13, 30, 15),
              makeCuboidShape(3.5, 28.5, 11.5, 4.5, 29.5, 12.5),
              makeCuboidShape(11.5, 28.5, 11.5, 12.5, 29.5, 12.5),
              makeCuboidShape(11.5, 28.5, 3.5, 12.5, 29.5, 4.5),
              makeCuboidShape(3.5, 28.5, 3.5, 4.5, 29.5, 4.5),
              makeCuboidShape(11.5, 28.5, 5.5, 12.5, 29.5, 6.5),
              makeCuboidShape(3.5, 28.5, 5.5, 4.5, 29.5, 6.5),
              makeCuboidShape(11.5, 28.5, 7.5, 12.5, 29.5, 8.5),
              makeCuboidShape(3.5, 28.5, 7.5, 4.5, 29.5, 8.5),
              makeCuboidShape(11.5, 28.5, 9.5, 12.5, 29.5, 10.5),
              makeCuboidShape(3.5, 28.5, 9.5, 4.5, 29.5, 10.5),
              //Walls uses full walls instead of angles because even though we have code to calculate the proper angles
              // it causes lag when looking at the overly complicated bounding box
              makeCuboidShape(0.5, 0, 14.5, 15.5, 32, 15.5),
              makeCuboidShape(14.5, 0, 0.5, 15.5, 32, 15.5),
              makeCuboidShape(0.5, 0, 0.5, 1.5, 32, 15.5)
        ), SEISMIC_VIBRATOR);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(1, 0, 1, 15, 14, 15),//Main chest
              makeCuboidShape(7, 7, 0, 9, 11, 1)//latch
        ), PERSONAL_CHEST);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(4, 4, 0, 12, 12, 1),//portFront
              makeCuboidShape(0, 4, 4, 1, 12, 12),//portRight
              makeCuboidShape(15, 4, 4, 16, 12, 12),//portLeft
              makeCuboidShape(4, 15, 4, 12, 16, 12),//portTop
              makeCuboidShape(4, 0, 4, 12, 1, 12),//portBottom
              makeCuboidShape(4, 4, 15, 12, 12, 16),//portBack
              makeCuboidShape(13, 13, 0, 16, 16, 3),//corner1
              makeCuboidShape(0, 13, 0, 3, 16, 3),//corner2
              makeCuboidShape(13, 13, 13, 16, 16, 16),//corner3
              makeCuboidShape(0, 13, 13, 3, 16, 16),//corner4
              makeCuboidShape(13, 0, 0, 16, 3, 3),//corner5
              makeCuboidShape(0, 0, 0, 3, 3, 3),//corner6
              makeCuboidShape(13, 0, 13, 16, 3, 16),//corner7
              makeCuboidShape(0, 0, 13, 3, 3, 16),//corner8
              makeCuboidShape(13, 3, 1, 15, 13, 3),//frame1
              makeCuboidShape(1, 3, 1, 3, 13, 3),//frame2
              makeCuboidShape(13, 3, 13, 15, 13, 15),//frame3
              makeCuboidShape(1, 3, 13, 3, 13, 15),//frame4
              makeCuboidShape(3, 1, 1, 13, 3, 3),//frame5
              makeCuboidShape(13, 1, 3, 15, 3, 13),//frame6
              makeCuboidShape(1, 1, 3, 3, 3, 13),//frame7
              makeCuboidShape(3, 1, 13, 13, 3, 15),//frame8
              makeCuboidShape(3, 13, 1, 13, 15, 3),//frame9
              makeCuboidShape(13, 13, 3, 15, 15, 13),//frame10
              makeCuboidShape(1, 13, 3, 3, 15, 13),//frame11
              makeCuboidShape(3, 13, 13, 13, 15, 15),//frame12
              makeCuboidShape(14.5, 3, 0.5, 15.5, 13, 1.5),//frameEdge1
              makeCuboidShape(0.5, 3, 0.5, 1.5, 13, 1.5),//frameEdge2
              makeCuboidShape(14.5, 3, 14.5, 15.5, 13, 15.5),//frameEdge3
              makeCuboidShape(0.5, 3, 14.5, 1.5, 13, 15.5),//frameEdge4
              makeCuboidShape(3, 0.5, 0.5, 13, 1.5, 1.5),//frameEdge5
              makeCuboidShape(14.5, 0.5, 3, 15.5, 1.5, 13),//frameEdge6
              makeCuboidShape(0.5, 0.5, 3, 1.5, 1.5, 13),//frameEdge7
              makeCuboidShape(3, 0.5, 14.5, 13, 1.5, 15.5),//frameEdge8
              makeCuboidShape(3, 14.5, 0.5, 13, 15.5, 1.5),//frameEdge9
              makeCuboidShape(14.5, 14.5, 3, 15.5, 15.5, 13),//frameEdge10
              makeCuboidShape(0.5, 14.5, 3, 1.5, 15.5, 13),//frameEdge11
              makeCuboidShape(3, 14.5, 14.5, 13, 15.5, 15.5)//frameEdge12
        ), QUANTUM_ENTANGLOPORTER, true);

        setShape(VoxelShapeUtils.rotate(VoxelShapeUtils.combine(
              makeCuboidShape(5, 5, 1, 11, 11, 15),//pipe
              makeCuboidShape(3, 3, 14, 13, 13, 15),//connectorBack
              makeCuboidShape(2, 2, 15, 14, 14, 16),//portBackLarge
              makeCuboidShape(4, 4, 16, 12, 12, 17),//portBack
              makeCuboidShape(3, 3, 0, 13, 13, 1),//portFront
              makeCuboidShape(11, 6.5, 3.5, 12, 9.5, 11.5),//panel1
              makeCuboidShape(4, 6.5, 3.5, 5, 9.5, 11.5),//panel2
              makeCuboidShape(7, 3, 4, 9, 5, 5),//pistonConnector1
              makeCuboidShape(7, 11, 4, 9, 13, 5),//pistonConnector2
              makeCuboidShape(7, 3.5, 1, 9, 5.5, 4),//pistonBrace1
              makeCuboidShape(7, 10.5, 1, 9, 12.5, 4),//pistonBrace2
              makeCuboidShape(7, 3, 9.01, 9, 5, 14.01),//pistonBase1
              makeCuboidShape(7, 11, 9.01, 9, 13, 14.01),//pistonBase2
              makeCuboidShape(7.5, 3.5, 5.01, 8.5, 4.5, 10.01),//pistonBar1
              makeCuboidShape(7.5, 11.5, 5.01, 8.5, 12.5, 10.01),//pistonBar2
              makeCuboidShape(4.5, 4.5, 1, 11.5, 11.5, 2),//ring1
              makeCuboidShape(4.5, 4.5, 3, 11.5, 11.5, 4),//ring2
              makeCuboidShape(4.5, 4.5, 5, 11.5, 11.5, 6),//ring3
              makeCuboidShape(4.5, 4.5, 7, 11.5, 11.5, 8),//ring4
              makeCuboidShape(4.5, 4.5, 9, 11.5, 11.5, 10),//ring5
              makeCuboidShape(4.5, 4.5, 11, 11.5, 11.5, 12),//ring6
              makeCuboidShape(4.5, 4.5, 13, 11.5, 11.5, 14),//ring7
              makeCuboidShape(11.5, 7.5, 6.5, 12.5, 8.5, 7.5),//led1
              makeCuboidShape(11.5, 7.5, 4.5, 12.5, 8.5, 5.5),//led2
              makeCuboidShape(3.5, 7.5, 4.5, 4.5, 8.5, 5.5),//led3
              makeCuboidShape(3.5, 7.5, 6.5, 4.5, 8.5, 7.5)//led4
        ), Direction.NORTH), LOGISTICAL_SORTER, true, true);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(0, 6, 0, 16, 13, 16),
              makeCuboidShape(0, 0, 0, 16, 5, 16),
              makeCuboidShape(1, 5, 1, 15, 6, 15),
              //Rough estimated of things that are at angles, so that we do not have overly complex shapes
              //keyboard
              makeCuboidShape(3, 13, 2, 13, 14, 7),
              //stand base
              makeCuboidShape(4, 13, 10, 12, 14, 14),
              //stand neck
              makeCuboidShape(7, 14, 13, 9, 15.5, 14),
              makeCuboidShape(7, 15.5, 12.875, 9, 17, 13.875),
              makeCuboidShape(7, 17, 12.75, 9, 18.5, 13.75),
              makeCuboidShape(7, 18.5, 12.625, 9, 20, 13.625),
              //monitor
              makeCuboidShape(1, 14.5, 9, 15, 15, 10),
              makeCuboidShape(1, 15, 8, 15, 16, 10.5),
              makeCuboidShape(1, 16, 8.5, 15, 17, 11),
              makeCuboidShape(1, 17, 9, 15, 18, 11.5),
              makeCuboidShape(1, 18, 9.5, 15, 19, 12),
              makeCuboidShape(1, 19, 10, 15, 20, 12.5),
              makeCuboidShape(1, 20, 10.5, 15, 21, 13),
              makeCuboidShape(1, 21, 11, 15, 22, 13.5),
              makeCuboidShape(1, 22, 11.5, 15, 23, 14),
              makeCuboidShape(1, 23, 12, 15, 24, 14.25),
              makeCuboidShape(1, 24, 12.5, 15, 24.5, 13),
              //monitor back
              makeCuboidShape(2, 16, 11, 14, 17, 12),
              makeCuboidShape(2, 17, 11.5, 14, 18, 12.5),
              makeCuboidShape(2, 18, 12, 14, 19, 13),
              makeCuboidShape(2, 19, 12.5, 14, 20, 13.5),
              makeCuboidShape(2, 20, 13, 14, 21.5, 14)
        ), SECURITY_DESK);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(3, 1.5, 3, 13, 13.5, 13),//tank
              makeCuboidShape(3.5, 0.5, 3.5, 12.5, 1.5, 12.5),//tankBase
              makeCuboidShape(6.5, 14.5, 6.5, 9.5, 15.5, 9.5),//valve
              makeCuboidShape(7, 13.5, 7, 9, 14.5, 9),//valveBase
              makeCuboidShape(6, 13, 4, 10, 14, 5),//rim0
              makeCuboidShape(10, 13, 4, 12, 16, 5),//rim1
              makeCuboidShape(11, 13, 5, 12, 16, 11),//rim2
              makeCuboidShape(4, 13, 11, 12, 16, 12),//rim3
              makeCuboidShape(4, 13, 5, 5, 16, 11),//rim4
              makeCuboidShape(4, 13, 4, 6, 16, 5)//rim5
        ), CHEMICAL_TANK);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(5, 15, 5, 11, 16, 11),
              makeCuboidShape(6, 11, 6, 10, 15, 10)
        ), INDUSTRIAL_ALARM, true);

        setShape(VoxelShapeUtils.combine(
              makeCuboidShape(1, 15, 1, 15, 16, 15)
        ), QIO_DASHBOARD, true);

        //TODO: Don't bother rotating the shape, it is the same for all rotations
        setShape(makeCuboidShape(2, 0, 2, 14, 16, 14), FLUID_TANK);
        setShape(makeCuboidShape(2, 0, 2, 14, 16, 14), RADIOACTIVE_WASTE_BARREL);
    }
}
