package uk.co.mtford.jalp.abduction.logic.instance.term;

import choco.kernel.model.variables.Variable;
import choco.kernel.model.variables.set.SetVariable;
import uk.co.mtford.jalp.abduction.logic.instance.*;
import uk.co.mtford.jalp.abduction.tools.NameGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static choco.Choco.makeSetVar;

/**
 * A list of integer constants e.g. [1,2,3]
 */
public class IntegerConstantListInstance extends ListInstance<IntegerConstantInstance> {
    public IntegerConstantListInstance() {
        super();
    }

    public IntegerConstantListInstance(Collection<IntegerConstantInstance> integers) {
        super();
        this.getList().addAll(integers);
    }

    public IntegerConstantListInstance(IntegerConstantInstance[] integers) {
        super();
        for (IntegerConstantInstance instance:integers) {
            this.getList().add(instance);
        }
    }

    public IntegerConstantListInstance(int ... integers) {
        super();
        for (int i:integers) {
            this.list.add(new IntegerConstantInstance(i));
        }
    }


    
    public IFirstOrderLogicInstance performSubstitutions(Map<VariableInstance, IUnifiableInstance> substitutions) {
        IntegerConstantListInstance newListInstance = new IntegerConstantListInstance();
        for (IntegerConstantInstance term:list) {
            IntegerConstantInstance newTerm = (IntegerConstantInstance) term.performSubstitutions(substitutions);
            newListInstance.getList().add(newTerm);
        }
        list = newListInstance.getList();
        return this;
    }

    
    public IFirstOrderLogicInstance deepClone(Map<VariableInstance, IUnifiableInstance> substitutions) {
        IntegerConstantListInstance newListInstance = new IntegerConstantListInstance();
        for (IntegerConstantInstance term:list) {
            newListInstance.getList().add((IntegerConstantInstance) term.deepClone(substitutions));
        }
        return newListInstance;
    }

    
    public IFirstOrderLogicInstance shallowClone() {
        IntegerConstantListInstance newListInstance = new IntegerConstantListInstance();
        for (IntegerConstantInstance term:list) {
            newListInstance.getList().add((IntegerConstantInstance) term.shallowClone());
        }
        return newListInstance;
    }

    
    public boolean reduceToChoco(List<Map<VariableInstance, IUnifiableInstance>> possSubst, HashMap<ITermInstance, Variable> termToVarMap) {
        if (!termToVarMap.containsKey(this)) {
            SetVariable setVariable = makeSetVar(NameGenerator.upperCaseNameGen.getNextName(), getMin(), getMax());
            termToVarMap.put(this,setVariable);
        }
        return true;
    }

    
    public boolean inList(CharConstantListInstance constantList, List<Map<VariableInstance, IUnifiableInstance>> possSubst) {
        throw new UnsupportedOperationException();
    }

    public int getMax() {
        int max = Integer.MIN_VALUE;
        for (IntegerConstantInstance constantInstance:list) {
            if (constantInstance.getValue()>max) max = constantInstance.getValue();
        }
        return max;
    }

    public int getMin() {
        int min = Integer.MAX_VALUE;
        for (IntegerConstantInstance constantInstance:list) {
            if (constantInstance.getValue()<min) min = constantInstance.getValue();
        }
        return min;
    }

    public int[] getIntArray() {
        int[] ints = new int[list.size()];
        int i =0;
        for (IntegerConstantInstance c:list) {
           ints[i]=c.getValue();
            i++;
        }
        return ints;
    }
}
