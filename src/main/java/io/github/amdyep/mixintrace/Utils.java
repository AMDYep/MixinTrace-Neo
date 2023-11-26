package io.github.amdyep.mixintrace;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {
    @SuppressWarnings("unchecked")
    public static void printTrace(StackTraceElement[] stackTrace, StringBuilder builder) {
        if (stackTrace == null || stackTrace.length == 0) return;
        builder.append("\nMixins in Heaven:");
        try {
            List<String> classNames = new ArrayList<>();
            for (StackTraceElement el : stackTrace) {
                if (!classNames.contains(el.getClassName())) classNames.add(el.getClassName());
            }
            boolean found = false;
            for (String className : classNames) {
                ClassInfo classInfo = ClassInfo.fromCache(className);
                if (classInfo != null) {
                    Object mixinInfoSetObject = null;
                    try {
                        Method getMixins = ClassInfo.class.getDeclaredMethod("getMixins");
                        getMixins.setAccessible(true);
                        mixinInfoSetObject = getMixins.invoke(classInfo);
                    } catch (Exception ignored) {

                    }
                    if (mixinInfoSetObject == null) {
                        try {
                            Field mixinsField = ClassInfo.class.getDeclaredField("mixins");
                            mixinsField.setAccessible(true);
                            mixinInfoSetObject = mixinsField.get(classInfo);
                        } catch (Exception ignored) {

                        }
                    }
                    if (mixinInfoSetObject == null) continue;
                    Set<IMixinInfo> mixinInfoSet = (Set<IMixinInfo>) mixinInfoSetObject;
                    if (!mixinInfoSet.isEmpty()) {
                        builder.append("\n\t");
                        builder.append(className);
                        builder.append(":");
                        for (IMixinInfo info : mixinInfoSet) {
                            builder.append("\n\t\t");
                            builder.append(info.getClassName());
                            builder.append(" (");
                            builder.append(info.getConfig().getName());
                            builder.append(")");
                        }
                        found = true;
                    }
                }
            }
            if (!found) builder.append(" None found");
        } catch (Exception e) {
            builder.append(" Failed to find Mixin metadata: ").append(e);
        }
    }
}
