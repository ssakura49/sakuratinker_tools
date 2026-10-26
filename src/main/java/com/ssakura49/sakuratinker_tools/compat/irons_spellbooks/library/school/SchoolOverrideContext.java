package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.school;

import io.redspace.ironsspellbooks.api.spells.SchoolType;

import java.util.ArrayDeque;
import java.util.Deque;


public final class SchoolOverrideContext {
    private static final ThreadLocal<Deque<SchoolType>> STACK = ThreadLocal.withInitial(ArrayDeque::new);

    private SchoolOverrideContext() {
    }

    public static void push(SchoolType school) {
        if (school != null) {
            STACK.get().push(school);
        }
    }

    public static void pop() {
        Deque<SchoolType> stack = STACK.get();
        stack.poll();
        if (stack.isEmpty()) {
            STACK.remove();
        }
    }

    public static void clear() {
        STACK.remove();
    }

    public static SchoolType peek() {
        return STACK.get().peek();
    }

    public static boolean isActive() {
        return !STACK.get().isEmpty();
    }
}
