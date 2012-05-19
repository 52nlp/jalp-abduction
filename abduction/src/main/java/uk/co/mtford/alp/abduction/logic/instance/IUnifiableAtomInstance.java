package uk.co.mtford.alp.abduction.logic.instance;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mtford
 * Date: 18/05/2012
 * Time: 10:09
 * To change this template use File | Settings | File Templates.
 */
public interface IUnifiableAtomInstance extends IAtomInstance {
    public List<IEqualitySolverResult> equalitySolve(VariableInstance other,
                                                     Map<VariableInstance, IUnifiableAtomInstance> assignment);

    public List<IEqualitySolverResult> equalitySolve(ConstantInstance other,
                                                     Map<VariableInstance, IUnifiableAtomInstance> assignment);

    public List<IEqualitySolverResult> equalitySolve(PredicateInstance other,
                                                     Map<VariableInstance, IUnifiableAtomInstance> assignment);
}