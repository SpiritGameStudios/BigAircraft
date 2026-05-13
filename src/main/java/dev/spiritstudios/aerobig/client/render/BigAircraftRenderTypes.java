package dev.spiritstudios.aerobig.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class BigAircraftRenderTypes {
    public static final RenderStateShard.TransparencyStateShard INVERT = new RenderStateShard.TransparencyStateShard(
            BigAircraft.MOD_ID + ":invert",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(
                        GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                        GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ZERO
                );
            },
            () -> {
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
            }
    );

    public static final RenderType GUI_INVERT = RenderType.create(
            BigAircraft.MOD_ID + ":gui_invert",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            786432,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_GUI_SHADER)
                    .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                    .setTransparencyState(INVERT)
                    .createCompositeState(false)
    );

    public static final Function<ResourceLocation, RenderType> NUMBER_INVERT = Util.memoize(
            texture -> RenderType.create(
                    BigAircraft.MOD_ID + ":number_invert",
                    DefaultVertexFormat.POSITION_TEX,
                    VertexFormat.Mode.QUADS,
                    786432,
                    RenderType.CompositeState.builder()
                            .setShaderState(RenderType.POSITION_TEX_SHADER)
                            .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                            .setTransparencyState(INVERT)
                            .createCompositeState(false)
            )
    );
}
