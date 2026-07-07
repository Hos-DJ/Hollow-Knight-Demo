package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;

import java.util.ArrayList;

public interface BossState {
    void enter(FalseKnight boss, Knight knight);
    void update (FalseKnight boss, Knight knight, ArrayList<Block> blocks , float delta);
    void exit(FalseKnight boss);
}
