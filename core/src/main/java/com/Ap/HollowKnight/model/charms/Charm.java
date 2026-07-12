package com.Ap.HollowKnight.model.charms;

import com.Ap.HollowKnight.model.Knight.Knight;

public enum Charm {

    SOUL_CATCHER {
        @Override
        public void equipEffect(Knight knight) {
            int multiplier = knight.getSOUL_PER_HIT_MULTIPLIER();
            int base = knight.getSOUL_PER_HIT();
            knight.setCurrentSoulPerHit(base * multiplier);
        }

        @Override
        public void unequipEffect(Knight knight) {
            knight.setCurrentSoulPerHit(knight.getSOUL_PER_HIT());
        }
    },

    DASH_MASTER {
        @Override
        public void equipEffect(Knight knight) {
            float multiplier = knight.getDASH_COOLDOWN_MULTIPLIER();
            float base = knight.getDASH_COOLDOWN();
            knight.setCurrentDashCooldown(base * multiplier);
        }

        @Override
        public void unequipEffect(Knight knight) {
            knight.setCurrentDashCooldown(knight.getDASH_COOLDOWN());
        }
    },

    UNBREAKABLE_STRENGTH {
        @Override
        public void equipEffect(Knight knight) {
            int multiplier = knight.getDAMAGE_MULTIPLIER();
            int base = knight.getNAIL_DAMAGE();
            knight.setCurrentNailDamage(base * multiplier);
        }

        @Override
        public void unequipEffect(Knight knight) {
            knight.setCurrentNailDamage(knight.getNAIL_DAMAGE());
        }
    },

    QUICK_SLASH {
        @Override
        public void equipEffect(Knight knight) {
            float multiplier = knight.getATTACK_COOLDOWN_MULTIPLIER();
            float base = knight.getATTACK_COOLDOWN();
            knight.setCurrentAttackCooldown(base * multiplier);
        }

        @Override
        public void unequipEffect(Knight knight) {
            knight.setCurrentAttackCooldown(knight.getATTACK_COOLDOWN());
        }
    },

    QUICK_FOCUS {
        @Override
        public void equipEffect(Knight knight) {
            float multiplier = knight.getFOCUS_DURATION_MULTIPLIER();
            float base = knight.getFOCUS_DURATION();
            knight.setCurrentFocusDuration(base * multiplier);
        }

        @Override
        public void unequipEffect(Knight knight) {
            knight.setCurrentFocusDuration(knight.getFOCUS_DURATION());
        }
    },

    HEAVY_BLOW {
        @Override
        public void equipEffect(Knight knight) {
            float multiplier = knight.getKNOCKBACK_DAMAGE_MULTIPLIER();
            float base = knight.getDAMAGE_KNOCKBACK_SPEED();
            knight.setCurrentKnockBack(base * multiplier);
        }

        @Override
        public void unequipEffect(Knight knight) {
            knight.setCurrentKnockBack(knight.getDAMAGE_KNOCKBACK_SPEED());
        }
    },

    SHARP_SHADOW {
        @Override
        public void equipEffect(Knight knight) {
            float multiplier = knight.getDASH_SPEED_MULTIPLIER();
            float base = knight.getDASH_SPEED();
            knight.setCurrentDashSpeed(base * multiplier);
            knight.setHasSharpShadow(true);
        }

        @Override
        public void unequipEffect(Knight knight) {
            knight.setCurrentDashSpeed(knight.getDASH_SPEED());
            knight.setHasSharpShadow(false);
        }
    },

    VOID_HEART {
        @Override
        public void equipEffect(Knight knight) {
            float multiplier =  knight.getSPELL_DAMAGE_MULTIPLIER();
            int base = knight.getBASE_SPELL_DAMAGE();
            int finalAmount = (int) multiplier * base;
            knight.setCurrentSpellDamage(finalAmount);
            knight.setHasVoidHeart(true);
        }

        @Override
        public void unequipEffect(Knight knight) {
            knight.setCurrentSpellDamage(knight.getBASE_SPELL_DAMAGE());
            knight.setHasVoidHeart(false);
        }
    };

    public abstract void equipEffect(Knight knight);
    public abstract void unequipEffect(Knight knight);
}
