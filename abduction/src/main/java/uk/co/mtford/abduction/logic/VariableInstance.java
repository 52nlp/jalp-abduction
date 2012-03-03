/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.mtford.abduction.logic;

import uk.co.mtford.unification.CouldNotUnifyException;
import uk.co.mtford.unification.Unifier;

/**
 *
 * @author mtford
 */
public class VariableInstance implements ITermInstance {
    String name;
    IAtomInstance value;

    public VariableInstance(String name, IAtomInstance value) {
        this.name = name;
        this.value = value;
    }

    public VariableInstance(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Returns the actual assigned value.
     * 
     * @return 
     */
    public IAtomInstance getValue() {
        return value;
    }

    public void setValue(IAtomInstance value) {
        this.value = value;
    }
    
    public boolean isAssigned() {
        return value!=null;
    }
    
    @Override
    public String toString() {
        if (value==null) return name;
        return name+"="+value.toString();
    }

    /** Returns true if variable names at the same. Not concerned with value. */
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 89 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public Object clone() {
        String clonedName = new String(name);
        IAtomInstance clonedValue = null;
        if (value!=null) {
            clonedValue = (IAtomInstance) value.clone();
        }
        return new VariableInstance(clonedName,clonedValue);
    }

    /** Returns true if same name.
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
        final VariableInstance other = (VariableInstance) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
    
    /** Returns true if same name or same value.
     * 
     * @param obj
     * @return 
     */
    public boolean deepEquals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ConstantInstance) {
            ConstantInstance constant = (ConstantInstance) obj;
            if (value == null ) {
                return false;
            }
            IAtomInstance realValue = value;
            while (realValue instanceof VariableInstance) {
                realValue=((VariableInstance)realValue).value;
            }
            if (realValue== null) {
                return false;
            }
            if (realValue instanceof ConstantInstance) {
                return value.equals(constant);
            }
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VariableInstance other = (VariableInstance) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.deepEquals(other.value))) {
            return false;
        }
        return true;
    }

    public boolean equalitySolve(IAtomInstance other) {
        try {
            Unifier.unify(this, other);
        } catch (CouldNotUnifyException ex) {
            return false;
        }
        return true;
    }

   
}
