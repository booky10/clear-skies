/*******************************************************************************
 * Copyright 2019 grondag
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package me.grondag.clearskies.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @ModifyVariable(method = "setupColor", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"), ordinal = 2)
    private static Vec3 onSampleColor(Vec3 value) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;

        if (world != null && world.dimensionType().hasSkyLight()) {
            return world.getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), minecraft.getFrameTime());
        } else {
            return value;
        }
    }

    @ModifyVariable(method = "setupColor", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/math/Vector3f;dot(Lcom/mojang/math/Vector3f;)F"), ordinal = 7)
    private static float afterPlaneDot(float dotProduct) {
        return 0;
    }

    @Redirect(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"))
    private static float onGetRainLevel(ClientLevel world, float tickDelta) {
        return 0;
    }

    @Redirect(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"))
    private static float onGetThunderLevel(ClientLevel world, float tickDelta) {
        return 0;
    }
}
