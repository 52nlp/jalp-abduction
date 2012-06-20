package uk.co.mtford.jalp.abduction;

import uk.co.mtford.jalp.abduction.logic.instance.DenialInstance;
import uk.co.mtford.jalp.abduction.logic.instance.IInferableInstance;
import uk.co.mtford.jalp.abduction.logic.instance.IUnifiableInstance;
import uk.co.mtford.jalp.abduction.logic.instance.PredicateInstance;
import uk.co.mtford.jalp.abduction.logic.instance.constraints.IConstraintInstance;
import uk.co.mtford.jalp.abduction.logic.instance.equalities.EqualityInstance;
import uk.co.mtford.jalp.abduction.logic.instance.equalities.InEqualityInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.VariableInstance;
import uk.co.mtford.jalp.abduction.rules.RuleNode;

import java.util.*;

/**
 *   Represents a single abductive explanation.
 *
**/
public class Result {
    private List<IInferableInstance> query;
    private RuleNode root;
    private Store store;
    private Map<VariableInstance, IUnifiableInstance> assignments;  // Theta

    public Result(Store store,Map<VariableInstance, IUnifiableInstance> assignments, List<IInferableInstance> query, RuleNode root) {
        this.assignments = assignments;
        this.store = store;
        this.query = query;
        this.root = root;
    }

    public List<IInferableInstance> getQuery() {
        return query;
    }

    public RuleNode getRoot() {
        return root;
    }

    public Store getStore() {
        return store;
    }

    public Map<VariableInstance, IUnifiableInstance> getAssignments() {
        return assignments;
    }

    public void setAssignments(Map<VariableInstance, IUnifiableInstance> assignments) {
        this.assignments = assignments;
    }

    public void setQuery(List<IInferableInstance> query) {
        this.query = query;
    }

    public void setRoot(RuleNode root) {
        this.root = root;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    /** Cycles through the store and assignments and removes any that do not include variables from
     *  relevantVariables.
     *
     * @param relevantVariables
     */
    public void reduce(Collection<VariableInstance> relevantVariables) {
        // Remove irrelevant assignments.
        Map<VariableInstance, IUnifiableInstance> newAssignments = new HashMap<VariableInstance, IUnifiableInstance>();
        for (VariableInstance v:relevantVariables) {
            IUnifiableInstance newValue = v;
            while (assignments.containsKey(newValue)) {
                newValue = assignments.get(newValue);
            }
            if (v!=newValue) {
                newAssignments.put(v,newValue);
            }
        }
        assignments = newAssignments;
        // Remove irrelevant denials
        List<DenialInstance> newDenials = new LinkedList<DenialInstance>();
        for (DenialInstance d:store.denials) {
         //   for (VariableInstance v:d.getVariables()) {
               // if (relevantVariables.contains(v)) {
                    if (!newDenials.contains(d)) { // Remove repeats.
                        newDenials.add(d);
                    }
             //   }
           // }
        }

        store.denials=newDenials;
        // Remove irrelevant equalities + repeat equalities.
        List<EqualityInstance> newEqualities = new LinkedList<EqualityInstance>();
        for (EqualityInstance e:store.equalities) {
            for (VariableInstance v:e.getVariables()) {
                if (relevantVariables.contains(v)) {
                    if (!newEqualities.contains(e)) {
                        newEqualities.add(e);
                    }
                    break;
                }
            }
        }
        store.equalities=newEqualities;
        // Remove irrelevant inEqualities + repeat inequalities.
        List<InEqualityInstance> newInEqualities = new LinkedList<InEqualityInstance>();
        for (InEqualityInstance e:store.inequalities) {
            for (VariableInstance v:e.getVariables()) {
                if (relevantVariables.contains(v)) {
                    if (!newInEqualities.contains(e)) {
                        newInEqualities.add(e);
                    }
                    break;
                }
            }
        }
        store.inequalities=newInEqualities;
        // Remove irrelevant constraints + repeat constraints
        List<IConstraintInstance> newConstraints = new LinkedList<IConstraintInstance>();
        for (IConstraintInstance e:store.constraints) {
            for (VariableInstance v:e.getVariables()) {
                if (relevantVariables.contains(v)) {
                    if (!newConstraints.contains(e)) {
                        newConstraints.add(e);
                    }
                    break;
                }
            }
        }
        store.constraints=newConstraints;
    }

    public String toString() {

        List epsilon = new LinkedList();

        for (InEqualityInstance e:store.inequalities) {
            epsilon.add(e);
        }

        for (EqualityInstance e:store.equalities) {
            epsilon.add(e);
        }

        boolean assignmentsEmpty = assignments.isEmpty();
        boolean abduciblesEmpty = store.abducibles.isEmpty();
        boolean constraintsEmpty = store.denials.isEmpty();
        boolean equalitiesEmpty = epsilon.isEmpty();
        boolean finiteDomainConstraintsEmpty = store.constraints.isEmpty();
        boolean allEmpty = assignmentsEmpty && constraintsEmpty && abduciblesEmpty && equalitiesEmpty &&
                           finiteDomainConstraintsEmpty;

        if (allEmpty) {
            return "Yes.";
        }
        else {
            String message = "";
            if (!assignmentsEmpty) {
                message+="Substitutions\n";
                for (VariableInstance v:assignments.keySet()) {
                    message+="  "+v+"/"+assignments.get(v)+"\n";
                }
            }
            if (!abduciblesEmpty) {
                message+="Abducibles\n";
                for (PredicateInstance p:store.abducibles) {
                    message+="  "+p+"\n";
                }
            }
            if (!constraintsEmpty) {
                message+="Denials\n";
                for (DenialInstance d:store.denials) {
                    message+="  "+d+"\n";
                }
            }
            if (!equalitiesEmpty) {
                message+="(In-)Equalities\n";
                for (Object o:epsilon) {
                    message+="  "+o+"\n";
                }
            }
            if (!finiteDomainConstraintsEmpty) {
                message+="Finite-Domain Constraints\n";
                for (IConstraintInstance c:store.constraints) {
                    message+="  "+c+"\n";
                }
            }

            return message.substring(0,message.length()-1); // Get rid of extra new line.
        }

    }

}
