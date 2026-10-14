package com.ssakura49.sakuratinker_tools.util.asm;

import com.ssakura49.sakura_library.coremod.forge.IClassProcessor;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService.Phase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class FantasyEndingInscriptionCompatProcessor implements IClassProcessor {
    public static final FantasyEndingInscriptionCompatProcessor INSTANCE = new FantasyEndingInscriptionCompatProcessor();
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String TARGET_CLASS = "com/mega/uom/mixin/irons_spellbook/InscriptionTableScreenMixin";
    private static final String MODIFY_CONSTANT_ANN = "Lorg/spongepowered/asm/mixin/injection/ModifyConstant;";
    private static final String CONSTANT_ANN = "Lorg/spongepowered/asm/mixin/injection/Constant;";

    @Override
    public void processClass(Phase phase, ClassNode classNode, Type classType, AtomicBoolean shouldWrite) {
        if (phase != Phase.BEFORE) {
            return;
        }
        if (!TARGET_CLASS.equals(classNode.name)) {
            return;
        }
        try {
            boolean changed = false;
            for (MethodNode method : classNode.methods) {
                if (!"modifyMaxSpells".equals(method.name) || !"(I)I".equals(method.desc)) {
                    continue;
                }
                if (appendMaxConstantMatch(method)) {
                    changed = true;
                }
                if (allowZeroSucceeded(method)) {
                    changed = true;
                }
                if (replaceWithConditionalReturn(method)) {
                    changed = true;
                }
            }
            if (changed) {
                shouldWrite.set(true);
                LOGGER.info("[SakuraTinkerTools] FantasyEnding InscriptionTableScreenMixin 已修补: 常量注入与条件返回");
            }
        } catch (Throwable t) {
            LOGGER.error("[SakuraTinkerTools] FantasyEndingInscriptionCompatProcessor 处理失败, 已跳过: {}", t.toString());
        }
    }

    private boolean appendMaxConstantMatch(MethodNode method) {
        List<AnnotationNode> anns = method.visibleAnnotations != null
                ? method.visibleAnnotations : method.invisibleAnnotations;
        if (anns == null) {
            return false;
        }
        for (AnnotationNode ann : anns) {
            if (!MODIFY_CONSTANT_ANN.equals(ann.desc) || ann.values == null) {
                continue;
            }
            for (int i = 0; i + 1 < ann.values.size(); i += 2) {
                if (!"constant".equals(ann.values.get(i))) {
                    continue;
                }
                Object constantsObj = ann.values.get(i + 1);
                if (!(constantsObj instanceof List<?> constants)) {
                    return false;
                }
                for (Object item : constants) {
                    if (item instanceof AnnotationNode) {
                        AnnotationNode c = (AnnotationNode) item;
                        if (CONSTANT_ANN.equals(c.desc) && c.values != null
                                && c.values.size() >= 2 && "intValue".equals(c.values.get(0))
                                && Integer.MAX_VALUE == (Integer) c.values.get(1)) {
                            return false; // 已存在，幂等
                        }
                    }
                }
                AnnotationNode maxConst = new AnnotationNode(CONSTANT_ANN);
                maxConst.values = new java.util.ArrayList<Object>(4);
                maxConst.values.add("intValue");
                maxConst.values.add(Integer.MAX_VALUE);
                ((List<AnnotationNode>) constants).add(maxConst);
                return true;
            }
        }
        return false;
    }


    private boolean allowZeroSucceeded(MethodNode method) {
        List<AnnotationNode> anns = method.visibleAnnotations != null
                ? method.visibleAnnotations : method.invisibleAnnotations;
        if (anns == null) {
            return false;
        }
        boolean changed = false;
        for (AnnotationNode ann : anns) {
            if (!MODIFY_CONSTANT_ANN.equals(ann.desc) || ann.values == null) {
                continue;
            }
            boolean foundRequire = false;
            for (int i = 0; i + 1 < ann.values.size(); i += 2) {
                if ("require".equals(ann.values.get(i))) {
                    ann.values.set(i + 1, 0);
                    foundRequire = true;
                    changed = true;
                }
            }
            if (!foundRequire) {
                ann.values.add("require");
                ann.values.add(0);
                changed = true;
            }
        }
        return changed;
    }

    private boolean replaceWithConditionalReturn(MethodNode method) {
        if ("(I)I".equals(method.desc) && method.instructions != null) {
            InsnList list = new InsnList();
            LabelNode elseLabel = new LabelNode();
            list.add(new VarInsnNode(Opcodes.ILOAD, 1));
            list.add(new LdcInsnNode(Integer.MAX_VALUE));
            list.add(new JumpInsnNode(Opcodes.IF_ICMPNE, elseLabel));
            list.add(new VarInsnNode(Opcodes.ILOAD, 1));
            list.add(new InsnNode(Opcodes.IRETURN));
            list.add(elseLabel);
            list.add(new IntInsnNode(Opcodes.BIPUSH, 20));
            list.add(new InsnNode(Opcodes.IRETURN));
            method.instructions = list;
            method.maxLocals = 2;
            method.maxStack = 2;
            return true;
        }
        return false;
    }
}