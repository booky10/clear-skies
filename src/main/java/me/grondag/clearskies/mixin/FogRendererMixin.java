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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.CubicSampler;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @WrapOperation(
            method = "setupColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private static Vec3 onSampleColor(
            Vec3 vec3, CubicSampler.Vec3Fetcher vec3Fetcher, Operation<Vec3> original,
            @Local(ordinal = 0) Vec3 skyColor, @Local(argsOnly = true) ClientLevel level
    ) {
        if (level.dimensionType().hasSkyLight()) {
            return skyColor;
        } else {
            return original.call(vec3, vec3Fetcher);
        }
    }

    @WrapOperation(
            method = "setupColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3f;dot(Lorg/joml/Vector3fc;)F",
                    remap = false
            )
    )
    private static float afterPlaneDot(Vector3f instance, Vector3fc v, Operation<Float> original) {
        return 0f;
    }

    @WrapOperation(
            method = "setupColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"
            )
    )
    private static float onGetRainLevel(ClientLevel instance, float v, Operation<Float> original) {
        return 0f;
    }

    @WrapOperation(
            method = "setupColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"
            )
    )
    private static float onGetThunderLevel(ClientLevel instance, float v, Operation<Float> original) {
        return 0f;
    }
}
