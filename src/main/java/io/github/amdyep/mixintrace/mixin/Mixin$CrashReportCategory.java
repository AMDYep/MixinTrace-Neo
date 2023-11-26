package io.github.amdyep.mixintrace.mixin;

import io.github.amdyep.mixintrace.Utils;
import net.minecraft.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReportCategory.class)
public abstract class Mixin$CrashReportCategory {
    @Shadow
    private StackTraceElement[] stackTrace;

    @Inject(method = "getDetails", at = @At("TAIL"))
    private void addTrace(StringBuilder builder, CallbackInfo ci) {
        Utils.printTrace(stackTrace, builder);
    }
}
