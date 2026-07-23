package com.ssakura49.sakuratinker_tools.compat.extrabotany.library.tool.stat;

import com.ssakura49.sakuratinker.SakuraTinker;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class EBToolStats {
    public static final FloatToolStat PHANTOM_AMOUNT = (FloatToolStat) ToolStats.register(new FloatToolStat(name("phantom_amount"), -3135232, 1.0F, 1.0F, 32.0F));

    private static ToolStatId name(String name) {
        return new ToolStatId(SakuraTinker.getResource(name));
    }
}
