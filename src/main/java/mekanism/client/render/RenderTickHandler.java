package mekanism.client.render;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nonnull;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mekanism.api.RelativeSide;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.client.MekanismClient;
import mekanism.client.gui.GuiUtils;
import mekanism.client.gui.element.bar.GuiBar;
import mekanism.client.render.MekanismRenderer.Model3D;
import mekanism.client.render.lib.effect.BoltRenderer;
import mekanism.client.render.tileentity.IWireFrameRenderer;
import mekanism.common.Mekanism;
import mekanism.common.base.ProfilerConstants;
import mekanism.common.block.BlockBounding;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.Attributes.AttributeCustomSelectionBox;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.ItemConfigurator;
import mekanism.common.item.ItemConfigurator.ConfiguratorMode;
import mekanism.common.item.gear.ItemFlamethrower;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import mekanism.common.item.interfaces.IItemHUDProvider;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.lib.Color;
import mekanism.common.lib.effect.BoltEffect;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.lib.radiation.RadiationManager;
import mekanism.common.lib.radiation.RadiationManager.RadiationScale;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismParticleTypes;
import mekanism.common.tile.TileEntityBoundingBlock;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.interfaces.ISideConfiguration;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.StorageUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.profiler.IProfiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderTickHandler {

    public Minecraft minecraft = Minecraft.getInstance();

    private static final ResourceLocation POWER_BAR = MekanismUtils.getResource(ResourceType.GUI_BAR, "horizontal_power_long.png");
    private static final Map<Direction, Map<TransmissionType, Model3D>> cachedOverlays = new EnumMap<>(Direction.class);
    private static final EquipmentSlotType[] EQUIPMENT_ORDER = new EquipmentSlotType[]{EquipmentSlotType.OFFHAND, EquipmentSlotType.MAINHAND,
                                                                                       EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS,
                                                                                       EquipmentSlotType.FEET};

    private static final double HUD_SCALE = 0.6;

    private static HUDRenderer hudRenderer = new HUDRenderer();

    public static int modeSwitchTimer = 0;
    public static double prevRadiation = 0;

    private static final BoltRenderer boltRenderer = new BoltRenderer();

    public static void resetCachedOverlays() {
        cachedOverlays.clear();
    }

    public static void renderBolt(Object renderer, BoltEffect bolt) {
        boltRenderer.update(renderer, bolt, MekanismRenderer.getPartialTick());
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        MatrixStack matrix = event.getMatrixStack();
        matrix.push();
        // here we translate based on the inverse position of the client viewing camera to get back to 0, 0, 0
        Vec3d camVec = minecraft.gameRenderer.getActiveRenderInfo().getProjectedView();
        matrix.translate(-camVec.x, -camVec.y, -camVec.z);
        IRenderTypeBuffer.Impl renderer = minecraft.getRenderTypeBuffers().getBufferSource();
        boltRenderer.render(minecraft.getRenderPartialTicks(), matrix, renderer);
        renderer.finish(RenderType.getLightning());
        matrix.pop();
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.ARMOR) {
            FloatingLong capacity = FloatingLong.ZERO, stored = FloatingLong.ZERO;
            for (ItemStack stack : minecraft.player.inventory.armorInventory) {
                IEnergyContainer container = StorageUtils.getEnergyContainer(stack, 0);
                if (stack.getItem() instanceof ItemMekaSuitArmor && container != null) {
                    capacity = capacity.plusEqual(container.getMaxEnergy());
                    stored = stored.plusEqual(container.getEnergy());
                }
            }
            if (!capacity.isZero()) {
                int x = event.getWindow().getScaledWidth() / 2 - 91;
                int y = event.getWindow().getScaledHeight() - ForgeIngameGui.left_height + 2;
                int length = (int) Math.round(stored.divide(capacity).doubleValue() * 79);

                GuiUtils.renderExtendedTexture(GuiBar.BAR, 2, 2, x, y, 81, 6);
                minecraft.getTextureManager().bindTexture(POWER_BAR);
                AbstractGui.blit(x + 1, y + 1, length, 4, 0, 0, length, 4, 79, 4);
                minecraft.getTextureManager().bindTexture(ForgeIngameGui.GUI_ICONS_LOCATION);
                ForgeIngameGui.left_height += 8;
            }
        }
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == ElementType.HOTBAR) {
            if (!minecraft.player.isSpectator() && MekanismConfig.client.enableHUD.get() && MekanismClient.renderHUD) {
                int count = 0;
                Map<EquipmentSlotType, List<ITextComponent>> renderStrings = new LinkedHashMap<>();
                for (EquipmentSlotType slotType : EQUIPMENT_ORDER) {
                    ItemStack stack = minecraft.player.getItemStackFromSlot(slotType);
                    if (stack.getItem() instanceof IItemHUDProvider) {
                        List<ITextComponent> list = new ArrayList<>();
                        ((IItemHUDProvider) stack.getItem()).addHUDStrings(list, stack, slotType);
                        int size = list.size();
                        if (size > 0) {
                            renderStrings.put(slotType, list);
                            count += size;
                        }
                    }
                }
                int start = (renderStrings.size() * 2) + (count * 9);
                boolean alignLeft = MekanismConfig.client.alignHUDLeft.get();
                MainWindow window = event.getWindow();
                int y = window.getScaledHeight();
                RenderSystem.pushMatrix();
                RenderSystem.scaled(HUD_SCALE, HUD_SCALE, HUD_SCALE);
                for (Map.Entry<EquipmentSlotType, List<ITextComponent>> entry : renderStrings.entrySet()) {
                    for (ITextComponent text : entry.getValue()) {
                        drawString(window, text, alignLeft, (int) (y * (1 / HUD_SCALE)) - start, 0xc8c8c8);
                        start -= 9;
                    }
                    start -= 2;
                }
                RenderSystem.popMatrix();

                if (minecraft.player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof ItemMekaSuitArmor) {
                    hudRenderer.renderHUD(event.getPartialTicks());
                }
            }
        }
    }

    @SubscribeEvent
    public void tickEnd(RenderTickEvent event) {
        if (event.phase == Phase.END) {
            if (minecraft.player != null && minecraft.player.world != null && !minecraft.isGamePaused()) {
                PlayerEntity player = minecraft.player;
                World world = minecraft.player.world;
                renderStatusBar(player);
                //Traverse active jetpacks and do animations
                for (UUID uuid : Mekanism.playerState.getActiveJetpacks()) {
                    PlayerEntity p = world.getPlayerByUuid(uuid);
                    if (p != null) {
                        Pos3D playerPos = new Pos3D(p).translate(0, 1.7, 0);
                        Vec3d playerMotion = p.getMotion();
                        float random = (world.rand.nextFloat() - 0.5F) * 0.1F;
                        Pos3D vLeft = new Pos3D(-0.43, -0.55, -0.54).rotatePitch(p.isCrouching() ? 20 : 0).rotateYaw(p.renderYawOffset);
                        renderJetpackSmoke(world, playerPos.translate(vLeft, playerMotion), vLeft.scale(0.2).translate(playerMotion, vLeft.scale(random)));
                        Pos3D vRight = new Pos3D(0.43, -0.55, -0.54).rotatePitch(p.isCrouching() ? 20 : 0).rotateYaw(p.renderYawOffset);
                        renderJetpackSmoke(world, playerPos.translate(vRight, playerMotion), vRight.scale(0.2).translate(playerMotion, vRight.scale(random)));
                        Pos3D vCenter = new Pos3D((world.rand.nextFloat() - 0.5) * 0.4, -0.86, -0.30).rotatePitch(p.isCrouching() ? 25 : 0).rotateYaw(p.renderYawOffset);
                        renderJetpackSmoke(world, playerPos.translate(vCenter, playerMotion), vCenter.scale(0.2).translate(playerMotion));
                    }
                }

                if (world.getGameTime() % 4 == 0) {
                    //Traverse active scuba masks and do animations
                    for (UUID uuid : Mekanism.playerState.getActiveScubaMasks()) {
                        PlayerEntity p = world.getPlayerByUuid(uuid);
                        if (p != null && p.isInWater()) {
                            Pos3D vec = new Pos3D(0.4, 0.4, 0.4).multiply(p.getLook(1)).translate(0, -0.2, 0);
                            Pos3D motion = vec.scale(0.2).translate(p.getMotion());
                            Pos3D v = new Pos3D(p).translate(0, 1.7, 0).translate(vec);
                            world.addParticle((BasicParticleType) MekanismParticleTypes.SCUBA_BUBBLE.getParticleType(), v.x, v.y, v.z, motion.x, motion.y + 0.2, motion.z);
                        }
                    }
                    //Traverse players and do animations for idle flame throwers
                    for (PlayerEntity p : world.getPlayers()) {
                        if (!p.isSwingInProgress && !Mekanism.playerState.isFlamethrowerOn(p)) {
                            ItemStack currentItem = p.getHeldItemMainhand();
                            if (!currentItem.isEmpty() && currentItem.getItem() instanceof ItemFlamethrower && ChemicalUtil.hasGas(currentItem)) {
                                Pos3D flameVec;
                                if (player == p && minecraft.gameSettings.thirdPersonView == 0) {
                                    flameVec = new Pos3D(1, 1, 1).multiply(p.getLook(1)).rotateYaw(5).translate(0, 1.6, 0);
                                } else {
                                    double flameXCoord = -0.2;
                                    double flameYCoord = 1;
                                    double flameZCoord = 1.2;
                                    if (p.isCrouching()) {
                                        flameYCoord -= 0.55;
                                        flameZCoord -= 0.15;
                                    }
                                    flameVec = new Pos3D(flameXCoord, flameYCoord, flameZCoord).rotateYaw(p.renderYawOffset);
                                }
                                Vec3d motion = p.getMotion();
                                Pos3D flameMotion = new Pos3D(motion.getX(), p.onGround ? 0 : motion.getY(), motion.getZ());
                                Pos3D playerPos = new Pos3D(p);
                                Pos3D mergedVec = playerPos.translate(flameVec);
                                world.addParticle((BasicParticleType) MekanismParticleTypes.JETPACK_FLAME.getParticleType(),
                                      mergedVec.x, mergedVec.y, mergedVec.z, flameMotion.x, flameMotion.y, flameMotion.z);
                            }
                        }
                    }
                }

                if (!player.isCreative()) {
                    player.getCapability(Capabilities.RADIATION_ENTITY_CAPABILITY).ifPresent(c -> {
                        double radiation = c.getRadiation();
                        double severity = RadiationScale.getScaledDoseSeverity(radiation) * 0.8;
                        if (prevRadiation < severity) {
                            prevRadiation = Math.min(severity, prevRadiation + 0.01);
                        }
                        if (prevRadiation > severity) {
                            prevRadiation = Math.max(severity, prevRadiation - 0.01);
                        }
                        if (severity > RadiationManager.BASELINE) {
                            int effect = (int) (prevRadiation * 255);
                            int color = (0x701E1E << 8) + effect;
                            MekanismRenderer.renderColorOverlay(0, 0, minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight(), color);
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public void configurableMachine(DrawHighlightEvent.HighlightBlock event) {
        PlayerEntity player = minecraft.player;
        if (player == null) {
            return;
        }
        BlockRayTraceResult rayTraceResult = event.getTarget();
        if (!rayTraceResult.getType().equals(Type.MISS)) {
            World world = player.getEntityWorld();
            BlockPos pos = rayTraceResult.getPos();
            IRenderTypeBuffer renderer = event.getBuffers();
            ActiveRenderInfo info = event.getInfo();
            MatrixStack matrix = event.getMatrix();
            IProfiler profiler = world.getProfiler();
            BlockState blockState = world.getBlockState(pos);
            boolean shouldCancel = false;
            profiler.startSection(ProfilerConstants.MEKANISM_OUTLINE);
            if (!blockState.isAir(world, pos) && world.getWorldBorder().contains(pos)) {
                BlockPos actualPos = pos;
                BlockState actualState = blockState;
                if (blockState.getBlock() instanceof BlockBounding) {
                    TileEntityBoundingBlock tile = MekanismUtils.getTileEntity(TileEntityBoundingBlock.class, world, pos);
                    if (tile != null) {
                        actualPos = tile.getMainPos();
                        actualState = world.getBlockState(actualPos);
                    }
                }
                if (Attribute.has(actualState.getBlock(), AttributeCustomSelectionBox.class)) {
                    TileEntity tile = MekanismUtils.getTileEntity(world, actualPos);
                    if (tile != null) {
                        TileEntityRenderer<TileEntity> tileRenderer = TileEntityRendererDispatcher.instance.getRenderer(tile);
                        if (tileRenderer instanceof IWireFrameRenderer) {
                            matrix.push();
                            Vec3d viewPosition = info.getProjectedView();
                            matrix.translate(actualPos.getX() - viewPosition.x, actualPos.getY() - viewPosition.y, actualPos.getZ() - viewPosition.z);
                            ((IWireFrameRenderer) tileRenderer).renderWireFrame(tile, event.getPartialTicks(), matrix, renderer.getBuffer(RenderType.getLines()), 0, 0, 0, 0.4F);
                            matrix.pop();
                            shouldCancel = true;
                        }
                    }
                }
            }
            profiler.endSection();

            ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemConfigurator)) {
                //If we are not holding a configurator, look if we are in the offhand
                stack = player.getHeldItem(Hand.OFF_HAND);
                if (stack.isEmpty() || !(stack.getItem() instanceof ItemConfigurator)) {
                    if (shouldCancel) {
                        event.setCanceled(true);
                    }
                    return;
                }
            }
            profiler.startSection(ProfilerConstants.CONFIGURABLE_MACHINE);
            ConfiguratorMode state = ((ItemConfigurator) stack.getItem()).getMode(stack);
            if (state.isConfigurating()) {
                TransmissionType type = Objects.requireNonNull(state.getTransmission(), "Configurating state requires transmission type");
                TileEntity tile = MekanismUtils.getTileEntity(world, pos);
                if (tile instanceof ISideConfiguration) {
                    ISideConfiguration configurable = (ISideConfiguration) tile;
                    TileComponentConfig config = configurable.getConfig();
                    if (config.supports(type)) {
                        Direction face = rayTraceResult.getFace();
                        DataType dataType = config.getDataType(type, RelativeSide.fromDirections(configurable.getOrientation(), face));
                        if (dataType != null) {
                            Vec3d viewPosition = info.getProjectedView();
                            matrix.push();
                            matrix.translate(pos.getX() - viewPosition.x, pos.getY() - viewPosition.y, pos.getZ() - viewPosition.z);
                            MekanismRenderer.renderObject(getOverlayModel(face, type), matrix, renderer.getBuffer(MekanismRenderType.resizableCuboid()),
                                  MekanismRenderer.getColorARGB(dataType.getColor(), 0.6F), MekanismRenderer.FULL_LIGHT);
                            matrix.pop();
                        }
                    }
                }
            }
            profiler.endSection();
            if (shouldCancel) {
                event.setCanceled(true);
            }
        }
    }

    private void renderStatusBar(@Nonnull PlayerEntity player) {
        //TODO: use vanilla status bar text? Note, the vanilla status bar text stays a lot longer than we have our message
        // display for, so we would need to somehow modify it. This can be done via ATs but does cause it to always appear
        // to be more faded in color, and blinks to full color just before disappearing
        if (modeSwitchTimer > 1) {
            if (minecraft.currentScreen == null && minecraft.fontRenderer != null) {
                ItemStack stack = player.getHeldItemMainhand();
                if (IModeItem.isModeItem(stack, EquipmentSlotType.MAINHAND)) {
                    ITextComponent scrollTextComponent = ((IModeItem) stack.getItem()).getScrollTextComponent(stack);
                    if (scrollTextComponent != null) {
                        int x = minecraft.getMainWindow().getScaledWidth();
                        int y = minecraft.getMainWindow().getScaledHeight();
                        String text = scrollTextComponent.getFormattedText();
                        int color = Color.rgbad(1, 1, 1, modeSwitchTimer / 100F).argb();
                        minecraft.fontRenderer.drawString(text, x / 2 - minecraft.fontRenderer.getStringWidth(text) / 2, y - 60, color);
                    }
                }
            }
            modeSwitchTimer--;
        }
    }

    private void renderJetpackSmoke(World world, Pos3D pos, Pos3D motion) {
        world.addParticle((BasicParticleType) MekanismParticleTypes.JETPACK_FLAME.getParticleType(), pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
        world.addParticle((BasicParticleType) MekanismParticleTypes.JETPACK_SMOKE.getParticleType(), pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
    }

    private void drawString(MainWindow window, ITextComponent textComponent, boolean leftSide, int y, int color) {
        String s = textComponent.getFormattedText();
        FontRenderer font = minecraft.fontRenderer;
        // Note that we always offset by 2 pixels when left or right aligned
        if (leftSide) {
            font.drawStringWithShadow(s, 2, y, color);
        } else {
            int width = font.getStringWidth(s) + 2;
            font.drawStringWithShadow(s, window.getScaledWidth() - width, y, color);
        }
    }

    private Model3D getOverlayModel(Direction side, TransmissionType type) {
        if (cachedOverlays.containsKey(side) && cachedOverlays.get(side).containsKey(type)) {
            return cachedOverlays.get(side).get(type);
        }

        Model3D toReturn = new Model3D();
        toReturn.setTexture(MekanismRenderer.overlays.get(type));
        cachedOverlays.computeIfAbsent(side, s -> new EnumMap<>(TransmissionType.class)).putIfAbsent(type, toReturn);

        switch (side) {
            case DOWN:
                toReturn.minY = -.01;
                toReturn.maxY = -.001;

                toReturn.minX = 0;
                toReturn.minZ = 0;
                toReturn.maxX = 1;
                toReturn.maxZ = 1;
                break;
            case UP:
                toReturn.minY = 1.001;
                toReturn.maxY = 1.01;

                toReturn.minX = 0;
                toReturn.minZ = 0;
                toReturn.maxX = 1;
                toReturn.maxZ = 1;
                break;
            case NORTH:
                toReturn.minZ = -.01;
                toReturn.maxZ = -.001;

                toReturn.minX = 0;
                toReturn.minY = 0;
                toReturn.maxX = 1;
                toReturn.maxY = 1;
                break;
            case SOUTH:
                toReturn.minZ = 1.001;
                toReturn.maxZ = 1.01;

                toReturn.minX = 0;
                toReturn.minY = 0;
                toReturn.maxX = 1;
                toReturn.maxY = 1;
                break;
            case WEST:
                toReturn.minX = -.01;
                toReturn.maxX = -.001;

                toReturn.minY = 0;
                toReturn.minZ = 0;
                toReturn.maxY = 1;
                toReturn.maxZ = 1;
                break;
            case EAST:
                toReturn.minX = 1.001;
                toReturn.maxX = 1.01;

                toReturn.minY = 0;
                toReturn.minZ = 0;
                toReturn.maxY = 1;
                toReturn.maxZ = 1;
                break;
        }
        return toReturn;
    }
}