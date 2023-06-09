package server.campaign.operations;

import java.util.TreeMap;
import server.campaign.CampaignMain;

/**
 * PayoutModifier takes the base payouts and modifies them by ELO, based on 
 * SO-configurable settings
 */
public class PayoutModifier {

    public TreeMap<String, Integer> calculate(String currName, ShortOperation so, int earnedMoney, int earnedRP, int earnedXP, int earnedFlu) {
        TreeMap<String, Integer> payout = new TreeMap<String, Integer>();
        payout.put("earnedMoney", earnedMoney);
        payout.put("earnedXP", earnedXP);
        payout.put("earnedFlu", earnedFlu);
        payout.put("earnedRP", earnedRP);
        if (CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO") && so.getAllPlayerNames().size() <= 2) {
            Double myRating;
            Double hisRating;
            if (so.getWinners().containsKey(currName)) {
                myRating = so.getWinners().get(currName).getRating();
                hisRating = so.getLosers().get(so.getLosers().keySet().iterator().next()).getRating();
            } else {
                myRating = so.getLosers().get(currName).getRating();
                hisRating = so.getWinners().get(so.getWinners().keySet().iterator().next()).getRating();
            }
            if (CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_Exp")) {
                earnedMoney = modifyMoney(earnedMoney, myRating, hisRating);
            }
            if (CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_Exp")) {
                earnedXP = modifyExperience(earnedXP, myRating, hisRating);
            }
            if (CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_Influence")) {
                earnedFlu = modifyInfluence(earnedFlu, myRating, hisRating);
            }
            if (CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_RP")) {
                earnedRP = modifyRP(earnedRP, myRating, hisRating);
            }
        }
        payout.put("earnedMoney", earnedMoney);
        payout.put("earnedXP", earnedXP);
        payout.put("earnedFlu", earnedFlu);
        payout.put("earnedRP", earnedRP);
        return payout;
    }

    private int modifyMoney(int earnedMoney, Double myRating, Double hisRating) {
        boolean modifyHigher = CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_Money_Higher");
        boolean modifyLower = CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_Money_Lower");
        Double maxELO = CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Money_MaxELO");
        Double minELO = CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Money_MinELO");
        myRating = Math.max(myRating, minELO);
        myRating = Math.min(myRating, maxELO);
        hisRating = Math.max(hisRating, minELO);
        hisRating = Math.min(hisRating, maxELO);
        Double ratingMultiplier = hisRating / myRating;
        boolean isHigher = ratingMultiplier < 1.0;
        if ((isHigher && !modifyHigher) || (!isHigher && !modifyLower)) {
            return earnedMoney;
        }
        earnedMoney = (int) Math.floor((earnedMoney * (Math.pow(ratingMultiplier, CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Multiplier"))) + 0.5));
        return earnedMoney;
    }

    private int modifyExperience(int earnedXP, Double myRating, Double hisRating) {
        boolean modifyHigher = CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_Exp_Higher");
        boolean modifyLower = CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_Exp_Lower");
        Double maxELO = CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Exp_MaxELO");
        Double minELO = CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Exp_MinELO");
        myRating = Math.max(myRating, minELO);
        myRating = Math.min(myRating, maxELO);
        hisRating = Math.max(hisRating, minELO);
        hisRating = Math.min(hisRating, maxELO);
        Double ratingMultiplier = hisRating / myRating;
        boolean isHigher = ratingMultiplier < 1.0;
        if ((isHigher && !modifyHigher) || (!isHigher && !modifyLower)) {
            return earnedXP;
        }
        earnedXP = (int) Math.floor((earnedXP * (Math.pow(ratingMultiplier, CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Multiplier"))) + 0.5));
        return earnedXP;
    }

    private int modifyInfluence(int earnedFlu, Double myRating, Double hisRating) {
        boolean modifyHigher = CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_Influence_Higher");
        boolean modifyLower = CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_Influence_Lower");
        Double maxELO = CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Influence_MaxELO");
        Double minELO = CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Influence_MinELO");
        myRating = Math.max(myRating, minELO);
        myRating = Math.min(myRating, maxELO);
        hisRating = Math.max(hisRating, minELO);
        hisRating = Math.min(hisRating, maxELO);
        Double ratingMultiplier = hisRating / myRating;
        boolean isHigher = ratingMultiplier < 1.0;
        if ((isHigher && !modifyHigher) || (!isHigher && !modifyLower)) {
            return earnedFlu;
        }
        earnedFlu = (int) Math.floor((earnedFlu * (Math.pow(ratingMultiplier, CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Multiplier"))) + 0.5));
        return earnedFlu;
    }

    private int modifyRP(int earnedRP, Double myRating, Double hisRating) {
        boolean modifyHigher = CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_RP_Higher");
        boolean modifyLower = CampaignMain.cm.getBooleanConfig("ModifyOpPayoutByELO_RP_Lower");
        Double maxELO = CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_RP_MaxELO");
        Double minELO = CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_RP_MinELO");
        myRating = Math.max(myRating, minELO);
        myRating = Math.min(myRating, maxELO);
        hisRating = Math.max(hisRating, minELO);
        hisRating = Math.min(hisRating, maxELO);
        Double ratingMultiplier = hisRating / myRating;
        boolean isHigher = ratingMultiplier < 1.0;
        if ((isHigher && !modifyHigher) || (!isHigher && !modifyLower)) {
            return earnedRP;
        }
        earnedRP = (int) Math.floor((earnedRP * (Math.pow(ratingMultiplier, CampaignMain.cm.getDoubleConfig("ModifyOpPayoutByELO_Multiplier"))) + 0.5));
        return earnedRP;
    }

    public PayoutModifier() {
    }
}
