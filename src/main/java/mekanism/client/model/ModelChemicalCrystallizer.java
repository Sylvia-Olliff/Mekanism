package mekanism.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javax.annotation.Nonnull;
import mekanism.client.render.MekanismRenderType;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelChemicalCrystallizer extends MekanismModel {

    private static final ResourceLocation CRYSTALLIZER_TEXTURE = MekanismUtils.getResource(ResourceType.RENDER, "chemical_crystallizer.png");
    private final RenderType RENDER_TYPE = getRenderType(CRYSTALLIZER_TEXTURE);
    private final RenderType GLASS_RENDER_TYPE = MekanismRenderType.mekStandard(CRYSTALLIZER_TEXTURE);

    private final ModelRenderer tray;
    private final ModelRenderer support4;
    private final ModelRenderer rimBack;
    private final ModelRenderer portRight;
    private final ModelRenderer rimRight;
    private final ModelRenderer rimLeft;
    private final ModelRenderer rimFront;
    private final ModelRenderer portLeft;
    private final ModelRenderer support3;
    private final ModelRenderer support2;
    private final ModelRenderer support1;
    private final ModelRenderer tank;
    private final ModelRenderer rod1;
    private final ModelRenderer rod2;
    private final ModelRenderer rod3;
    private final ModelRenderer base;
    private final ModelRenderer glass;

    public ModelChemicalCrystallizer() {
        super(RenderType::getEntitySolid);
        textureWidth = 128;
        textureHeight = 64;

        tray = new ModelRenderer(this, 48, 0);
        tray.addBox(0F, 0F, 0F, 10, 1, 10, false);
        tray.setRotationPoint(-5F, 18.5F, -5F);
        tray.setTextureSize(128, 64);
        tray.mirror = true;
        setRotation(tray, 0F, 0F, 0F);
        support4 = new ModelRenderer(this, 0, 0);
        support4.addBox(0F, 0F, 0F, 1, 5, 1, false);
        support4.setRotationPoint(6.5F, 13F, 6.5F);
        support4.setTextureSize(128, 64);
        support4.mirror = true;
        setRotation(support4, 0F, 0F, 0F);
        rimBack = new ModelRenderer(this, 0, 46);
        rimBack.addBox(0F, 0F, 0F, 16, 2, 2, false);
        rimBack.setRotationPoint(-8F, 17F, 6F);
        rimBack.setTextureSize(128, 64);
        rimBack.mirror = true;
        setRotation(rimBack, 0F, 0F, 0F);
        portRight = new ModelRenderer(this, 54, 42);
        portRight.mirror = true;
        portRight.addBox(0F, 0F, 0F, 1, 10, 10, true);
        portRight.setRotationPoint(7.01F, 11F, -5F);
        portRight.setTextureSize(128, 64);
        setRotation(portRight, 0F, 0F, 0F);
        rimRight = new ModelRenderer(this, 0, 50);
        rimRight.mirror = true;
        rimRight.addBox(0F, 0F, 0F, 2, 2, 12, false);
        rimRight.setRotationPoint(6F, 17F, -6F);
        rimRight.setTextureSize(128, 64);
        setRotation(rimRight, 0F, 0F, 0F);
        rimLeft = new ModelRenderer(this, 0, 50);
        rimLeft.addBox(0F, 0F, 0F, 2, 2, 12, false);
        rimLeft.setRotationPoint(-8F, 17F, -6F);
        rimLeft.setTextureSize(128, 64);
        rimLeft.mirror = true;
        setRotation(rimLeft, 0F, 0F, 0F);
        rimFront = new ModelRenderer(this, 0, 42);
        rimFront.addBox(0F, 0F, 0F, 16, 2, 2, false);
        rimFront.setRotationPoint(-8F, 17F, -8F);
        rimFront.setTextureSize(128, 64);
        rimFront.mirror = true;
        setRotation(rimFront, 0F, 0F, 0F);
        portLeft = new ModelRenderer(this, 36, 42);
        portLeft.addBox(0F, 0F, 0F, 1, 8, 8, false);
        portLeft.setRotationPoint(-8.01F, 12F, -4F);
        portLeft.setTextureSize(128, 64);
        portLeft.mirror = true;
        setRotation(portLeft, 0F, 0F, 0F);
        support3 = new ModelRenderer(this, 0, 0);
        support3.addBox(0F, 0F, 0F, 1, 5, 1, false);
        support3.setRotationPoint(-7.5F, 13F, 6.5F);
        support3.setTextureSize(128, 64);
        support3.mirror = true;
        setRotation(support3, 0F, 0F, 0F);
        support2 = new ModelRenderer(this, 0, 0);
        support2.addBox(0F, 0F, 0F, 1, 5, 1, false);
        support2.setRotationPoint(6.5F, 13F, -7.5F);
        support2.setTextureSize(128, 64);
        support2.mirror = true;
        setRotation(support2, 0F, 0F, 0F);
        support1 = new ModelRenderer(this, 0, 0);
        support1.addBox(0F, 0F, 0F, 1, 5, 1, false);
        support1.setRotationPoint(-7.5F, 13F, -7.5F);
        support1.setTextureSize(128, 64);
        support1.mirror = true;
        setRotation(support1, 0F, 0F, 0F);
        tank = new ModelRenderer(this, 0, 0);
        tank.addBox(0F, 0F, 0F, 16, 5, 16, false);
        tank.setRotationPoint(-8F, 8F, -8F);
        tank.setTextureSize(128, 64);
        tank.mirror = true;
        setRotation(tank, 0F, 0F, 0F);
        rod1 = new ModelRenderer(this, 8, 0);
        rod1.addBox(0F, 0F, 0F, 1, 2, 1, false);
        rod1.setRotationPoint(-2F, 13F, 0F);
        rod1.setTextureSize(128, 64);
        rod1.mirror = true;
        setRotation(rod1, 0F, 0F, 0F);
        rod2 = new ModelRenderer(this, 8, 3);
        rod2.addBox(0F, 0F, 0F, 1, 3, 1, false);
        rod2.setRotationPoint(1F, 13F, 1F);
        rod2.setTextureSize(128, 64);
        rod2.mirror = true;
        setRotation(rod2, 0F, 0F, 0F);
        rod3 = new ModelRenderer(this, 4, 0);
        rod3.addBox(0F, 0F, 0F, 1, 4, 1, false);
        rod3.setRotationPoint(-0.5F, 13F, -2F);
        rod3.setTextureSize(128, 64);
        rod3.mirror = true;
        setRotation(rod3, 0F, 0F, 0F);
        base = new ModelRenderer(this, 0, 21);
        base.addBox(0F, 0F, 0F, 16, 5, 16, false);
        base.setRotationPoint(-8F, 19F, -8F);
        base.setTextureSize(128, 64);
        base.mirror = true;
        setRotation(base, 0F, 0F, 0F);
        glass = new ModelRenderer(this, 64, 11);
        glass.addBox(0F, 0F, 0F, 14, 4, 14, false);
        glass.setRotationPoint(-7F, 13F, -7F);
        glass.setTextureSize(128, 64);
        glass.mirror = true;
        setRotation(glass, 0F, 0F, 0F);
    }

    public void render(@Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight, boolean hasEffect) {
        render(matrix, getVertexBuilder(renderer, RENDER_TYPE, hasEffect), light, overlayLight, 1, 1, 1, 1);
        //Render the glass on a more translucent layer
        //Note: The glass makes water, ice etc behind it invisible. This is due to an engine limitation
        glass.render(matrix, getVertexBuilder(renderer, GLASS_RENDER_TYPE, hasEffect), light, overlayLight, 1, 1, 1, 1);
    }

    @Override
    public void render(@Nonnull MatrixStack matrix, @Nonnull IVertexBuilder vertexBuilder, int light, int overlayLight, float red, float green, float blue, float alpha) {
        tray.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        support4.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        rimBack.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        portRight.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        rimRight.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        rimLeft.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        rimFront.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        portLeft.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        support3.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        support2.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        support1.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        tank.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        rod1.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        rod2.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        rod3.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        base.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
    }
}