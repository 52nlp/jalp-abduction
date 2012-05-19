package uk.co.mtford.alp.abduction.logic.instance;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: mtford
 * Date: 19/05/2012
 * Time: 12:45
 * To change this template use File | Settings | File Templates.
 */
public interface IFirstOrderLogic {

    /**
     * Returns a clone of the formula, performing all substitutions specified by the mapping.
     *
     * @param substitutions
     * @return
     */
    public IFirstOrderLogic performSubstitutions(Map<VariableInstance, IUnifiableAtomInstance> substitutions);

    /**
     * Returns a list of all variables nested in this first order logic formula.
     *
     * @return
     */
    public Set<VariableInstance> getVariables();
}
