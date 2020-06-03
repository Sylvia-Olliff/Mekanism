package mekanism.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javax.annotation.Nonnull;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelScubaTank extends MekanismModel {

    private static final ResourceLocation TANK_TEXTURE = MekanismUtils.getResource(ResourceType.RENDER, "scuba_set.png");
    private final RenderType RENDER_TYPE = getRenderType(TANK_TEXTURE);

    private final ModelRenderer tankL;
    private final ModelRenderer tankR;
    private final ModelRenderer tankdock;
    private final ModelRenderer capL;
    private final ModelRenderer capR;
    private final ModelRenderer tankbridge;
    private final ModelRenderer tankpipelower;
    private final ModelRenderer tankpipeupper;
    private final ModelRenderer tankbackbrace;

    public ModelScubaTank() {
        super(RenderType::getEntitySolid);
        textureWidth = 128;
        textureHeight = 64;

        tankL = new ModelRenderer(this, 23, 54);
        tankL.addBox(-1F, 2F, 4F, 3, 7, 3, false);
        tankL.setRotationPoint(0F, 0F, 0F);
        tankL.setTextureSize(128, 64);
        tankL.mirror = true;
        setRotation(tankL, -0.2443461F, 0.5235988F, 0F);
        tankR = new ModelRenderer(this, 23, 54);
        tankR.addBox(-2F, 2F, 4F, 3, 7, 3, false);
        tankR.setRotationPoint(0F, 0F, 0F);
        tankR.setTextureSize(128, 64);
        tankR.mirror = true;
        setRotation(tankR, -0.2443461F, -0.5235988F, 0F);
        tankR.mirror = false;
        tankdock = new ModelRenderer(this, 0, 55);
        tankdock.addBox(-2F, 5F, 1F, 4, 4, 5, false);
        tankdock.setRotationPoint(0F, 0F, 0F);
        tankdock.setTextureSize(128, 64);
        tankdock.mirror = true;
        setRotation(tankdock, 0F, 0F, 0F);
        capL = new ModelRenderer(this, 23, 51);
        capL.addBox(-0.5F, 1F, 4.5F, 2, 1, 2, false);
        capL.setRotationPoint(0F, 0F, 0F);
        capL.setTextureSize(128, 64);
        capL.mirror = true;
        setRotation(capL, -0.2443461F, 0.5235988F, 0F);
        capR = new ModelRenderer(this, 23, 51);
        capR.addBox(-1.5F, 1F, 4.5F, 2, 1, 2, false);
        capR.setRotationPoint(0F, 0F, 0F);
        capR.setTextureSize(128, 64);
        capR.mirror = true;
        setRotation(capR, -0.2443461F, -0.5235988F, 0F);
        tankbridge = new ModelRenderer(this, 0, 47);
        tankbridge.addBox(-1F, 3F, -1.5F, 2, 5, 3, false);
        tankbridge.setRotationPoint(0F, 0F, 0F);
        tankbridge.setTextureSize(128, 64);
        tankbridge.mirror = true;
        setRotation(tankbridge, 0.5934119F, 0F, 0F);
        tankpipelower = new ModelRenderer(this, 0, 37);
        tankpipelower.addBox(-0.5F, 2F, 3F, 1, 4, 1, false);
        tankpipelower.setRotationPoint(0F, 0F, 0F);
        tankpipelower.setTextureSize(128, 64);
        tankpipelower.mirror = true;
        setRotation(tankpipelower, 0.2094395F, 0F, 0F);
        tankpipeupper = new ModelRenderer(this, 4, 38);
        tankpipeupper.addBox(-0.5F, 1F, 1.5F, 1, 1, 3, false);
        tankpipeupper.setRotationPoint(0F, 0F, 0F);
        tankpipeupper.setTextureSize(128, 64);
        tankpipeupper.mirror = true;
        setRotation(tankpipeupper, 0F, 0F, 0F);
        tankbackbrace = new ModelRenderer(this, 0, 42);
        tankbackbrace.addBox(-3F, 2F, 0.5F, 6, 3, 2, false);
        tankbackbrace.setRotationPoint(0F, 0F, 0F);
        tankbackbrace.setTextureSize(128, 64);
        tankbackbrace.mirror = true;
        setRotation(tankbackbrace, 0.2443461F, 0F, 0F);
    }

    public void render(@Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight, boolean hasEffect) {
        render(matrix, getVertexBuilder(renderer, RENDER_TYPE, hasEffect), light, overlayLight, 1, 1, 1, 1);
    }

    @Override
    public void render(@Nonnull MatrixStack matrix, @Nonnull IVertexBuilder vertexBuilder, int light, int overlayLight, float red, float green, float blue, float alpha) {
        tankL.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        tankR.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        tankdock.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        capL.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        capR.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        tankbridge.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        tankpipelower.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        tankpipeupper.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        tankbackbrace.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
    }
}