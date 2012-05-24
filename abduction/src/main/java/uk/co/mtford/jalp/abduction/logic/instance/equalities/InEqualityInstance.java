package uk.co.mtford.jalp.abduction.logic.instance.equalities;

import uk.co.mtford.jalp.abduction.AbductiveFramework;
import uk.co.mtford.jalp.abduction.logic.instance.*;
import uk.co.mtford.jalp.abduction.rules.RuleNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: mtford
 * Date: 21/05/2012
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class InEqualityInstance implements IEqualityInstance {

    private EqualityInstance equalityInstance;

    public InEqualityInstance(EqualityInstance equalityInstance) {
        this.equalityInstance = equalityInstance;
    }

    @Override
    public String toString() {
        return equalityInstance.getLeft()+"=\\="+equalityInstance.getRight();
    }

    @Override
    public RuleNode getPositiveRootRuleNode(AbductiveFramework abductiveFramework, List<IInferableInstance> goals) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public RuleNode getNegativeRootRuleNode(AbductiveFramework abductiveFramework, List<DenialInstance> nestedDenials, List<IInferableInstance> goals) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public IFirstOrderLogicInstance performSubstitutions(Map<VariableInstance, IUnifiableAtomInstance> substitutions) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public IFirstOrderLogicInstance deepClone(Map<VariableInstance, IUnifiableAtomInstance> substitutions) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public IFirstOrderLogicInstance shallowClone() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public Set<VariableInstance> getVariables() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public boolean equalitySolve(Map<VariableInstance, IUnifiableAtomInstance> equalitySolverAssignments) {
        return !equalityInstance.equalitySolve(new HashMap<VariableInstance, IUnifiableAtomInstance>(equalitySolverAssignments));
    }
}
