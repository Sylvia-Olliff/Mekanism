package mekanism.client.render.armor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.client.render.lib.QuadTransformation;
import mekanism.client.render.lib.QuadUtils;
import mekanism.client.render.obj.OBJModelCache;
import mekanism.client.render.obj.OBJModelCache.OBJModelData;
import mekanism.client.render.obj.TransmitterBakedModel.QuickHash;
import mekanism.common.Mekanism;
import mekanism.common.content.gear.Modules;
import mekanism.common.content.gear.Modules.ModuleData;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import mekanism.common.util.EnumUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.geometry.IModelGeometryPart;

public class MekaSuitArmor extends CustomArmor {

    private static final String LED_TAG = "led";
    private static final String OVERRIDDEN_TAG = "overridden_";
    private static final String EXCLUSIVE_TAG = "excl_";
    private static final String SHARED_TAG = "shared_";

    public static MekaSuitArmor HELMET = new MekaSuitArmor(0.5F, EquipmentSlotType.HEAD, EquipmentSlotType.CHEST);
    public static MekaSuitArmor BODYARMOR = new MekaSuitArmor(0.5F, EquipmentSlotType.CHEST, EquipmentSlotType.HEAD);
    public static MekaSuitArmor PANTS = new MekaSuitArmor(0.5F, EquipmentSlotType.LEGS, EquipmentSlotType.FEET);
    public static MekaSuitArmor BOOTS = new MekaSuitArmor(0.5F, EquipmentSlotType.FEET, EquipmentSlotType.LEGS);

    private static final Table<EquipmentSlotType, ModuleData<?>, ModuleModelSpec> moduleModelSpec = HashBasedTable.create();

    static {
        registerModule("solar_helmet", Modules.SOLAR_RECHARGING_UNIT, EquipmentSlotType.HEAD);
        registerModule("jetpack", Modules.JETPACK_UNIT, EquipmentSlotType.CHEST);
        registerModule("modulating", Modules.GRAVITATIONAL_MODULATING_UNIT, EquipmentSlotType.CHEST);
        // TODO make sure these are correct
    }

    private static final QuadTransformation BASE_TRANSFORM = QuadTransformation.list(QuadTransformation.rotate(0, 0, 180), QuadTransformation.translate(new Vec3d(-1, 0.5, 0)));

    private final LoadingCache<QuickHash, ArmorQuads> cache = CacheBuilder.newBuilder().build(new CacheLoader<QuickHash, ArmorQuads>() {
        @Override
        @SuppressWarnings("unchecked")
        public ArmorQuads load(@Nonnull QuickHash key) {
            return createQuads((Set<ModuleModelSpec>) key.get()[0], (Set<EquipmentSlotType>) key.get()[1]);
        }
    });

    private final EquipmentSlotType type;
    private final EquipmentSlotType adjacentType;

    private MekaSuitArmor(float size, EquipmentSlotType type, EquipmentSlotType adjacentType) {
        super(size);
        this.type = type;
        this.adjacentType = adjacentType;
        OBJModelCache.reloadCallback(cache::invalidateAll);
    }

    @Override
    public void render(@Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight, boolean hasEffect, LivingEntity entity, ItemStack stack) {
        cache.getUnchecked(key(entity)).getMap().forEach((modelPos, quads) -> {
            matrix.push();
            modelPos.translate(this, matrix);
            render(renderer, matrix, light, overlayLight, quads);
            matrix.pop();
        });
    }

    private void render(IRenderTypeBuffer renderer, MatrixStack matrix, int light, int overlayLight, List<BakedQuad> quads) {
        IVertexBuilder builder = renderer.getBuffer(RenderType.getCutout());
        MatrixStack.Entry last = matrix.getLast();
        for (BakedQuad quad : quads) {
            builder.addVertexData(last, quad, 1, 1, 1, 1, light, overlayLight);
        }
    }

    private static List<BakedQuad> getQuads(OBJModelData data, Set<String> parts, Set<String> ledParts, QuadTransformation transform) {
        List<BakedQuad> quads = data.getBakedModel(new MekaSuitModelConfiguration(parts))
              .getQuads(null, null, Minecraft.getInstance().world.getRandom(), EmptyModelData.INSTANCE);
        List<BakedQuad> ledQuads = data.getBakedModel(new MekaSuitModelConfiguration(ledParts))
              .getQuads(null, null, Minecraft.getInstance().world.getRandom(), EmptyModelData.INSTANCE);
        quads.addAll(QuadUtils.transformBakedQuads(ledQuads, QuadTransformation.fullbright));
        if (transform != null) {
            quads = QuadUtils.transformBakedQuads(quads, transform);
        }
        return quads;
    }

