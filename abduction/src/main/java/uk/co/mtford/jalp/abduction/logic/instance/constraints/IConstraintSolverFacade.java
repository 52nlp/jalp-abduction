package uk.co.mtford.jalp.abduction.logic.instance.constraints;

import uk.co.mtford.jalp.abduction.logic.instance.IUnifiableInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.VariableInstance;

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
    public List<Map<VariableInstance,IUnifiableInstance>> execute(Map<VariableInstance,IUnifiableInstance> subst, List<IConstraintInstance> listConstraints);
}
