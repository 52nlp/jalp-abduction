package uk.co.mtford.jalp.abduction.logic.instance.constraints;

import choco.kernel.model.constraints.Constraint;
import uk.co.mtford.jalp.abduction.logic.instance.IUnifiableAtomInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.VariableInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mtford
 * Date: 29/05/2012
 * Time: 09:44
 * To change this template use File | Settings | File Templates.
 */
public interface IConstraintSolverFacade {
    public List<Map<VariableInstance,IUnifiableAtomInstance>> execute(Map<VariableInstance,IUnifiableAtomInstance> subst, List<IConstraintInstance> listConstraints);
}
