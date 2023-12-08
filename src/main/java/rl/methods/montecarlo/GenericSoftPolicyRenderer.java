package rl.methods.montecarlo;

import com.google.common.collect.HashBasedTable;
import rl.base.RLAction;
import rl.base.RLState;
import rl.base.SoftPolicy;

public class GenericSoftPolicyRenderer implements RLSoftPolicyRenderer {
    @Override
    public void renderPolicy(SoftPolicy policy) {
        HashBasedTable<RLState, RLAction,Double> actionProbs= policy.getActionProbs();
        for (RLState s: actionProbs.rowKeySet())
        {
            for (RLAction a: actionProbs.row(s).keySet())
            {
                System.out.println(s + "- " + a + " : " + actionProbs.get(s,a));
            }
        }
    }
}