    public enum ModelPos {
        HEAD(BASE_TRANSFORM, s -> s.contains("head")),
        BODY(BASE_TRANSFORM, s -> s.contains("body")),
        LEFT_ARM(BASE_TRANSFORM.and(QuadTransformation.translate(new Vec3d(-0.3125, -0.125, 0))), s -> s.contains("left_arm")),
        RIGHT_ARM(BASE_TRANSFORM.and(QuadTransformation.translate(new Vec3d(0.3125, -0.125, 0))), s -> s.contains("right_arm")),
        LEFT_LEG(BASE_TRANSFORM.and(QuadTransformation.translate(new Vec3d(-0.125, -0.75, 0))), s -> s.contains("left_leg")),
        RIGHT_LEG(BASE_TRANSFORM.and(QuadTransformation.translate(new Vec3d(0.125, -0.75, 0))), s -> s.contains("right_leg"));

        public static final ModelPos[] VALUES = values();

        private final QuadTransformation transform;
        private final Predicate<String> modelSpec;

        ModelPos(QuadTransformation transform, Predicate<String> modelSpec) {
            this.transform = transform;
            this.modelSpec = modelSpec;
        }

        public QuadTransformation getTransform() {
            return transform;
        }

        public boolean contains(String s) {
            return modelSpec.test(s);
        }

        public static ModelPos get(String name) {
            for (ModelPos pos : VALUES) {
                if (pos.contains(name.toLowerCase())) {
                    return pos;
                }
            }
            return null;
        }

        public void translate(MekaSuitArmor armor, MatrixStack matrix) {
            switch (this) {
                case HEAD:
                    armor.bipedHead.translateRotate(matrix);
                    break;
                case BODY:
                    armor.bipedBody.translateRotate(matrix);
                    break;
                case LEFT_ARM:
                    armor.bipedLeftArm.translateRotate(matrix);
                    break;
                case RIGHT_ARM:
                    armor.bipedRightArm.translateRotate(matrix);
                    break;
                case LEFT_LEG:
                    armor.bipedLeftLeg.translateRotate(matrix);
                    break;
                case RIGHT_LEG:
                    armor.bipedRightLeg.translateRotate(matrix);
                    break;
            }
        }
    }

    public ArmorQuads createQuads(Set<ModuleModelSpec> modules, Set<EquipmentSlotType> wornParts) {
        Map<ModelPos, Set<String>> moduleQuadsToRender = new Object2ObjectOpenHashMap<>();
        Map<ModelPos, Set<String>> moduleLEDQuadsToRender = new Object2ObjectOpenHashMap<>();
        // map of normal model part name to overwritten model part name (i.e. chest_body_box1 -> jetpack_chest_body_overridden_box1
        Map<String, String> overrides = new Object2ObjectOpenHashMap<>();

        for (IModelGeometryPart part : OBJModelCache.MEKASUIT_MODULES.getModel().getParts()) {
            String name = part.name();
            ModuleModelSpec matchingSpec = modules.stream().filter(m -> m.contains(name)).findFirst().orElse(null);
            if (matchingSpec == null) {
                Mekanism.logger.error("MekaSuit module part '" + name + "' doesn't correspond to any supported module model. Ignoring.");
                continue;
            }
            if (name.contains(OVERRIDDEN_TAG)) {
                overrides.put(matchingSpec.processOverrideName(name), name);
            }
            // if this armor unit controls rendering of this module
            if (type == matchingSpec.slotType) {
                ModelPos pos = ModelPos.get(name);
                if (pos == null) {
                    Mekanism.logger.warn("MekaSuit part '" + name + "' is invalid. Ignoring.");
                }
                if (name.contains(LED_TAG)) {
                    moduleLEDQuadsToRender.computeIfAbsent(pos, p -> new HashSet<>()).add(name);
                } else {
                    moduleQuadsToRender.computeIfAbsent(pos, p -> new HashSet<>()).add(name);
                }
            }
        }

        Map<ModelPos, Set<String>> armorQuadsToRender = new Object2ObjectOpenHashMap<>();
        Map<ModelPos, Set<String>> armorLEDQuadsToRender = new Object2ObjectOpenHashMap<>();

        for (IModelGeometryPart part : OBJModelCache.MEKASUIT.getModel().getParts()) {
            String name = part.name();
            // skip if it's the wrong equipment type
            if (!checkEquipment(type, name)) {
                continue;
            }
            // skip if the part is exclusive and the adjacent part is present
            if (name.startsWith(EXCLUSIVE_TAG) && wornParts.contains(adjacentType)) {
                continue;
            }
            // skip if the part is shared and the shared part already rendered
            if (name.startsWith(SHARED_TAG) && wornParts.contains(adjacentType) && adjacentType.ordinal() > type.ordinal()) {
                continue;
            }

            ModelPos pos = ModelPos.get(name);
            if (pos == null) {
                Mekanism.logger.warn("MekaSuit part '" + name + "' is invalid. Ignoring.");
            }

            String override = overrides.get(name);
            if (override != null) {
                if (override.contains(LED_TAG)) {
                    moduleLEDQuadsToRender.computeIfAbsent(pos, p -> new HashSet<>()).add(override);
                } else {
                    moduleQuadsToRender.computeIfAbsent(pos, p -> new HashSet<>()).add(override);
                }
            } else {
                if (name.contains(LED_TAG)) {
                    armorLEDQuadsToRender.computeIfAbsent(pos, p -> new HashSet<>()).add(name);
                } else {
                    armorQuadsToRender.computeIfAbsent(pos, p -> new HashSet<>()).add(name);
                }
            }
        }

        Map<ModelPos, List<BakedQuad>> map = new Object2ObjectOpenHashMap<>();
        for (ModelPos pos : ModelPos.VALUES) {
            map.computeIfAbsent(pos, p -> new ArrayList<>()).addAll(getQuads(OBJModelCache.MEKASUIT_MODULES,
                  moduleQuadsToRender.getOrDefault(pos, new HashSet<>()),
                  moduleLEDQuadsToRender.getOrDefault(pos, new HashSet<>()),
                  pos.getTransform()));
            map.get(pos).addAll(getQuads(OBJModelCache.MEKASUIT,
                  armorQuadsToRender.getOrDefault(pos, new HashSet<>()),
                  armorLEDQuadsToRender.getOrDefault(pos, new HashSet<>()),
                  pos.getTransform()));
        }
        return new ArmorQuads(map);
    }

