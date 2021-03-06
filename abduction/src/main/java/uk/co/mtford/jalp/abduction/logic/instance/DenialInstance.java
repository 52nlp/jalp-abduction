/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.mtford.jalp.abduction.logic.instance;

import org.apache.log4j.Logger;
import uk.co.mtford.jalp.abduction.AbductiveFramework;
import uk.co.mtford.jalp.abduction.logic.instance.term.VariableInstance;
import uk.co.mtford.jalp.abduction.rules.RuleNode;

import java.util.*;

/**
 * Represents a denial of the form ic :- ...
 *
 * @author mtford
 */
public class DenialInstance implements IInferableInstance, IFirstOrderLogicInstance {

    private static final Logger LOGGER = Logger.getLogger(DenialInstance.class);

    private List<IInferableInstance> body;
    private Set<VariableInstance> universalVariables;

    public DenialInstance(List<IInferableInstance> body,
                          List<VariableInstance> universalVariables) {
        this.body = body;
        this.universalVariables = new HashSet<VariableInstance>(universalVariables);
    }

    public DenialInstance(List<IInferableInstance> body
    ) {
        this.body = body;
        this.universalVariables = new HashSet<VariableInstance>();
    }

    public DenialInstance(List<VariableInstance> universalVariables, IInferableInstance... body) {
        this.universalVariables = new HashSet<VariableInstance>(universalVariables);
        this.body = new LinkedList<IInferableInstance>(Arrays.asList(body));
    }

    public DenialInstance(IInferableInstance... body) {
        universalVariables = new HashSet<VariableInstance>();
        this.body = new LinkedList<IInferableInstance>(Arrays.asList(body));
    }

    public DenialInstance() {
        body = new LinkedList<IInferableInstance>();
        universalVariables = new HashSet<VariableInstance>();
    }

    public List<IInferableInstance> getBody() {
        return body;
    }

    public void setBody(List<IInferableInstance> body) {
        this.body = body;
    }

    public Set<VariableInstance> getUniversalVariables() {
        return universalVariables;
    }

    public void setUniversalVariables(List<VariableInstance> universalVariables) {
        this.universalVariables = new HashSet<VariableInstance>(universalVariables);
    }

    @Override
    public String toString() {
        String rep = "ic";
        if (!universalVariables.isEmpty()) {
           rep+="(";
           for (VariableInstance v:universalVariables) rep+=v+",";
           rep = rep.substring(0,rep.length()-1);
           rep+=")";
       }
        rep += " :- ";
        String bodyRep = body.toString();
        bodyRep = bodyRep.substring(1, bodyRep.length() - 1);
        rep += bodyRep + ".";
        return rep;
    }

    public DenialInstance shallowClone() {
        LinkedList<IInferableInstance> newBody = new LinkedList<IInferableInstance>();
        for (IInferableInstance inferable:body){
            newBody.add((IInferableInstance) inferable.shallowClone());
        }
        return new DenialInstance(newBody,
                new LinkedList<VariableInstance>(universalVariables));
    }

    public RuleNode getPositiveRootRuleNode(AbductiveFramework abductiveFramework, List<IInferableInstance> query, List<IInferableInstance> goals) {
        if (this.getBody().size()==0) {
            FalseInstance f = new FalseInstance();
            RuleNode n = f.getPositiveRootRuleNode(abductiveFramework,query,goals);
            goals.remove(0);
            goals.add(0,f);
            return n;
        }
        return this.getBody().get(0).getNegativeRootRuleNode(abductiveFramework, query, goals);
    }

    public RuleNode getNegativeRootRuleNode(AbductiveFramework abductiveFramework, List<IInferableInstance> query, List<IInferableInstance> goals) {
        throw new UnsupportedOperationException(); // TODO: Nested denials...?
    }

    public IFirstOrderLogicInstance performSubstitutions(Map<VariableInstance, IUnifiableInstance> substitutions) {
        // Substitute universal variables.
        LinkedList<IInferableInstance> newBody = new LinkedList<IInferableInstance>();

        for (VariableInstance key:substitutions.keySet()) {
            if (universalVariables.contains(key)) {
                universalVariables.remove(key);
            }
        }

        for (IInferableInstance inferable : body) {
            newBody.add((IInferableInstance) inferable.performSubstitutions(substitutions));
        }
        body = newBody;
        return this;
    }

    public IFirstOrderLogicInstance deepClone(Map<VariableInstance, IUnifiableInstance> substitutions) {
        // Substitute universal variables.
        LinkedList<IInferableInstance> newBody = new LinkedList<IInferableInstance>();
        LinkedList<VariableInstance> newUniversalVariables = new LinkedList<VariableInstance>();

        for (VariableInstance v:universalVariables) {
            v.deepClone(substitutions);
            IUnifiableInstance newV = v;
            while (substitutions.containsKey(newV)) newV = substitutions.get(newV);
            if (newV instanceof VariableInstance) newUniversalVariables.add((VariableInstance)newV);
        }

        Map<VariableInstance, IUnifiableInstance> tempSubst = new HashMap<VariableInstance,IUnifiableInstance>(substitutions);

        for (IInferableInstance inferable : body) {
            newBody.add((IInferableInstance) inferable.deepClone(tempSubst));
        }

        return new DenialInstance(newBody, newUniversalVariables);
    }


    public Set<VariableInstance> getVariables() {
        HashSet<VariableInstance> variables = new HashSet<VariableInstance>();
        for (IInferableInstance inferable : body) {
            variables.addAll(inferable.getVariables());
        }
        return variables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DenialInstance)) return false;

        DenialInstance that = (DenialInstance) o;

        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (universalVariables != null ? !universalVariables.equals(that.universalVariables) : that.universalVariables != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = body != null ? body.hashCode() : 0;
        result = 31 * result + (universalVariables != null ? universalVariables.hashCode() : 0);
        return result;
    }
}
