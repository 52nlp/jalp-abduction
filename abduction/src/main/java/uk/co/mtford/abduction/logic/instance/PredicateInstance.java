/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.mtford.abduction.logic.instance;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.co.mtford.abduction.AbductiveFramework;
import uk.co.mtford.abduction.asystem.ASystemState;
import uk.co.mtford.abduction.asystem.EqualityInstance;
import uk.co.mtford.abduction.asystem.RuleUnfoldException;
import uk.co.mtford.unification.CouldNotUnifyException;
import uk.co.mtford.unification.Unifier;

/**
 *
 * @author mtford
 */
public class PredicateInstance implements ILiteralInstance, IAtomInstance {
    
    private static final Logger LOGGER = Logger.getLogger(PredicateInstance.class);
    
    protected String name;
    protected IAtomInstance[] parameters;
    
    public PredicateInstance(String name, IAtomInstance ... parameters) {
        this.name=name;
        this.parameters=parameters;
    }
    
     public PredicateInstance(String name, List<IAtomInstance> parameters) {
        this.name=name;
        this.parameters=parameters.toArray(new IAtomInstance[1]);
    }
    
    
    public PredicateInstance(String name, String varName, IAtomInstance varValue) {
        this.name=name;
        this.parameters=new IAtomInstance[1];
        parameters[0] = new VariableInstance(varName,varValue);
    }
    
    public PredicateInstance(String name, String varName) {
        this.name=name;
        this.parameters=new IAtomInstance[1];
        parameters[0] = new VariableInstance(varName);
    }
    
    public PredicateInstance(String name) {
        this.name = name;
        this.parameters=null;
    }

    public IAtomInstance[] getParameters() {
        return parameters;
    }

    public void setParameters(IAtomInstance[] parameters) {
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Returns true if same name and same num parameters.
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PredicateInstance other = (PredicateInstance) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.parameters.length!=other.parameters.length) {
            return false;
        }
        return true;
    }
    
    /** Returns true if same name and parameters are the same.
     * 
     * @param obj
     * @return 
     */
    
    public boolean deepEquals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PredicateInstance other = (PredicateInstance) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.parameters.length!=other.parameters.length) {
            return false;
        }
        for (int i=0;i<parameters.length;i++) {
            if (!parameters[i].deepEquals(other.parameters[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean contains(IAtomInstance parameter) {
        for (int i=0;i<parameters.length;i++) {
            if (parameters[i].equals(parameter)) return true;
        }
        return false;
    }
    
    public boolean replaceParameter(int num, VariableInstance parameter) {
        if (num<0||num>parameters.length) {
            return false;
        }
        parameters[num] = parameter;
        return true;
    }
    
    public boolean replaceParameter(VariableInstance toBeReplaced, VariableInstance toReplace) {
        for (int i=0; i<parameters.length; i++) {
            if (parameters[i].equals(toBeReplaced)) {
                parameters[i] = toReplace;
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 43 * hash + Arrays.deepHashCode(this.parameters);
        return hash;
    }
    
    public int getNumParams() {
        return parameters.length;
    }

    public void setParameters(VariableInstance[] params) {
        this.parameters=params;
    }
    
    public IAtomInstance getParameter(int i) {
        if (i>parameters.length||i<0) return null;
        return parameters[i];
    }
    
    @Override
    public String toString() {
        String paramList = "";
        for (IAtomInstance v : parameters) {
            paramList += v + ",";
        }
        paramList = paramList.substring(0, paramList.length() - 1);
        return name + "(" + paramList + ")";
    }

    @Override
    public Object clone() {
        String clonedName = new String(name);
        IAtomInstance[] clonedParams = new IAtomInstance[parameters.length];
        for (int i=0;i<clonedParams.length;i++) {
            clonedParams[i]=(IAtomInstance) parameters[i].clone();
        }
        return new PredicateInstance(clonedName,clonedParams);
    }

    public boolean equalitySolveAssign(IAtomInstance other) {
        try {
            Unifier.unify(this, other);
        } catch (CouldNotUnifyException ex) {
            return false;
        }
        return true;
    }
    
    public List<EqualityInstance> equalitySolve(IAtomInstance other) {
        List<EqualityInstance> equalityInstances = new LinkedList<EqualityInstance>();
        if (other.getClass().equals(this.getClass())) {
            PredicateInstance otherPredicate = (PredicateInstance)other;
            if (this.equals(otherPredicate)) {
                for (int i=0;i<parameters.length;i++) {
                    EqualityInstance equalityInstance = new EqualityInstance(parameters[i],otherPredicate.getParameter(i));
                    equalityInstances.add(equalityInstance);
                }
                return equalityInstances;
            }
            return null;
        }
        return null;
    }

    /** Implementation of D1 and A1.
     * 
     * @param framework
     * @param s
     * @return 
     */
    public List<ASystemState> applyInferenceRule(AbductiveFramework framework, ASystemState s) {
        LinkedList<ASystemState> possibleStates = new LinkedList<ASystemState>();
        if (framework.isAbducible(this)) { // A1
            
        }
        else { // D1
            List<List<ILiteralInstance>> possibleUnfolds = null;
            try {
                possibleUnfolds = framework.unfoldRule(this);
            } catch (RuleUnfoldException ex) {
                LOGGER.error(ex);
                System.exit(-1);
            }
            for (List<ILiteralInstance> unfold:possibleUnfolds) {
                ASystemState possibleState = (ASystemState) s.clone();
                possibleState.putGoals(unfold);
                possibleStates.add(possibleState);
            }
        }
        return possibleStates;
    }

    /** Implementation of D2 and A2.
     * 
     * @param framework
     * @param s
     * @return 
     */
    public List<ASystemState> applyDenialInferenceRule(AbductiveFramework framework, ASystemState s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object clone(Map<String, VariableInstance> variablesSoFar) {
        String clonedName = new String(name);
        IAtomInstance[] clonedParams = new IAtomInstance[parameters.length];
        for (int i=0;i<clonedParams.length;i++) {
            clonedParams[i]=(IAtomInstance) parameters[i].clone(variablesSoFar);
        }
        return new PredicateInstance(clonedName,clonedParams);
    }
    
}