    public static boolean checkEquipment(EquipmentSlotType type, String text) {
        if (type == EquipmentSlotType.HEAD && text.contains("helmet")) {
            return true;
        } else if (type == EquipmentSlotType.CHEST && text.contains("chest")) {
            return true;
        } else if (type == EquipmentSlotType.LEGS && text.contains("leggings")) {
            return true;
        } else {
            return type == EquipmentSlotType.FEET && text.contains("boots");
        }
    }

    public static class ArmorQuads {

        private Map<ModelPos, List<BakedQuad>> quads = new Object2ObjectOpenHashMap<>();

        public ArmorQuads(Map<ModelPos, List<BakedQuad>> quads) {
            this.quads = quads;
        }

        public Map<ModelPos, List<BakedQuad>> getMap() {
            return quads;
        }
    }

    public static class ModuleModelSpec {

        private final ModuleData<?> module;
        private final EquipmentSlotType slotType;
        private final String name;
        private final Predicate<String> modelSpec;

        public ModuleModelSpec(ModuleData<?> module, EquipmentSlotType slotType, String name) {
            this.module = module;
            this.slotType = slotType;
            this.name = name;
            this.modelSpec = (s) -> s.contains(name + "_");
        }

        public boolean contains(String s) {
            return modelSpec.test(s);
        }

        public String processOverrideName(String part) {
            return part.replace(OVERRIDDEN_TAG, "").replace(name + "_", "");
        }
    }

    private static void registerModule(String name, ModuleData<?> module, EquipmentSlotType slotType) {
        moduleModelSpec.put(slotType, module, new ModuleModelSpec(module, slotType, name));
    }

    public QuickHash key(LivingEntity player) {
        Set<ModuleModelSpec> modules = new ObjectOpenHashSet<>();
        Set<EquipmentSlotType> wornParts = EnumSet.noneOf(EquipmentSlotType.class);
        for (EquipmentSlotType slotType : EnumUtils.ARMOR_SLOTS) {
            if (player.getItemStackFromSlot(slotType).getItem() instanceof ItemMekaSuitArmor) {
                wornParts.add(slotType);
            }
            for (ModuleData<?> module : moduleModelSpec.row(slotType).keySet()) {
                if (Modules.load(player.getItemStackFromSlot(slotType), module) != null) {
                    modules.add(moduleModelSpec.get(slotType, module));
                }
            }
        }
        return new QuickHash(modules, wornParts);
    }

    private static class MekaSuitModelConfiguration implements IModelConfiguration {

        private final Set<String> parts;

        public MekaSuitModelConfiguration(Set<String> parts) {
            this.parts = parts;
        }

        @Nullable
        @Override
        public IUnbakedModel getOwnerModel() {
            return null;
        }

        @Nonnull
        @Override
        public String getModelName() {
            return "mekasuit";
        }

        @Override
        public boolean isTexturePresent(@Nonnull String name) {
            return false;
        }

        @Nonnull
        @Override
        public Material resolveTexture(@Nonnull String name) {
            return ModelLoaderRegistry.blockMaterial(name);
        }

        @Override
        public boolean isShadedInGui() {
            return false;
        }

        @Override
        public boolean isSideLit() {
            return false;
        }

        @Override
        public boolean useSmoothLighting() {
            return false;
        }

        @Nonnull
        @Override
        @Deprecated
        public ItemCameraTransforms getCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }

        @Nonnull
        @Override
        public IModelTransform getCombinedTransform() {
            return ModelRotation.X0_Y0;
        }

        @Override
        public boolean getPartVisibility(@Nonnull IModelGeometryPart part, boolean fallback) {
            //Ignore fallback as we always have a true or false answer
            return getPartVisibility(part);
        }

        @Override
        public boolean getPartVisibility(@Nonnull IModelGeometryPart part) {
            return parts.contains(part.name());
        }
    }
}
