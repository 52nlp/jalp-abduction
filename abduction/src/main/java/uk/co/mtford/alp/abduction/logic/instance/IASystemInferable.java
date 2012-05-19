/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.mtford.alp.abduction.logic.instance;

import uk.co.mtford.alp.abduction.AbductiveFramework;
import uk.co.mtford.alp.abduction.rules.RuleNode;

import java.util.List;

/**
 * @author mtford
 */
public interface IASystemInferable extends IFirstOrderLogic {
    /**
     * Takes an abductive framework and a list of goals that doesn't include this inferable and produces
     * a root rule node for use in producing an ASystem derivation in a positive mode of reasoning.
     *
     * @param abductiveFramework,goals
     * @return A root rule node
     */
    public RuleNode getPositiveRootRuleNode(AbductiveFramework abductiveFramework, List<IASystemInferable> goals);

    /**
     * Takes an abductive framework and a list of goals that includes this inferable and the denials that this
     * inferable came from which also includes this inferable and produces root rule node for use in
     * producing an ASystem derivation in a positive mode of reasoning.
     *
     * @param abductiveFramework,goals,denials
     *
     * @return A root rule node.
     */
    public RuleNode getNegativeRootRuleNode(AbductiveFramework abductiveFramework, List<DenialInstance> nestedDenials, List<IASystemInferable> goals);
}
